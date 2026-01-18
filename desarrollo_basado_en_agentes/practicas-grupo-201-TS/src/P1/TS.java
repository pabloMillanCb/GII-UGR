/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package P1;

import Environment.Environment;
import agents.BB1F;
import agents.DEST;
import agents.DroidShip;
import agents.MTT;
import agents.YV;
import ai.Choice;
import ai.DecisionSet;
import ai.Plan;
import geometry.Point3D;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import tools.emojis;
import world.Perceptor;

/**
 * 
 * @author albertoLlamasGonzalez
 * @author pabloMillanCubero
 * @author pabloBorregoMacias
 * @author antonioFernandezCabrera
 * @author franciscoAntonioPinoMuñoz
 */

public class TS extends AgentLARVAFull {
    //=========================================== VARIABLES =========================================================
    String problems[] = { "Dagobah.Apr1", "Dagobah.Apr2", "Dagobah.Not1", 
        "Dagobah.Not2", "Endor.Sob1", "Endor.Sob2", "Endor.Hon1", "Endor.Hon2", "AlertDeathStar" },
    cities[];
    
    protected int indexplan = 0, myEnergy = 0, indexSensor = 0;
    protected boolean showPerceptions = false, useAlias = false;
    
    boolean notFound = false;
    boolean tryLeft = false;
    boolean nextTryLeft = true;
    boolean decideWall = false;
    double distancePlan;
    double lastWallDecision = -150;
    double umbralDecision = 150;
    
    protected String whichWall, nextWhichwall; // Which turn did we take in the first obstacle, left or right?
    protected double distance, nextdistance; // Which was the distance to the goal in that moment
    protected Point3D point, nextPoint; // Which was the GPS position of the agente in that moment?
    
    protected Plan behaviour = null;
    protected Environment Ei, Ef;
    protected Choice a;
    protected String selectedMission;
    
    String goal;
    Stack<String> peopleCities = new Stack<String>(); //Stack to retrieve people in cities in REPORT
    
    //=========================================== METHODS ========================================================= 
    @Override
    public void setup() {
        enableDeepLARVAMonitoring();
        super.setup();
        deactivateSequenceDiagrams();
        
        this.setupEnvironment();
        A = new DecisionSet();
        A.addChoice(new Choice("MOVE"));
        A.addChoice(new Choice("RIGHT"));
        A.addChoice(new Choice("LEFT"));
        A.addChoice(new Choice("UP"));
        A.addChoice(new Choice("DOWN"));   
    }
    
    @Override
    public void Execute() {
        Info("Status: " + myStatus.name());
        switch (myStatus) {
            case START:
                problem = inputSelect("Select map", problems, "");
                myStatus = Status.CHECKIN;
                break;
            case CHECKIN:
                // The execution of a state (as a method) returns
                // the next state
                myStatus = MyCheckin();
                break;
            case OPENPROBLEM:
                myStatus = MyOpenProblem();
                break;
            case JOINSESSION:
                myStatus = MyJoinSession();
                break;
            case SOLVEPROBLEM:
                myStatus = MySolveProblem();
                break;
            case CLOSEPROBLEM:
                myStatus = MyCloseProblem();
                break;
            case CHECKOUT:
                myStatus = MyCheckout();
                break;
            case EXIT:
            default:
                doExit();
                myStatus = MyCloseProblem();
                break;
        }
    }
    
