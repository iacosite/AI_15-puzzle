package AI;

import Solver.*;
//import Solver.DepthFirstSolver;
import Solver.Solver;
import Solver.AStar.Displacement;
import Solver.AStar.Manhattan;
import State.State;

public class Problem {

	public static void main(String[] args) {
		//State i = new State(1,2,3,4,5,6,7,8,9,10,0,12,13,14,11,15);
		
		State i = new State();
		
		Solver s = new Manhattan(i);
		Solver t = new Displacement(i);
		t.SolveWithGoals();
		if (t.isSolved()) {
			System.out.println("[" + t.getSolution().size() + " steps] ");
			System.out.println("Solution is: " + t.getSolution().toString());
			System.out.println(t.getStat());
		} else {
			System.out.println("The problem was not solvable in A* Displacement!");
		}
		System.out.println("############################################################");
		s.SolveWithGoals();
		if (s.isSolved()) {
			System.out.println("[" + s.getSolution().size() + " steps] ");
			System.out.println("Solution is: " + s.getSolution().toString());
			System.out.println(s.getStat());
		} else {
			System.out.println("The problem was not solvable in A*Manhattan!");
		}
		
		System.out.println(s.getStat().versus(t.getStat()));

	}
}
