/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitas;

import java.util.ArrayList;

/**
 *
 * @author pablo
 */
public class CasillaCalle extends Casilla {
    
    private TituloPropiedad tituloPropiedad;
    
    CasillaCalle(TituloPropiedad titulo) {
        
        super (titulo.getNombre());
        tituloPropiedad = titulo;
    }
    
    //CasillaCalle
    protected TituloPropiedad getTituloPropiedad() {
        
        return tituloPropiedad;
    }
    
    @Override
    protected void recibeJugador(int actual, ArrayList<Jugador> todos){
        
        if(jugadorCorrecto(actual, todos)){
            super.informe(actual, todos);
            Jugador jugador = todos.get(actual);
            
            if(!tituloPropiedad.tienePropietario())
                jugador.puedeComprarCasilla();
            else
                tituloPropiedad.tramitarAlquiler(jugador);
        }
    }
    
    public String to_String() {
        
        String s = "Nombre: " + nombre;
               s += "\nTipo: Calle" ;
        
        return s;
    }
    
}
