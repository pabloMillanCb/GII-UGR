#include "imagen.h"
#include "imagenES.h"
#include <iostream>

using namespace std;


Imagen ConstruirImagen(int nfilas, int ncols, unsigned char *l){

    Imagen tmp(nfilas, ncols);
    int n = 0;

    for (int i = 0; i < nfilas; i++){
        for (int j = 0; j < ncols; j++){
            tmp.asigna_pixel(i, j, l[n]);
            n++;
        }
    }

    delete [] l;
    return tmp;
}

unsigned char* DeconstruirImagen(Imagen &imag){

    unsigned char *escritura;
    escritura = new unsigned char [imag.num_filas() * imag.num_columnas()];
    int n = 0;

    for (int i = 0; i < imag.num_filas(); i++){
        for (int j = 0; j < imag.num_columnas(); j++){

            escritura[n] = imag.valor_pixel(i,j);
            n++;
        }
    }

    return escritura;
}

void ConvertirRGB(const char *fichE, const char *fichS) {

    int fils, cols;
    long int n = 0;
    unsigned char *ppm = LeerImagenPPM(fichE, fils, cols);
    Imagen imagen(fils, cols);

    for (int i = 0; i < imagen.num_filas(); i++){
        for (int j = 0; j < imagen.num_columnas(); j++){


            unsigned char valor = (ppm[n++]*0.2989) + (ppm[n++]*0.587) + (ppm[n++]*0.114);
            imagen.asigna_pixel(i, j, valor);
        }
    }

    delete [] ppm;

    ppm = DeconstruirImagen(imagen);
    EscribirImagenPGM(fichS, ppm, imagen.num_filas(), imagen.num_columnas());

    delete [] ppm;
}

void UmbralizarEscalaGrises(const char *fichE, const char *fichS, int T_1, int T_2){
    int filas, columnas;
    unsigned char *pgm;

    if(T_1 > T_2){
        cerr << "Error: T_1 debe ser MENOR que T_2." << endl;
        cerr << "Terminando la ejecucion del programa." << endl;
        exit (1);
    }

    pgm = LeerImagenPGM(fichE, filas, columnas);

    if(!pgm){
        cerr << "Error: No pudo leerse la imagen." << endl;
        cerr << "Terminando la ejecucion del programa." << endl;
        exit (1);
    }

    Imagen a_modificar = ConstruirImagen(filas, columnas, pgm);

    for (int i = 0; i < a_modificar.num_filas(); i++){
        for (int j = 0; j < a_modificar.num_columnas(); j++){

            if(a_modificar.valor_pixel(i, j) < T_1 || a_modificar.valor_pixel(i, j) > T_2)
                a_modificar.asigna_pixel(i, j, 255);
        }
    }

    pgm = DeconstruirImagen(a_modificar);
    EscribirImagenPGM(fichS, pgm, a_modificar.num_filas(), a_modificar.num_columnas());

    delete [] pgm;
}

