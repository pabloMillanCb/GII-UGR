
package civitas;

import java.util.ArrayList;
import java.util.Collections;

public class Jugador implements Comparable<Jugador> {
    
    protected static final int CasasMax = 4;
    protected static final int CasasPorHotel = 4;
    protected static final int HotelesMax = 4;
    protected static final float PasoPorSalida = 1000f;
    protected static final float PrecioLibertad = 200f;
    protected static final float SaldoInicial = 7500f;
    
    protected boolean encarcelado;
    protected String nombre;
    protected int numCasillaActual;
    protected boolean puedeComprar;
    protected float saldo;
    
    protected SorpresaSalirCarcel salvoconducto;
    
    protected ArrayList<TituloPropiedad> propiedades = new ArrayList<TituloPropiedad>();
    
    Jugador (String nombre_) {
        
        nombre = nombre_;
        encarcelado = false;
        numCasillaActual = 0;
        puedeComprar = false;
        saldo = SaldoInicial;
        salvoconducto = null;
    }
    
    protected Jugador (Jugador otro) {
        
        nombre = otro.getNombre();
        encarcelado = otro.isEncarcelado();
        numCasillaActual = otro.getNumCasillaActual();
        puedeComprar = otro.getPuedeComprar();
        saldo = otro.getSaldo();
        propiedades = otro.getPropiedades();
        salvoconducto = otro.salvoconducto; //revisar
        
    }
    
    protected boolean debeSerEncarcelado(){
        
        System.out.println ("jelou\n");
        
        boolean out = !isEncarcelado();
        
        if (out && tieneSalvoconducto()){
            
            perderSalvoconducto();
            Diario.getInstance().ocurreEvento(this.getNombre() + "debe ser encarcelado.\n");
            out = false;
        }
        
        return out;
    }
    
    protected boolean encarcelar(int numCasillaCarcel) {
        
        if (debeSerEncarcelado()) {

            moverACasilla(numCasillaCarcel);
            encarcelado = true;
            Diario.getInstance().ocurreEvento("Se encarcela al jugador "+this.getNombre()+"\n");
        }
        
        return isEncarcelado();
    }
    
    boolean obtenerSalvoconducto(SorpresaSalirCarcel s) {
        
        if (!isEncarcelado())
            salvoconducto = s;
        
        return !isEncarcelado();
    }
    
    protected void perderSalvoconducto() {
        
        salvoconducto.usada();
        salvoconducto = null;
    }
    
    boolean tieneSalvoconducto() {
        
        boolean s = false;
        
        if (salvoconducto != null) {
            s = true;
        }
        
        return s;
    }
    
    boolean puedeComprarCasilla() {
        
        puedeComprar = !isEncarcelado();
        return puedeComprar;
    }
    
    boolean paga(float cantidad){
        
        return modificaSaldo(-1*cantidad);
    }
    
    boolean pagaImpuesto(float cantidad) {
        
        boolean s = !isEncarcelado();
        
        if (s){
            s = paga(cantidad);
        }
        
       return s;
    }
    
    boolean pagaAlquiler(float cantidad) {
        
        boolean s = !isEncarcelado();
        
        if (s){
            s = paga(cantidad);
        }
        
       return s;
    }
    
    boolean recibe(float cantidad) {
        
        boolean s = !isEncarcelado();
        
        if (s) {
            s = modificaSaldo(cantidad);
        }
        
        return s;
    }
    
    boolean modificaSaldo(float cantidad) {
        
        saldo += cantidad;
        Diario.getInstance().ocurreEvento("Saldo modificado.\n");
        
        return true;
    }
    
    boolean moverACasilla(int numCasilla) {
        
        if (!isEncarcelado()) {
            
            numCasillaActual = numCasilla;
            puedeComprar = false;
        }
        
        return !isEncarcelado();
    }
    
    boolean puedoGastar(float precio) {
        
        boolean out = !isEncarcelado();
        
        if (out){
            
            out = getSaldo() >= precio;
        }
        
        return out;
    }
    
    boolean vender(int ip) {
        
        boolean out = !isEncarcelado();
        
        if (out && existeLaPropiedad(ip)) {
            
            out = getPropiedades().get(ip).vender(this);
            
            if (out){
                Diario.getInstance().ocurreEvento("Se ha vendido la propiedad "
                                                    + getPropiedades().get(ip).getNombre()
                                                    + "\n");
                getPropiedades().remove(ip);
            }
        }
        
        return out;
    }
    
    boolean tieneAlgoQueGestionar() {
        
        return propiedades.size() > 0;
    }
    
    private boolean puedeSalirCarcelPagando() {
        
        return getSaldo() >= getPrecioLibertad();
    }
    
    boolean salirCarcelPagando() {
        
        boolean out = false;
        
        if (isEncarcelado() && puedeSalirCarcelPagando()) {
            
            paga(getPrecioLibertad());
            out = true;
            Diario.getInstance().ocurreEvento("Sale de la carcel el jugador " + getNombre()+".\n");
            encarcelado = false;
            
        }
        
        return out;
    }
    
    boolean salirCarcelTirando() {
        
        boolean out = Dado.getInstance().salgoDeLaCarcel();
        
        if (out){
            Diario.getInstance().ocurreEvento("Sale de la carcel el jugador " + getNombre() + ".\n");
            encarcelado = false;
        }
        
        return out;
    }
    
    boolean pasaPorSalida() {
        
        modificaSaldo(getPremioPasaPorSalida());
        Diario.getInstance().ocurreEvento("Se pasa por salida.\n");
        return true;
    }
    
