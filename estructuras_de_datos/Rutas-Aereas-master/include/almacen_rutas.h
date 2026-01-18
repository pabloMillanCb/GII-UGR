/**
  Fichero: almacen_rutas.h
  Alumnos: Jordi Conde Molina
           Pablo Millán Cubero 
  */

#ifndef __ALMACEN_RUTAS__
#define __ALMACEN_RUTAS__
#include "ruta.h"
#include <map>
#include<fstream>

using namespace std;

class Almacen_Rutas{

    private:
        map<string,Ruta> datos;
        multimap<Punto,string> puntos;

    public:
        Almacen_Rutas(){}

        void Insertar(const pair<string,Ruta> &R);

        void Borrar(const string R);

        Ruta GetRuta(const string R);

        Almacen_Rutas GetRutas_Con_Punto(const Punto &P);

         class iterator{
	        private:
	            map<string,Ruta>::iterator p;
	        public:
	            iterator(){}
                iterator(const iterator & it);

	            iterator & operator ++();
	            iterator & operator --();

	            bool operator ==(const iterator  & it);
	            bool operator !=(const iterator  & it);

	            pair<string,Ruta> operator*()const ;

	        friend class Almacen_Rutas;
	        friend class const_iterator;
	    };

        class const_iterator{
            private:
                map<string,Ruta>::const_iterator p;
            public:
                const_iterator(){}
                const_iterator(const const_iterator & it);

                const_iterator & operator=(const iterator & it);

                const_iterator & operator ++();
                const_iterator & operator --();

                bool operator ==(const const_iterator  & it);
                bool operator !=(const const_iterator  & it);
                pair<string,Ruta> operator*()const ;

            friend class Almacen_Rutas; 
        };

        iterator begin();
	    const_iterator cbegin() const;
	    iterator end();
	    const_iterator cend() const;

	    iterator find(const string p);

        friend istream & operator>>(istream & is, Almacen_Rutas & R);
        friend ostream & operator<<(ostream & os,const Almacen_Rutas & R);
};
#endif