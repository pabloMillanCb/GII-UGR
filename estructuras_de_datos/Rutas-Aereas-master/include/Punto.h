/**
  Fichero: Punto.h
  Alumnos: Jordi Conde Molina
           Pablo Millán Cubero 
  */

#ifndef __PUNTO__
#define __PUNTO__
#include<fstream>
#include<string>

using namespace std;

class Punto {

private:

    double latitud;
    double longitud;

public:

    Punto();
    Punto(const Punto &orig);
    Punto(double lat, double lon);
    Punto(double lat, double lon, string s);

    double GetLongitud() const;
    double GetLatitud() const;

    void SetLongitud(double d);
    void SetLatitud(double d);

    bool operator==(const Punto &p2) const;
    bool operator!=(const Punto &p2) const;
    bool operator<(const Punto &p2) const;
    bool operator>(const Punto &p2) const;

    friend istream& operator>>(istream &is, Punto &p);
    friend ostream& operator<<(ostream &out, const Punto &p);
};
#endif