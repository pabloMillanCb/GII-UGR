package civitas;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Sorpresa {
    
    private String texto;
    
    Sorpresa(String texto) {
        this.texto = texto;
    }
    
    protected void informe (int actual, ArrayList<Jugador> todos){
        String s = todos.get(actual).getNombre();
        Diario.getInstance().ocurreEvento("Se aplica sorpresa "+ texto+" al jugador " + s + "\n");
    }
    
    public boolean jugadorCorrecto(int actual, ArrayList<Jugador> todos){
        
        return actual < todos.size();
    }
    
    protected void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        
    }
    
    public String toString() {
        
        return texto;
    }
}
