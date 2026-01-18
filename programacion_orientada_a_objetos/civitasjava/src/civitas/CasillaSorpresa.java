/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitas;

import java.util.ArrayList;

public class CasillaSorpresa extends Casilla {
    
    private MazoSorpresas mazo;
    
    CasillaSorpresa(MazoSorpresas mazo, String nombre){
        
        super (nombre);
        this.mazo = mazo;
    }
    
    @Override
    protected void recibeJugador(int actual, ArrayList<Jugador> todos){
        
        if(jugadorCorrecto(actual, todos)){

            Sorpresa sorpresa = mazo.siguiente();
            informe(actual, todos);
            sorpresa.aplicarAJugador(actual, todos);
        }
    }
    
    public String to_String() {
        
        String s = "Nombre: " + nombre;
               s += "\nTipo: Sorpresa" ;
        
        return s;
    }
}
