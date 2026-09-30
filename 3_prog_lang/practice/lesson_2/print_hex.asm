; print_hex.asm
section .data
codes:
    db      '0123456789ABCDEF'

section .text
global _start
_start:
    mov     rdi, 0x1122334455667788
    call    print_hex

    mov     rdi, 0x0123456789ABCDEF
    call    print_hex

    mov     rdi, 0x8877665544332211
    call    print_hex

    jmp     exit

exit:
    mov     rax, 60
    xor     rdi, rdi
    syscall

; Принимает код символа и выводит его в stdout
global  print_char
print_char:
    push    rdi
    mov     rax, 1
    mov     rdi, 1
    mov     rsi, rsp
    mov     rdx, 1
    syscall
    pop     rdi
    ret

; Переводит строку (выводит символ с кодом 0xA)
print_newline:
    mov     rdi, 0xA
    call    print_char
    ret

print_hex:
    mov     rax, rdi

    mov     rdi, 1
    mov     rdx, 1
    mov     rcx, 64
.loop:
    push    rax
    sub     rcx, 4
    sar     rax, cl
    and     rax, 0xf

    lea     rsi, [codes + rax]
    mov     rax, 1

    push    rcx
    syscall
    pop     rcx

    pop     rax
    test    rcx, rcx
    jnz     .loop

    call    print_newline
    ret
