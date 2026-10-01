global _start

section .bss
    num_buf: resb 64

section .text
_start:
    xor     r12, r12            ; sum = 0

.read_loop:
    mov     rdi, num_buf
    mov     rsi, 64
    call    read_word
    test    rax, rax
    jz      .done               ; EOF или слово слишком длинное

    mov     rdi, num_buf
    call    parse_int
    test    rdx, rdx
    jz      .done               ; не число — прекращаем

    add     r12, rax
    jmp     .read_loop

.done:
    mov     rdi, r12
    call    print_int
    call    print_newline

    xor     rdi, rdi
    call    exit

