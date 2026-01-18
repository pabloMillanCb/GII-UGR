package civitas;

import java.util.ArrayList;


public class SorpresaEspeculador extends Sorpresa{
    
    private float fianza;
    
    SorpresaEspeculador(float fianza) {
        
        super("Especulador");
        this.fianza = fianza;
        
    }
    
    @Override
    protected void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        
        if (jugadorCorrecto(actual, todos)){
            
            JugadorEspeculador especulador = new JugadorEspeculador(todos.get(actual), fianza);
            this.informe(actual, todos);
            todos.set(actual, especulador);
        }
    }
    
}
