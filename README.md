# project-HPP

**Test Cases Examples**

_Example_

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
Step 2 result: France 5, 10
Step 3 result: France , 10
Step 4 result: France 13, 10
Step 5 result: France 13, 10; France 14, 10
Step 6 result: France 19, 10
```
Final Output
```
France 19, 10
```