    @Override
    public Status MyOpenProblem() {
        // Look i the DF who is in charge of service PMANAGER
        if (this.DFGetAllProvidersOf(service).isEmpty()) {
            Error("Service PMANAGER is down");
            return Status.CHECKOUT;
        }
        problemManager = this.DFGetAllProvidersOf(service).get(0);
        Info("Found problem manager " + problemManager);
        
        // Send it a message to open a problem instance
        this.outbox = new ACLMessage();
        outbox.setSender(getAID());
        outbox.addReceiver(new AID(problemManager, AID.ISLOCALNAME));
        //Modifying for REPORT
        outbox.setContent("Request open " + problem + " alias " + sessionAlias);
        
        this.LARVAsend(outbox);
        Info("Request opening problem " + problem + " to " + problemManager);
        
        // There will be arriving two messages, one coming from the
        // Problem Manager and the other from the brand new Session Manager
        open = LARVAblockingReceive();
        Info(problemManager + " says: " + open.getContent());
        content = open.getContent();
        contentTokens = content.split(" ");
        if (contentTokens[0].toUpperCase().equals("AGREE")) {
            sessionKey = contentTokens[4];
            session = LARVAblockingReceive();
            sessionManager = session.getSender().getLocalName();
            Info(sessionManager + " says: " + session.getContent());
            return Status.JOINSESSION;
        } else {
            Error(content);
            return Status.CHECKOUT;
        }
    }
    
    /**
    * @author albertoLlamasGonzalez
    * @author pabloMillanCubero
    * @author pabloBorregoMegías
    */
    public Status MyJoinSession() {
        this.resetAutoNAV();
        // Retrieve cities info
        Info("Querying CITIES");
        outbox = new ACLMessage();
        outbox.setSender(this.getAID());
        outbox.addReceiver(new AID(sessionManager, AID.ISLOCALNAME));
        outbox.setContent("Query CITIES session " + sessionKey);
        this.LARVAsend(outbox);
        session = LARVAblockingReceive();
        getEnvironment().setExternalPerceptions(session.getContent());
        
        // Retrieve city list
        cities = getEnvironment().getCityList();
        
        Info("----------------- CIUDADES ------------------");
        
        this.DFAddMyServices(new String[]{"TYPE TS"});
        
        //Checking problem to assign origin city
        Info ("Problem: " + problem);
        String p = "";
        String ciudad = "";
        boolean surpriseMap = false;
        
        
        if (problem.contains("Dagobah")){
            ciudad = "Dagobah";
        }else if(problem.contains("Endor")){
            ciudad = "GuildHouse";
        }
        else if(problem.contains("AlertDeathStar")){
            ciudad = "ALPHA";
            surpriseMap = true;
        }

        
        outbox.setContent("Request join session "+sessionKey+ " in " + ciudad);
        LARVAsend(outbox);
        session = LARVAblockingReceive();
        
        //Only launches NPC type DEST if isn't surprise map
        if (!surpriseMap)
            this.doPrepareNPC(1, DEST.class);
        
        //QUERY MISIONES
        Info("Querying Missions");
        outbox = new ACLMessage();
        outbox.setSender(this.getAID());
        outbox.addReceiver(new AID(sessionManager, AID.ISLOCALNAME));
        outbox.setContent("Query MISSIONS session " + sessionKey);
        this.LARVAsend(outbox);
        session = LARVAblockingReceive();
     
        getEnvironment().setExternalPerceptions(session.getContent());
        MyReadPerceptions();
        
        AgentLARVAFull.Status mission = SelectMission();
        
        Info("Creando NPCs");
        
       // Launching NPCs
        this.doPrepareNPC(2, BB1F.class);
        this.doPrepareNPC(0, YV.class);
        this.doPrepareNPC(0, MTT.class);
        
        DroidShip.Debug();       
        return  mission; //AgentLARVAFull.Status.SOLVEPROBLEM
    }

