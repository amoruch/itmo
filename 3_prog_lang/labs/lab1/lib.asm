section .text
 
 
; Принимает код возврата и завершает текущий процесс
global  exit
exit: 
    mov     rax, 60
    syscall

; Принимает указатель на нуль-терминированную строку, возвращает её длину
global  string_length
string_length:
    xor     rax, rax
.loop:
    cmp     byte [rdi+rax], 0
    je      .end
    inc     rax
    jmp     .loop
.end:
    ret

; Принимает указатель на нуль-терминированную строку, выводит её в stdout
global  print_string
print_string:
    sub     rsp, 8      ; выравниваем стек
    call    string_length
    add     rsp, 8
    mov     rdx, rax
    mov     rax, 1
    mov     rsi, rdi
    mov     rdi, 1
    syscall
    ret

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
global print_newline
print_newline:
    mov rdi, 0xA
    sub     rsp, 8      ; выравниваем стек
    call print_char
    add     rsp, 8
    ret

; Выводит беззнаковое 8-байтовое число в десятичном формате 
; Совет: выделите место в стеке и храните там результаты деления
; Не забудьте перевести цифры в их ASCII коды.
global print_uint
print_uint:
    push    rbp
    mov     rbp, rsp
    sub     rsp, 32         ; выделяем 32 ячейки для сохранения

    mov     rax, rdi
    mov     rcx, 10
    lea     r8, [rbp - 1]   ; указатель на последнюю позицию буфера
    xor     r9, r9          ; счетчик цифр

.loop_div:
    xor     rdx, rdx
    div     rcx
    add     rdx, '0'
    dec     r8
    mov     [r8], dl
    inc     r9
    test    rax, rax
    jne      .loop_div

.loop_print:
    movzx   edi, byte [r8]
    call    print_char
    inc     r8
    dec     r9
    jnz     .loop_print
    
    leave
    ret

; Выводит знаковое 8-байтовое число в десятичном формате 
global print_int
print_int:
    test    rdi, rdi
    jns     .positive
    push    rdi
    mov     rdi, '-'
    call    print_char
    pop     rdi
    neg     rdi
.positive:
    sub     rsp, 8      ; выравниваем стек
    call    print_uint
    add     rsp, 8
    ret

; Принимает два указателя на нуль-терминированные строки, возвращает 1 если они равны, 0 иначе
global string_equals
string_equals:
    mov     rax, 1
.loop:
    mov     dl, [rdi]
    cmp     dl, [rsi]
    jne      .neq
    test    dl, dl
    je      .exit
    inc     rdi
    inc     rsi
    jmp     .loop
.neq:
    mov     rax, 0
.exit:
    ret

; Читает один символ из stdin и возвращает его. Возвращает 0 если достигнут конец потока
global read_char
read_char:
    xor     rax, rax
    xor     rdi, rdi
    sub     rsp, 8
    mov     rsi, rsp
    mov     rdx, 1
    syscall

    test    rax, rax
    je      .eof
    movzx   eax, byte [rsp]
.eof:
    add     rsp, 8
    ret 

; Принимает: адрес начала буфера, размер буфера
; Читает в буфер слово из stdin, пропуская пробельные символы в начале, .
; Пробельные символы это пробел 0x20, табуляция 0x9 и перевод строки 0xA.
; Останавливается и возвращает 0 если слово слишком большое для буфера
; При успехе возвращает адрес буфера в rax, длину слова в rdx.
; При неудаче возвращает 0 в rax
; Эта функция должна дописывать к слову нуль-терминатор
global read_word
read_word:
    push    rdi         ; сохраняем адрес начала буфера
    mov     rcx, rsi
    dec     rcx         ; место под нуль символ
    xor     rdx, rdx
    xor     r8, r8      ; флаг пропуска пробелов

.loop:
    sub     rsp, 8      ; выравниваем стек
    push    rcx
    push    rdi
    push    rdx
    call    read_char
    pop     rdx
    pop     rdi
    pop     rcx
    add     rsp, 8

    test    rax, rax
    je      .eof
    
    cmp     rax, 0x20
    je      .space
    cmp     rax, 0x9
    je      .space
    cmp     rax, 0xA
    je      .space

    mov     r8, 1
    cmp     rdx, rcx
    jae     .fail
    mov     byte [rdi+rdx], al
    inc     rdx
    jmp     .loop
.space:
    test    r8, r8
    jz      .loop
    jmp     .success
.eof:
    test    r8, r8
    jz      .fail           ; EOF до начала слова — пустой ввод
    jmp     .success
.fail:
    pop     rdi
    xor     rax, rax
    ret
.success:
    pop     rax             ; возвращаем адрес начала буфера
    mov     [rdi+rdx], 0
    ret

 

; Принимает указатель на строку, пытается
; прочитать из её начала беззнаковое число.
; Возвращает в rax: число, rdx : его длину в символах
; rdx = 0 если число прочитать не удалось
global parse_uint
parse_uint:
    xor     rax, rax
    xor     rdx, rdx
.loop:
    movzx   ecx, byte [rdi]
    
    cmp     cl, '0'
    jb      .end
    cmp     cl, '9'
    ja      .end
    sub     rcx, '0'
    
    imul    rax, rax, 10
    add     rax, rcx

    inc     rdi
    inc     rdx
    jmp     .loop
.end:
    ret


; Принимает указатель на строку, пытается
; прочитать из её начала знаковое число.
; Если есть знак, пробелы между ним и числом не разрешены.
; Возвращает в rax: число, rdx : его длину в символах (включая знак, если он был) 
; rdx = 0 если число прочитать не удалось
global parse_int
parse_int:
    xor     r8, r8
    movzx   ecx, byte [rdi]
    cmp     cl, '-'
    jne     .check_plus
    mov     r8, 1
    jmp     .parse

.check_plus:
    cmp     cl, '+'
    jne     parse_uint
    
.parse:
    inc     rdi
    sub     rsp, 8      ; выравниваем стек
    call    parse_uint
    add     rsp, 8
    test    rdx, rdx
    jz      .fail
    inc     rdx
    test    r8, r8
    jz      .ret
    neg     rax
.ret:
    ret

.fail:
    xor     rax, rax
    xor     rdx, rdx
    ret 

; Принимает указатель на строку, указатель на буфер и длину буфера
; Копирует строку в буфер
; Возвращает длину строки если она умещается в буфер, иначе 0
global string_copy
string_copy:
    xor     rax, rax
.loop:
    cmp     rax, rdx
    jae     .fail
    movzx   r8, byte [rdi]
    test    r8, r8
    jz      .end
    mov     [rsi+rax], r8b
    inc     rdi
    inc     rax
    jmp     .loop
.end:
    mov     byte [rsi+rax], 0
    ret
.fail:
    xor     eax, eax
    ret