    public int compareTo(Jugador otro) {
        
        int out = 0;
        
        if (this.getSaldo() > otro.getSaldo())
            out = -1;
        
        if (this.getSaldo() < otro.getSaldo())
            out = 1;
                    
        return out;
    }
    
    boolean enBancarrota() {
        
        return getSaldo() < 0;
    }
    
    private boolean existeLaPropiedad(int ip) {
        
        return ip < propiedades.size();
    }
    
    protected int getCasasMax() {
        
        return CasasMax;
    }
    
    int getCasasPorHotel() {
        
        return CasasPorHotel;
    }
    
    protected int getHotelesMax() {
        
        return HotelesMax;
    }
    
    protected String getNombre() {
        
        return nombre;
    }
    
    int getNumCasillaActual() {
        
        return numCasillaActual;
    }
    
    private float getPrecioLibertad() {
        
        return PrecioLibertad;
    }
    
    private float getPremioPasaPorSalida() {
        
        return PasoPorSalida;
    }
    
    public ArrayList<TituloPropiedad> getPropiedades() {
        
        return propiedades;
    }
    
    boolean getPuedeComprar() {
        
        return puedeComprar;
    }
    
    public float getSaldo() {
        
        return saldo;
    }
    
    public boolean isEncarcelado() {
        
        return encarcelado;
    }
    
    private boolean puedoEdificarCasa(TituloPropiedad propiedad){
        boolean puedoEdificarCasa = false;
        float precio = propiedad.getPrecioEdificar();

        if(puedoGastar(precio) &&
           propiedad.getNumCasas()<getCasasMax())
            puedoEdificarCasa = true;

        return puedoEdificarCasa;
    }
    
    private boolean puedoEdificarHotel(TituloPropiedad propiedad){
        boolean puedoEdificarHotel = false;
        float precio = propiedad.getPrecioEdificar();
        
        if(puedoGastar(precio) &&
           propiedad.getNumHoteles()<getHotelesMax() &&
           propiedad.getNumCasas()>=getCasasPorHotel())
            puedoEdificarHotel = true;

        return puedoEdificarHotel;
    }
    
    protected int cantidadCasaHoteles() {
        
        int cantidad = 0;
        
        for (int i = 0; i < propiedades.size(); i++){
            cantidad += propiedades.get(i).cantidadCasaHoteles();
        }
        
        return cantidad;
    }
    
    protected boolean construirCasa(int ip){
        boolean result = false;

        if(!encarcelado && existeLaPropiedad(ip)){
            TituloPropiedad propiedad = propiedades.get(ip);
            if(puedoEdificarCasa(propiedad)){
                result = propiedad.construirCasa(this);
                Diario.getInstance().ocurreEvento("El jugador "+nombre+" construye casa en la propiedad "+ip+".\n");
            }
        }
        return result;
    }
    
    protected boolean construirHotel(int ip){
        boolean result = false;

        if(!encarcelado && existeLaPropiedad(ip)){
            TituloPropiedad propiedad = propiedades.get(ip);
            if(puedoEdificarHotel(propiedad)){
                result = propiedad.construirHotel(this);
                propiedad.derruirCasas(getCasasPorHotel(), this);
                Diario.getInstance().ocurreEvento("El jugador "+nombre+" construye hotel en la propiedad "+ip+".\n");
            }
        }
        return result;
    }
    
    protected boolean cancelarHipoteca(int ip){
        boolean result = false;

        if(!encarcelado && existeLaPropiedad(ip)){
            TituloPropiedad propiedad = propiedades.get(ip);
            float cantidad = propiedad.getImporteCancelarHipoteca();
            if(puedoGastar(cantidad)){
                result = propiedad.cancelarHipoteca(this);
                if(result)
                    Diario.getInstance().ocurreEvento("El jugador "+nombre+" cancela la hipoteca de la propiedad "+ip+".\n");
            }
        }
        return result;
    }
    
    public String toString() {
        
        String out = "Nombre: " + this.getNombre() +
                     "\nSaldo: " + Float.toString(this.getSaldo()) +
                     "\nEncarcelado: " + Boolean.toString(this.isEncarcelado()) +
                     "\nCasilla actual: " + Integer.toString(this.getNumCasillaActual()) +
                     "\nPuede comprar: " + Boolean.toString(this.getPuedeComprar()) +
                     "\nPropiedades: " + Integer.toString(propiedades.size()) +
                     "\nTiene salvoconducto: " + Boolean.toString(this.tieneSalvoconducto()) + "\n";
        
        return out;
    }
    
    protected boolean comprar(TituloPropiedad titulo) {
        boolean result = false;

        if(!encarcelado && puedeComprar){
            if(puedoGastar(titulo.getPrecioCompra())){
                result = titulo.comprar(this);
                if(result){
                    propiedades.add(titulo);
                    Diario.getInstance().ocurreEvento("El jugador "+nombre+" compra la propiedad "+titulo.getNombre()+".\n");
                }
            }
        }
        return result;
    }
    
    boolean hipotecar (int ip) {
        boolean result = false;

        if(!encarcelado && existeLaPropiedad(ip)){
            TituloPropiedad propiedad = propiedades.get(ip);
            result = propiedad.hipotecar(this);
            Diario.getInstance().ocurreEvento("El jugador "+nombre+" hipoteca la propiedad "+ip+".\n");
        }
        return result;
    }
    
}
