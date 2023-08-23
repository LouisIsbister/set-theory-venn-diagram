# Set-Theory-Venn-Diagram, Louis Isbister
#
#
# About:
# This application evaluates expressions in set theory and graphically represents the result
# using Venn diagrams. That is, a user can enter an expression in set theory and be presented
# with its graphical representation of the data.
#
#
# Sets:
#   A set is a collection of unique elements that represent a group of data. In this application
# the user does not need to provide any data as it is predetermined.
# Set identifiers:
#   For the sake of cleaness and simplicity, the user must express sets as lower or upper case
# letters in their expression. However, lowercase letters will automatically be converted to
# uppercase. For example:
#   A, b, c, x, Y, z are all valid identifiers. But, ab, [empty string], BC, aCCD are all invalid.
# Furthermore, using the same letter in either upper or lowercase form at different points in
# an expression will result in them pointing to the same set. For example:
# ... a .... A .... A, each of these sets are equal (a == A == A)
#   The universal set: A set that contains all data in the domain (in this case the domain is all
# the pixels in the graphical view).
#
#
# Operators:
#   There are four different operators: intersect, union, difference, and compliment, each perform
# unique operations on sets.
# Let A and B be sets, let U represent the universal set.
# 1. intersect (∩):
#       - The intersect operator defines all the unique elements that are members of both A
#         and B. It must be followed by TWO arguments (A B) enclosed in brackets.
# 2. union (∪):
#       - The union operator defines all the elements that are members of A plus all the
#         elements that are members of B. It must be followed by TWO arguments (A B) enclosed
#         in brackets
# 3. Difference (\):
#       - The difference operator defines all the elements that are in A but not in B. Bust be
#         followed by TWO arguments (A B) enclosed in brackets
# 4. Compliment (U\A):
#       - The complement operator defines all the elements all the elements of U minus all the
#         elements of A. It must be followed by ONE (A) argument enclosed in brackets


# How to use the application:
#   The user can formulate an expression that consists of operators and sets, when writing
# an expression operators MUST be written using their english name, NOT mathematical notation
# (Although I should implement this...)
#   The application evaluates an expression by first parsing it into a binary tree, where the sets
# themselves are leaf nodes and operators are parent nodes. Because of this expressions must be
# written in Cambridge-Polish notation where the operator is written first followed by its arguments
# that are enclosed in brackets, note the brackets do not NEED to be closed buuuuuuut it looks nicer
# if they are.
# For example: the equation (A ∩ B) could be written as either of the following:
#       intersect(A B), or intersect(A B
#
#
# Arguments:
# An argument represents a sub-expression within the equation, an argument can be two things; a set,
# or an operator. Hence, an operator can have further operators as arguments.
#
#
# How to convert to Cambridge-Polish notation:
# Take the operator of two arguments and remove it outside the brackets, leaving the arguments
# inside the brackets. For example:
#                      ((B ∪ C) ∩ A) ∪ (B ∩ C)
#                                    ∪
#                                 /     \
#                              ∩           ∩
#                           /     \     /     \
#                         ∪        A   B       C
#                      /     \
#                    B        C
# Can be expressed as ∪(∩(∪(B C) A) ∩(B C)), to express it as a readable string for this program,
# convert the operators to their english name:
# union(intersect(union(B C) A) intersect(B C)) is a valid expression.
#  
#
# The following are a series of valid expressions:
# "union(intersect(A B) intersect(C D))"
# "union(A intersect(B C))"
# "intersect(A difference(B C))"
# "difference(intersect(C union(A B)) intersect(A B))"
# "union(intersect(C D) difference(A intersect(union(B C) union(D E))))"
# "complement(A)"
# "complement(difference(B C))"
# "union(intersect(A complement(union(B C))) intersect(B C))"
#
# Invalid cases:
# "complement(A difference(B C))" reason: complement must take one argument!
# "union(intersect(A C D))" reason: union and intersect must take two arguments.
# "" reason: no expression provided.
# "intersect(AA B)" reason: invalid set identifier.