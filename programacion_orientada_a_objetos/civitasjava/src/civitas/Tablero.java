package civitas;

import java.util.ArrayList;

public class Tablero {
   private int numCasillaCarcel;
   private ArrayList<Casilla> casillas;
   private int porSalida;
   private boolean tieneJuez;
   
   protected Tablero (int n){
       if(n<1)
           numCasillaCarcel=1;
       else
           numCasillaCarcel=n;
       
       casillas = new ArrayList<>();
       Casilla salida = new Casilla("Salida");
       casillas.add(salida);
   }
   
   private Boolean correcto(){
        boolean correcto;
        if(casillas.size()>numCasillaCarcel && tieneJuez)
            correcto = true;
        else
            correcto = false;
        
        return correcto;
   }
   
   private Boolean correcto(int numCasilla){
       boolean correcto;
       if(correcto() && numCasilla>=0 && numCasilla<casillas.size())
           correcto = true;
       else
           correcto = false;
       
       return correcto;
   }
   
   protected int getCarcel(){
       return numCasillaCarcel;
   }
   
   protected int getPorSalida(){
       int salida;
       if(porSalida>0){
           porSalida--;
           salida = porSalida+1;
       }else
           salida = porSalida;
       
       return salida;
   }
   
   protected void añadeCasilla(Casilla casilla){
       if(casillas.size()==numCasillaCarcel){
           Casilla carcel = new Casilla("Carcel");
           casillas.add(carcel);
       }
       
       casillas.add(casilla);
       
       if(casillas.size()==numCasillaCarcel){
           Casilla carcel = new Casilla("Carcel");
           casillas.add(carcel);
       }   
   }
   
   protected void añadeJuez(){
       if(!tieneJuez){
           CasillaJuez juez = new CasillaJuez(numCasillaCarcel, "Juez");
           casillas.add(juez);
           tieneJuez=true;
       }
   }
     
   protected Casilla getCasilla(int numCasilla){
       if(correcto(numCasilla))
           return casillas.get(numCasilla);
       else
           return null;
   }
   
   protected int nuevaPosicion(int actual, int tirada){
       int n;
       if(!correcto())
           n = -1;
       else{
           if((actual+tirada)%casillas.size()==actual+tirada)
               n = actual+tirada;
           else{
               porSalida++;
               n = (actual+tirada)%casillas.size();
           }
        }
       
       return n;
   }
   
   protected int calcularTirada(int origen, int destino){
       int tirada = destino - origen;
       if(tirada<1)
           tirada+=casillas.size();
       
       return tirada;
   }
}
