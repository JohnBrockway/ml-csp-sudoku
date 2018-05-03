# ml-csp-sudoku
A Java implementation of Constraint Satisfaction Problem solving, with options for both simple backtracking search and forward checking search.

Run with the following commands from the src directory:
```
javac *.java
java Sudoku [file] [solver-type] [maximum-assignments]
```
[file] is the relative or absolute location of the input file. This input file must have exactly 9 lines of text, each with exactly 9 numbers from 0-9 seperated by spaces. 0s represent unfilled squares. This file as a whole represents a sudoku grid; for example, [example.sd](example.sd) represents the following sudoku puzzle, where * is an unsolved square:
```
* * * | * * * | 3 7 * 
2 * 3 | * * * | * * * 
* * * | * * * | * * 1 
---------------------
* * 2 | * * * | 1 8 * 
* * 7 | * * 5 | * * * 
9 * * | * 8 * | * * * 
---------------------
* * * | * * 9 | 5 * * 
* 3 * | * * * | * * * 
* * * | * 1 * | * 2 *
```
[solver-type] is one of
* simple
* forward

for a simple backtracking algorithm or a backtracking algorithm with forward checking, respectively.

[maximum-assignments] is an optional parameter to specify the maximum number of assignments allowed for the algorithm; if the puzzle is not solved by that number of assignments and reassignments, it will return an incomplete grid as it is solved up to that point. Forward Checking results in fewer overall assignments, and will almost always complete in the default maximum of 10,000, but comes at the cost of more memory usage.

See Wikipedia to learn more about [CSPs](https://en.wikipedia.org/wiki/Constraint_satisfaction_problem) and [forward checking](https://en.wikipedia.org/wiki/Look-ahead_(backtracking))
