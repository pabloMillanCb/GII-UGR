package civitas;

import java.util.ArrayList;

public class SorpresaIrCarcel extends Sorpresa {
    
    private Tablero tablero; 
    
    SorpresaIrCarcel(Tablero tablero) {
        super ("IRCARCEL");
        this.tablero = tablero;
    }
    
    @Override
    protected void aplicarAJugador(int actual, ArrayList<Jugador> todos) {
        
        informe(actual, todos);
        todos.get(actual).encarcelar(tablero.getCarcel());
    }
}
