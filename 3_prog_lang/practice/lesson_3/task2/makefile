all: program

ASM=nasm
ASMFLAGS=-f elf64
LD=ld

%.o: %.asm
	$(ASM) $(ASMFLAGS) -o $@ $<

main.o: main.asm lib.inc lib.o

program: main.o lib.o
	$(LD) -o program $^

.PHONY: clean
clean: 
	rm -f *.o