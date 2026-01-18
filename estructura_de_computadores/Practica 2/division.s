.section .data
lista:		.int 0x33333337,0x33313333,0x83333333,0x53335333,0x333f3333,0x44433333,0x33333333,0x33333333,0x33333333,0x33333333,0x33333333,0x33333333,0x33333333,0x33333333,0x33333333,0x33333333
		#	.int -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
		#	.int 0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff,0xffffffff
		#	.int 6, 4, 3, 7, 8, 2, 1
		#	.int 1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2


longlista:	.int   (.-lista)/4
resultado:	.int   0
resto: 		.int   0
resultadoq: .quad  0
restoq:		.quad  0
formato: 	.asciz	"media = %d resto = %d \n"
formatoq: 	.asciz	"media = %ld resto = %ld hecho en 64 bits\n"

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
	call dividir
	mov  %eax, resultado
	mov  %edx, resto
	call sumaq
	call dividirq
	mov  %rax, resultadoq
	mov  %rdx, restoq
	ret

suma:
	mov  $0, %eax
	mov  $0, %edx
	mov  $0, %r8
	mov  $0, %r9
	mov  $0, %rsi
bucle:
	mov  (%rbx,%rsi,4), %eax
	inc   %rsi

	cdq 		   # extiende a EDX:EAX
	add %eax, %r8d # R9D:R8D
	adc %edx, %r9d

	cmp   %rsi,%rcx
	jne    bucle

	ret

sumaq:
	mov $0, %rax
	mov $0, %rdx
	mov $0, %rsi
bucleq:
	mov (%rbx,%rsi,4), %eax
	inc %rsi

	cltq

	add %rax, %rdx

	cmp %rsi,%rcx
	jne bucleq

	ret

dividir:
	mov %r8d, %eax
	mov %r9d, %edx
	idiv %ecx
	ret

dividirq:
	mov %rdx, %rax
	cqo
	idiv %rcx

	ret

imprim_C:			# requiere libC
	mov   $formato, %rdi
	mov   resultado,%rsi
	mov   resto,%edx
	mov          $0,%eax	# varargin sin xmm
	call  printf		# == printf(formato, res, res);
	mov	  $formatoq, %rdi
	mov   resultadoq,%rsi
	mov   restoq,	 %rdx
	mov          $0,%eax	# varargin sin xmm
	call  printf		# == printf(formato, res, res);

	ret

acabar_C:			# requiere libC
	mov  resultado, %edi
	call _exit		# ==  exit(resultado)
	ret 
