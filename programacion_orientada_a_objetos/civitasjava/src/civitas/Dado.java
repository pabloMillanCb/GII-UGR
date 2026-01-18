package civitas;

import java.util.Random;

public class Dado {
    private Random random;
    private int ultimoResultado;
    private boolean debug;
    
    private static Dado instance = new Dado();
    private static final int SalidaCarcel=5;
    
    public Dado(){
        random = new Random();
        debug=false;
    }
    
    public static Dado getInstance(){
        return instance;
    }
    
    public int tirar(){
        int tirada;
        if(debug)
            tirada=1;
        else
            tirada=random.nextInt(6)+1;
        
        ultimoResultado=tirada;
        return tirada;
    }
    
    public boolean salgoDeLaCarcel(){
        boolean salida;
        int tirada=random.nextInt(6)+1;
        if(tirada==SalidaCarcel){
            salida=true;
            System.out.println("Sale de la cárcel.\n");
        }
        else{
            salida=false;
            System.out.println("No sale de la cárcel.\n");
        }
        
        return salida;
    }
    
    public int quienEmpieza(int n){
        int primero = random.nextInt(n);
        return primero;
    }
    
    public void setDebug(boolean d){
        debug=d;
        Diario diario= Diario.getInstance();
        if(d)
            diario.ocurreEvento("Dado debug activado.\n");
        else
            diario.ocurreEvento("Dado debug desactivado.\n");
    }
    
    public int getUltimoResultado(){
        return ultimoResultado;
    }
}
