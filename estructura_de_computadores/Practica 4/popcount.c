// 9.- gcc suma_09_Casm.c -o suma_09_Casm -O / -Og -g

#include <stdio.h>		// para printf()
#include <stdlib.h>		// para exit()
#include <sys/time.h>		// para gettimeofday(), struct timeval


int resultado=0;

#ifndef TEST
#define TEST 2
#endif

    #if TEST==1

    #define SIZE 4
    unsigned lista[SIZE]={0x80000000, 0x00400000, 0x00000200, 0x00000001};
    #define RESULT 4 

#elif TEST==2

    #define SIZE 8
    unsigned lista[SIZE]={0x7fffffff, 0xffbfffff, 0xfffffdff, 0xfffffffe, 0x01000023, 0x00456700, 0x8900ab00, 0x00cd00ef}; 
    #define RESULT 4 

#elif TEST==3

    #define SIZE 8
    unsigned lista[SIZE]={0x0, 0x01020408, 0x35906a0c, 0x70b0d0e0, 0xffffffff, 0x12345678, 0x9abcdef0, 0xdeadbeef};
    #define RESULT 4 


#elif TEST==4 || TEST==0

    #define NBITS 20
    #define SIZE (1<<NBITS)
    unsigned lista[SIZE];
    #define RESULT ( NBITS * ( 1 << NBITS-1 ) )

#endif

int popcount1(unsigned* array, size_t len)
{
    int result = 0;

    for (size_t i = 0; i < len; i++) {
    unsigned x = array[i];

        for (size_t j = 0; j < 8*sizeof(int); j++) {
            result += x & 0x1;
            x >>= 1;
        }
    }
    return result;
}

int popcount2(unsigned* array, size_t len) {

    int result = 0;

    for (size_t i = 0; i < len; i++){

        unsigned x = array[i];
        while (x) {

            result += x & 0x1;
            x >>=1;
        }
    }

    return result;
}

int popcount3(unsigned* array, size_t len) {
    int result = 0;
    for (size_t i = 0; i < len; i++) {
        unsigned x = array[i];
        asm("\n"
            "ini3:          \n\t"
            "shr %[x]       \n\t"
            "adc $0, %[r]   \n\t"
            "test %[x],%[x] \n\t"
            "jne ini3       \n\t"
            : [r] "+r"  (result)
            : [x] "r"   (x)
        );
    }

    return result;
}

int popcount4(unsigned* array, size_t len) {

    int result = 0;
    for (size_t i = 0; i < len; i++) {
        unsigned x = array[i];
        asm("\n"
            "clc            \n\t"
        "ini4:              \n\t"
            "adc $0, %[r]   \n\t"
            "shr %[x]       \n\t"
            "jne ini4       \n\t"
            "adc $0, %[r]   \n\t"
            : [r] "+r"  (result)
            : [x] "r"   (x)
        );
    }

    return result;
}

int popcount5(unsigned* array, size_t len) {

    int result = 0;
    for (size_t i = 0; i < len; i++) {
        
       unsigned x = array[i];
       unsigned val = 0;
       for (int i = 0; i < 8; i++){
           val += x & 0x01010101;
           x >>= 1;
       }
       val += (val >> 16);
       val += (val >> 8);
       result += val & 0xFF;
    }
    return result;
}

const unsigned m1  = 0x55555555;
const unsigned m2  = 0x33333333;
const unsigned m4  = 0x0f0f0f0f;
const unsigned m8  = 0x00ff00ff;
const unsigned m16 = 0x0000ffff;

int popcount6(unsigned* array, size_t len) {

    int result = 0;
    for (size_t i = 0; i < len; i++) {
       unsigned x = array[i];
       x = (x & m1 ) + ((x >>  1) & m1 );
       x = (x & m2 ) + ((x >>  2) & m2 );
       x = (x & m4 ) + ((x >>  4) & m4 );
       x = (x & m8 ) + ((x >>  8) & m8 );
       x = (x & m16 ) + ((x >>  16) & m16 );
       result += x;

    }
    return result;
}


void crono(int (*func)(), char* msg){
    struct timeval tv1,tv2; 			// gettimeofday() secs-usecs
    long           tv_usecs;			// y sus cuentas

    gettimeofday(&tv1,NULL);
    resultado = func(lista, SIZE);
    gettimeofday(&tv2,NULL);

    tv_usecs=(tv2.tv_sec -tv1.tv_sec )*1E6+
             (tv2.tv_usec-tv1.tv_usec);
    printf("resultado = %d\t", resultado);
    printf("%s:%9ld us\n", msg, tv_usecs);
}

int main()
{
#if TEST==0 || TEST==4
    size_t i;
    for (i=0; i<SIZE; i++)
        lista[i]=i;
#endif

crono(popcount1, "popcount1 (lenguaje C -       for)");
crono(popcount2, "popcount2 (lenguaje C -     while)");
crono(popcount3, "popcount3 (leng.ASM-body while 4i)");
crono(popcount4, "popcount4 (leng.ASM-body while 3i)");
crono(popcount5, "popcount5 (leng.ASM-body while 3i)");
crono(popcount6, "popcount6 (leng.ASM-body while 3i)");

    exit(0);
}
