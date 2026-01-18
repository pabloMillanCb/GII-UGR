
package civitas;

import java.util.ArrayList;

public class CivitasJuego {
    
    private int indiceJugadorActual;
    EstadosJuego estado;
    GestorEstados gestorEstados;
    ArrayList<Jugador> jugadores = new ArrayList();
    Tablero tablero;
    MazoSorpresas mazo;
    
    public CivitasJuego(ArrayList<String> nombres){
        
        indiceJugadorActual = 0;
        tablero = new Tablero(10);
        mazo = new MazoSorpresas();
        estado = EstadosJuego.INICIO_TURNO;
        inicializarTablero(mazo);
        inicializarMazoSorpresas(tablero);
        Jugador jugador;
        
        for (int i = 0; i < nombres.size(); i++){
            jugador = new Jugador(nombres.get(i));
            jugadores.add(jugador);
        }
        
    }
    
    private void avanzaJugador() {

        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        int posicionActual = jugadorActual.getNumCasillaActual();
        int tirada = Dado.getInstance().tirar();
        int posicionNueva = tablero.nuevaPosicion(posicionActual, tirada);
        Casilla casilla = tablero.getCasilla(posicionNueva);
        contabilizarPasosPorSalida(jugadorActual);
        jugadorActual.moverACasilla(posicionNueva);
        casilla.recibeJugador(indiceJugadorActual, jugadores);
        contabilizarPasosPorSalida(jugadorActual);
    }
    
    public boolean cancelarHipoteca(int ip){
        
        return getJugadorActual().cancelarHipoteca(ip);
    }
    
    public boolean comprar(){
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        int numCasillaActual = jugadorActual.getNumCasillaActual();
        CasillaCalle casilla = (CasillaCalle) tablero.getCasilla(numCasillaActual);
        TituloPropiedad titulo = casilla.getTituloPropiedad();
        return jugadorActual.comprar(titulo);
    }
    
    public boolean construirCasa(int ip){
        
        return getJugadorActual().construirCasa(ip);
    }
    
    public boolean construirHotel(int ip) {
        
        return getJugadorActual().construirHotel(ip);
    }
    
    private void contabilizarPasosPorSalida(Jugador jugadorActual) {
        
        while (tablero.getPorSalida() > 0) {
            jugadorActual.pasaPorSalida();
        }
    }
    
    public boolean finalDelJuego() {
        boolean out = false;
        
        for (int i = 0; i < jugadores.size() && !out; i++){
            if (jugadores.get(i).enBancarrota())
                out = true;
        }
        
        return out;
    }
    
    public Casilla getCasillaActual() {
        
        return tablero.getCasilla(getJugadorActual().getNumCasillaActual());
    }
    
    public Jugador getJugadorActual(){
        
        return jugadores.get(indiceJugadorActual);
    }
    
    public boolean hipotecar(int ip) {
        
        return getJugadorActual().vender(ip);
    }
    
    public String infoJugadorTexto() {
        
        return getJugadorActual().toString();
    }
    
    private void inicializarMazoSorpresas(Tablero tablero) {
        
        mazo.alMazo(new SorpresaIrCarcel(tablero));
        mazo.alMazo(new SorpresaSalirCarcel(mazo));
        
        mazo.alMazo(new SorpresaIrCasilla(tablero, tablero.getCarcel(), "Ir a la carcel"));
        mazo.alMazo(new SorpresaIrCasilla(tablero, 0, "Ir a la salida"));
        mazo.alMazo(new SorpresaIrCasilla(tablero, 14, "Ir al parking"));
        
        mazo.alMazo(new SorpresaPagarCobrar(-200, "Pagas 200"));
        mazo.alMazo(new SorpresaPagarCobrar(200, "Cobras 200"));
        
        mazo.alMazo(new SorpresaPorCasaHotel(-200, "Pagas por propiedades"));
        mazo.alMazo(new SorpresaPorCasaHotel(200, "Pagas por propiedades"));
        
        mazo.alMazo(new SorpresaPorJugador(-200, "Pagas al resto de jugadores"));
        mazo.alMazo(new SorpresaPorJugador(200, "Cobras del resto de jugadores"));
        
        mazo.alMazo(new SorpresaEspeculador(200.0f));
    }
    
    private void inicializarTablero(MazoSorpresas mazo){
        
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle1", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new CasillaSorpresa(mazo, "Sorpresa1"));
        //tablero.añadeCasilla(new CasillaSorpresa(mazo, "Sorpresa0")); // temporal
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle2", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle3", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new CasillaImpuesto(200.0f, "Impuesto"));
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle4", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle5", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new CasillaSorpresa(mazo, "Sorpresa2"));
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle6", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle7", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new CasillaSorpresa(mazo, "Sorpresa1"));
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle8", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new Casilla("Parking"));
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle9", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle10", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeJuez();
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle11", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
        tablero.añadeCasilla(new CasillaCalle(new TituloPropiedad("Calle12", 10.0f, 10.0f, 10.0f, 10.0f, 10.0f )));
    }
    
    private void pasarTurno(){
        
        indiceJugadorActual = (indiceJugadorActual+1)%jugadores.size();
    }
    
    public ArrayList<Jugador> ranking() {
        
        ArrayList<Jugador> ranking = jugadores;
        Jugador tmp;
        
        for (int j = 0; j < ranking.size(); j++){
            for (int i = ranking.size()-1; i > 0; i--){
                
                if (ranking.get(i).compareTo(ranking.get(i-1)) < 0){
                    
                    tmp = ranking.get(i);
                    ranking.set(i, ranking.get(i-1));
                    ranking.set(i-1, tmp);
                }
            }
        }
        
        return ranking;
    }
    
    public boolean salirCarcelPagando() {
        
        return getJugadorActual().salirCarcelPagando();
    }
    
    public boolean salirCarcelTirando() {
        
        return getJugadorActual().salirCarcelTirando();
    }
    
    public OperacionesJuego siguientePaso() {
        Jugador jugadorActual = jugadores.get(indiceJugadorActual);
        OperacionesJuego operacion = GestorEstados.operacionesPermitidas(jugadorActual, estado);

        switch(operacion){
            case PASAR_TURNO:
                pasarTurno();
                siguientePasoCompletado(operacion);
            break;

            case AVANZAR:
                avanzaJugador();
                siguientePasoCompletado(operacion);
            break;
        }
        
        return operacion;
    }
    
    public void siguientePasoCompletado(OperacionesJuego operacion){

        EstadosJuego nuevoestado = gestorEstados.siguienteEstado(getJugadorActual(), estado, operacion);
        estado = nuevoestado;
    }
    
    public boolean vender(int ip) {
        
        return getJugadorActual().vender(ip);
    }
    
}
