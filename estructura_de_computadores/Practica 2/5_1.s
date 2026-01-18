.section .data
lista:		.int 0x33333333,0x33333333,0x33333333,0x33333333
			.int 0x33333333,0x33333333,0x33333333,0x33333333
			.int 0x33333333,0x33333333,0x33333333,0x33333333
			.int 0x33333333,0x33333333,0x33333333,0x33333333

longlista:	.int   (.-lista)/4
resultado:	.quad  0
acarreo: 	.int   0
  formato: 	.asciz	"suma = %lx \n"

# opción: 1) no usar printf, 2)3) usar printf/fmt/exit, 4) usar tb main
# 1) as  suma.s -o suma.o
#    ld  suma.o -o suma					1232 B
# 2) as  suma.s -o suma.o				6520 B
#    ld  suma.o -o suma -lc -dynamic-linker /lib64/ld-linux-x86-64.so.2
# 3) gcc suma.s -o suma -no-pie –nostartfiles		6544 B
# 4) gcc suma.s -o suma	-no-pie				8664 B

.section .text
main: .global  main

	call trabajar	# subrutina de usuario
	call imprim_C	# printf()  de libC
	call acabar_C	# exit()    de libC
	ret 

trabajar:
	mov     $lista, %rbx
	mov  longlista, %ecx
	call suma		# == suma(&lista, longlista);

	mov  %eax, resultado
	mov  %edx, resultado+4
	# mov  %edx, acarreo
	ret

suma:

	mov  $0, %eax
	mov  $0, %edx
	mov  $0, %r9
bucle:
	add  (%rbx,%r9,4), %eax # rbx + r9*4

	jnc acarr
	inc %edx

acarr:
	inc   %r9

	cmp   %r9,%rcx
	jne    bucle

	ret

imprim_C:			# requiere libC
	mov   $formato, %rdi
	mov   resultado,%rsi
	mov   $0,%edx
	mov          $0,%eax	# varargin sin xmm
	call  printf		# == printf(formato, res, res);
	ret

acabar_C:			# requiere libC
	mov  resultado, %edi
	call _exit		# ==  exit(resultado)
	ret 