void UmbralizarAutomatico(const char *fichE, const char *fichS, float& T){
    int filas, columnas;
    unsigned char *pgm;
    pgm = LeerImagenPGM(fichE, filas, columnas);

    if(!pgm){
        cerr << "Error: No pudo leerse la imagen." << endl;
        cerr << "Terminando la ejecucion del programa." << endl;
        exit (1);
    }

    Imagen a_modificar = ConstruirImagen(filas, columnas, pgm);
    Imagen I_1(a_modificar), I_2(a_modificar);

    int media = 0;

    for (int i = 0; i < a_modificar.num_filas(); i++)
        for (int j = 0; j < a_modificar.num_columnas(); j++)
            media += a_modificar.valor_pixel(i, j);
    media = media / (a_modificar.num_filas()*a_modificar.num_columnas());

    float submedia1 = 0, submedia2 = 0;
    int pixeles1 = 0, pixeles2 = 0;

    for (int i = 0; i < I_1.num_filas(); i++){
        for (int j = 0; j < I_1.num_columnas(); j++){

            if(I_1.valor_pixel(i, j) > media){
                I_1.asigna_pixel(i, j, 255);
            }
            else{
                submedia1 += I_1.valor_pixel(i, j);
                pixeles1++;
            }

            if(I_2.valor_pixel(i, j) <= media){
                I_2.asigna_pixel(i, j, 0);
            }
            else{
                submedia2 += I_2.valor_pixel(i, j);
                pixeles2++;
            }
        }
    }
    submedia1 = submedia1 / pixeles1;
    submedia2 = submedia2 / pixeles2;
    T = (submedia1 + submedia2) / 2;
    float T_ans = 0, submedia1_ans, submedia2_ans;

    while(abs(T - T_ans) >= 0.0001){

        T_ans = T;
        submedia1_ans = submedia1;
        submedia2_ans = submedia2;
        pixeles1 = 0;
        pixeles2 = 0;
        submedia1 = 0;
        submedia2 = 0;

        for (int i = 0; i < a_modificar.num_filas(); i++){
            for (int j = 0; j < a_modificar.num_columnas(); j++){

                if(I_1.valor_pixel(i, j) > submedia1_ans){
                    I_1.asigna_pixel(i, j, 255);
                }
                else{
                    submedia1 += I_1.valor_pixel(i, j);
                    pixeles1++;
                }

                if(I_2.valor_pixel(i, j) < submedia2_ans){
                    I_2.asigna_pixel(i, j, 0);
                }
                else{
                    submedia2 += I_2.valor_pixel(i, j);
                    pixeles2++;
                }
            }
        }
        submedia1 = submedia1 / pixeles1;
        submedia2 = submedia2 / pixeles2;
        T = (submedia1 + submedia2) / 2;
    }

    for (int i = 0; i < a_modificar.num_filas(); i++)
        for (int j = 0; j < a_modificar.num_columnas(); j++)
            if(a_modificar.valor_pixel(i, j) <= T)
                a_modificar.asigna_pixel(i, j, 255);

    pgm = DeconstruirImagen(a_modificar);
    EscribirImagenPGM(fichS, pgm, a_modificar.num_filas(), a_modificar.num_columnas());

    delete [] pgm;
}

void Zoom(const char *fichE, const char *fichS, int x_1, int y_1, int x_2, int y_2) {

    int fils, cols, c = x_2 - x_1, f = y_2 - y_1, nfils = 2*f-1, ncols = 2*c-1;
    unsigned char *pgm = LeerImagenPGM(fichE, fils, cols);
    Imagen imagenE, imagenS(nfils, ncols);

    if (!pgm || nfils != ncols) {

        cerr << "Error: No pudo leerse la imagen." << endl;
        cerr << "Terminando la ejecucion del programa." << endl;
        exit (1);
    }

    imagenE = ConstruirImagen(fils, cols, pgm);

    for (int i = 0; i < f; i++){
        for (int j = 0; j < c; j++){

            imagenS.asigna_pixel(i*2, j*2, imagenE.valor_pixel(i+y_1, j+x_1));                
        }
    }

    imagenE = imagenS;


    for (int j = 1; j < ncols; j+=2){
        for (int i = 0; i < nfils; i++)

            imagenS.asigna_pixel(i, j, (imagenS.valor_pixel(i, j-1)+imagenS.valor_pixel(i, j+1))/2);
    }

        for (int i = 1; i < nfils; i+=2){
        for (int j = 0; j < ncols; j++)

            imagenS.asigna_pixel(i, j, (imagenS.valor_pixel(i-1, j)+imagenS.valor_pixel(i+1, j))/2);
    }

    pgm = DeconstruirImagen(imagenS);
    EscribirImagenPGM(fichS, pgm, imagenS.num_filas(), imagenS.num_columnas());

    delete [] pgm;

}

