# Set-Theory-Venn-Diagram, Louis Isbister


# About:
This application evaluates expressions in set theory and graphically represents the result
using Venn Diagrams. That is, a user can enter an expression in set theory and be presented
with the graphical representation of the data.

Example:

<img width="377" alt="SetTheorySH" src="https://github.com/LouisIsbister/Set-Theory-Venn-Diagram/assets/104889878/d0e393b9-6c02-43e7-a570-7fa3d528f366">


# Opening the program:
Download the code as a .zip file from the "<>Code" tab in git.
## BlueJ
From here you can directly open it in BlueJ by going to "Project" -> "Open ZIP/JAR" and selecting the zip file.
You'll likely get a pop-up about "Package line mismatches", you can click continue and it simply adds the statement "package src;" to each file. 
## Eclipse
If you're using eclipse you will need to extract the .zip file to a new folder. You can then create a new java project in eclipse and right click on it, selecting "Import" ->  "File system" and navigate to the "src" directory within your new folder, then click "select folder". Select the java source files in eclipse and make sure to set the "into folder" as "[your_project_name]/src. Then click "finish".
## VSC
If you're using Visual Studio Code then you just need to extract the .zip file to a new folder. Then navigate to "File" -> "Open Folder", then, navigate to the directory of the folder you've just created and select the "src" folder. Then click "Select folder" and it will open the source files in VSC. 

# Running the program:
The program is run via the main method which is located in the "Main" class. 
To run your own expressions simply run the main method, you will greeted with a dialog box that 
asks you to enter an expression, once you have entered your expression, click "confirm", this will
parse and display your expression if it is valid. 
Then, if you want to run a new expression navigate to the "Menu" icon in the top leftand select the "Enter new expression" item. This will allow you to enter a new expression.


# Sets:
A set is a collection of unique elements that represent a group of data. In this application the user does not need to provide any data as it is predetermined.
## Set identifiers:
For the sake of cleaness and simplicity, the user must express sets as lower or upper case
letters in their expression. However, uppercase letters will automatically be converted to
lowercase (so `A` will be the same as `a`).
`A, b, c, x, Y, z` are all valid identifiers. But, `ab, [empty string], BC, aCCD` are all invalid.
Furthermore, using the same letter in either upper or lowercase form at different points in
an expression will result in them pointing to the same set. For example: `... a .... A .... A`, each of these sets are equal `(a == A == A)`.
# The universal set:
A set that contains all data in the domain (in this case the domain is all the pixels in the graphical view).


# Operators:
There are four different operators: intersect, union, difference, and complement, each perform unique operations on sets.
Let A and B be sets, let U represent the universal set.
# Intersect (∩):
- The intersect operator defines all the unique elements that are members of both A and B. It must be followed by TWO arguments (A B) enclosed in brackets.
# Union (∪):
- The union operator defines all the elements that are members of A plus all the elements that are members of B. It must be followed by TWO arguments (A B) enclosed in brackets.
# Difference (A\B):
- The difference operator defines all the elements that are in A but not in B. Must be followed by TWO arguments (A B) enclosed in brackets.
# Complement (U\A):
- The complement operator defines all the elements of U minus all the elements of A. It must be followed by ONE (A) argument enclosed in brackets.


# How to use the application:
The user can formulate an expression that consists of operators and sets, when writing an expression operators MUST be written using their english name, NOT mathematical notation
(Although I should implement this...) The application evaluates an expression by first parsing it into a binary tree, where the sets themselves are leaf nodes and operators are parent nodes. Because of this expressions must bewritten in Cambridge-Polish notation where the operator is written first followed by its arguments that are enclosed in brackets, note the brackets do not NEED to be closed buuuuuuut it looks nicer if they are.

For example: the equation (A ∩ B) could be written as either of the following:
intersect(A B), or intersect(A B

# Arguments:
An argument represents a sub-expression within the equation, an argument can be two things; a set, or an operator. Hence, an operator can have further operators as arguments.

## Converting to Cambridge-Polish notation:
Take the operator of two arguments and remove it outside the brackets, leaving the arguments inside the brackets. For example:
```
                       ((B ∪ C) ∩ A) ∪ (B ∩ C)
                                     ∪
                                  /     \
                               ∩           ∩
                            /     \     /     \
                          ∪        A   B       C
                       /     \
                     B        C
```
Can be expressed as ∪(∩(∪(B C) A) ∩(B C)), to express it as a readable string for this program, convert the operators to their english name: union(intersect(union(B C) A) intersect(B C)) is a valid expression.


# Examples of valid expressions:
- "union(intersect(A B) intersect(C D))"
- "union(A intersect(B C))"
- "intersect(A difference(B C))"
- "difference(intersect(C union(A B)) intersect(A B))"
- "union(intersect(C D) difference(A intersect(union(B C) union(D E))))", this is the expression used for the example!
- "complement(A)"
- "complement(difference(B C))"
- "union(intersect(A complement(union(B C))) intersect(B C))"

# Examples of Invalid expressions:
- "complement(A difference(B C))", reason: complement can only take one argument.
- "union(intersect(A C D))", reason: union and intersect must take two arguments.
- "", reason: no expression provided.
- "intersect(AA B)", reason: invalid set identifier.
