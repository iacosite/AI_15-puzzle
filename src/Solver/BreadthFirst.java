package Solver;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import State.Evaluation;
import State.State;
import State.mStep;

/***
 * The problem is solved by applying a Breadth First Search to the search tree
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
 */
public class BreadthFirst extends Solver{

	private Deque<List<mStep>> queue;

	public BreadthFirst(State initial) {
		super("Breadth First", initial);
		this.queue = new LinkedList<>();
	}


	public long Solve() {
		long startTime = System.currentTimeMillis();
		List<mStep> curPath;
		List<mStep> tmp;
		Evaluation eval;
		Boolean goalFound = false;
		
		stat.setState(initial);		
		eval = initial.eval(Arrays.asList(mStep.NOP));
		visited.add(eval.getHash());
		if (!eval.isOptimal() && initial.isSolvable()) {
			this.stat.increaseIterations();
			queue.addLast(new LinkedList<mStep>(Arrays.asList(mStep.Up)));
			queue.addLast(new LinkedList<mStep>(Arrays.asList(mStep.Down)));
			queue.addLast(new LinkedList<mStep>(Arrays.asList(mStep.Left)));
			queue.addLast(new LinkedList<mStep>(Arrays.asList(mStep.Right)));
			this.stat.setQueueSize(queue.size());
			while (!goalFound) {
				curPath = queue.removeFirst();
				eval = initial.eval(curPath);
				this.stat.increaseIterations();
				if (eval.isValid() && visited.add(eval.getHash())) {
					this.stat.increaseVisitedStates();
					if (!eval.isOptimal()) {
						tmp = new LinkedList<>(curPath);
						tmp.add(mStep.Up);
						queue.addLast(tmp);
						tmp = new LinkedList<>(curPath);
						tmp.add(mStep.Down);
						queue.addLast(tmp);
						tmp = new LinkedList<>(curPath);
						tmp.add(mStep.Left);
						queue.addLast(tmp);
						tmp = new LinkedList<>(curPath);
						tmp.add(mStep.Right);
						queue.addLast(tmp);
						this.stat.setQueueSize(queue.size());
					} else {
						// The state is optimal, our job is complete!
						this.solutionFound = true;
						this.solution = curPath;
						this.stat.setTime(System.currentTimeMillis() - startTime);
						return this.stat.getTime();
					}
				} else {
					if(eval.isValid()){
						this.stat.increaseDuplicateStates();						
					} else {
						this.stat.increaseInvalidStates();
					}
				}
			}

		} else {
			if (eval.isOptimal()) {
				this.solutionFound = true;
				this.solution = new LinkedList<>(Arrays.asList(mStep.NOP));
			} else {
				this.solutionFound = false;
				this.solution = new LinkedList<>(Arrays.asList(mStep.NOP));
			}
		}
		this.stat.setTime(System.currentTimeMillis() - startTime);
		return this.stat.getTime();
	}

	public long SolveWithGoals() {
		int goals[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
		long startTime = System.currentTimeMillis();
		List<mStep> curPath;
		State curState;
		List<mStep> tmp;
		Evaluation eval;
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
		curPath = new LinkedList<>();
		curPath.add(mStep.NOP);
		curState = new State(initial);
		queue.addLast(curPath);

		/*
		 * Our objective is to divide the broader bigger problem is sub goals.
		 * Each goal should be easy enough to solve in a reasonable amount of
		 * time
		 */
		for (int goal : goals) {
			goalFound = false;
			this.stat.addGoal(goal);
			while (!goalFound) {
				this.stat.increaseIterations();
				curPath = queue.removeFirst();
				eval = curState.evalGoal(curPath, goal);
				if (eval.isValid()) {
					if (eval.isOptimal()) {
						goalFound = true;
					} else {
						if (visited.add(eval.getHash())) {
							this.stat.increaseVisitedStates();
							// Add all the possible children to fetch
							tmp = new LinkedList<>(curPath);
							tmp.add(mStep.Up);
							queue.addLast(tmp);
							tmp = new LinkedList<>(curPath);
							tmp.add(mStep.Down);
							queue.addLast(tmp);
							tmp = new LinkedList<>(curPath);
							tmp.add(mStep.Left);
							queue.addLast(tmp);
							tmp = new LinkedList<>(curPath);
							tmp.add(mStep.Right);
							queue.addLast(tmp);
							this.stat.setQueueSize(queue.size());
						} else {
							this.stat.increaseDuplicateStates();
						}
					}
				} else {
					this.stat.increaseInvalidStates();
				}
			}

			// Update the solution
			this.solution.addAll(curPath);
			// Update the current state of the board (we will start from here
			// for the new goal search)
			curState = curState.calculateState(curPath);
			this.stat.setState(curState);
			// Clear all the temporary data:
			// The path must start from scratch (having the base the curState)
			if (curPath.size() > 0)
				curPath.clear();
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
			curPath.add(mStep.NOP);
			queue.addLast(curPath);

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
