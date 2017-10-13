package Solver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import State.Evaluation;
import State.State;
import State.mStep;

public class DeepningDepthFirst extends Solver {

	private State curState;
	private List<mStep> curPath;

	public DeepningDepthFirst(State initial) {
		super("Deepning Depth First", initial);
		curState = new State(initial);
		curPath = new LinkedList<>();
	}

	@Override
	public long SolveWithGoals() {
		int goals[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		long startTime = System.currentTimeMillis();
		Boolean goalFound;

		// Check feasibility
		if (!initial.isSolvable()) {
			this.solutionFound = false;
			this.solution = new LinkedList<>();
			this.solution.add(mStep.NOP);
			this.stat.increaseIterations();
			return startTime - System.currentTimeMillis();
		}

		// Iterate for each goal we decided
		for (int goal : goals) {
			goalFound = false;
			this.stat.addGoal(goal);
			for (int mDepth = 1; !goalFound; mDepth++) {
				List<mStep> tmp = new LinkedList<>();
				tmp.add(mStep.NOP);
				goalFound = solveGoal(goal, tmp, mDepth);

				// Update the solution
				this.solution.addAll(curPath);
				// Update the current state of the board (we will start from
				// here
				// for the new goal search)
				curState = curState.calculateState(curPath);
				this.stat.setState(curState);

				// Clear all the temporary data:
				// The path must start from scratch (having the base the
				// curState)
				if (curPath.size() > 0)
					curPath.clear();
				// We consider duplicated states depending on the actual goal we
				// want to achieve, It can happen
				// to visit a configuration while looking for a completely
				// different
				// one.
				if (this.visited.size() > 0)
					this.visited.clear();
			}

		}

		// I have fulfilled all the goals! time to clear the solution from the
		// useless NOPs
		this.solution.removeAll(Arrays.asList(mStep.NOP));
		if (this.solution.size() == 0) {
			this.solution.add(mStep.NOP);
		}
		this.solutionFound = true;
		this.stat.setTime(System.currentTimeMillis() - startTime);
		return this.stat.getTime();

	}

	@Override
	public long Solve() {
		Boolean goalFound = false;
		long startTime = System.currentTimeMillis();
		// Check feasibility, otherwise we risk to loop forever
		if (!initial.isSolvable()) {
			this.solutionFound = false;
			this.solution = new LinkedList<>();
			this.solution.add(mStep.NOP);
			this.stat.increaseIterations();
			return startTime - System.currentTimeMillis();
		}
		for (int mDepth = 1; !goalFound; mDepth++) {
			List<mStep> tmp = new LinkedList<>();
			tmp.add(mStep.NOP);
			goalFound = solve(tmp, mDepth);
			if (this.visited.size() > 0)
				this.visited.clear();
		}
		this.stat.setTime(System.currentTimeMillis() - startTime);
		// Our program tends to add some NOP steps to the solution (NOP is used as starting step)
		// Hence we need to remove them.
		this.solution.removeAll(Arrays.asList(mStep.NOP));
		if (this.solution.size() == 0) {
			this.solution.add(mStep.NOP);
		}
		return this.stat.getTime();
	}
/***
 * The actual recursive solving solution
 * @param steps The current steps we are evaluating
 * @param maxDepth the max depth we want to reach with the function
 * @return If we found a goal within the desired depth
 */
	private Boolean solve(List<mStep> steps, int maxDepth) {
		Evaluation eval = initial.eval(steps);
		List<mStep> tmp;
		this.stat.increaseIterations();
		if (steps.size() > maxDepth) {
			return false;
		}
		if (eval.isValid() && visited.add(eval.getHash())) {
			this.stat.increaseVisitedStates();
			if (!eval.isOptimal()) {
				tmp = new LinkedList<>(steps);
				tmp.add(mStep.Up);
				if (solve(tmp, maxDepth)) {
					return true;
				}
				tmp = new LinkedList<>(steps);
				tmp.add(mStep.Down);
				if (solve(tmp, maxDepth)) {
					return true;
				}
				tmp = new LinkedList<>(steps);
				tmp.add(mStep.Left);
				if (solve(tmp, maxDepth)) {
					return true;
				}
				tmp = new LinkedList<>(steps);
				tmp.add(mStep.Right);
				if (solve(tmp, maxDepth)) {
					return true;
				}
				return false;
			} else {
				// The state is optimal, our job is complete!
				this.solution = steps;
				this.solutionFound = true;
				return true;
			}
		} else {
			if (eval.isValid()) {
				this.stat.increaseDuplicateStates();
			} else {
				this.stat.increaseInvalidStates();
			}
			return false;
		}
	}
/***
 * Splits the problem in minor tasks. It has to be used with SolveWithGoals. The behavior is similar to solve, but solveGoal
 * is designed to run multiple times with different goals
 * @param goal The goal we want to achieve
 * @param steps The actual steps we have taken so far
 * @param maxDetpth The max depth we want to reach
 * @return Whether we reached the desired goal within the max depth
 */
	private Boolean solveGoal(int goal, List<mStep> steps, int maxDetpth) {
		Evaluation eval = curState.evalGoal(steps, goal);
		List<mStep> tmp;
		this.stat.increaseIterations();
		if (steps.size() > maxDetpth) {
			return false;
		}
		eval = curState.evalGoal(steps, goal);
		if (eval.isValid()) {
			if (eval.isOptimal()) {
				this.curPath = steps;
				return true;
			} else {
				if (visited.add(eval.getHash())) {
					this.stat.increaseVisitedStates();
					// Add all the possible children to fetch
					tmp = new LinkedList<>(steps);
					tmp.add(mStep.Up);
					// Recursive part, here depth first is implemented
					// We need to check the return: in case it is positive the result is propagated, otherwise we 
					// Fetch the other children
					if (solveGoal(goal, tmp, maxDetpth)) {
						return true;
					}
					tmp = new LinkedList<>(steps);
					tmp.add(mStep.Down);
					if (solveGoal(goal, tmp, maxDetpth)) {
						return true;
					}
					tmp = new LinkedList<>(steps);
					tmp.add(mStep.Left);
					if (solveGoal(goal, tmp, maxDetpth)) {
						return true;
					}
					tmp = new LinkedList<>(steps);
					tmp.add(mStep.Right);
					if (solveGoal(goal, tmp, maxDetpth)) {
						return true;
					}
					// if none of the children returned true, the solution is not on this branch
					return false;
				} else {
					this.stat.increaseDuplicateStates();
					return false;
				}
			}
		} else {
			this.stat.increaseInvalidStates();
			return false;
		}
	}

}
