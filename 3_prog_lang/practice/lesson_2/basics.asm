; basics.asm

; registers:
; r0, rax - accumulator, for syscall 
; r1, rcx - for cycle
; r2. rdx - data during i/o, 0-input, 1-output, 2-err
; r3, rbx - base register, buffer?
; r4, rsp - stack pointer, dont touch it
; r5, rbp - stack frame's base?
; r6, rsi - source string index 
; r7, rdi - destination index string, for syscall
; r8 - blank?
; r9-r15  - temporal, general purpose 64x registers?

; function args: rdi, rsi, rdx, rcx, r8, r9, other on stack
; function ret:  rax, rdx

; callee saved regs: rbx, rbp, rsp, r12-15
; caller saved regs: all not callee saves


section .data
    string: db  'basics', 10

section .text
    global _start

_start:
    mov     rax, 1
    mov     rdi, 1
    mov     rsi, string
    mov     rdx, 7      ; set reg or oper to 2 reg/oper
    syscall

    jmp     exit        ; mov rip, exit

    add     rax, 0      ; add 2 oper to 1 oper and save at 1 oper
    cmp     rax, rdi    ; sub 2 oper from 1 oper and set flags
    test    rax, rdi    ; cmp version for logic AND 
    inc     rax         ; increment by 1
    dec     rax         ; decrement by 1

    mul  rdi            ; rdx:rax = rax * rdi  (беззнаковое)
    imul rdi            ; rdx:rax = rax * rdi  (знаковое)
    imul rax, rdi       ; rax     = rax * rdi  (знаковое, без rdx — двухоперандная форма)
    div  rdi            ; rax = rdx:rax / rdi, rdx = остаток  (беззнаковое)
    idiv rdi            ; то же, но знаковое

    sub     rax, rdi    ; subtract 2 oper from 1 oper
    neg     rax         ; negate
    call    .function   ; same as - push rip, mov rip, .function
    push    rax         ; push on top of stack
    pop     rax         ; pull from stack

.function:
    xor rax, rax
    ret                 ; pop rip

exit:
    mov     rax, 60
    xor     rdi, rdi
    syscall
