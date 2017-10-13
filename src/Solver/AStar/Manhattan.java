package Solver.AStar;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Solver.Solver;
import State.CostStep;
import State.State;
import State.mStep;

/***
 * The problem is solved by applying a A* Search combined with the manhattan distance to the search tree
 * we generate. In order to speed up computation and optimize memory usage, we
 * will not represent the states as a matrix. Each node in the tree will be
 * represented by the series of step we need to apply to its initial state in
 * order to obtain the desired node.
 * Hashset has been optimized by implementing gnu Trove high performance Collections
 * 
 * Note on use:
 * 1) Create the State with the given constructors
 * 2) Two different solving methods are available
 * 		- Solve()
 * 			Solve by considering as a goal the final complete state of the
 * 			problem. Very intensive memory and cpu usage mainly to hashing
 * 			function in the hashSet.
 * 		- SolveWithStates()
 * 			Solve by dividing the problem in consequent goals and solving them 
 * 			as a single problem each. It is faster and has lighter memory
 * 			usage, hence it can be used to solve all the feasible problems.
 * 			There is the possibility of duplicate states, since duplication is
 * 			checked only among the current goal
 * @author Olivo Iacopo
 *
 */
public class Manhattan extends Solver{

	private List<CostStep> queue;

	public Manhattan(State initial) {
		super("A* Manhattan Distance", initial);
		queue = new LinkedList<>();
	}

	@Override
	public long Solve() {
		long startTime = System.currentTimeMillis();
		List<mStep> tmp;
		CostStep cStep;
		Boolean goalFound = false;

		stat.setState(initial);		
		// Check feasibility
		if (!initial.isSolvable()) {
			this.solutionFound = false;
			this.solution = new LinkedList<>();
			this.solution.add(mStep.NOP);
			this.stat.increaseIterations();
			return startTime - System.currentTimeMillis();
		}

		cStep = new CostStep(Arrays.asList(mStep.NOP), initial.evalWithManhattan(Arrays.asList(mStep.NOP)));
		queue.add(cStep);
		if (initial.isSolvable()) {
			while (!goalFound) {
				cStep = queue.remove(0);
				this.stat.increaseIterations();
				if (cStep.getEval().isValid() && visited.add(cStep.getEval().getHash())) {
					this.stat.increaseVisitedStates();
					if (!cStep.getEval().isOptimal()) {
						tmp = new LinkedList<>(cStep.getStep());
						tmp.add(mStep.Up);
						queue.add(new CostStep(tmp, initial.evalWithManhattan(tmp)));
						tmp = new LinkedList<>(cStep.getStep());
						tmp.add(mStep.Down);
						queue.add(new CostStep(tmp, initial.evalWithManhattan(tmp)));
						tmp = new LinkedList<>(cStep.getStep());
						tmp.add(mStep.Left);
						queue.add(new CostStep(tmp, initial.evalWithManhattan(tmp)));
						tmp = new LinkedList<>(cStep.getStep());
						tmp.add(mStep.Right);
						queue.add(new CostStep(tmp, initial.evalWithManhattan(tmp)));
						this.stat.setQueueSize(queue.size());
						queue.sort(CostStep::compareTo);
					} else {
						// The state is optimal, our job is complete!
						this.solutionFound = true;
						this.solution = cStep.getStep();
						goalFound = true;
					}
				} else {
					if (cStep.getEval().isValid()) {
						this.stat.increaseDuplicateStates();
					} else {
						this.stat.increaseInvalidStates();
					}
				}
			}

		} else {
			this.solutionFound = false;
			this.solution = new LinkedList<>(Arrays.asList(mStep.NOP));

		}
		this.stat.setTime(System.currentTimeMillis() - startTime);
		this.solution.removeAll(Arrays.asList(mStep.NOP));
		if (this.solution.size() == 0) {
			this.solution.add(mStep.NOP);
		}
		return this.stat.getTime();
	}

	public long SolveWithGoals() {
		int goals[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		long startTime = System.currentTimeMillis();
		CostStep cStep;
		State curState;
		List<mStep> tmp;
		Boolean goalFound;

		// Check feasibility
		if (!initial.isSolvable()) {
			this.solutionFound = false;
			this.solution = new LinkedList<>();
			this.solution.add(mStep.NOP);
			this.stat.increaseIterations();
			return startTime - System.currentTimeMillis();
		}

		// The problem is feasible, let's try
		tmp = new LinkedList<>();
		tmp.add(mStep.NOP);
		cStep = new CostStep(tmp, initial.evalGoalWithManhattan(tmp, 0));
		queue.add(cStep);
		curState = new State(initial);

		/*
		 * Our objective is to divide the broader bigger problem is sub goals. Each goal
		 * should be easy enough to solve in a reasonable amount of timeS
		 */
		for (int goal : goals) {
			goalFound = false;
			this.stat.addGoal(goal);
			while (!goalFound) {
				this.stat.increaseIterations();
				cStep = queue.remove(0);
				if (cStep.getEval().isValid()) {
					if (cStep.getEval().isOptimal()) {
						goalFound = true;
					} else {
						if (visited.add(cStep.getEval().getHash())) {
							this.stat.increaseVisitedStates();
							// Add all the possible children to fetch
							tmp = new LinkedList<>(cStep.getStep());
							tmp.add(mStep.Up);
							queue.add(new CostStep(tmp, curState.evalGoalWithManhattan(tmp, goal)));
							tmp = new LinkedList<>(cStep.getStep());
							tmp.add(mStep.Down);
							queue.add(new CostStep(tmp, curState.evalGoalWithManhattan(tmp, goal)));
							tmp = new LinkedList<>(cStep.getStep());
							tmp.add(mStep.Left);
							queue.add(new CostStep(tmp, curState.evalGoalWithManhattan(tmp, goal)));
							tmp = new LinkedList<>(cStep.getStep());
							tmp.add(mStep.Right);
							queue.add(new CostStep(tmp, curState.evalGoalWithManhattan(tmp, goal)));
							this.stat.setQueueSize(queue.size());
							queue.sort(CostStep::compareTo);
						} else {
							this.stat.increaseDuplicateStates();
						}
					}
				} else {
					this.stat.increaseInvalidStates();
				}
			}
			// Update the solution
			this.solution.addAll(cStep.getStep());
			// Update the current state of the board (we will start from here
			// for the new goal search)
			curState = curState.calculateState(cStep.getStep());
			this.stat.setState(curState);
			// Clear all the temporary data:
			// The path must start from scratch (having the base the curState)
			if (cStep.getStep().size() > 0)
				cStep.getStep().clear();
			// We consider duplicated states depending on the actual goal we
			// want to achieve, It can happen
			// to visit a configuration while looking for a completely different
			// one.
			if (this.visited.size() > 0)
				this.visited.clear();
			// We are solving the problem toward a new goal, hence we don't care
			// of the older paths
			if (this.queue.size() > 0)
				this.queue.clear();

			// I will have to move from this state, otherwise it is a duplicate
			// one
			tmp = new LinkedList<>();
			tmp.add(mStep.NOP);
			cStep = new CostStep(tmp, curState.evalGoalWithManhattan(tmp, goal+1));
			queue.add(cStep);

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


}
