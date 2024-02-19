# Set Theory Venn Diagrams


# About:
This app evaluates expressions in set theory and graphically represents the result
using Venn Diagrams.

Example: `(c ∩ d) ∪ (a \ ((b ∪ c) ∩ (d ∪ e)))`

<img width="377" alt="SetTheorySH" src="https://github.com/LouisIsbister/Set-Theory-Venn-Diagram/assets/104889878/d0e393b9-6c02-43e7-a570-7fa3d528f366">


# Running the program:
The program is run via the main method which is located in the "Main" class. 
To run your own expressions simply run the main method, a window will pop up where you can navigate to the "menu" icon in the top lefthand corner. Then click of "Enter new expression", this will prompt a dialog box where you can enter an expression, once you have entered your expression, click "confirm". This will parse and display your expression if it is valid.


# Sets:
A set is a collection of elements that represent a group of data. In this application the user does not need to provide any data as this is simply a visualisation.
## Set identifiers:
For the sake of cleaness and simplicity, sets must be expressed as lower or upper case
letters in an expression. However, uppercase letters will automatically be converted to
lowercase, so `A` will be treated as `a`.
`A, b, c, x, Y, z` are all valid identifiers. But, `ab, [empty string], BC, aCCD` are not.
Furthermore, using the same letter in either upper or lowercase form at different points in
an expression will result in them pointing to the same set. For example: `... a .... A .... A`, each of these sets are equal `(a == A == A)`.
## The universal set:
A set that contains all data in the domain (in this case the domain is all the pixels in the graphical view)
It is denoted by the capital letter "U".


# Operators:
There are four different operators: intersect, union, difference, and complement, each perform unique operations on sets.
Let A and B be sets, let U represent the universal set.
## Intersect (∩):
- The intersect operator defines all the unique elements that are members of both A and B. 
## Union (∪):
- The union operator defines all the elements that are members of A plus all the elements that are members of B. 
## Difference (A\B):
- The difference operator defines all the elements that are in A but not in B. 
## Complement (U\A, is represented by '~'):
- The complement operator defines all the elements of U minus all the elements of A.


# Some valid expressions:  
a ∪ (b ∩ c)  
(a ∩ b) ∪ (c ∩ d)  
((b ∪ c) ∩ a) ∪ (b ∩ c)  
a ∩ (b \ c)    
a \ (b ∩ c)  
(c ∩ (a ∪ b)) \ (a ∩ b)    
~ (a \ b)  
a ∩ b ∩ (~ (c ∪ d))  
(c ∩ d) ∪ (a \ ((b ∪ c) ∩ (d ∪ e)))    

# Examples of Invalid expressions:  
~ (a b \ c), reason: complement can only take one argument.  
(a ∪ b c), reason: union can only take two arguments.  
, reason: no expression provided.  
AA ∪ B, reason: invalid set identifier.  