    /**
    * @author albertoLlamasGonzalez
    * @author pabloMillanCubero
    * @author pabloBorregoMegías
    * @author antonioFernandezCabrera
    * @author franciscoAntonioPinoMuñoz
    */
    @Override
    public Status MySolveProblem() {
        MyReadPerceptions();
        
        //Defining goal
        if (!notFound) {
            goal = getEnvironment().getCurrentGoal();
            if (goal.contains("LIST")) {
                String datos[] = goal.split(" ");
                this.doQueryPeople(datos[1]);
            }else if (goal.contains("MOVEIN")) {
                for (int i = 0; i < cities.length && !notFound; i++){
                    if (goal.contains(cities[i])){
                        goal = cities[i];
                        notFound = true;
                    }
                }
                myAssistedNavigation(goal);
            } else if (goal.contains("REPORT")) {
               doReport();
            }
        }
        
        //Evaluating goal
        if (getEnvironment().getCurrentMission().isOver()) {
            Message("Mission completed!! Well done! ;)");
            return Status.CLOSEPROBLEM;
        }
        if (G(E)) {
            getEnvironment().getCurrentMission().nextGoal();
            notFound = false;
        }
        if (!Ve(E)) {
            Alert("Ups! You've crashed! Closing problem...");
            return Status.CLOSEPROBLEM;
        }
        if (notFound) {
            
            if (E.getNSteps() - lastWallDecision >= umbralDecision) {
                nextTryLeft = true;
            }
            
            this.resetAutoNAV();
            tryLeft = nextTryLeft;
            Plan p1 = AgPlan(E, A);
            double d = distancePlan;
            
            behaviour = p1;
            
            if (decideWall && E.getNSteps() - lastWallDecision >= umbralDecision) {
                
                
                lastWallDecision = E.getNSteps();
                this.resetAutoNAV();
                tryLeft = !nextTryLeft;
                Plan p2 = AgPlan(E, A);
                
                decideWall = false;

                if (distancePlan < d) {
                    behaviour = p2;
                    //Info ("DECIDE IZQUIERDA");
                    nextTryLeft = !nextTryLeft;
                } 
            }

            if (behaviour == null || behaviour.isEmpty()) {
                Alert("Found no plan to execute");
                return Status.CLOSEPROBLEM;
            } else {// Execute
                //Info("Found plan: " + behaviour.toString());
                while (!behaviour.isEmpty()) {
                    a = behaviour.get(0);
                    behaviour.remove(0);
                    //Info("Excuting " + a);
                    this.MyExecuteAction(a.getName());
                    //Info("\n---------------------------------------------------\n"
                    //        + "executing:" + a.getName() + " :\n " + this.easyPrintPerceptions(E, A)
                    //        + "\nPlan: " + behaviour.toString() + "\n");
                    if (!Ve(E)) {
                        this.Error("The agent is not alive: " + E.getStatus());
                        return Status.CLOSEPROBLEM;
                    }
                }

            }
        }
        return Status.SOLVEPROBLEM;
    }
    
    /**
    * @author pabloMillanCubero
    * @author pabloBorregoMegías
    * @brief close problem and destroy NPCs
    */
    @Override
    public Status MyCloseProblem() {
        this.doDestroyNPC();
        return super.MyCloseProblem();  
    }
    
    /**
     * @brief Queries number of people of type given in current city
     */
    protected Status doQueryPeople(String type) {
        Info("Quering people "+type);
        outbox = session.createReply();
        outbox.setContent("Query "+type.toUpperCase()+" session " + sessionKey);
        this.LARVAsend(outbox);
        session = LARVAblockingReceive();
        getEnvironment().setExternalPerceptions(session.getContent());
        //Adding people in cities for report
        peopleCities.add(getEnvironment().getCurrentCity() + " " + type+ " " + getEnvironment().getPeople().length);
        
        Message("Found "+getEnvironment().getPeople().length+" "+
                type+" in "+getEnvironment().getCurrentCity());
        return myStatus;
    }
    
    /**
     * @brief Method to select current mission
     */
    protected String chooseMission() {
        Environment e = getEnvironment();
        Info("Choosing a mission");
        // Info(Integer.toString(getEnvironment().getAllMissions().length));
        String m = "";
        if (getEnvironment().getAllMissions().length == 1) {
            m = getEnvironment().getAllMissions()[0];
        }
        else {
            m = this.inputSelect("Please choose a mision",
                    getEnvironment().getAllMissions(), "");
        }
        Info("Selected mission " + m);
        return m;
    }

    /**
     * @brief Method to select and set current mission
     */
    
