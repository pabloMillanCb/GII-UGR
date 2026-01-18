/**
  Fichero: ruta.cpp
  Alumnos: Jordi Conde Molina
           Pablo Millán Cubero 
  */

#include "../include/ruta.h"

using namespace std;

//class ruta

void Ruta::Insertar(const Punto &P)
{

    datos.push_back(P);
}

Ruta::iterator Ruta::begin()
{
    iterator it;
    it.p = datos.begin();
    return it;
}

Ruta::const_iterator Ruta::cbegin() const
{
    const_iterator it;
    it.p = datos.begin();
    return it;
}
Ruta::iterator Ruta::end()
{
    iterator it;
    it.p = datos.end();
    return it;
}
Ruta::const_iterator Ruta::cend() const
{
    const_iterator it;
    it.p = datos.end();
    return it;
}
Ruta::iterator Ruta::find(const Punto &p)
{
    iterator it;
    list<Punto>::iterator i;
    for (i = datos.begin(); i != datos.end() && !((*i) == p); ++i)
        ;
    it.p = i;
    return it;
}

//class iterator

Ruta::iterator &Ruta::iterator::operator++()
{
    ++p;
    return *this;
}

Ruta::iterator &Ruta::iterator::operator--()
{
    --p;
    return *this;
}

Ruta::iterator &Ruta::iterator::operator=(const iterator &it)
{
    p = it.p;
    return *this;
}

bool Ruta::iterator::operator==(const iterator &it)
{
    return it.p == p;
}
bool Ruta::iterator::operator!=(const iterator &it)
{
    return it.p != p;
}
const Punto &Ruta::iterator::operator*() const
{
    return *p;
}

//clase const iterator

Ruta::const_iterator::const_iterator(const iterator &it)
{
    p = it.p;
}

Ruta::const_iterator &Ruta::const_iterator::operator=(const const_iterator &it)
{
    p = it.p;
    return *this;
}
Ruta::const_iterator &Ruta::const_iterator::operator++()
{
    ++p;
    return *this;
}

Ruta::const_iterator &Ruta::const_iterator::operator--()
{
    --p;
    return *this;
}
bool Ruta::const_iterator::operator==(const const_iterator &it)
{
    return it.p == p;
}
bool Ruta::const_iterator::operator!=(const const_iterator &it)
{
    return it.p != p;
}
const Punto &Ruta::const_iterator::operator*() const
{
    return *p;
}

//funciones externas

istream &operator>>(istream &is, Ruta &R)
{
    Ruta rlocal;
    int puntos;
    Punto nuevo;
    is >> puntos;

    for (int i = 0; i < puntos; i++)
    {

        while (is.peek() == ' ' || is.peek() == '\n' || is.peek() == '\t')
            is.get();

        is >> nuevo;
        rlocal.Insertar(nuevo);
    }
    R = rlocal;
    return is;
}

ostream &operator<<(ostream &os, const Ruta &R)
{
    Ruta::const_iterator it;
    for (it = R.cbegin(); it != R.cend(); ++it)
        os << *it << endl;
    return os;
}