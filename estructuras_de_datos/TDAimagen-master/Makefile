exe: imagenES.o imagen.o main.o
	g++ -g -o editor_imagen imagen.o imagenES.o main.o
	rm *.o

imagenES.o: imagenES.cpp imagenES.h
	g++ -g -c -o imagenES.o -I. imagenES.cpp

imagen.o: imagen.cpp imagen.h
	g++ -g -c -o imagen.o -I. imagen.cpp

main.o: main.cpp
	g++ -g -c -o main.o main.cpp
