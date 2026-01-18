#include <iostream>
#include <cstdlib>
#include <vector>
#include <queue>
#include "player.h"
#include "environment.h"

using namespace std;

const double masinf=9999999999.0, menosinf=-9999999999.0;


// Constructor
Player::Player(int jug){
    jugador_=jug;
}

// Actualiza el estado del juego para el jugador
void Player::Perceive(const Environment & env){
    actual_=env;
}

double Puntuacion(int jugador, const Environment &estado){
    double suma=0;

    for (int i=0; i<7; i++)
      for (int j=0; j<7; j++){
         if (estado.See_Casilla(i,j)==jugador){
            if (j<3)
               suma += j;
            else
               suma += (6-j);
         }
      }

    return suma;
}

int siguienteJugador(const Environment &estado)
{
   Environment e[8];
   estado.GenerateAllMoves(e);
   return e[0].JugadorActivo();
}


// Funcion de valoracion para testear Poda Alfabeta
double ValoracionTest(const Environment &estado, int jugador){
    int ganador = estado.RevisarTablero();

    if (ganador==jugador)
       return 99999999.0; // Gana el jugador que pide la valoracion
    else if (ganador!=0)
            return -99999999.0; // Pierde el jugador que pide la valoracion
    else if (estado.Get_Casillas_Libres()==0)
            return 0;  // Hay un empate global y se ha rellenado completamente el tablero
    else
          return Puntuacion(jugador,estado);
}

// ------------------- Los tres metodos anteriores no se pueden modificar

bool PerteneceAJugador(char ficha, int jugador)
{
   return (ficha == jugador || ficha == jugador+3);
}

bool CasillaOcupada(Environment estado, int i, int j)
{
   if (i == 0)
      return true;
   else
      return estado.See_Casilla(i-1,j) != 0;
}

