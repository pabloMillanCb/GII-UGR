/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package civitas;

import java.util.ArrayList;


public class CasillaImpuesto extends Casilla {
    
    private float importe;
    
    CasillaImpuesto(float cantidad, String nombre) {
        
        super (nombre);
        importe = cantidad;
    }
    
    @Override
    protected void recibeJugador(int actual, ArrayList<Jugador> todos){
        
        if (jugadorCorrecto(actual, todos)){
            
            informe(actual, todos);
            todos.get(actual).pagaImpuesto(importe);
        }
    }
    
    public String to_String() {
        
        String s = "Nombre: " + nombre;
               s += "\nTipo: Impuesto" ;
               s += "\nValor: " + Float.toString(importe);
        
        return s;
    }
    
}
