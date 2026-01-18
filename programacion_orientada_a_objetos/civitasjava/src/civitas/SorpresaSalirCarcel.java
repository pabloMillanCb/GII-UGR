package civitas;

import java.util.ArrayList;

public class SorpresaSalirCarcel extends Sorpresa {
    
    private MazoSorpresas mazo;
    
    SorpresaSalirCarcel (MazoSorpresas mazo) {
        
        super ("SALIRCARCEL");
        this.mazo = mazo;
    }
    
    @Override
    protected void aplicarAJugador(int actual, ArrayList<Jugador> todos){
        
        if (jugadorCorrecto(actual, todos)){
        
            informe(actual, todos);

            boolean salvoconductoLibre = true;

            for (int i = 0; i < todos.size() && salvoconductoLibre; i++) {
                if (todos.get(i).tieneSalvoconducto())
                    salvoconductoLibre = false;
            }

            if (salvoconductoLibre){
                todos.get(actual).obtenerSalvoconducto(this);
                salirDelMazo();
            }
        }
    }
    
    protected void usada() {
        
        mazo.habilitarCartaEspecial(this);
    }
    
    protected void salirDelMazo() {
        
        mazo.inhabilitarCartaEspecial(this);
    }
}
