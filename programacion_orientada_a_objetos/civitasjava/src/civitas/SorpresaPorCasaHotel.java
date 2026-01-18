package civitas;

import java.util.ArrayList;

public class SorpresaPorCasaHotel extends Sorpresa{
    
    private int valor;
    
    SorpresaPorCasaHotel(int valor, String texto) {
        super (texto);
        this.valor = valor;
    }
    
    @Override
    protected void aplicarAJugador(int actual, ArrayList<Jugador> todos){
        
        informe(actual, todos);
        todos.get(actual).modificaSaldo(valor*todos.get(actual).cantidadCasaHoteles());
    }
}