    public Status SelectMission(){
        String m = chooseMission();
        if(m == null) {
            return Status.CLOSEPROBLEM;
        }
        
        Info("LA MISION ES: "+ m);
        
        selectedMission = m;
        
        this.getEnvironment().setCurrentMission(m);
        
        return Status.SOLVEPROBLEM;
    }
   
    /**
    * @author albertoLlamasGonzalez
    * @author pabloMillanCubero
    * @author pabloBorregoMegías
    * @author antonioFernandezCabrera
    * @brief sends report message to TYPE DEST
    */
    protected void doReport() {
        Info("Reporting people...");
        
        String currentCity = peopleCities.peek().split(" ")[0];
        String report = "REPORT;" + peopleCities.peek();
        peopleCities.pop();
        
        while (!peopleCities.empty()) {
            if (peopleCities.peek().split(" ")[0].compareTo(currentCity) == 0) {
                report += " " + peopleCities.peek().split(" ")[1] + " " + peopleCities.peek().split(" ")[2];
            }
            else {
                report += ";" + peopleCities.peek();
                currentCity = peopleCities.peek().split(" ")[0];
            }
            peopleCities.pop();
        }
        
        report += ";";
        
        String elDest = "";
        ArrayList<String> npcProviders =  this.DFGetAllProvidersOf("TYPE DEST");
        //Comprobamos que el npc está en nuestra session
        for (int i = 0; i < npcProviders.size(); i++){
            if (this.DFHasService(npcProviders.get(i), sessionKey)){
                elDest = npcProviders.get(i);
            }
        }
        // Infos used to understand how to do REPORT
//        Info("----------------------------------------------------------------------------------------");
//        Info(npcProviders.get(0));
//        Info("Providers: "+ this.DFGetAllProvidersOf("TYPE DEST"));
//        Info("Services: "+this.DFGetServiceList());
//        Info(this.getAID(npcProviders.get(0)).toString());        
//        Info("Tiene servicio");

        outbox = new ACLMessage();
        outbox.setSender(this.getAID());
        outbox.addReceiver(new AID(elDest, AID.ISLOCALNAME));
        // outbox = session.createReply();
        outbox.setContent(report);
        this.LARVAsend(outbox);
        session = LARVAblockingReceive();

       getEnvironment().getCurrentMission().nextGoal();
       getEnvironment().setExternalPerceptions(session.getContent()); 
    }

    /**
    * @author albertoLlamasGonzalez
    * @author pabloMillanCubero
    * @author pabloBorregoMegías
    * @author antonioFernandezCabrera
    * @brief Modified to request course given a city
    */
    protected Status myAssistedNavigation(String city) {
        Info("Requesting course to " + city);
        outbox = session.createReply();
        outbox.setContent("Request course in " + city + " session " + sessionKey);
        this.LARVAsend(outbox);
        session = this.LARVAblockingReceive();
        getEnvironment().setExternalPerceptions(session.getContent());
        MyReadPerceptions();
        return Status.CHECKIN.SOLVEPROBLEM;
    }

    // 100% New method to execute an action
    protected boolean MyExecuteAction(String action) {
        Info("Executing action " + action);
        outbox = session.createReply();
        // Remember to include sessionID in all communications
        outbox.setContent("Request execute " + action + " session " + sessionKey);
        this.LARVAsend(outbox);
        session = this.LARVAblockingReceive();
        if (!session.getContent().startsWith("Inform")) {
            Error("Unable to execute action " + action + " due to " + session.getContent());
            return false;
        }
        return true;
    }

    // Read perceptions and send them directly to the Environment instance,
    // so we can query any items of sensors and added-value information
    protected boolean MyReadPerceptions() {
        Info("Reading perceptions");
        outbox = session.createReply();
        outbox.setContent("Query sensors session " + sessionKey);
        this.LARVAsend(outbox);
        //this.myEnergy++;
        session = this.LARVAblockingReceive();
        if (session.getContent().startsWith("Failure")) {
            Error("Unable to read perceptions due to " + session.getContent());
            return false;
        }
        getEnvironment().setExternalPerceptions(session.getContent());
        //Info(this.easyPrintPerceptions());
        return true;
    }
    
