package civitas;

public class JugadorEspeculador extends Jugador{
    
    private static int factorEspeculador = 2;
    private float fianza;
    
    JugadorEspeculador(Jugador jugador, float fianza) {
        
        super(jugador);
        this.fianza = fianza;
        
        for (int i = 0; i < jugador.propiedades.size(); i++) {
            
            jugador.propiedades.get(i).actualizaPropietarioPorConversion(this);
        }
    }
    
    @Override
    protected boolean debeSerEncarcelado(){
        
        boolean out = super.debeSerEncarcelado();
        
        if (out && saldo >= fianza)
            out = !paga(fianza);
        
        return out;
    }
    
    @Override
    boolean pagaImpuesto(float cantidad) {
        
        boolean s = !isEncarcelado();
        
        if (s){
            s = paga(cantidad/factorEspeculador);
        }
        
       return s;
    }
    
    @Override
    protected int getCasasMax() {
        
        return CasasMax*factorEspeculador;
    }
    
    @Override
    protected int getHotelesMax() {
        
        return HotelesMax*factorEspeculador;
    }
}
