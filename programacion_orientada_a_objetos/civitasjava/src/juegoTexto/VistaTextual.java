package juegoTexto;

import civitas.CivitasJuego;
import civitas.Diario;
import civitas.OperacionesJuego;
import civitas.SalidasCarcel;
import civitas.Respuestas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import civitas.Casilla;
import civitas.CivitasJuego;
import civitas.Diario;
import civitas.Jugador;
import civitas.OperacionesJuego;
import civitas.Respuestas;
import civitas.SalidasCarcel;
import civitas.TituloPropiedad;

public class VistaTextual {
  
  CivitasJuego juegoModel; 
  int iGestion=-1;
  int iPropiedad=-1;
  private static String separador = "=====================";
  
  private Scanner in;
  
  VistaTextual () {
    in = new Scanner (System.in);
  }
  
  void mostrarEstado(String estado) {
    System.out.println (estado);
  }
              
  void pausa() {
    System.out.print ("Pulsa una tecla");
    in.nextLine();
  }

  int leeEntero (int max, String msg1, String msg2) {
    Boolean ok;
    String cadena;
    int numero = -1;
    do {
      System.out.print (msg1);
      cadena = in.nextLine();
      try {  
        numero = Integer.parseInt(cadena);
        ok = true;
      } catch (NumberFormatException e) { // No se ha introducido un entero
        System.out.println (msg2);
        ok = false;  
      }
      if (ok && (numero < 0 || numero >= max)) {
        System.out.println (msg2);
        ok = false;
      }
    } while (!ok);

    return numero;
  }

  int menu (String titulo, ArrayList<String> lista) {
    String tab = "  ";
    int opcion;
    System.out.println (titulo);
    for (int i = 0; i < lista.size(); i++) {
      System.out.println (tab+i+"-"+lista.get(i));
    }

    opcion = leeEntero(lista.size(),
                          "\n"+tab+"Elige una opción: ",
                          tab+"Valor erróneo");
    return opcion;
  }

  SalidasCarcel salirCarcel() {
    int opcion = menu ("Elige la forma para intentar salir de la carcel",
      new ArrayList<> (Arrays.asList("Pagando","Tirando el dado")));
    return (SalidasCarcel.values()[opcion]);
  }

  Respuestas comprar() {
      int opcion = menu ("¿Quiere comprar la calle?",
        new ArrayList<> (Arrays.asList("No","Sí")));
      return (Respuestas.values()[opcion]);
  }

  void gestionar () {
      int opcion = menu ("Elige una gestión inmobiliaria",
        new ArrayList<> (Arrays.asList("Vender","Hipotecar","Cancelar hipteca",
                              "Construir casa","Construir hotel","Terminar")));

      iGestion = opcion;
      ArrayList<String> propiedades = new ArrayList<String>();

      for(int i = 0; i<juegoModel.getJugadorActual().getPropiedades().size(); i++)
          propiedades.add(juegoModel.getJugadorActual().getPropiedades().get(i).toString());

      if(opcion != 5){
        opcion = menu ("Elige la propiedad", propiedades);
        iPropiedad = opcion;
      }
  }
  
  public int getGestion(){
      return iGestion;
  }
  
  public int getPropiedad(){
      return iPropiedad;
  }
    

  void mostrarSiguienteOperacion(OperacionesJuego operacion) {
      System.out.print("Siguiente operación: "+operacion + "\n");
  }


  void mostrarEventos() {
      while(Diario.getInstance().eventosPendientes())
          System.out.print(Diario.getInstance().leerEvento());
  }
  
  public void setCivitasJuego(CivitasJuego civitas){ 
        juegoModel=civitas;
        this.actualizarVista();

    }
  
  void actualizarVista(){
      System.out.print(separador + "\n");
      System.out.print(juegoModel.getJugadorActual().toString());
      System.out.print(separador + "\n");
      System.out.print(juegoModel.getCasillaActual().toString());
      System.out.print(separador + "\n");
  }
}
