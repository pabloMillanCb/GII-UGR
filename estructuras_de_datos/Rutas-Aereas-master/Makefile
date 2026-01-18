SRC = ./src
INC = ./include
OBJ = ./obj
BIN = ./bin
CXX = g++
CPPFLAGS = -Wall -g  -I$(INC) -c

rutas_aereas: $(SRC)/rutas_aereas.cpp
	g++ -o $(BIN)/rutas_aereas $(SRC)/rutas_aereas.cpp $(SRC)/almacen_rutas.cpp $(SRC)/ruta.cpp $(SRC)/Punto.cpp $(SRC)/imagen.cpp $(SRC)/imagenES.cpp

pruebapegado:
	g++ -o $(BIN)/pruebapegado $(SRC)/pruebapegado.cpp $(SRC)/imagen.cpp $(SRC)/imagenES.cpp

pruebarotacion:
	g++ -o $(BIN)/pruebarotacion $(SRC)/pruebarotacion.cpp $(SRC)/imagen.cpp $(SRC)/imagenES.cpp

# ************ Generación de documentación ******************
documentacion:
	doxygen doc/doxys/Doxyfile


# ************ Limpieza ************
clean :
	-rm $(OBJ)/* $(BIN)/*~

mrproper : clean
	-rm $(BIN)/* doc/html/*
