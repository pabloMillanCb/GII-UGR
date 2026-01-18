package juegoTexto;

import civitas.CivitasJuego;
import civitas.GestionesInmobiliarias;
import civitas.OperacionInmobiliaria;
import civitas.OperacionesJuego;
import civitas.Respuestas;
import civitas.SalidasCarcel;
import civitas.Jugador;
import java.util.ArrayList;

public class Controlador {
    private CivitasJuego juego;
    private VistaTextual vista;
    
    Controlador(CivitasJuego civitas, VistaTextual interfaz){
        juego = civitas;
        vista = interfaz;
    }
    
    void juega(){
        vista.setCivitasJuego(juego);

        while(!juego.finalDelJuego()){
            vista.actualizarVista();
            vista.pausa();
            OperacionesJuego operacion = juego.siguientePaso();

            if(operacion != OperacionesJuego.PASAR_TURNO)
                vista.mostrarEventos();

            if(!juego.finalDelJuego())

                switch(operacion){
                    case COMPRAR:
                        
                        if(vista.comprar() == Respuestas.SI){
                            if(!juego.comprar())
                                System.out.print("No se ha comprado la propiedad\n");
                            else
                                System.out.print("Propiedad comprada\n");
                        }

                        else
                            System.out.print("No se ha comprado la propiedad\n");

                        juego.siguientePasoCompletado(operacion);
                    break;

                    case GESTIONAR:
                    
                        vista.gestionar();
                        int gestion = vista.getGestion();
                        int propiedad = vista.getPropiedad();
                        OperacionInmobiliaria nuevaop = new OperacionInmobiliaria(GestionesInmobiliarias.values()[gestion], propiedad);

                        switch(nuevaop.getGestion()){
                            case VENDER:
                                juego.vender(propiedad);
                            break;

                            case HIPOTECAR:
                                juego.hipotecar(propiedad);
                            break;

                            case CANCELAR_HIPOTECA:
                                juego.cancelarHipoteca(propiedad);
                            break;

                            case CONSTRUIR_CASA:
                                juego.construirCasa(propiedad);
                            break;

                            case CONSTRUIR_HOTEL:
                                juego.construirHotel(propiedad);
                            break;

                            case TERMINAR:
                                juego.siguientePasoCompletado(operacion);
                            break;
                        }
                    break;

                    case SALIR_CARCEL:

                        if(vista.salirCarcel() == SalidasCarcel.PAGANDO)
                            juego.salirCarcelPagando();
                        else
                            juego.salirCarcelTirando();

                        juego.siguientePasoCompletado(operacion);
                    break;
                }
            
        }

        ArrayList<Jugador> ranking = juego.ranking();
        for(int i = 0; i<ranking.size(); i++)
            System.out.printf("Puesto número "+(i+1)+": "+ranking.get(i).toString()+" con "+ranking.get(i).getSaldo()+" dineros.\n");
    }
}
