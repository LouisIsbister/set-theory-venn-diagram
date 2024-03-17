# Set Theory Venn Diagrams


## About:
This app evaluates expressions in set theory and graphically represents the result
using Venn Diagrams.

Example: `(c ∩ d) ∪ (a \ ((b ∪ c) ∩ (d ∪ e)))`

<img width="377" alt="SetTheorySH" src="https://github.com/LouisIsbister/Set-Theory-Venn-Diagram/assets/104889878/d0e393b9-6c02-43e7-a570-7fa3d528f366">


## Running the program:
The program is run via the main method which is located in the "Main" class. 
When the program is executed a window will pop up where you can navigate to the "menu" icon in the top left hand corner. To enter your own expressions click on "Enter new expression", this will prompt a dialog box where you can enter your expression. Once you have entered your expression, click "confirm", this will parse and display your expression if it is valid.


## Expression ambiguity:
Sometimes it is not clear how an expression should be evaluated due to a lack of brackets to provide precedence. For example, the expression `a ∩ b ∪ c` could be evaluated as `(a ∩ b) ∪ c` where the intersect of 'a' and 'b' is evaluated first. Or it could be evaluated as `a ∩ (b ∪ c)` where the union of 'b' and 'c' in evaluated first, both expressions will produce vastly different outputs. To remove this ambiguity an expression will always be treated as having 'right precedence' when it is ambiguous, i.e. the expression `a ∩ b ∪ c` will be evaluated as `a ∩ (b ∪ c)` by default. So, if you want `a ∩ b ∪ c` to evaluate the intersect of 'a' and 'b' first, then you must use brackets to show this precedence `(a ∩ b) ∪ c`. 


## Sets:
A set is a collection of elements that represent a group of data. In this application the user does not need to provide any data as this is simply a visualisation.
### Set identifiers:
For the sake of cleaness and simplicity, sets must be expressed as lower or upper case
letters in an expression. However, uppercase letters will automatically be converted to
lowercase, so `A` will be treated as `a`.
`A, b, c, x, Y, z` are all valid identifiers. But, `ab, [empty string], BC, aCCD` are not.
Furthermore, using the same letter in either upper or lowercase form at different points in
an expression will result in them pointing to the same set. For example: `... a .... A .... A`, each of these sets are equal `(a == A == A)`.
### The universal set:
A set that contains all data in the domain (in this case the domain is all the pixels in the graphical view)
It is denoted by the capital letter "U".


## Operators:
There are four different operators: intersect, union, difference, and complement, each perform unique operations on sets.
Let A and B be sets, let U represent the universal set.
#### Intersect (∩):
- The intersect operator defines all the unique elements that are members of both A and B. 
#### Union (∪):
- The union operator defines all the elements that are members of A plus all the elements that are members of B. 
#### Difference (A\B):
- The difference operator defines all the elements that are in A but not in B. 
#### Complement (U\A, represented by '~'):
- The complement operator defines all the elements of U minus all the elements of A.
