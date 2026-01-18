package civitas;


public class TituloPropiedad {
    
    private Jugador propietario;
    
    private float alquilerBase;
    private static float factorInteresesHipoteca = 1.1f;
    private float factorRevalorizacion;
    private float hipotecaBase;
    private boolean hipotecado;
    private String nombre;
    private int numCasas;
    private int numHoteles;
    private float precioCompra;
    private float precioEdificar;
    
    TituloPropiedad(String nom, float ab, float fr, float hb, float pc, float pe){
        
        nombre = nom;
        alquilerBase = ab;
        factorRevalorizacion = fr;
        hipotecaBase = hb;
        precioCompra = pc;
        precioEdificar = pe;   
        propietario = null;
    }
    
    void actualizaPropietarioPorConversion(Jugador jugador) {
        
        propietario = jugador;
    }
    
    boolean cancelarHipoteca(Jugador jugador){
        boolean result = false;
        if(hipotecado)
            if(esEsteElPropietario(jugador)){
                propietario.paga(getImporteCancelarHipoteca());
                hipotecado = false;
                result = true;
            }
        return result;
    }
    
    int cantidadCasaHoteles(){
        
       return numCasas+numHoteles;
    }
    
    boolean comprar(Jugador jugador) {
        boolean result = false;
        if(!tienePropietario()){
            propietario = jugador;
            propietario.paga(getPrecioCompra());
            result = true;
        }
        return result;
    }
    
    boolean construirCasa(Jugador jugador) {
        boolean result = false;
        if(esEsteElPropietario(jugador)){
            propietario.paga(precioEdificar);
            numCasas++;
            result = true;
        }
        return result;
    }
    
    boolean construirHotel(Jugador jugador) {
        boolean result = false;
        if(esEsteElPropietario(jugador)){
            propietario.paga(precioEdificar);
            numHoteles++;
            derruirCasas(jugador.getCasasPorHotel(), jugador);
            result = true;
        }
        return result;
    }
    
    boolean derruirCasas(int n, Jugador jugador){ 
        
        boolean s = false;
        
        if (esEsteElPropietario(jugador) && n <= numCasas){
            
            s = true;
            numCasas -= n;
        }
        
        return s;
    }
    
    private boolean esEsteElPropietario(Jugador jugador) {
        
        boolean s = false;
        
        if (propietario == jugador){
            s = true;
        }
        
        return s;
    }
    
    public boolean getHipotecado() {
        return hipotecado;
    }
    
    float getImporteCancelarHipoteca() {
        
        return hipotecaBase*factorInteresesHipoteca;
    }
    
    private float getImporteHipoteca(){
        
        return hipotecaBase;
    }
    
    String getNombre() {
        
        return nombre;
    }
    
    int getNumCasas() {
        
        return numCasas;
    }
    
    int getNumHoteles(){
        
        return numHoteles;
    }
    
    private float getPrecioAlquiler() {
        
        return (alquilerBase*(1+numCasas*0.5f)+(numHoteles*2.5f));
        
    }
    
    float getPrecioCompra() {
        
        return precioCompra;
    }
    
    float getPrecioEdificar() {
        
        return precioEdificar;
    }
    
    private float getPrecioVenta(){
        
        return precioCompra+(numCasas+5*numHoteles)*precioEdificar*factorRevalorizacion;
    }
    
    Jugador getPropietario(){
        
        return propietario;
        
    }
    
    boolean hipotecar(Jugador jugador){
        boolean salida = false;
        if(!hipotecado && esEsteElPropietario(jugador)){
            propietario.recibe(getImporteHipoteca());
            hipotecado = true;
            salida = true;
        }
        return salida;
    }
    
    private boolean propietarioEncarcelado() {
        
        return propietario.isEncarcelado();
        
    }
    
    boolean tienePropietario() {
        
        boolean tiene = true;

        if (propietario == null){
            tiene = false;
        }
        
        return tiene;
    }
    
    void tramitarAlquiler(Jugador jugador) {
        
        if (tienePropietario() && !esEsteElPropietario(jugador)){
            
            jugador.pagaAlquiler(getPrecioAlquiler());
            propietario.recibe(getPrecioAlquiler());
        }
    }
    
    boolean vender(Jugador jugador){
        
        boolean s = false;
        
        if (esEsteElPropietario(jugador) && !hipotecado){
            
            propietario.recibe(getPrecioVenta());
            s = true;
            numCasas = 0;
            numHoteles = 0;
            actualizaPropietarioPorConversion(jugador);
        }
        return s;
    }
    
    public String toString(){
        
        String nombrePropietario = "no tiene";
        
        if (tienePropietario()){
            nombrePropietario = propietario.getNombre();
        }
        
        String S = "Nombre " + nombre + "\n"
                + "Propietario: " + nombrePropietario + "\n"
                + "Hipotecado: " + hipotecado + "\n"
                + "Numero de casas: " + numCasas + "\n"
                + "Numero de hoteles: " + numHoteles + "\n";
        
        return S;
        
    }
}