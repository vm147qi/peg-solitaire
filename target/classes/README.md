...
Game name: Peg Solitaire

Implemented: Veronika Mukosii 2025

Short description of the game:
//   Slovenská verzia ->
I. Generovať dosku: vytvorí štandardné (alebo vlastné) rozloženie figúrok s jednou
prázdnou bunkou.
II. Kontrola stavu hry:
    Ak zostal len jedna figúrka - WON. 
    Ak nie sú možné žiadne ťahy, ale je viac ako jedna figúrka - NO_MOVES.
    V opačnom prípade - HRAJE SA.
III. Vykonanie ťahu: Hráč si vyberie bunku s figúrkou a cieľom, ktorý spĺňa pravidlá
skoku (cez jednu figúrku na prázdnu bunku). V tomto prípade:
    Pôvodná bunka sa stane prázdnou.
    Preskočená figúrka sa odstráni z hracej plochy (stane sa prázdnou).
    Cieľová bunka sa obsadí figúrkou.
IV. Aktualizácia a výstup: Po každom ťahu sa skontroluje, či hra skončila, a zobrazí sa
nový stav šachovnice.

Základná logika hry Peg Solitaire spočíva v postupnom preskakovaní figúrok s cieľom
ponechať na hracej ploche iba jeden. Ak to nie je možné a nie sú žiadne ďalšie ťahy,
hra sa skončí v stave NO_MOVES.

    English version ->
I. Generate field: creates a standard (or arbitrary) layout of chips with
one empty cell.
II. Check the status of the game:
    If there is one chip left - WON.
    If there are no possible moves, but there are more than one chip - NO_MOVES
    NO_MOVES.
    Otherwise - PLAYING.
III. Execution of the move: the player chooses a cell with a chip and a target that meets
the rules of jumping (through one chip to an empty cell). In this case:
    The original cell becomes empty.
    The jumped piece is removed from the board (becomes empty).
    The target cell becomes occupied by a piece.
IV. Update and output: After each turn, the game is checked to see if it is over and
    the new state of the board is displayed.

The basic logic of Peg Solitaire is to jump chips in sequence in order to leave only one chip on the board. If this is not possible and there are no more moves
no more moves, the game ends with the state NO_MOVES.

    Video of the game can be found at the link below -->>
...