; main.asm
%include "lib.inc"

section .data
codes:
    db      '0123456789ABCDEF'

section .text

global _start
_start:
    mov  rdi, 0x1122334455667788
    call print_hex
    call exit
