/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitas;

import java.util.ArrayList;

public class CasillaJuez extends Casilla {
    
    private static int carcel;
    
    CasillaJuez(int numCasillaCarcel, String nombre) {
        
        super (nombre);
        carcel = numCasillaCarcel;
    }
    
    @Override
    protected void recibeJugador(int actual, ArrayList<Jugador> todos) {
        
        if (jugadorCorrecto(actual, todos)){
            
            informe(actual, todos);
            todos.get(actual).encarcelar(carcel);
        }
    }
    
    public String to_String() {
        
        String s = "Nombre: " + nombre;
               s += "\nTipo: Juez" ;
        
        return s;
    }
}