void Icono(const char *fichE, const char *fichS, int fils, int cols) {

    int f, c, a, b;
    unsigned char *pgm = LeerImagenPGM(fichE, f, c), buffer[fils*cols];
    Imagen imagenG, imagen(fils,cols);

    if (!pgm && fils != cols) {

        cerr << "Error: No pudo leerse la imagen." << endl;
        cerr << "Terminando la ejecucion del programa." << endl;
        exit (1);
    }

    imagenG = ConstruirImagen(f, c, pgm);

    a = imagenG.num_filas()/fils;
    b = imagenG.num_columnas()/cols;
    
    for (int k = 0; k/a < imagen.num_filas(); k += a){
        for (int t = 0; t/b < imagen.num_columnas(); t += b){
            
            double media = 0;

            int n = 0;

            for (int i = 0; i < a; i++)
                for (int j = 0; j < b; j++)
                    media += imagenG.valor_pixel(i+k, j+t);

            media = media/(a*b);
            imagen.asigna_pixel(k/a, t/b, media);
        }
    }

    pgm = DeconstruirImagen(imagen);

    EscribirImagenPGM(fichS, pgm, imagen.num_filas(), imagen.num_columnas());

    delete [] pgm;
}

void ContrasteTransformacionLineal(const char *fichE, const char *fichS, const int min, const int max){
    int filas, columnas;
    unsigned char *pgm;

    if(min > max){
        cerr << "Error: min debe ser MENOR que max." << endl;
        cerr << "Terminando la ejecucion del programa." << endl;
        exit (1);
    }

    pgm = LeerImagenPGM(fichE, filas, columnas);

    if(!pgm){
        cerr << "Error: No pudo leerse la imagen." << endl;
        cerr << "Terminando la ejecucion del programa." << endl;
        exit (1);
    }

    Imagen original = ConstruirImagen(filas, columnas, pgm);
    Imagen a_modificar(original);
    int a = 255, b = 0;

    for (int i = 0; i < original.num_filas(); i++)
        for (int j = 0; j < original.num_columnas(); j++){

            if (original.valor_pixel(i, j) < a)
                a = original.valor_pixel(i, j);
            if (original.valor_pixel(i, j) > b)
                b = original.valor_pixel(i, j);
        }

    const double constante = (long double) (max - min) / (b - a);
    double nuevo_valor;

    for (int i = a; i <= b; i++){
        nuevo_valor = min + (constante * (i - a));
        for (int j = 0; j < original.num_filas(); j++)
            for (int k = 0; k < original.num_columnas(); k++)
                if (original.valor_pixel(j, k) == i){

                    a_modificar.asigna_pixel(j, k, nuevo_valor);
                }
    }

    pgm = DeconstruirImagen(a_modificar);
    EscribirImagenPGM(fichS, pgm, a_modificar.num_filas(), a_modificar.num_columnas());

    delete [] pgm;
}

void Morphing (const char *fich_orig, const char *fich_rdo, int fich_intermedios) {

    unsigned char *pgm_1, *pgm_2, *pgm_s;
    int filas, columnas, n=0;
    float k_final = min(256, fich_intermedios) +1, k = 1/k_final;
    bool procesando = true;
    Imagen img_orig, img_rdo;

    pgm_1 = LeerImagenPGM(fich_orig, filas, columnas);
    img_orig = ConstruirImagen(filas, columnas, pgm_1);
    pgm_2 = LeerImagenPGM(fich_rdo, filas, columnas);
    img_rdo = ConstruirImagen(filas, columnas, pgm_2);

    if (!pgm_1 || !pgm_2) {

        cerr << "Error: No pudo leerse la imagen." << endl;
        cerr << "Terminando la ejecucion del programa." << endl;
        exit (1);
    }

    Imagen img_inter(min(img_orig.num_filas(), img_rdo.num_filas()), min(img_orig.num_columnas(), img_rdo.num_columnas()));

    while (k < 1) {

        for (int i = 0; i < img_inter.num_filas(); i++) {
            for (int j = 0; j < img_inter.num_columnas(); j++) {

                img_inter.asigna_pixel(i, j, (1.0-k)*img_orig.valor_pixel(i, j) + k*img_rdo.valor_pixel(i, j));
            }
        }

        k += 1/k_final;

        string salida = "frame" + to_string(n);
        pgm_s = DeconstruirImagen(img_inter);
        EscribirImagenPGM(salida.c_str(), pgm_s, img_inter.num_filas(), img_inter.num_columnas());
        n++;
        delete [] pgm_s;
    }
}

