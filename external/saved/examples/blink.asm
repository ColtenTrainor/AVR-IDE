.includepath "C:\dev\projects\java\AVR-IDE\build\libs\avra\includes"
.include "m328Pdef.inc"

	ldi r16, 0b00000001
	ldi r17, 0b00000001
	out DDRB, r16
	
mainLoop:
	out PORTB, r16
	
; Delay assembly code auto-generated
; by utility from Bret Mulvey
; Delay 8 000 000 cycles
; 1s at 8 MHz

    ldi  r18, 41
    ldi  r19, 150
    ldi  r20, 128
L1: dec  r20
    brne L1
    dec  r19
    brne L1
    dec  r18
    brne L1

	
; end of generated delay code

	eor r16, r17
	
    rjmp mainLoop
