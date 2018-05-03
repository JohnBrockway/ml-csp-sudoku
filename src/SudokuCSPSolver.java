/**
 * Interface for classes which use CSP solving strategies to solve Sudoku puzzles
 * @author John Brockway
 */
public interface SudokuCSPSolver {
	
	/**
	 * Solves the given Sudoku board
	 * @param board The initial state of the board as a 9x9 grid, with empty spaces represented by 0
	 * @return The solved board
	 */
	public int[][] solve(int[][] board);
	
	/**
	 * Accessor for the maximum allowed assignments variable
	 * @return The maximum allowed assignements for the CSP solver
	 */
	public int getMaxAssignments();
}
