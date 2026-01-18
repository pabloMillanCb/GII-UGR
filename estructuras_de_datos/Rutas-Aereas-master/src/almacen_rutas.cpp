/**
  Fichero: almacen_rutas.cpp
  Alumnos: Jordi Conde Molina
           Pablo Millán Cubero 
  */

#include "../include/almacen_rutas.h"
#include <fstream>
#include <iostream>

using namespace std;

//class almacen rutas

void Almacen_Rutas::Insertar(const pair<string, Ruta> &R)
{
    datos.insert(R);
    Ruta::const_iterator it;
    for (it = R.second.cbegin(); it != R.second.cend(); ++it){
        pair<Punto,string> p((*it),R.first);
        puntos.insert(p);
    }
}

void Almacen_Rutas::Borrar(const string R)
{
    datos.erase(R);
    multimap<Punto,string>::iterator it;
    for (it = puntos.begin(); it != puntos.end(); ++it){
        if((*it).second == R){
            puntos.erase(it);
            --it;
        }
    }
}

Ruta Almacen_Rutas::GetRuta(const string R)
{
    map<string, Ruta>::iterator it;
    it = datos.find(R);
    return (*it).second;
}

Almacen_Rutas Almacen_Rutas::GetRutas_Con_Punto(const Punto &P)
{
    Almacen_Rutas rlocal;
    multimap<Punto,string>::iterator it;
    pair<multimap<Punto,string>::iterator, multimap<Punto,string>::iterator> ret;
    ret = puntos.equal_range(P);
    for(it = ret.first; it != ret.second; ++it){
        pair<string,Ruta> R((*it).second, GetRuta((*it).second));
        rlocal.Insertar(R);
    }
    return rlocal;
}

Almacen_Rutas::iterator Almacen_Rutas::begin()
{
    iterator it;
    it.p = datos.begin();
    return it;
}

Almacen_Rutas::const_iterator Almacen_Rutas::cbegin() const
{
    const_iterator it;
    it.p = datos.begin();
    return it;
}

Almacen_Rutas::iterator Almacen_Rutas::end()
{
    iterator it;
    it.p = datos.end();
    return it;
}

Almacen_Rutas::const_iterator Almacen_Rutas::cend() const
{
    const_iterator it;
    it.p = datos.end();
    return it;
}

Almacen_Rutas::iterator Almacen_Rutas::find(const string p)
{
    iterator it;
    map<string, Ruta>::iterator i;
    for (i = datos.begin(); i != datos.end() && !(i->first == p); ++i)
        ;
    it.p = i;
    return it;
}

istream &operator>>(istream &is, Almacen_Rutas &R)
{

    if (is.peek() == '#')
    {

        string a;
        getline(is, a);
        char c = is.peek();

        pair<string, Ruta> P;
        while (is >> P.first && !is.eof())
        {

            while (is.peek() == ' ' || is.peek() == '\n')
                is.get();

            is >> P.second;

            R.Insertar(P);

            while (is.peek() == ' ' || is.peek() == '\n')
                is.get();
        }
    }
    return is;
}

ostream &operator<<(ostream &os, const Almacen_Rutas &R)
{
    Almacen_Rutas::const_iterator it;
    for (it = R.cbegin(); it != R.cend(); ++it)
    {
        os << (*it).first << " " << (*it).second << endl;
    }
    return os;
}

//class iterator

Almacen_Rutas::iterator::iterator(const iterator &it)
{
    p = it.p;
}

Almacen_Rutas::iterator &Almacen_Rutas::iterator::operator++()
{
    ++p;
    return *this;
}

Almacen_Rutas::iterator &Almacen_Rutas::iterator::operator--()
{
    --p;
    return *this;
}

bool Almacen_Rutas::iterator::operator==(const iterator &it)
{
    return it.p == p;
}

bool Almacen_Rutas::iterator::operator!=(const iterator &it)
{
    return it.p != p;
}

pair<string, Ruta> Almacen_Rutas::iterator::operator*() const
{
    pair<string, Ruta> salida(*p);
    return salida;
}

//class const iterator

Almacen_Rutas::const_iterator::const_iterator(const const_iterator &it)
{
    p = it.p;
}

Almacen_Rutas::const_iterator &Almacen_Rutas::const_iterator::operator=(const iterator &it)
{
    p = it.p;
    return *this;
}

Almacen_Rutas::const_iterator &Almacen_Rutas::const_iterator::operator++()
{
    ++p;
    return *this;
}

Almacen_Rutas::const_iterator &Almacen_Rutas::const_iterator::operator--()
{
    --p;
    return *this;
}

bool Almacen_Rutas::const_iterator::operator==(const const_iterator &it)
{
    return it.p == p;
}

bool Almacen_Rutas::const_iterator::operator!=(const const_iterator &it)
{
    return it.p != p;
}

pair<string, Ruta> Almacen_Rutas::const_iterator::operator*() const
{
    pair<string, Ruta> salida(*p);
    return salida;
}