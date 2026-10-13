mov rax, 0x1122334455667788      ; rax = 0x1122334455667788
mov eax, 0x42                    ; !rax = 0x00 00 00 00 00 00 00 42
                                 ; why not rax = 0x1122334400000042 ??

mov rax, 0x1122334455667788      ; rax = 0x1122334455667788
mov ax, 0x9999                   ; rax = 0x1111222233339999, as expected
                                 ; this works as expected

mov rax, 0x1122334455667788      ; rax = 0x1122334455667788
xor eax, eax                     ; rax = 0x0000000000000000
                                 ; why not rax = 0x1122334400000000?
