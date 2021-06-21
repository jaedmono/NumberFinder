# NumberFinder

Class developed for this test OtherNumberFinder.

# Instructions

Technical Test

New specs and modified classes
 - made CustomNumberEntity final with private constructors
 - compare method will wait between 5 to 10 sec

Write a class to check if an int value is contained in a list of type CustomNumberEntity by the fastest means possible.

Constraints:
- Your class MUST implement the provided NumberFinder interface.

- The list of CustomNumberEntity values should be read from a Json file, a short example is given below.

- The contains method of your implementation MUST use the provided FasterComparator.compare method to compare the int value with each CustomNumberEntity. How you do this in the fastest possible time is the key. FastestComparator.compare cannot be modified and no other comparison method should be used (hashing, indexes etc)

- Do not cast or convert provided parameter types, compare method from FastestComparator will handle this. e.g. do not cast from int to String, or CustomNumberEntity.number to int (even if is not used for comparison purpose)

- You MUST include Junit tests for running your code.
- Write your code using java 7 or 8
