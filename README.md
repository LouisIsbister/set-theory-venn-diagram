# Set Theory Venn Diagrams


# About:
This application evaluates expressions in set theory and graphically represents the result
using Venn Diagrams. That is, you can enter an expression in set theory and be presented
with the graphical representation of the data.

Example '(c ∩ d) ∪ (a \ ((b ∪ c) ∩ (d ∪ e)))':

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
# The universal set:
A set that contains all data in the domain (in this case the domain is all the pixels in the graphical view)
It is denoted by the capital letter "U".


# Operators:
There are four different operators: intersect, union, difference, and complement, each perform unique operations on sets.
Let A and B be sets, let U represent the universal set.
# Intersect (∩):
- The intersect operator defines all the unique elements that are members of both A and B. It must be followed by TWO arguments (A B) enclosed in brackets.
# Union (∪):
- The union operator defines all the elements that are members of A plus all the elements that are members of B. It must be followed by TWO arguments (A B) enclosed in brackets.
# Difference (A\B):
- The difference operator defines all the elements that are in A but not in B. Must be followed by TWO arguments (A B) enclosed in brackets.
# Complement (U\A, denoted as ~ in this application):
- The complement operator defines all the elements of U minus all the elements of A. It must be followed by ONE (A) argument enclosed in brackets.


# How to use the application:
The user can formulate an expression that consists of operators and sets, when writing an expression operators it must be written mathematical notation. The application evaluates an expression by first restructuring it into cambridge polish notation and then parsing it into a binary tree, where the sets themselves are leaf nodes and operators are parent nodes.

For example, a valid the equation could be '(A ∩ B)' or 'A ∩ B ∩ C'.  

# Expression ambiguity:  
Sometimes it is ambigious as to how an expressions should be executed, for example, there may be mulitple ways that an expression such as `a ∩ b ∪ c \ d` coud be interpreted or executed. It could be read as "a and b, OR, the difference of c and d" and expressed as `(a ∩ b) ∪ (c \ d)`. It could also be read as "a AND the combination of b and the difference of c and d", which is expressed as `a ∩ (b ∪ (c \ d))` and so on.  
To eleviate this ambiguity all expressions are read from left to right and brackets take precendence in an expression. As a result, the expression `a ∩ b ∪ c \ d` will be executed the same as the expression `a ∩ (b ∪ (c \ d))`.   

# More valid expressions:  
a ∪ (b ∩ c)              "a or b and c"  
(a ∩ b) ∪ (c ∩ d)        "a and b or c and d"  
((b ∪ c) ∩ a) ∪ (b ∩ c)  "a and b or c or b and c"  
a ∩ (b \ c)              "a and the difference of b and c"  
a \ (b ∩ c)              "the difference of a and the intersection of b and c"  
(c ∩ (a ∪ b)) \ (a ∩ b)  "the difference of the (intersection of c and the union of a and b) and (a and b)"  
~ (a \ b)                 "complement of the difference between a and b"  
a ∩ b ∩ (~ (c ∪ d))       "a and b and the complement of c or d"  
(c ∩ d) ∪ (a \ ((b ∪ c) ∩ (d ∪ e))) "c and d or the difference between a and the intersection of c and d's union and d and e's union"  

# Examples of Invalid expressions:  
~ (a b \ c), reason: complement can only take one argument.  
(a ∪ b c), reason: union can only take two arguments.  
, reason: no expression provided.  
AA ∪ B, reason: invalid set identifier.  