    public void resetAutoNAV() {
        nextWhichwall = whichWall = "NONE";
        nextdistance = distance = Choice.MAX_UTILITY;
        nextPoint=point=null;
    }
    
    @Override
    protected Choice Ag(Environment E, DecisionSet A) {
        
        // G comprueba si se ha llegado a la meta
        if (G(E)) {
            return null;
        } else if (A.isEmpty()) {
            return null;
        } else {
            
            // Prioritize llama a U(E, a) para todas las acciones, ordena
                // en funcion de la puntuacion
            A = Prioritize(E, A);
            whichWall = nextWhichwall;
            distance = nextdistance;
            point=nextPoint;
            return A.BestChoice();
        }
    }
    
    /**
    * @author pabloMillanCubero
    * @author pabloBorregoMegías
    */
    protected Plan AgPlan(Environment E, DecisionSet A) {
        distancePlan = Choice.MAX_UTILITY;
        Plan result;
        Ei = E.clone();
        Plan p = new Plan();
        for (int i = 0; i < Ei.getRange() / 2 - 2; i++) {
            Ei.cache();
            //Info("\n====================================================\n"
            //        + "Planning STEP:" + i + " :\n " + this.easyPrintPerceptions(Ei, A)
            //        + "\nPlan: " + p.toString() + "\n");
            
            // Ve() checks if Environment ain't null or hasn't crashed
            if (!Ve(Ei)) {
                return null;
            } 
            
            // G comprueba si el plan ha llegado al objetivo
            // G() checks if plan has done mission
            else if (G(Ei)) {
                distancePlan = 0;
                return p;
            }
            
            // Ag escoge la mejor siguiente accion
            // Ag chooses best next action
            else {
                a = Ag(Ei, A);
                if (a != null) {
                    p.add(a);
                    Ef = S(Ei, a); // Simmulates next environment
                    Ei = Ef;
                } else {
                    return null;
                }
            }
        }
        distancePlan = Ef.getDistance();
        return p;
    }
    
    @Override
    protected double U(Environment E, Choice a) {
        if (E.getDistance() == 0 && E.getGPS().getZ() > E.getTarget().getZ()) {
            return goLanding(E, a);
        } else if (E.getDistance() == 0 && E.getGPS().getZ() < E.getTarget().getZ()) {
            return goTakeOff(E, a);
        } else if (whichWall.equals("LEFT")) {
            return goFollowWallLeft(E, a);
        } else if (whichWall.equals("RIGHT")) {
            return goFollowWallRight(E, a);
        } else if (!E.isFreeFront()) {
            return goAvoid(E, a);
        } else if ((E.getVisualFront() > E.getGPS().getZ() && E.getVisualFront() < 254)) {
            return goTakeOff(E, a);
        } else {
            return goAhead(E, a);
        }
    }
    
    public boolean isFreeFront() {
        int visualHere, visualFront, slopeFront;
        visualHere = E.getPolarVisual()[0][0];
        visualFront = E.getPolarVisual()[2][1];
        slopeFront = visualHere - visualFront;
        return visualFront >= E.getMinlevel() &&
                visualFront <= E.getMaxlevel() &&
                slopeFront <= E.getMaxslope() && 
                slopeFront >= -E.getMaxslope() &&
                E.getGPS().getX() >= 0 &&
                E.getGPS().getX() < E.getWorldWidth();
    }
    
    protected double goAhead(Environment E, Choice a) {
        if (a.getName().equals("MOVE")) {
            return U(S(E, a));
        } else if (a.getName().equals("LEFT") || a.getName().equals("RIGHT")) {
            return U(S(E, a), new Choice("MOVE"));
        }
        return Choice.MAX_UTILITY;
    }
    
