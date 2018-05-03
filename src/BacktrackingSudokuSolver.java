import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Standard Backtracking Constraint Satisfaction Problem solver to solve the Sudoku problem
 * @author John Brockway
 */
public class BacktrackingSudokuSolver implements SudokuCSPSolver {

	/**
	 * The maximum assignments allowed for the CSP solver
	 */
	int maxAssignmentsAllowed;

	/**
	 * Constructor specifying the maximum assignments allowed
	 * @param maxAssignmentsAllowed The maximum assignments allowed for the CSP solver
	 */
	public BacktrackingSudokuSolver(int maxAssignmentsAllowed) {
		this.maxAssignmentsAllowed = maxAssignmentsAllowed;
	}
	
	@Override
	public int[][] solve(int[][] board) {
		
		// Keep track of the number of reassignments needed.
		// If at any point it exceeds the maximum allowed, we return the incomplete board
		int numberOfAssignments = 0;

		// Unchanging array which keeps a record of which numbers were specified by the puzzle, and which were added during the search
		boolean[][] given = new boolean[9][9];
		for (int i = 0 ; i < 9 ; i++) {
			for (int j = 0 ; j < 9 ; j++) {
				if (board[i][j] == 0) {
					given[i][j] = false;
				} else {
					given[i][j] = true;
				}
			}
		}

		int row = 0;
		int column = 0;

		// Used during navigation to determine whether we should move in a forward direction (an acceptable assignment was found
		// for a particular variable) or whether we need to return up the tree to try a different parent assignment
		boolean backtracking = false;

		while (row < 9) {
			while (column < 9) {

				// Can't change given values, so make sure this isn't one
				if (!given[row][column]) {

					// If viableNumbers[i] is true, this means that for this space, (i+1) is a valid assignment
					boolean [] viableNumbers = new boolean[9];
					for (int i = 0 ; i < 9 ; i++) {
						viableNumbers[i] = true;
					}

					// Since we pick the lowest-value valid assignment, this must mean that any value less than the current
					// value is invalid. Also, since we're backtracking, the current value must have been found to be invalid
					if (backtracking) {
						for (int i = 0 ; i < board[row][column] ; i++) {
							viableNumbers[i] = false;
						}
					}

					// Find all values that have been used in the current row and mark them invalid
					for (int i = 0 ; i < 9 ; i++) {
						if (board[row][i] != 0) {
							viableNumbers[board[row][i] - 1] = false;
						}
					}

					// Find all values that have been used in the current column and mark them invalid
					for (int i = 0 ; i < 9 ; i++) {
						if (board[i][column] != 0) {
							viableNumbers[board[i][column] - 1] = false;
						}
					}

					// Find all values that have been used in the current 9-block and mark them invalid
					for (int i = (row/3)*3 ; i < ((row/3)+1)*3 ; i++) {
						for (int j = (column/3)*3 ; j < ((column/3)+1)*3 ; j++) {
							if (board[i][j] != 0) {
								viableNumbers[board[i][j] - 1] = false;
							}
						}
					}

					// Set the current values to prepare for failure in case no valid assignment is found
					board[row][column] = 0;
					backtracking = true;

					// Find the lowest value viable assignment
					for (int i = 0 ; i < 9 ; i++) {
						if (viableNumbers[i]) {
							numberOfAssignments++;
							if (numberOfAssignments == maxAssignmentsAllowed) {
								return board;
							}
							board[row][column] = i + 1;
							backtracking = false;
							break;
						}
					}
				}

				// Movement around the board
				if (backtracking) {
					// If backtracking, we move in bottom-up, right to left order
					if (column > 0) {
						column--;
					} else {
						// If we're in the leftmost column, we move to the rightmost column of the next row up
						column = 8;
						row--;
						break;
					}
				} else {
					// If not backtracking, we move in top-down, left to right order
					if (column < 8) {
						column++;
					} else {
						// If we're in the rightmost column, we move to the leftmost column of the next row down
						column = 0;
						row++;
						break;
					}
				}
			}
		}

		return board;
	}
	
	@Override
	public int getMaxAssignments() {
		return maxAssignmentsAllowed;
	}

}
