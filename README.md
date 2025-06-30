# Set Theory Venn Diagrams
This app evaluates expressions in set theory and graphically represents the result using venn diagrams.  
e.g. `(c ∩ d) ∪ (a \ (b ∩ e))`  

![Screenshot 2024-06-18 165959](https://github.com/LouisIsbister/set-theory-venn-diagram/assets/104889878/3d77d2b2-20f6-4fb0-b45e-75d6bc1ec99c)


## Building and Running the Program:
You can build and run the project using Maven by running the following commands from the root project directory:  

```
mvn clean install
mvn exec:java
```

If you do not have Maven installed you can execute the `.jar` file that is located in the `bin` directory using the command:  

`java -jar bin\Set-Theory-Venn-Diagram-1.0-SNAPSHOT.jar`  

When the program is executed a window will pop up where you can navigate to the "menu" icon in the top left-hand corner. To enter your own expressions click on "Enter new expression", this will prompt a dialog box where you can enter your expression. Once you have entered your expression, click "confirm," this will parse and display your expression if it is valid, otheriwse it will give you some feedback on what went wrong.


## Expression Ambiguity and Execution:
Each expression is executed in two phases, first it is parsed into Polish Notation (PN) which is then used to build a binary tree that can be executed to retrieve the expression output. However, sometimes it isn't clear how an expression should be evaluated due to a lack of brackets to show precedence. For example, the expression `a ∩ b ∪ c` could be evaluated as `(a ∩ b) ∪ c` where the intersect of `a` and `b` is evaluated first. Or it could be evaluated as `a ∩ (b ∪ c)` where the union of `b` and `c` in evaluated first, both expressions will produce vastly different outputs. To remove this ambiguity an expression will always be treated as having 'right precedence' when it is ambiguous, i.e. the expression `a ∩ b ∪ c` will be evaluated as `a ∩ (b ∪ c)` by default. So, if you want `a ∩ b ∪ c` to evaluate the intersect of `a` and `b` first, then you must use brackets to show the precedence `(a ∩ b) ∪ c`. You can check this by pressing "Execution Representation" which lets you view the expression in its execution form and see the precedence that is enforced during parsing. This may help with debugging where to put brackets in an expression:

![Screenshot 2025-06-30 134303](https://github.com/user-attachments/assets/84f3ffbe-5716-4b26-b6ec-3aea25f5fae2)
![Screenshot 2025-06-30 133857](https://github.com/user-attachments/assets/47909e17-425b-4f7d-a62d-ef0be88024d6)


## Sets:
A set is a collection of elements that represent a group of data, but in this application you do not need to provide any data as it is simply a visualisation.
### Set identifiers:
For the sake of cleaness and simplicity, sets must be expressed as lower or upper case letters in an expression. However, uppercase letters will automatically be converted to lowercase, so `A` will be treated as `a`.
`A, b, c, x, Y, z` are all valid identifiers. But, `ab, [empty string], BC, aCCD` are not. Furthermore, using the same letter in either upper or lowercase form at different points in an expression will result in them pointing to the same set. For example: `... a .... A .... A`, each of these sets are treated as the same set.


## Operators:
There are four supported operators: intersect, union, difference, and complement, each perform unique operations on sets.
Let A and B be sets, and U represent the universal set.
#### Intersect (∩):
- The intersect operator defines all the unique elements that are members of both A and B. 
#### Union (∪):
- The union operator defines all the elements that are members of A plus all the elements that are members of B. 
#### Difference (A\B):
- The difference operator defines all the elements that are in A but not in B. 
#### Complement (U\A, represented by ~A for simplicity):
- The complement operator defines all the elements of U minus all the elements of A.
