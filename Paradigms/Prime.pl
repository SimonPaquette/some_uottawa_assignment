/*
Simon Paquette
300044038
CSI 2520
Devoir 2 - prolog
Q3
*/

entierListe(0,[]).
entierListe(N,[N|L]):- N > 0, N1 is N-1, entierListe(N1,L).

retireMultiple(X,[],[]).
retireMultiple(X,[T|Q],Resultat) :- T>X, divise(T,X), retireMultiple(X,Q,Resultat),!.
retireMultiple(X,[T|Q],[T|Resultat]) :- retireMultiple(X,Q,Resultat).

retireTousLesMultiples(N,[],[]).
retireTousLesMultiples(1,L,L).
retireTousLesMultiples(N,Li,L):- N>1, retireMultiple(N,Li,LL), N1 is N-1, retireTousLesMultiples(N1,LL,L).

nMembre(X,[X|L]).
nMembre(X,[Y|L]) :- nMembre(X,L).

divise(A,B) :- 0 is mod(A,B).

%question A
isprime(2).
isprime(X) :- X > 2, entierListe(X, L), X1 is X-1, retireTousLesMultiples(X1,L,R), nMembre(X,R).

%question B
prime(X) :- next(2, X).

next(X, X) :- isprime(X).
next(X, Y) :- X2 is X+1, next(X2, Y).