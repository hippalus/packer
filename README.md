# Package Challenge

## Introduction

The Package Challenge is a task where you need to determine the optimal items to include in a package based on weight and cost
constraints.

## Assignment Description

You want to send a package to your friend with different things. Each thing has an index number, weight, and cost. The package has
a weight limit, and the goal is to determine which things to put into the package so that the total weight is within the limit and
the total cost is maximized. The assignment requires implementing a class `Packer` with a static method `pack` that accepts a file
path as input and returns the solution as a string.

## Code Structure

The codebase for the Package Challenge consists of the following components:

- `Packer`: The main class that solves the package optimization problem.
- `PackageSolver`: An interface defining the contract for solving the package optimization problem.
- `InputParser`: An interface for parsing the input file and converting it into domain objects.
- `InputValidator`: An interface for validating the input data and ensuring it meets the constraints.
- `KnapsackSolver`: An implementation of the `PackageSolver` interface using
  the [Knapsack algorithm](https://en.wikipedia.org/wiki/Knapsack_problem)
- `TextInputParser`: An implementation of the `InputParser` interface for parsing the input file.
- `TextInputValidator`: An implementation of the `InputValidator` interface for validating the input data.
- `APIException`: A custom exception class for handling API-related exceptions.
- `PackerValidationException`: A custom exception class for handling input validation exceptions.

## Running the Code

To run the Package Challenge code, follow these steps:

1. Clone the repository or download the source code.
2. Build the code using Java 17 and your build tool (e.g., Maven, Gradle).
3. Ensure that the necessary dependencies are resolved.
4. Run the `PackerTest` class to execute the test cases and verify the functionality.
5. Modify the input file or add additional test cases as needed.
6. Use the `Packer.pack(filePath)` method to solve the package optimization problem for the given file path.
7. The output will be returned as a string.

## Constraints

The following constraints should be considered while developing the solution:

1. The maximum weight that a package can take is ≤ 100.
2. There might be up to 15 items to choose from.
3. The maximum weight and cost of an item are ≤ 100.