main (){

    int opcion;
    string fichE, fichS;

    cout << "Elige una opción:" << endl << endl
         << "0. Conversión de RGB a niveles de gris." << endl
         << "1. Umbralizar una imagen usando una escala de grises." << endl
         << "2. Umbralizar una imagen de forma automática." << endl
         << "3. Zoom de una porción de la imagen." << endl
         << "4. Crear un icono a partir de una imagen." << endl
         << "5. Aumento de contraste de una imagen mediante transformación lineal." << endl
         << "6. Morphing." << endl << endl;

    cin >> opcion;

    switch (opcion) {
        case 0:{
            cout << "Conversión de RGB a niveles de gris." << endl
                 << "Introduce el nombre de la imagen de entrada (PPM) y el de la imagen de salida (PGM):" <<endl;
            cin >> fichE >> fichS;

            ConvertirRGB(fichE.c_str(), fichS.c_str());

            cout << endl << "Éxito: imagen guardada en " << fichS << "." << endl;
        }   break;
        case 1:{ 
            cout << "Umbralizar una imagen usando una escala de grises." << endl
                 << "Introduce, en este orden, nombre del fichero de entrada, del fichero de salida,"
                 << " nivel inferior y nivel superior del intervalo de umbralización:" << endl;

            int T_1, T_2;
            cin >> fichE >> fichS >> T_1 >> T_2;

            UmbralizarEscalaGrises(fichE.c_str(), fichS.c_str(), T_1, T_2);

            cout << endl << "Éxito: imagen guardada en " << fichS << "." << endl;
        }   break;
        case 2:{
            cout << "Umbralizar una imagen de forma automática." << endl
                 << "Introduce los nombres de la imagen de entrada y de salida:" << endl;

            float T;
            cin >> fichE >> fichS;

            UmbralizarAutomatico(fichE.c_str(), fichS.c_str(), T);

            cout << endl << "Éxito: imagen guardada en " << fichS << " (umbral automático: " << T << ")." << endl;
        }   break;
        case 3:{
            cout << "Zoom de una porción de la imagen." << endl
                 << "Introduce los nombres de la imagen de entrada y de salida y las coordenadas de las esquinas"
                 << " superior izquierda e inferior derecha respectivamente (deben formar un cuadrado):" << endl;

            int x_1, y_1, x_2, y_2;
            cin >> fichE >> fichS >> x_1 >> y_1 >> x_2 >> y_2;

            Zoom(fichE.c_str(), fichS.c_str(), x_1, y_1, x_2, y_2);

            cout << endl << "Éxito: imagen guardada en " << fichS << "." << endl;
        }   break;
        case 4:{
            cout << "Crear un icono a partir de una imagen." << endl
                 << "Introduce los nombres de la imagen de entrada y de salida y el número de filas y columnas del icono:" << endl;

            int nf, nc;
            cin >> fichE >> fichS >> nf >> nc;

            Icono(fichE.c_str(), fichS.c_str(), nf, nc);

            cout << endl << "Éxito: imagen guardada en " << fichS << "." << endl;
        }   break;
        case 5:{
            cout << "Aumento de contraste de una imagen mediante una transformación lineal." << endl
                 << "Introduce los nombres de la imagen de entrada y de salida y el mínimo y máximo del nuevo rango de la imagen:" << endl;
            int min, max;
            cin >> fichE >> fichS >> min >> max;

            ContrasteTransformacionLineal(fichE.c_str(), fichS.c_str(), min, max);

            cout << endl << "Éxito: imagen guardada en " << fichS << "." << endl;
        }   break;
        case 6:{
            cout << "Morphing." << endl
                 << "Introduce los nombres de la imagen inicial, final y numero de pasos en ese orden:" << endl;
            int fich_intermedios;
            cin >> fichE >> fichS >> fich_intermedios;

            Morphing(fichE.c_str(), fichS.c_str(), fich_intermedios);

            cout << endl << "Éxito: imagenes guardadas." << endl;
        }   break;
        default:{
            cout << "Error: " << opcion << " no es una opción seleccionable." << endl;
        }   break;
    }
}