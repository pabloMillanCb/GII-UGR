package civitas;

import java.util.ArrayList;

public class SorpresaIrCasilla extends Sorpresa {
    
    private int valor;
    private Tablero tablero;
    
    SorpresaIrCasilla(Tablero tablero, int valor, String texto) {
        
        super (texto);
        this.valor = valor;
        this.tablero = tablero;
    }
    
    @Override
    protected void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        
        informe(actual, todos);
        int tirada = tablero.calcularTirada(todos.get(actual).getNumCasillaActual(), valor);
        int nuevaPosicion = tablero.nuevaPosicion(todos.get(actual).getNumCasillaActual(), tirada);
        todos.get(actual).moverACasilla(nuevaPosicion);
        tablero.getCasilla(nuevaPosicion).recibeJugador(actual, todos);
    }
}
