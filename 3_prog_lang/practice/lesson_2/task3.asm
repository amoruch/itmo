; print_hex.asm
section .data
codes:
    db      '0123456789ABCDEF'

section .text
global _start
_start:
    call    task3
    call    exit

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

; Выводит в stdout переданное число в hex формате
; и переводит строку
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

task3:
    push    rbp
    mov     rbp, rsp
    sub     rsp, 32

    mov     qword [rbp - 8],  0xaa
    mov     qword [rbp - 16], 0xbb
    mov     qword [rbp - 24], 0xcc
    mov     qword [rbp - 32], 0xff

    mov     rdi, [rbp - 8]
    call    print_hex
    mov     rdi, [rbp - 16]
    call    print_hex
    mov     rdi, [rbp - 24]
    call    print_hex
    mov     rdi, [rbp - 32]
    call    print_hex

    leave
    ret
