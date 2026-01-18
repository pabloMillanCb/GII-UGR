package civitas;

import java.util.ArrayList;

public class SorpresaPorJugador extends Sorpresa{
    
    private int valor;
    
    SorpresaPorJugador(int valor, String texto) {
        
        super (texto);
        this.valor = valor;
    }
    
    @Override
    protected void aplicarAJugador(int actual, ArrayList<Jugador> todos){
        
        informe(actual, todos);
        SorpresaPagarCobrar s1 = new SorpresaPagarCobrar(valor*(-1), "PAGARCOBRAR"),
                 s2 = new SorpresaPagarCobrar(valor*(todos.size()-1), "PAGARCOBRAR");
        
        for (int i = 0; i < todos.size(); i++){
            if (i != actual)
                s1.aplicarAJugador(i, todos);
        }
        s2.aplicarAJugador(actual, todos);
    }
}
