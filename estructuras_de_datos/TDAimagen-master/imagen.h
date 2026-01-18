/**
 * @brief Clase para el TDA Imagen
 * @param nf numero de filas
 * @param nc numero de columnas
 * @param img puntero a la matriz dinamica de valores
 **/
class Imagen {

private:

    int nf, nc;
    unsigned char **img;

    /**
     * @brief Reserva memoria para la matriz dinamica
     * @param filas numero de filas a reservar
     * @param columnas numero de columnas a reservar
     **/
    void allocate(int const filas, const int columnas);

    /**
     * @brief Libera la memoria a la que apunta img
     * @pre Si no hay memoria asignada, no hace nada
     **/
    void deallocate();

public:

    /**
     * @brief Constructor sin argumentos de Imagen. Inicializa el puntero a nullptr y nf = nc = 0
     **/
    Imagen();

    /**
     * @brief Constructor de copia de Imagen.
     * @param J Imagen a copiar
     **/
    Imagen(const Imagen & J);

    /**
     * @brief Constructor con parametros para definir tamaño
     * @param filas numero de filas
     * @param cols numero de columas
     **/
    Imagen(int filas, int cols);
    ~Imagen();

    /**
     * @brief Devuelve el numero de filas de la imagen. Es constante.
     * @return nf
     **/
    int num_filas() const;

    /**
     * @brief Devuelve el numero de columnas de la imagen. Es constante.
     * @return nc
     **/
    int num_columnas() const;

    /**
     * @brief Asigna el valor de un pixel de la imagen
     * @param fila primera coordenada del pixel
     * @param col segunda coordenada del pixel
     * @param valor valor a asignar al pixel
     * @pre fila y col deben ser >= 0 y < que numero de filas/columnas
     * @pre valor debe ser <= 0 y < 255
     * 
     **/
    void asigna_pixel(int fila, int col, unsigned char valor);

    /**
     * @brief Consultor del valor del valor de un pixel de la imagen
     * @param fila primera coordenada del pixel
     * @param col segunda coordenada del pixel
     * @return valor del pixel (fila, col)
     * @pre fila y col deben ser >= 0 y < que numero de filas/columnas
     **/
    unsigned char valor_pixel(int fila, int col) const;

    /**
     * @brief Iguala el objeto actual al del otro lado del operando
     * @param orig objeto al que igualar
     **/
    Imagen& operator=(const Imagen &orig);

};