double Heuristica(const Environment &estado, int jugador_)
{
   double valoracion = 0;
   int jugador, jugador2;
   int w_vert = 2, w_horz = 4, w_diag = 4;

   if (jugador_ == 1)
   {
      jugador = 1;
      jugador2 = 2;
   }
   else
   {
      jugador = 2;
      jugador2 = 1;
   }

   //Evaluación de columnas (vertical)

   for (int j = 0; j < 7; j++)
   {
      int fichas_j1 = 0;
      int fichas_j2 = 0;

      for (int i = 0; i < 7; i++)
      {
         char ficha = estado.See_Casilla(i,j);
         
         if (i < 4)
         {
            //Si la ficha actual pertenece al jugador 1, sumarla
            if (PerteneceAJugador(ficha, jugador))
            {
               for (i; i < 7 && PerteneceAJugador(estado.See_Casilla(i,j), jugador); i++)
               {
                  fichas_j1++;
               }

               //Si encontramos una fichas del jugador 2 por encima, descartamos los puntos acumulados
               if (i < 7 && PerteneceAJugador(estado.See_Casilla(i,j), jugador2))
                  fichas_j1 = 0;
            }
         }

         if (i < 4)
         {
            
            ficha = estado.See_Casilla(i,j);

            //Si la ficha actual pertenece al jugador 2, sumarla
            if (PerteneceAJugador(ficha, jugador2))
            {
               for (i; i < 7 && PerteneceAJugador(estado.See_Casilla(i,j), jugador2); i++)
               {
                  fichas_j2++;
               }

               //Si encontramos una fichas del jugador 1 por encima, descartamos los puntos acumulados
               if (i < 7 && PerteneceAJugador(estado.See_Casilla(i,j), jugador))
                  fichas_j2 = 0;
            }
         }
      }

      //Añadir a la puntuación las fichas encontradas
      if (fichas_j1 > 1)
         valoracion += fichas_j1*w_vert;
      if (fichas_j2 > 1)
         valoracion -= fichas_j2*w_vert;
   }


   //Evaluación de filas (horizontal)
   for (int i = 0; i < 7; i++)
   {

      int fichas_j1 = 0;
      int fichas_j2 = 0;
      int fichas_blanco = 0;

      for (int j = 0; j < 7; j++)
      {
         int ficha = estado.See_Casilla(i,j);

         //Si encontramos una ficha del jugador 1, ir sumando hasta encontrar una del oponente o terminar
         if (PerteneceAJugador(ficha, jugador))
         {
            fichas_j2 = 0;

            for (j; j < 7 && !PerteneceAJugador(estado.See_Casilla(i,j), jugador2); j++)
            {
               ficha = estado.See_Casilla(i,j);

               if (PerteneceAJugador(ficha, jugador))
                  fichas_j1++;
               
               //Si se encuenta casilla no ocupada, contabilizarla
               else if (ficha == 0 && CasillaOcupada(estado, i, j))
                  fichas_blanco++;
            }

            //Si encontramos una ficha del jugador 2, retroceder el contador hasta la ficha del jugador 1
            if (PerteneceAJugador(estado.See_Casilla(i,j), jugador2))
            {
               while (!PerteneceAJugador(estado.See_Casilla(i,j), jugador))
                  j--;
            }

            //Si hay posibilidades de hacer conecta 4, sumo los puntos
            if (fichas_j1 + fichas_blanco >= 4 && fichas_j1 > 1)
               valoracion += fichas_j1*fichas_j1*w_horz;
         }

         //Si encontramos una ficha del jugador 2, ir sumando hasta encontrar una del oponente o terminar
         else if (PerteneceAJugador(ficha, jugador2))
         {

            fichas_j1 = 0;

            for (j; j < 7 && !PerteneceAJugador(estado.See_Casilla(i,j), jugador); j++)
            {
               ficha = estado.See_Casilla(i,j);

               if (PerteneceAJugador(ficha, jugador2))
                  fichas_j2++;

               //Si se encuenta casilla no ocupada, contabilizarla
               else if (ficha == 0 && CasillaOcupada(estado, i, j))
                  fichas_blanco++;
            }

            //Si encontramos una ficha del jugador 1, retroceder el contador hasta la ficha del jugador 2
            if (PerteneceAJugador(estado.See_Casilla(i,j), jugador))
            {
               while (!PerteneceAJugador(estado.See_Casilla(i,j), jugador2))
                  j--;
            }

            //Si hay posibilidades de hacer conecta 4, resto los puntos
            if (fichas_j2 + fichas_blanco >= 4 && fichas_j2 > 1)
               valoracion -= fichas_j2*fichas_j2*w_horz;
         }

         //Si se encuentra una casilla no ocupada, contabilizarla
         else if (ficha == 0)
         {
            fichas_blanco++;
         }
      }
   }

   int t = 0;
   int s = 3;

   //Evaluación de diagonales 1
   for (int k = 0; k < 7 ; k++)
   {
      int i = t;
      int j = s;

      int fichas_j1 = 0;
      int fichas_j2 = 0;
      int fichas_blanco = 0;

      while((j < 7 && s > 0) || (i < 7 && s == 0))
      {
         int ficha = estado.See_Casilla(i,j);
         
         //Si encontramos una ficha del jugador 1, ir sumando hasta encontrar una del oponente o terminar
         if (PerteneceAJugador(ficha, jugador))
         {
            fichas_j2 = 0;

            for (j; ( ((j < 7 && s > 0) || (i < 7 && s == 0)) && !PerteneceAJugador(estado.See_Casilla(i,j), jugador2) ) ; j++, i++)
            {
               ficha = estado.See_Casilla(i,j);

               if (PerteneceAJugador(ficha, jugador))
                  fichas_j1++;

               //Si se encuenta casilla no ocupada, contabilizarla
               else if (ficha == 0 && CasillaOcupada(estado, i, j))
                  fichas_blanco++;
            }

            //Si encontramos una ficha del jugador 2, retroceder el contador hasta la ficha del jugador 1
            if ( ((j < 7 && s > 0) || (i < 7 && s == 0)) && PerteneceAJugador(estado.See_Casilla(i,j), jugador2))
            {
               while (!PerteneceAJugador(estado.See_Casilla(i,j), jugador))
               {
                  j--;
                  i--;
               }
            }

            if (fichas_j1 + fichas_blanco >= 4 && fichas_j1 > 1)
               valoracion += fichas_j1*w_diag;
         }

         //Si encontramos una ficha del jugador 2, ir sumando hasta encontrar una del oponente o terminar
         if (PerteneceAJugador(ficha, jugador2))
         {
            fichas_j1 = 0;

            for (j; ( ((j < 7 && s > 0) || (i < 7 && s == 0)) && !PerteneceAJugador(estado.See_Casilla(i,j), jugador) ) ; j++, i++)
            {
               ficha = estado.See_Casilla(i,j);

               if (PerteneceAJugador(ficha, jugador2))
                  fichas_j2++;

               //Si se encuenta casilla no ocupada, contabilizarla
               else if (ficha == 0 && CasillaOcupada(estado, i, j))
                  fichas_blanco++;
            }

            //Si encontramos una ficha del jugador 1, retroceder el contador hasta la ficha del jugador 2
            if ( ((j < 7 && s > 0) || (i < 7 && s == 0)) && PerteneceAJugador(estado.See_Casilla(i,j), jugador))
            {
               while (!PerteneceAJugador(estado.See_Casilla(i,j), jugador2))
               {
                  j--;
                  i--;
               }
            }

            if (fichas_j2 + fichas_blanco >= 4 && fichas_j2 > 1)
               valoracion -= fichas_j2*w_diag;
         }

         //Si se encuenta casilla no ocupada, contabilizarla
         else if (ficha == 0)
         {
            fichas_blanco++;
         }

         i++; j++;
      }

      if (s > 0)
         s--;
      else
         t++;
   }

   t = 0;
   s = 3;

   //Evaluación de diagonales 2
   for (int k = 0; k < 7 ; k++)
   {
      int i = t;
      int j = s;

      int fichas_j1 = 0;
      int fichas_j2 = 0;
      int fichas_blanco = 0;

      while((j > 0 && s < 7) || (i < 7 && s == 6))
      {
         int ficha = estado.See_Casilla(i,j);
            
         //Si encontramos una ficha del jugador 1, ir sumando hasta encontrar una del oponente o terminar
         if (PerteneceAJugador(ficha, jugador))
         {
            fichas_j2 = 0;

            for (j; ( ((j >= 0 && s < 7) || (i < 7 && s == 7)) && !PerteneceAJugador(estado.See_Casilla(i,j), jugador2)); j--, i++)
            {
               ficha = estado.See_Casilla(i,j);

               if (PerteneceAJugador(ficha, jugador))
                  fichas_j1++;

               //Si se encuenta casilla no ocupada, contabilizarla
               else if (ficha == 0 && CasillaOcupada(estado, i, j))
                  fichas_blanco++;
            }

            //Si encontramos una ficha del jugador 2, retroceder el contador hasta la ficha del jugador 1
            if (((j >= 0 && s < 7) || (i < 7 && s == 7)) && PerteneceAJugador(estado.See_Casilla(i,j), jugador2))
            {
               while (!PerteneceAJugador(estado.See_Casilla(i,j), jugador))
               {
                  j++;
                  i--;
               }
            }

            if (fichas_j1 + fichas_blanco >= 4 && fichas_j1 > 1)
               valoracion += fichas_j1*w_diag;
         }

         //Si encontramos una ficha del jugador 2, ir sumando hasta encontrar una del oponente o terminar
         if (PerteneceAJugador(ficha, jugador2))
         {
            fichas_j1 = 0;

            for (j; (((j >= 0 && s < 7) || (i < 7 && s == 7)) && !PerteneceAJugador(estado.See_Casilla(i,j), jugador)); j--, i++)
            {
               ficha = estado.See_Casilla(i,j);

               if (PerteneceAJugador(ficha, jugador2))
                  fichas_j2++;

               //Si se encuenta casilla no ocupada, contabilizarla
               else if (ficha == 0 && CasillaOcupada(estado, i, j))
                  fichas_blanco++;
            }

            //Si encontramos una ficha del jugador 1, retroceder el contador hasta la ficha del jugador 2
            if (((j >= 0 && s < 7) || (i < 7 && s == 7)) && PerteneceAJugador(estado.See_Casilla(i,j), jugador))
            {
               while (!PerteneceAJugador(estado.See_Casilla(i,j), jugador2))
               {
                  j++;
                  i--;
               }
            }

            if (fichas_j2 + fichas_blanco >= 4 && fichas_j2 > 1)
               valoracion -= fichas_j2*w_diag;

         }

         //Si se encuenta casilla no ocupada, contabilizarla
         else if (ficha == 0)
         {
            fichas_blanco++;
         }

         i++; j--;
      }

      if (s < 7)
         s++;
      else
         t++;
   }

   return valoracion;
}

