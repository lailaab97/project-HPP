# project-HPP

**Test Cases Examples**

_Example 1_

Input: *French data*
```
4, "Daniel", "ROBINSON", 1995-08-21 00:00:00, 1582161158.5235808, unknown
5, "Jessica", "WATSON", 1968-11-21 00:00:00, 1583091884.9200459, unknown
9, "Stephanie", "MITCHELL", 1924-03-17 00:00:00, 1585699579.2617905, 4
13, "Ashley", "WARD", 1976-03-12 00:00:00, 1587417223.6139328, 4
14, "Kathleen", "GREEN", 2012-06-27 00:00:00, 1587769422.7054172, 5
19, "Joseph", "JONES", 2013-04-09 00:00:00, 1589238000.0, 13
```
Process
```
Step 1 result: France 4, 10
Step 2 result: France 5, 10; France 4, 4
Step 3 result: France 9, 10
Step 4 result: France 13, 10
Step 5 result: France 14, 10; France 13, 10
Step 6 result: France 19, 10
```
Final Output
```
France 19, 10
```


_Example 2_

Input: *Spanish data*
```
0, "Angela", "HARRIS", 1986-08-19 00:00:00, 1573945200.0, unknown, "promenade avec ma fille à la salle de gym"
2, "Michelle", "CHAVEZ", 1963-12-15 00:00:00, 1579553464.1818361, 0, "escalade avec le fils au super maché"
3, "Donald", "RICHARDSON", 1951-06-19 00:00:00, 1581022041.4764192, 0, "promenade avec ma mère au marché"
7, "Timothy", "JONES", 1993-02-07 00:00:00, 1584560462.2146287, 3, "handball avec mon père à la salle de gym"
8, "Laura", "ROBERTS", 2007-03-08 00:00:00, 1585161728.3636725, 3, "courses avec le copain au super maché"
11, "James", "JAMES", 1975-09-25 00:00:00, 1586630305.6582553, 3, "sport avec la mère en grande surface"
16, "Steven", "ALLEN", 1951-07-07 00:00:00, 1588408363.2841413, 11, "vélo avec la mère en grande surface"

```
Process
```
Step 1 result: Spain 0, 10
Step 2 result: Spain 2,10
Step 3 result: Spain 3,10
Step 4 result: Spain 7,10
Step 5 result: Spain 7,4; Spain 8,10
Step 6 result: Spain 11, 10
Step 7 result: Spain 16, 10
```
Final Output
```
Spain 16, 10
```

_Example 3_

Input: *Italian data*
```
1, "Gary", "ADAMS", 2008-06-04 00:00:00, 1577483620.7382095, unknown, "escalade avec mon fils au marchÃ©"
6, "Dennis", "YOUNG", 1989-05-21 00:00:00, 1583878802.8757234, 1, "travail avec le grand-pÃ¨re en ville"
10, "Samuel", "PEREZ", 1977-07-13 00:00:00, 1586186124.581804, 6, "apÃ©ro avec le pÃ¨re au super machÃ©"
12, "Joshua", "EDWARDS", 1945-12-09 00:00:00, 1587038912.6391647, 1, "apÃ©ro avec mon fils Ã  la salle de gym"
15, "Daniel", "HERNANDEZ", 1962-09-25 00:00:00, 1588098882.9528382, unknown, "course Ã  pieds avec une copine au super machÃ©"
17, "Kelly", "RUIZ", 1988-09-15 00:00:00, 1588700149.101882, unknown, "vÃ©lo avec ma mÃ¨re en ville"
18, "Ronald", "WILLIAMS", 1981-10-08 00:00:00, 1588976154.8079798, unknown, "sport avec le chien Ã  la campagne"

```
Process
```
Step 1 result: Italy 1,10
Step 2 result: Italy 6,10
Step 3 result: Italy 10,10
Step 4 result: Italy 10,4 ; Italy 14,10
Step 5 result: Italy 15,10
Step 6 result: Italy 17, 10 ; Italy 18, 10
```
Final Output
```
Italy 17, 10 ; Italy 18, 10
```