    /**
    * @author pabloMillanCubero
    * @author pabloBorregoMegías
    */
    //Modificado
    public double goAvoid(Environment E, Choice a) {
        decideWall = true;
        
        if (a.getName().equals("RIGHT") && !tryLeft) {
            nextWhichwall = "LEFT";
            nextdistance = E.getDistance();
            nextPoint=E.getGPS();
            //Info("--------------------goAvoid LEFT");
            
            return Choice.ANY_VALUE;
        }
        
        if (a.getName().equals("LEFT") && tryLeft) {
            nextWhichwall = "RIGHT";
            nextdistance = E.getDistance();
            nextPoint=E.getGPS();
            //Info("--------------------goAvoid RIGHT");
            
            return Choice.ANY_VALUE;
        }
        
        return Choice.MAX_UTILITY;
    }
    
    // Move up to the flying level
    public double goTakeOff(Environment E, Choice a) {
        if (a.getName().equals("UP")) {
            
            return Choice.ANY_VALUE;
        }
        return Choice.MAX_UTILITY;
    }

    // Go down towards the ground level
    public double goLanding(Environment E, Choice a) {
        if (a.getName().equals("DOWN")) {
            
            return Choice.ANY_VALUE;
        }
        return Choice.MAX_UTILITY;
    }
    
    public double goKeepOnWall(Environment E, Choice a) {
        if (a.getName().equals("MOVE")) {
            
            return Choice.ANY_VALUE;
        }
        return Choice.MAX_UTILITY;
    }

    public double goTurnOnWallLeft(Environment E, Choice a) {
        if (a.getName().equals("LEFT")) {
            
            return Choice.ANY_VALUE;
        }
        return Choice.MAX_UTILITY;

    }

    public double goRevolveWallLeft(Environment E, Choice a) {
        if (a.getName().equals("RIGHT")) {
            
            return Choice.ANY_VALUE;
        }
        return Choice.MAX_UTILITY;
    }

    public double goStopWallLeft(Environment E, Choice a) {
        if (a.getName().equals("RIGHT")) {
            this.resetAutoNAV();
            
            return Choice.ANY_VALUE;
        }
        return Choice.MAX_UTILITY;
    }

    public double goFollowWallLeft(Environment E, Choice a) {
        if (E.isFreeFrontLeft()) {
            return goTurnOnWallLeft(E, a);
        } else if (E.isTargetFrontRight()
                && E.isFreeFrontRight()
                && //E.getDistance() < distance
                    E.getDistance() < point.planeDistanceTo(E.getTarget())
                ) {
            return goStopWallLeft(E, a);
        } else if (E.isFreeFront()) {
            return goKeepOnWall(E, a);
        } else {
            return goRevolveWallLeft(E, a);
        }
    }
    
    public double goTurnOnWallRight(Environment E, Choice a) {
        if (a.getName().equals("RIGHT")) {
            
            return Choice.ANY_VALUE;
        }
        return Choice.MAX_UTILITY;

    }

    public double goRevolveWallRight(Environment E, Choice a) {
        if (a.getName().equals("LEFT")) {
            
            return Choice.ANY_VALUE;
        }
        return Choice.MAX_UTILITY;
    }

    public double goStopWallRight(Environment E, Choice a) {
        if (a.getName().equals("LEFT")) {
            this.resetAutoNAV();
            
            return Choice.ANY_VALUE;
        }
        return Choice.MAX_UTILITY;
    }

    public double goFollowWallRight(Environment E, Choice a) {
        if (E.isFreeFrontRight()) {
            return goTurnOnWallRight(E, a);
        } else if (E.isTargetFrontLeft()
                && E.isFreeFrontLeft()
                && //E.getDistance() < distance
                    E.getDistance() < point.planeDistanceTo(E.getTarget())
                ) {
            return goStopWallRight(E, a);
        } else if (E.isFreeFront()) {
            return goKeepOnWall(E, a);
        } else {
            return goRevolveWallRight(E, a);
        }
    }
    
}