package civitas;

import java.util.ArrayList;

public class SorpresaPagarCobrar extends Sorpresa{
    
    private int valor;
    
    SorpresaPagarCobrar(int valor, String texto) {
        
        super (texto);
        this.valor = valor;
    }
    
    @Override
    protected void aplicarAJugador(int actual, ArrayList<Jugador> todos){
        
        informe(actual, todos);
        todos.get(actual).modificaSaldo(valor);
    }
}
