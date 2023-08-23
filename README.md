# Set-Theory-Venn-Diagram, Louis Isbister
#
#
# About:
# This application evaluates expressions in set theory and graphically represents the result 
# using Venn diagrams. That is, a user can enter an expression in set theory and be presented 
# with its graphical representation of the data.
#
#
# How to use the application:
# The application evaluates an expression by first parsing it into a binary tree, where the sets
# themselves are leaf nodes and operators are parent nodes. This means the expression must be written
# in Cambridge-Polish notation. Furthermore, an operator (intersect, union, difference, or compliment)
# must be followed by arguments that are in brackets, note the brackets do not need to be closed.
# For example: the equation (A ∩ B) could be written as either of the following:
# 		intersect(A B), or intersect(A B 
#
#
# Arguments:
# An argument represents a sub-expression within the equation, an argument can be two things; a set, or an operator.
# Hence, an operator can have further operators as arguments. 
#
#
# Set identifiers:
# For the sake of cleaness and simplicity, the user must express sets as lower or upper case letters in their 
# expression. However, lowercase letters will automatically be converted to uppercase.
# Therefore, both of the following are valid:
# intersect(A B) 
# intersect(A b) 
# Furthermore, if the user enters the same letter at different in the expression but one is lower and one
# is uppercase, they will be treated as the same set:
# For example:
# intersect(b B) is equivalent to intersect(B B)
# intersect(a union(A B)) is equivalent to intersect(A union(A B))
#
#
# Operator rules:
# 1. intersect (∩):
# 		The intersect operator must be followed by TWO arguments enclosed in brackets
# 2. union (∪): 
# 		The union operator must be followed by TWO arguments enclosed in brackets
# 3. Difference (\): 
# 		The difference operator must be followed by TWO arguments enclosed in brackets
# 4. Compliment (U\A):
# 		The complement operator must be followed by ONE argument enclosed in brackets
#
#
# How to convert to Cambridge-Polish notation:
# Take the operator of two arguments and remove it outside the brackets, leaving the arguments inside the brackets.
# For example:
#                      ((B ∪ C) ∩ A) ∪ (B ∩ C) 
#                                    ∪
#                                 /     \
#                              ∩           ∩
#                           /     \     /     \
#                         ∪        A   B       C
#                      /     \
#                    B        C
# Which can be expressed as ∪(∩(∪(B C) A) ∩(B C)) == union(intersect(union(B C) A) intersect(B C))
#  
#
# The following are a series of valid equations:
# "union(intersect(A B) intersect(C D))"
# "union(A intersect(B C))"
# "intersect(A difference(B C))"
# "difference(intersect(C union(A B)) intersect(A B))"
# "union(intersect(C D) difference(A intersect(union(B C) union(D E))))"
# "complement(A)
# "complement(difference(B C))"
# "union(intersect(A complement(union(B C))) intersect(B C))"
#
# Invalid cases:
# "complement(A difference(B C))"
# "union(intersect(A C D))"
