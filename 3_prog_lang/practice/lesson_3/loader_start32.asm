lgdt cs:[_gdtr]

mov eax, cr0    ; !! privileged instruction
or al, 1        ; this is the bit responsible for protected mode
mov cr0, eax    ; !! privileged instruction

jmp (0x1 << 3):start32  ; assign first seg selector to cs

align 16        ; stores GDT's last entry index + GDT address
_gdtr:
dw 47
dq _gdt

alight 16

_gdt:
; Null descriptor (should be present in any GDT)
dd 0x00, 0x00
; x32 code descriptor:
db 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x9A, 0xCF,     0x00   ; differ by exec bit
; x32 data descriptor:
db 0xFF, 0xFF, 0x00, 0x00, 0x00, 0x92, 0xCF,     0x00   ; execution off (0x92)
;  size  size  base  base  base  util  util|size base
