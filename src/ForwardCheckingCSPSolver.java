import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Backtracking Constraint Satisfaction Problem solver with forward checking to solve the Sudoku problem
 * @author John Brockway
 */
public class ForwardCheckingCSPSolver implements SudokuCSPSolver {
	
	/**
	 * The maximum assignments allowed for the CSP solver
	 */
	int maxAssignmentsAllowed;

	/**
	 * Constructor specifying the maximum assignments allowed
	 * @param maxAssignmentsAllowed The maximum assignments allowed for the CSP solver
	 */
	public ForwardCheckingCSPSolver(int maxAssignmentsAllowed) {
		this.maxAssignmentsAllowed = maxAssignmentsAllowed;
	}
	
	@Override
	public int[][] solve(int[][] board) {

		// Keep track of the number of reassignments needed.
		// If at any point it exceeds the maximum allowed, we return the incomplete board
		// This should never happen, but is included to fail gracefully in case of some bad input
		int numberOfAssignments = 0;

		// Each element of this array stores the domain of every variable, and there is one Domain per search level possible
		Domain[] domainLevels = new Domain[82];
		domainLevels[0] = new Domain();

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

		for (int i = 0; i < 9 ; i++) {
			for (int j = 0; j < 9 ; j++) {
				for (int k = 0; k < 9 ; k++) {
					domainLevels[0].domains[i][j][k] = true;
				}
			}
		}

		for (int row = 0 ; row < 9 ; row++) {
			for (int column = 0 ; column < 9 ; column++) {
				if (board[row][column] == 0) {
					// Find all values that have been used in the current row and mark them invalid
					for (int i = 0 ; i < 9 ; i++) {
						if (board[row][i] != 0) {
							domainLevels[0].domains[row][column][board[row][i] - 1] = false;
						}
					}
					// Find all values that have been used in the current column and mark them invalid
					for (int i = 0 ; i < 9 ; i++) {
						if (board[i][column] != 0) {
							domainLevels[0].domains[row][column][board[i][column] - 1] = false;
						}
					}
					// Find all values that have been used in the current 9-block and mark them invalid
					for (int i = (row/3)*3 ; i < ((row/3)+1)*3 ; i++) {
						for (int j = (column/3)*3 ; j < ((column/3)+1)*3 ; j++) {
							if (board[i][j] != 0) {
								domainLevels[0].domains[row][column][board[i][j] - 1] = false;
							}
						}
					}
				}
			}
		}

		for (int i = 1 ; i < 82 ; i++) {
			domainLevels[i] = new Domain();
			domainLevels[i].deepCopy(domainLevels[0]);
		}

		int row = 0;
		int column = 0;

		// Keep track of what level of search we're on to determine which domain to use
		int searchLevel = 0;

		// Used during navigation to determine whether we should move in a forward direction (an acceptable assignment was found
		// for a particular variable) or whether we need to return up the tree to try a different parent assignment
		boolean backtracking = false;

		while (row < 9) {
			while (column < 9) {

				// Can't change given values, so make sure this isn't one
				if (!given[row][column]) {

					// Since we're backtracking, the current value must have been found to be invalid
					if (backtracking) {
						domainLevels[searchLevel].domains[row][column][board[row][column] - 1] = false;
					}

					// Set the current values to prepare in case no valid assignment is found
					board[row][column] = 0;
					backtracking = true;

					// Remove any changes applied at the lower levels; those changes assumed we were using a value we are no longer using
					for (int i = searchLevel+1 ; i < 82 ; i++) {
						domainLevels[i].deepCopy(domainLevels[searchLevel]);
					}

					// Find the lowest value viable assignment
					for (int i = 0 ; i < 9 ; i++) {
						if (domainLevels[searchLevel].domains[row][column][i]) {

							// Remove this value from the domains of all variables in the same row
							for (int j = 0 ; j < 9 ; j++) {
								if (j != column && board[row][j] == 0) {
									domainLevels[searchLevel+1].domains[row][j][i] = false;
								}
							}
							// Remove this value from the domains of all variables in the same column
							for (int j = 0 ; j < 9 ; j++) {
								if (j != row && board[j][column] == 0) {
									domainLevels[searchLevel+1].domains[j][column][i] = false;
								}
							}
							// Remove this value from the domains of all variables in the same 9-block
							for (int j = (row/3)*3 ; j < ((row/3)+1)*3 ; j++) {
								for (int k = (column/3)*3 ; k < ((column/3)+1)*3 ; k++) {
									if (!(j == row && k == column) && board[j][k] == 0) {
										domainLevels[searchLevel+1].domains[j][k][i] = false;
									}
								}
							}

							// Check if there is any variable in the set that has an empty domain
							boolean emptyDomainExists = false;
							for (int j = 0 ; j < 9 && !emptyDomainExists ; j++) {
								for (int k = 0 ; k < 9 && !emptyDomainExists; k++) {
									boolean emptyDomain = true;
									for (int l = 0 ; l < 9 ; l++) {
										if (domainLevels[searchLevel+1].domains[j][k][l]) {
											emptyDomain = false;
											break;
										}
									}
									if (emptyDomain) {
										emptyDomainExists = true;
									}
								}
							}

							if (emptyDomainExists) {
								// If there is an empty domain, this is not a valid assignment, so undo changes and go to next possible value
								domainLevels[searchLevel+1].deepCopy(domainLevels[searchLevel]);
							} else {
								numberOfAssignments++;
								if (numberOfAssignments == maxAssignmentsAllowed) {
									return board;
								}

								board[row][column] = i + 1;

								// Set this to be the new standard for subsequent domain levels
								for (int j = searchLevel + 2 ; j < 82 ; j++) {
									domainLevels[j].deepCopy(domainLevels[searchLevel+1]);
								}

								backtracking = false;
								break;
							}
						}
					}
				}

				// Movement around the board
				if (backtracking) {
					searchLevel--;
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
					searchLevel++;
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
