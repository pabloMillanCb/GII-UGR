package civitas;

import java.util.ArrayList;

public class Casilla {
    
    protected String nombre; //General
    
    Casilla(String n){
        
        nombre = n;
    }
    
    //General
    public String getNombre(){
        return nombre;
    }
    
    //General
    protected void informe(int actual, ArrayList<Jugador> todos){
        
        Diario.getInstance().ocurreEvento("El jugador " + todos.get(actual).getNombre() 
                                          + " cae en la casilla " + nombre + "\n");
    }
    //General
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos){
        
        return actual < todos.size();
    }

    protected void recibeJugador(int actual, ArrayList<Jugador> todos) {
        
        informe(actual, todos);
    }

}