// Funcion heuristica (ESTA ES LA QUE TENEIS QUE MODIFICAR)
double Valoracion(const Environment &estado, int jugador, int profundidad){

   int ganador = estado.RevisarTablero();

    if (ganador==jugador)
       return 10000*(9-profundidad);//*(9-profundidad); // Gana el jugador que pide la valoracion
    else if (ganador!=0)
            return -10000*(9-profundidad);//-(1000*(9-profundidad)); // Pierde el jugador que pide la valoracion
    else if (estado.Get_Casillas_Libres()==0)
            return 0;  // Hay un empate global y se ha rellenado completamente el tablero
    else
          return Heuristica(estado, jugador);

}

// Esta funcion no se puede usar en la version entregable
// Aparece aqui solo para ILUSTRAR el comportamiento del juego
// ESTO NO IMPLEMENTA NI MINIMAX, NI PODA ALFABETA
void JuegoAleatorio(bool aplicables[], int opciones[], int &j){
    j=0;
    for (int i=0; i<8; i++){
        if (aplicables[i]){
           opciones[j]=i;
           j++;
        }
    }
}


int Poda_AlfaBeta(Environment const &actual_, int jugador_, Environment::ActionType &accion, int profundidad_,  int PROFUNDIDAD_ALFABETA, int alfa_, int beta_)
{
   
   Environment acciones[8], salida;
   int n_acciones;
   double alfa = alfa_, beta = beta_;

   bool buscando = true;

   n_acciones = actual_.GenerateAllMoves(acciones);

   //Comprobación de si ha ganado alguien, para dejar de generar estados
   if (actual_.JuegoTerminado() || profundidad_ == PROFUNDIDAD_ALFABETA )//|| (profundidad_ == PROFUNDIDAD_ALFABETA-1 && actual_.JugadorActivo() == jugador_)
      return Valoracion(actual_, jugador_, profundidad_);

   //Bucle principal para la generación de nodos si no es un nodo hoja
   for (int i = 0; i < n_acciones && buscando; i++)
   {
      //Se llama recursivamente a la función para cada uno de los hasta 49 hijos generados
      int puntuacion = Poda_AlfaBeta(acciones[i], jugador_, accion, profundidad_+1, PROFUNDIDAD_ALFABETA, alfa, beta);

      if (profundidad_ == 0)
         cout << "Puntos: " << puntuacion << "\t\tAccion: " << (int)acciones[i].Last_Action(jugador_) << endl;

      //Si es un nodo max, actualizar valor maximo/alfa si procede
      if (jugador_ == actual_.JugadorActivo())
      {
         if (puntuacion > alfa)
         {
            alfa = puntuacion;

            if (profundidad_ == 0)
               accion = static_cast< Environment::ActionType >(acciones[i].Last_Action(jugador_));
         }
      }

      //Si es un nodo min, actualizar valor minimo/beta si procede
      else
      {
         if (puntuacion < beta)
         {
            beta = puntuacion;
         }
      }

      //Si alfa es mayor o igual que beta, se realiza poda alfa/beta
      if (alfa >= beta)
      {
         buscando = false;
      }
   }

   //Si no es el nodo raíz, devolver valor maximo/minimo
   if (jugador_ == actual_.JugadorActivo())
      return alfa;
   else
      return beta;
}

