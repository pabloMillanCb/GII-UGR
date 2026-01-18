/**
  Fichero: ruta.h
  Alumnos: Jordi Conde Molina
           Pablo Millán Cubero 
  */

#ifndef __RUTA__
#define __RUTA__
#include "Punto.h"
#include<fstream>
#include <list>

using namespace std;

class Ruta{
    private:
        list<Punto> datos;

    public:
        Ruta(){}

        void Insertar(const Punto &P);

        class iterator{
	        private:
	            list<Punto>::iterator p;
	        public:
	            iterator(){}

	            iterator & operator ++();
	            iterator & operator --();

                iterator & operator=(const iterator & it);

	            bool operator ==(const iterator  & it);
	            bool operator !=(const iterator  & it);
	            
	            const Punto & operator*()const;

	        friend class Ruta;
	        friend class const_iterator;
	    }; 

        class const_iterator{
            private:
                list<Punto>::const_iterator p;
            public:
                const_iterator(){}
                const_iterator(const iterator & it);

                const_iterator & operator=(const const_iterator & it);

                const_iterator & operator ++();
                const_iterator & operator --();

                bool operator ==(const const_iterator  & it);
                bool operator !=(const const_iterator  & it);

                const Punto &operator*() const;

            friend class Ruta; 
        };

	    iterator begin();
	    const_iterator cbegin() const;
	    iterator end();
	    const_iterator cend() const;

	    iterator find(const Punto &p);

        friend istream & operator>>(istream & is, Ruta & R);
        friend ostream & operator<<(ostream & os, const Ruta &R);
};
#endif