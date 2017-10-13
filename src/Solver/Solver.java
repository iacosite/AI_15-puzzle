package Solver;

import java.util.LinkedList;
import java.util.List;

import State.State;
import State.mStep;
import Statistics.Statistics;
import gnu.trove.set.hash.THashSet;

/***
 * Basic frame in order to standardize the various solving methods we implement.
 * It is not an interface due to the need of implementing some basic methods,
 * data structures and constructor
 * 
 * @author oliver
 *
 */
public abstract class Solver {
	protected State initial;
	protected THashSet<Long> visited;
	protected List<mStep> solution;
	protected Boolean solutionFound;
	protected Statistics stat;

	public Solver(String type, State initial) {
		this.initial = initial;
		this.visited = new THashSet<>();
		this.solution = new LinkedList<>();
		this.solutionFound = false;
		this.stat = new Statistics(type, 0);
		stat.addGoal(0);
	}

	/***
	 * @return number of inversions needed to solve the problem
	 */
	public int expectedSteps() {
		return initial.getInversions();
	}

	/***
	 * @return whether we found a solution for the problem.
	 */
	public Boolean isSolved() {
		return this.solutionFound;
	}

	/***
	 * 
	 * @return The solution we found.
	 */
	public List<mStep> getSolution() {
		return solution;
	}

	public Statistics getStat() {
		return this.stat;
	}

	/***
	 * Solve by dividing the problem in consequent goals and solving them 
 * 			as a single problem each. It is faster and has lighter memory
 * 			usage, hence it can be used to solve all the feasible problems.
 * 			There is the possibility of duplicate states, since duplication is
 * 			checked only among the current goal
	 * 
	 * @return The time needed to solve the problem
	 */
	public abstract long SolveWithGoals();
	/***
	 * Solve by considering as a goal the final complete state of the
 * 			problem. Very intensive memory and cpu usage mainly to hashing
 * 			function in the hashSet.
	 * 
	 * @return The time needed to solve the problem
	 */
	public abstract long Solve();
}
