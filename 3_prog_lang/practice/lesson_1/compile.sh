nasm -f elf64 hello.asm -o hello.o
gcc -nostdlib hello.o -o hello
./hello

