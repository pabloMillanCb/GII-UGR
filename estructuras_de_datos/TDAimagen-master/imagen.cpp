#include "imagen.h"
#include <cassert>

Imagen::Imagen() {

    nf = 0;
    nc = 0;
    img = nullptr;
}

Imagen::Imagen(const Imagen & J) {

    img = nullptr;
    *this = J;
}

Imagen::Imagen(int filas, int cols) {

    allocate(filas, cols);
}

Imagen::~Imagen() {

    deallocate();
}

void Imagen::allocate(const int filas, const int cols) {

    assert( filas > 0 && cols > 0);

    nf = filas;
    nc = cols;
    img = new unsigned char *[filas];

    for (int i = 0; i < filas; i++) {

        img[i] = new unsigned char [cols];
    }
}

void Imagen::deallocate(){

    if (img != nullptr){

        for(int i=0; i < nf; ++i)
            delete[] img[i];

        delete[] img;
    }
}

int Imagen::num_columnas() const {

    return nc;
}

int Imagen::num_filas() const {

    return nf;
}

void Imagen::asigna_pixel(int fila, int col, unsigned char valor) {

    assert(fila < num_filas() && col < num_columnas() && fila >= 0 && col >= 0 && valor <= 255 && valor >= 0);
    img[fila][col] = valor;
}

unsigned char Imagen::valor_pixel(int fila, int col) const {

    assert(fila < num_filas() && col < num_columnas() && fila >= 0 && col >= 0);
    return img[fila][col];
}

Imagen& Imagen::operator=(const Imagen &orig) {
    
    deallocate();
    allocate(orig.num_filas(), orig.num_columnas());

    for (int i = 0; i < orig.num_filas(); i++){
        for (int j = 0; j < orig.num_columnas(); j++){

            asigna_pixel(i, j, orig.valor_pixel(i, j));
        }
    }
    
    return *this;
}