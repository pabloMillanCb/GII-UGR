package juegoTexto;

import civitas.CivitasJuego;
import java.util.ArrayList;
import civitas.Dado;

public class TestP3 {
    public static void main(String[] args){
        
        Dado.getInstance().setDebug(true);
        
        ArrayList<String> nombres = new ArrayList();
        for(int i = 1; i <= 1; i++){

            nombres.add("Jugador "+i);
        }

        VistaTextual vista = new VistaTextual();
        CivitasJuego juego = new CivitasJuego(nombres);
        //Dado.getInstance().setDebug(true);
        Controlador controlador = new Controlador(juego, vista);

        controlador.juega();
    }
}
