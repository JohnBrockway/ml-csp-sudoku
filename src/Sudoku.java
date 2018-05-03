import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Sudoku {

	/**
	 * @param args Contains the file of the problem instance in a specified form (each line of the puzzle on a new line, numbers separated by spaces, blank spaces denoted by 0). 
	 * Also contains the solver type to use; 'simple' or 'forward'. 
	 * Optionally, has the number of assignments allowed for the retrieval of the solution.
	 * @throws IOException
	 */
	public static void main(String[] args) {
		try {
			// Read the file
			int[][] board = new int[9][9];
			BufferedReader input = new BufferedReader(new FileReader(args[0]));
			for (int i = 0 ; i < 9 ; i++) {
				String line = input.readLine();
				String[] lineSplit = line.split(" ");
				for (int j = 0 ; j < 9 ; j++) {
					board[i][j] = Integer.parseInt(lineSplit[j]);
				}
			}
			input.close();
			
			// Call the appropriate CSP solver
			int maxAssignmentsAllowed = 10000;
			if (args.length > 2) {
				maxAssignmentsAllowed = Integer.parseInt(args[2]);
			}
			SudokuCSPSolver solver;
			if (args[1].equals("simple")) {
				solver = new BacktrackingSudokuSolver(maxAssignmentsAllowed);
			}
			else if (args[1].equals("forward")) {
				solver = new ForwardCheckingCSPSolver(maxAssignmentsAllowed);
			}
			else {
				System.out.println(String.format("Invalid args: %s", args[1]));
				return;
			}
			int[][] solvedBoard = solver.solve(board);
			
			// Formatted printing of solution board
			boolean solved = true;
			for (int i = 0 ; i < 9 ; i++) {
				if (i % 3 == 0 && i != 0) {
					System.out.println("- - - - - - - - - - - ");
				}
				for (int j = 0 ; j < 9 ; j++) {
					if (j % 3 == 0 && j != 0) {
						System.out.print("| ");
					}
					if (solvedBoard[i][j] != 0) {
						System.out.print(solvedBoard[i][j] + " ");
					}
					else {
						System.out.print("* ");
						solved = false;
					}
				}
				System.out.print("\n");
			}
			
			// User-friendly prompts to help troubleshoot if a puzzle is unsolved
			if (!solved) {
				System.out.println("This puzzle was unsolved with the allowed maximum assignments of " + solver.getMaxAssignments());
				if (solver instanceof BacktrackingSudokuSolver) {
					System.out.println("Try using the 'forward' argument instead of 'simple', to use the more efficient Forward Checking mode");
				}
				System.out.println("Try increasing the number of allowed assignments from the default of 10,000 (keep in mind this will cause the solver to take longer)");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