// Invoca el siguiente movimiento del jugador
Environment::ActionType Player::Think(){
    const int PROFUNDIDAD_MINIMAX = 6;  // Umbral maximo de profundidad para el metodo MiniMax
    const int PROFUNDIDAD_ALFABETA = 8; // Umbral maximo de profundidad para la poda Alfa_Beta

    Environment::ActionType accion; // acci�n que se va a devolver
    bool aplicables[8]; // Vector bool usado para obtener las acciones que son aplicables en el estado actual. La interpretacion es
                        // aplicables[0]==true si PUT1 es aplicable
                        // aplicables[1]==true si PUT2 es aplicable
                        // aplicables[2]==true si PUT3 es aplicable
                        // aplicables[3]==true si PUT4 es aplicable
                        // aplicables[4]==true si PUT5 es aplicable
                        // aplicables[5]==true si PUT6 es aplicable
                        // aplicables[6]==true si PUT7 es aplicable
                        // aplicables[7]==true si BOOM es aplicable



    double valor; // Almacena el valor con el que se etiqueta el estado tras el proceso de busqueda.
    double alpha, beta; // Cotas de la poda AlfaBeta

    int n_act; //Acciones posibles en el estado actual


    n_act = actual_.possible_actions(aplicables); // Obtengo las acciones aplicables al estado actual en "aplicables"
    int opciones[10];

    // Muestra por la consola las acciones aplicable para el jugador activo
    //actual_.PintaTablero();
    cout << " Acciones aplicables ";
    (jugador_==1) ? cout << "Verde: " : cout << "Azul: ";
    for (int t=0; t<8; t++)
      if (aplicables[t])
         cout << " " << actual_.ActionStr( static_cast< Environment::ActionType > (t)  );
    cout << endl;

   cout << "njugada: " << actual_.N_Jugada() << endl;
    
    /*
    //--------------------- COMENTAR Desde aqui
    cout << "\n\t";
    int n_opciones=0;
    JuegoAleatorio(aplicables, opciones, n_opciones);

    if (n_act==0){
      (jugador_==1) ? cout << "Verde: " : cout << "Azul: ";
      cout << " No puede realizar ninguna accion!!!\n";
      //accion = Environment::actIDLE;
    }
    else if (n_act==1){
           (jugador_==1) ? cout << "Verde: " : cout << "Azul: ";
            cout << " Solo se puede realizar la accion "
                 << actual_.ActionStr( static_cast< Environment::ActionType > (opciones[0])  ) << endl;
            accion = static_cast< Environment::ActionType > (opciones[0]);

         }
         else { // Hay que elegir entre varias posibles acciones
            int aleatorio = rand()%n_opciones;
            cout << " -> " << actual_.ActionStr( static_cast< Environment::ActionType > (opciones[aleatorio])  ) << endl;
            accion = static_cast< Environment::ActionType > (opciones[aleatorio]);
         }

    //--------------------- COMENTAR Hasta aqui
    */
    //--------------------- AQUI EMPIEZA LA PARTE A REALIZAR POR EL ALUMNO ------------------------------------------------

   valor = Poda_AlfaBeta(actual_, jugador_, accion, 0, 8, -100000, 100000);

    // Opcion: Poda AlfaBeta
    // NOTA: La parametrizacion es solo orientativa
    // valor = Poda_AlfaBeta(actual_, jugador_, 0, PROFUNDIDAD_ALFABETA, accion, alpha, beta);
    cout << "Valor MiniMax: " << valor << "  Accion: " << actual_.ActionStr(accion) << endl;

    return accion;
}

