...
Game name: Peg Solitaire

Implemented: Veronika Mukosii 2025

Short description of the game:

    Slovenská verzia ->
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

 Video of the game can be found at the link below -->> (https://youtu.be/XLeBIAOcjwY)

 Functionality of JPA and REST services -->> (https://youtu.be/ZUgxLLbSUrg)

 Playing in a web browser with GUI -->> (https://youtu.be/rmMkRDtBuCk)

English version ===================

Functions for the game that were performed outside the task:

1. The story of the game (comic book) is about two pegs in love, where they found an enchanted game, the boy was captured by the game and to free him, the girl peg has to defeat all three levels.

2.  Also added three levels of difficulty (Triangle - easy, Classic - medium, European - hard).

3. The leaderboard has been updated with the best player's picture.

4.  Added the site icon to the browser tab (it is called favicon).

5. Animation of buttons and texts + Jumping pag.

6. Comic skip button

7. Added the function of passing levels in order (the rest are closed until the previous one is completed) to free up the peg.

8. Added screens for winning, losing and ending the game with congratulations to the player.

9.  Added a hint with animation of the move on the game page itself.

10. Saving game progress if you go to the main menu and then return to the game!

12. The function to replay the game.

13. Function to cancel a move.

14. Dokončené úrovne sú označené žltým rámčekom a slovom dokončené


Slovenská verzia ==============

Funkcie pre hru, ktoré boli vykonané mimo úlohy:

1. Príbeh hry (komiksu) je o dvoch zamilovaných gulok, ktorí našli zakliatu hru, chlapca hra zajala a aby ho oslobodila, musí dievčenský gulka poraziť všetky tri úrovne.

2.  Pridané boli aj tri úrovne obtiažnosti (Triangle - ľahká, Classic - stredná, European - ťažká).

3. Rebríček bol aktualizovaný o obrázok najlepšieho hráča.

4.  Na kartu prehliadača bola pridaná ikona stránky (nazýva sa favicon).

5. Animácia tlačidiel a textov + skákanie gulky.

6. Tlačidlo na preskočenie komiksu.

7. Pridaná funkcia prechádzania úrovní v poradí (ostatné sú zatvorené, kým sa nedokončí predchádzajúca), aby sa uvoľnil gulku.

8. Pridané obrazovky pre výhru, prehru a ukončenie hry s blahoželaním hráčovi.

9.  Pridaná nápoveda s animáciou ťahu na samotnej stránke hry.

10. Uloženie priebehu hry, ak prejdete do hlavnej ponuky a potom sa vrátite do hry!

12. Funkcia na opakované prehrávanie hry.

13. Funkcia na zrušenie ťahu.

14. Completed levels are marked with a yellow box and the word completed
...