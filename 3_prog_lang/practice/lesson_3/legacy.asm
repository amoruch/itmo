; legacy.asm

; real mode - start, basic registers
; protected - 
; virtual
; sys management mode
; long mode

; real mode
; segments: cs, ds, ss
; mov al, [0004] === mov al, ds:0004
; mov al, cs:[0004]
; after program is loaded, cs:ip is entry point

; protected mode
; wider version of registers
; protection mechanisms, such as: 
;   LDT/GDT - tables or segments
;   RPL/DPL - privilege level (rings)
;   some other shit maybe

; loader_start32.asm

; tables are hard to work with and this mechanism that no-one really adopted
; disabled in long mode completele

; gdt64.asm

; accessing parts of registers

; risc_cisc.asm

; complete instruction set computer (or complex)
; reduced instruction set computer

; changing lower bits, sets higher bits to 0, because its easier and produces less problems