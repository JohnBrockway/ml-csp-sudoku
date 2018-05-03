/**
 * Class to represent the set of domains (possible values) of all variables at a particular search level
 * @author John Brockway
 */
public class Domain {

	/**
	 * The array of domains for each location in the grid.
	 * For each variable in row i and column j, if domains[i][j][k] is true then the ij variable can have the value k+1.
	 * Otherwise, that value is invalid for that position.
	 */
	public boolean[][][] domains;

	/**
	 * Default constructor; initializes the domains array
	 */
	public Domain() {
		domains = new boolean[9][9][9];
	}

	/**
	 * Copies the exact state of the passed Domain into the Domain this method is called on
	 * @param d The Domain to copy from
	 */
	public void deepCopy(Domain d) {
		for (int i = 0; i < 9 ; i++) {
			for (int j = 0; j < 9 ; j++) {
				for (int k = 0; k < 9 ; k++) {
					this.domains[i][j][k] = d.domains[i][j][k];
				}
			}
		}
	}
}