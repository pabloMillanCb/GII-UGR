/**
  Fichero: Punto.cpp
  Alumnos: Jordi Conde Molina
           Pablo Millán Cubero 
  */

#include"../include/Punto.h"
#include <iostream>

Punto::Punto() {

    latitud = 0.0;
    longitud = 0.0;
}

Punto::Punto(const Punto &orig) {

    latitud = orig.GetLatitud();
    longitud = orig.GetLongitud();
}

Punto::Punto(double lat, double lon) {

    latitud = lat;
    longitud = lon;
}

Punto::Punto(double lat, double lon, string s) {

    latitud = lat;
    longitud = lon;
}

double Punto::GetLatitud() const {

    return latitud;
}

double Punto::GetLongitud() const {

    return longitud;
}

void Punto::SetLatitud(double d) {

    latitud = d;
}

void Punto::SetLongitud(double d) {

    longitud = d;
}

bool Punto::operator==(const Punto &p2) const {

    return longitud == p2.GetLongitud() && latitud == p2.GetLatitud();
}

bool Punto::operator!=(const Punto &p2) const {

    return longitud != p2.GetLongitud() || latitud != p2.GetLatitud();
}

bool Punto::operator<(const Punto &p2) const {

    bool salida = latitud == p2.GetLatitud();

    if (salida) {

        salida = abs(longitud) > abs(p2.GetLongitud());

    }

    else
        salida = abs(latitud) > abs(p2.GetLatitud());
    
    return salida;
}

bool Punto::operator>(const Punto &p2) const {

    bool salida = latitud == p2.GetLatitud();

    if (salida)
        salida = abs(longitud) < abs(p2.GetLongitud());

    else
        salida = abs(latitud) < abs(p2.GetLatitud());
    
    return salida;
}

ostream& operator<<(ostream &out, const Punto &p) {

    string salida = "(";
    
    salida += to_string(p.GetLatitud());
    salida += ",";
    salida += to_string(p.GetLongitud());
    salida += ")";

    out << salida;

    return out;
}

istream& operator>>(istream &is, Punto &p) {
    double lat, lon;
    string tmp;

    while(is.peek() == ' ')
        is.get();
    is.get(); //leemos (

    while(is.peek() == ' ')
        is.get();

    is >> lat;
    
    while(is.peek() != ',')
        is.get();

    is.get(); //leemos ,

    while(is.peek() == ' ')
        is.get();

    is >> lon;

    while(is.peek() != ')')
        is.get();

    //cout << "el parentesi " << (char)is.peek() << endl;
    is.get();//leemos )

    p.SetLatitud(lat);
    p.SetLongitud(lon);

    //cout << p << endl;

    return is;
}