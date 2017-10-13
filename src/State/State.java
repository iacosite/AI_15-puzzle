package State;

import java.util.*;

/***
 * Representation of a state. The grid is represented as a matrix. Each number
 * is represented by relative Integer. 0 is space.
 * 
 * @author oliver
 *
 */
public class State {
	private List<List<Integer>> elem = new ArrayList<>();
	private Boolean isvalid;
	private int startZeroColumn;
	private int startZeroRow;

	// Common function used by all the constructors in order to initialize the
	// element
	private void _init(List<Integer> s) {
		this.isvalid = true;
		this.elem.add(s.subList(0, 4));
		this.elem.add(s.subList(4, 8));
		this.elem.add(s.subList(8, 12));
		this.elem.add(s.subList(12, 16));
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.elem.get(i).get(j) == 0) {
					startZeroRow = i;
					startZeroColumn = j;
				}
			}
		}
	}

	/*
	 * Generate a random initial state
	 */
	public State() {
		List<Integer> tmp = new ArrayList<>();
		tmp.add(0);
		tmp.add(1);
		tmp.add(2);
		tmp.add(3);
		tmp.add(4);
		tmp.add(5);
		tmp.add(6);
		tmp.add(7);
		tmp.add(8);
		tmp.add(9);
		tmp.add(10);
		tmp.add(11);
		tmp.add(12);
		tmp.add(13);
		tmp.add(14);
		tmp.add(15);

		Collections.shuffle(tmp);
		_init(tmp);
	}

	/*
	 * Given state
	 */
	public State(List<Integer> s) {
		_init(s);
	}

	/*
	 * Set the initial state
	 */
	public State(int a, int b, int c, int d, int e, int f, int g, int h, int i, int j, int k, int l, int m, int n,
			int o, int p) {
		List<Integer> tmp = new ArrayList<>();
		tmp.add(a);
		tmp.add(b);
		tmp.add(c);
		tmp.add(d);
		tmp.add(e);
		tmp.add(f);
		tmp.add(g);
		tmp.add(h);
		tmp.add(i);
		tmp.add(j);
		tmp.add(k);
		tmp.add(l);
		tmp.add(m);
		tmp.add(n);
		tmp.add(o);
		tmp.add(p);

		_init(tmp);
	}

	public State(State state) {
		List<Integer> elements = new LinkedList<>();
		for (int i = 0; i < 4; i++) {
			elements.addAll(state.elem.get(i));
		}
		_init(elements);
	}

	/*
	 * END OF CONSTRUCTORS
	 */

	public Boolean isValid() {
		return isvalid;
	}

	public Boolean isSolvable() {
		if (this.getInversions() % 2 != startZeroRow % 2) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * Evaluate the resulting state after applying a series of action. The only goal
	 * considered is to obtain the optimal configuration
	 * 
	 * @param stepList
	 *            the list of actions to apply
	 * @return the evaluation of the state
	 */
	public Evaluation eval(List<mStep> stepList) {
		State state = new State(this);
		int rowZ = startZeroRow;
		int colZ = startZeroColumn;
		Evaluation e = new Evaluation();

		for (mStep s : stepList) {
			switch (s) {
			case Up:
				if (rowZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ - 1).get(colZ));
					state.elem.get(rowZ - 1).set(colZ, 0);
					rowZ--;
				}
				break;
			case Down:
				if (rowZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ + 1).get(colZ));
					state.elem.get(rowZ + 1).set(colZ, 0);
					rowZ++;
				}
				break;
			case Left:
				if (colZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ - 1));
					state.elem.get(rowZ).set(colZ - 1, 0);
					colZ--;
				}
				break;
			case Right:
				if (colZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ + 1));
					state.elem.get(rowZ).set(colZ + 1, 0);
					colZ++;
				}
				break;
			case NOP:
			default:
				break;
			}
		}
		e.setValidity(true);
		e.setHash(state.getHash());
		e.setOptimality(state.isOptimal());
		return e;
	}

	/***
	 * Evaluate one action in reach of one specified goal To use with
	 * Solver.SolveWithGoals
	 * 
	 * @param stepList
	 *            actions to test
	 * @param goal
	 *            the goal we want to fulfill
	 * @return Evaluation of the goal fulfillment
	 */
	public Evaluation evalGoal(List<mStep> stepList, int goal) {
		State state = new State(this);
		int rowZ = startZeroRow;
		int colZ = startZeroColumn;
		Evaluation e = new Evaluation();

		for (mStep s : stepList) {
			switch (s) {
			case Up:
				if (rowZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ - 1).get(colZ));
					state.elem.get(rowZ - 1).set(colZ, 0);
					rowZ--;
				}
				break;
			case Down:
				if (rowZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ + 1).get(colZ));
					state.elem.get(rowZ + 1).set(colZ, 0);
					rowZ++;
				}
				break;
			case Left:
				if (colZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ - 1));
					state.elem.get(rowZ).set(colZ - 1, 0);
					colZ--;
				}
				break;
			case Right:
				if (colZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ + 1));
					state.elem.get(rowZ).set(colZ + 1, 0);
					colZ++;
				}
				break;
			case NOP:
			default:
				break;
			}
		}
		e.setValidity(true);
		e.setHash(state.getHash());
		e.setOptimality(state.isGoalReached(goal));
		return e;
	}


	/***
	 * Evaluate the resulting state after applying a series of action. The only goal
	 * considered is to obtain the optimal configuration. Calculates the distance in terms of displacement.
	 * 
	 * @param stepList
	 *            the list of actions to apply
	 * @return the evaluation of the state
	 */
	public Evaluation evalWithDisplacement(List<mStep> stepList) {
		State state = new State(this);
		int rowZ = startZeroRow;
		int colZ = startZeroColumn;
		Evaluation e = new Evaluation();

		for (mStep s : stepList) {
			switch (s) {
			case Up:
				if (rowZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ - 1).get(colZ));
					state.elem.get(rowZ - 1).set(colZ, 0);
					rowZ--;
				}
				break;
			case Down:
				if (rowZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ + 1).get(colZ));
					state.elem.get(rowZ + 1).set(colZ, 0);
					rowZ++;
				}
				break;
			case Left:
				if (colZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ - 1));
					state.elem.get(rowZ).set(colZ - 1, 0);
					colZ--;
				}
				break;
			case Right:
				if (colZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ + 1));
					state.elem.get(rowZ).set(colZ + 1, 0);
					colZ++;
				}
				break;
			case NOP:
			default:
				break;
			}
		}
		e.setValidity(true);
		e.setHash(state.getHash());
		e.setOptimality(state.isOptimal());

		// Cost evaluation
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.elem.get(i).get(j) != ((i+1)+(j*4))%16) {
					e.increaseCost();
				}
			}
		}
		return e;
	}

	/***
	 * Evaluate one action in reach of one specified goal To use with
	 * Solver.SolveWithGoals
	 * Calculate the distance in terms of displacement
	 * 
	 * @param stepList
	 *            actions to test
	 * @param goal
	 *            the goal we want to fulfill
	 * @return Evaluation of the goal fulfillment
	 */
	public Evaluation evalGoalWithDisplacement(List<mStep> stepList, int goal) {
		State state = new State(this);
		int rowZ = startZeroRow;
		int colZ = startZeroColumn;
		Evaluation e = new Evaluation();

		for (mStep s : stepList) {
			switch (s) {
			case Up:
				if (rowZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ - 1).get(colZ));
					state.elem.get(rowZ - 1).set(colZ, 0);
					rowZ--;
				}
				break;
			case Down:
				if (rowZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ + 1).get(colZ));
					state.elem.get(rowZ + 1).set(colZ, 0);
					rowZ++;
				}
				break;
			case Left:
				if (colZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ - 1));
					state.elem.get(rowZ).set(colZ - 1, 0);
					colZ--;
				}
				break;
			case Right:
				if (colZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ + 1));
					state.elem.get(rowZ).set(colZ + 1, 0);
					colZ++;
				}
				break;
			case NOP:
			default:
				break;
			}
		}
		e.setValidity(true);
		e.setHash(state.getHash());
		e.setOptimality(state.isGoalReached(goal));

		// Cost evaluation
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.elem.get(i).get(j) != ((i+1)+(j*4))%16) {
					e.increaseCost();
				}
			}
		}
		return e;
	}
	/***
	 * Evaluate the resulting state after applying a series of action. The only goal
	 * considered is to obtain the optimal configuration. Calculates the distance in terms of manhattan distance.
	 * 
	 * @param stepList
	 *            the list of actions to apply
	 * @return the evaluation of the state
	 */
	public Evaluation evalWithManhattan(List<mStep> stepList) {
		State state = new State(this);
		int rowZ = startZeroRow;
		int colZ = startZeroColumn;
		Evaluation e = new Evaluation();

		for (mStep s : stepList) {
			switch (s) {
			case Up:
				if (rowZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ - 1).get(colZ));
					state.elem.get(rowZ - 1).set(colZ, 0);
					rowZ--;
				}
				break;
			case Down:
				if (rowZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ + 1).get(colZ));
					state.elem.get(rowZ + 1).set(colZ, 0);
					rowZ++;
				}
				break;
			case Left:
				if (colZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ - 1));
					state.elem.get(rowZ).set(colZ - 1, 0);
					colZ--;
				}
				break;
			case Right:
				if (colZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ + 1));
					state.elem.get(rowZ).set(colZ + 1, 0);
					colZ++;
				}
				break;
			case NOP:
			default:
				break;
			}
		}
		e.setValidity(true);
		e.setHash(state.getHash());
		e.setOptimality(state.isOptimal());

		// Cost evaluation
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// The column the element should be in can be retrieved by (N-1)%4  - -1 in order to normalize from (1-4)to(0-3)
				// The row can be found by the quotient of the division (N-1)/4
				// These values are subtracted to the effective position, the absolute value is taken
				e.increaseCost(Math.abs(i - Math.floor((this.elem.get(i).get(j).doubleValue() - 1) / 4	)) + Math.abs( j - (this.elem.get(i).get(j) - 1)%4));
			}
		}
		return e;
	}
	
	/***
	 * Evaluate one action in reach of one specified goal To use with
	 * Solver.SolveWithGoals
	 * Calculate the manhattan distance
	 * 
	 * @param stepList
	 *            actions to test
	 * @param goal
	 *            the goal we want to fulfill
	 * @return Evaluation of the goal fulfillment
	 */
	public Evaluation evalGoalWithManhattan(List<mStep> stepList, int goal) {
		State state = new State(this);
		int rowZ = startZeroRow;
		int colZ = startZeroColumn;
		Evaluation e = new Evaluation();

		for (mStep s : stepList) {
			switch (s) {
			case Up:
				if (rowZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ - 1).get(colZ));
					state.elem.get(rowZ - 1).set(colZ, 0);
					rowZ--;
				}
				break;
			case Down:
				if (rowZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ + 1).get(colZ));
					state.elem.get(rowZ + 1).set(colZ, 0);
					rowZ++;
				}
				break;
			case Left:
				if (colZ == 0) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ - 1));
					state.elem.get(rowZ).set(colZ - 1, 0);
					colZ--;
				}
				break;
			case Right:
				if (colZ == 3) {
					e.setValidity(false);
					return e;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ + 1));
					state.elem.get(rowZ).set(colZ + 1, 0);
					colZ++;
				}
				break;
			case NOP:
			default:
				break;
			}
		}
		e.setValidity(true);
		e.setHash(state.getHash());
		e.setOptimality(state.isGoalReached(goal));

		// Cost evaluation
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				// The column the element should be in can be retrieved by (N-1)%4  - -1 in order to normalize from (1-4)to(0-3)
				// The row can be found by the quotient of the division (N-1)/4
				// These values are subtracted to the effective position, the absolute value is taken
				if(state.elem.get(i).get(j) != 0) {
					e.increaseCost(Math.abs(i - Math.floor((state.elem.get(i).get(j).doubleValue() - 1) / 4	)) + Math.abs( j - (state.elem.get(i).get(j) - 1)%4));
				} else {
					e.increaseCost(Math.abs(i - 3) + Math.abs( j - 3));
				}
			}
		}
		return e;
	}



	/***
	 * Used in order to calculate the resulting state given a series of actions It
	 * is similar to Eval and EvalGoal
	 * 
	 * @param stepList
	 *            The actions we need to apply to this state
	 * @return resulting state after actions
	 */
	public State calculateState(List<mStep> stepList) {
		State state = new State(this);
		int rowZ = startZeroRow;
		int colZ = startZeroColumn;

		for (mStep s : stepList) {
			switch (s) {
			case Up:
				if (rowZ == 0) {
					state.isvalid = false;
					return state;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ - 1).get(colZ));
					state.elem.get(rowZ - 1).set(colZ, 0);
					rowZ--;
				}
				break;
			case Down:
				if (rowZ == 3) {
					state.isvalid = false;
					return state;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ + 1).get(colZ));
					state.elem.get(rowZ + 1).set(colZ, 0);
					rowZ++;
				}
				break;
			case Left:
				if (colZ == 0) {
					state.isvalid = false;
					return state;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ - 1));
					state.elem.get(rowZ).set(colZ - 1, 0);
					colZ--;
				}
				break;
			case Right:
				if (colZ == 3) {
					state.isvalid = false;
					return state;
				} else {
					state.elem.get(rowZ).set(colZ, state.elem.get(rowZ).get(colZ + 1));
					state.elem.get(rowZ).set(colZ + 1, 0);
					colZ++;
				}
				break;
			case NOP:
			default:
				break;
			}
		}
		state.isvalid = true;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (state.elem.get(i).get(j) == 0) {
					state.startZeroRow = i;
					state.startZeroColumn = j;
				}
			}
		}
		return state;
	}

	/***
	 * -----------------------------------------------------------------------------------------------
	 * GOAL SYSTEM AND DEFINITIONS
	 * -----------------------------------------------------------------------------------------------
	 * 
	 * Evaluates if the current state has reached a determined goal. The reference
	 * goals are: (src: http://www.instructables.com/id/How-To-Solve-The-15-Puzzle/
	 * ) Step 1: Move the 1 Tile and the 2 Tile Into Their Final Positions Step 2:
	 * Move Tiles 3 and 4 Into Setup Positions Step 3: Move Tiles 3 and 4 Into Final
	 * Position Step 4: Move Tiles 5 and 6 Into Final Position Step 5: Move Tiles 7
	 * and 8 Into Setup Positions Step 6: Move Tiles 7 and 8 Into Final Positions
	 * Step 7: Move Tiles 13 and 9 Into Setup Positions Step 8: Move Tiles 13 and 9
	 * Into Final Positions Step 9: Move Tiles 14 and 10 Into Setup Positions Step
	 * 10: Move Tiles 14 and 10 Into Their Final Positions Step 11: Completing the
	 * Puzzle
	 * 
	 * They are further split in order to speed up computation
	 *
	 * @param goal
	 *            the identifier of the goal we want to check
	 * @return wether the goas has been reached or not
	 */
	public Boolean isGoalReached(int goal) {
		// originally started at 1 :(
		goal++;
		Boolean ret;
		switch (goal) {
		case 1:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			ret = ret || this.isGoal10() || this.isGoal9() || this.isGoal8() || this.isGoal7() || this.isGoal6()
					|| this.isGoal5() || this.isGoal4() || this.isGoal3() || this.isGoal2();
			return this.isGoal1() || ret;
		case 2:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			ret = ret || this.isGoal10() || this.isGoal9() || this.isGoal8() || this.isGoal7() || this.isGoal6()
					|| this.isGoal5() || this.isGoal4() || this.isGoal3();
			return this.isGoal2() || ret;
		case 3:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			ret = ret || this.isGoal10() || this.isGoal9() || this.isGoal8() || this.isGoal7() || this.isGoal6()
					|| this.isGoal5() || this.isGoal4();
			return this.isGoal3() || ret;
		case 4:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			ret = ret || this.isGoal10() || this.isGoal9() || this.isGoal8() || this.isGoal7() || this.isGoal6()
					|| this.isGoal5();
			return this.isGoal4() || ret;
		case 5:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			ret = ret || this.isGoal10() || this.isGoal9() || this.isGoal8() || this.isGoal7() || this.isGoal6();
			return this.isGoal5() || ret;
		case 6:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			ret = ret || this.isGoal10() || this.isGoal9() || this.isGoal8() || this.isGoal7();
			return this.isGoal6() || ret;
		case 7:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			ret = ret || this.isGoal10() || this.isGoal9() || this.isGoal8();
			return this.isGoal7() || ret;
		case 8:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			ret = ret || this.isGoal10() || this.isGoal9();
			return this.isGoal8() || ret;
		case 9:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			ret = ret || this.isGoal10();
			return this.isGoal9() || ret;
		case 10:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12() || this.isGoal11();
			return this.isGoal10() || ret;
		case 11:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13()
					|| this.isGoal12();
			return this.isGoal11() || ret;
		case 12:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14() || this.isGoal13();
			return this.isGoal12() || ret;
		case 13:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15() || this.isGoal14();
			return this.isGoal13() || ret;
		case 14:
			ret = this.isOptimal() || this.isGoal16() || this.isGoal15();
			return this.isGoal14()|| ret;
		case 15:
			ret = this.isOptimal() || this.isGoal16();
			return this.isGoal15() || ret;
		case 16:
			ret = this.isOptimal();
			return this.isGoal16() || ret;
		case 17:
			return this.isOptimal();
		default:
			return false;
		}
	}

	// Goal 1: Tile 1 in position
	private Boolean isGoal1() {
		if (this.elem.get(0).get(0) != 1)
			return false;
		return true;
	}

	// Goal 2: Tile 1 and 2 in position
	private Boolean isGoal2() {
		if (!this.isGoal1()) {
			return false;
		}
		if (this.elem.get(0).get(1) != 2)
			return false;
		return true;
	}

	// Goal 3: Tiles 1,2 in position, 4 in setup
	private Boolean isGoal3() {
		if (!this.isGoal2()) {
			return false;
		}
		if (this.elem.get(0).get(2) != 4)
			return false;
		return true;
	}

	// Goal 4: Tiles 1,2 in position, 3,4 in setup
	private Boolean isGoal4() {
		if (!this.isGoal3()) {
			return false;
		}
		if (this.elem.get(1).get(2) != 3)
			return false;
		return true;
	}

	// Goal 5: Tiles 1,2,3,4 in position
	private Boolean isGoal5() {
		if (!this.isGoal2()) {
			return false;
		}
		if (this.elem.get(0).get(2) != 3)
			return false;
		if (this.elem.get(0).get(3) != 4)
			return false;
		return true;
	}

	// Goal 6: Tiles 1,2,3,4,5 in position
	private Boolean isGoal6() {
		if (!this.isGoal5()) {
			return false;
		}
		if (this.elem.get(1).get(0) != 5)
			return false;
		return true;
	}

	// Goal 7: Tiles 1,2,3,4,5,6 in position
	private Boolean isGoal7() {
		if (!this.isGoal6()) {
			return false;
		}
		if (this.elem.get(1).get(1) != 6)
			return false;
		return true;
	}

	// Goal 9: Tiles 1,2,3,4,5,6 in position, 7,8 in setup
	private Boolean isGoal9() {
		if (!this.isGoal8()) {
			return false;
		}
		if (this.elem.get(2).get(2) != 7)
			return false;
		return true;
	}

	// Goal 8: Tiles 1,2,3,4,5,6 in position, 8 in setup
	private Boolean isGoal8() {
		if (!this.isGoal7()) {
			return false;
		}
		if (this.elem.get(1).get(2) != 8)
			return false;
		return true;
	}

	// Goal 10: Tiles 1,2,3,4,5,6,7,8 in position
	private Boolean isGoal10() {
		if (!this.isGoal7()) {
			return false;
		}
		if (this.elem.get(1).get(2) != 7)
			return false;
		if (this.elem.get(1).get(3) != 8)
			return false;
		return true;
	}

	// Goal 11: Tiles 1,2,3,4,5,6,7,8 in position, 13 in setup
	private Boolean isGoal11() {
		if (!this.isGoal10()) {
			return false;
		}
		if (this.elem.get(2).get(0) != 13)
			return false;
		return true;
	}

	// Goal 12: Tiles 1,2,3,4,5,6,7,8 in position, 13,9 in setup
	private Boolean isGoal12() {
		if (!this.isGoal11()) {
			return false;
		}
		if (this.elem.get(2).get(1) != 9)
			return false;
		return true;
	}

	// Goal 13: Tiles 1,2,3,4,5,6,7,8,9,13 in position
	private Boolean isGoal13() {
		if (!this.isGoal10()) {
			return false;
		}
		if (this.elem.get(2).get(0) != 9)
			return false;
		if (this.elem.get(3).get(0) != 13)
			return false;
		return true;
	}

	// Goal 14: Tiles 1,2,3,4,5,6,7,8,9,13 in position, 14 in setup
	private Boolean isGoal14() {
		if (!this.isGoal13()) {
			return false;
		}
		if (this.elem.get(2).get(1) != 14)
			return false;
		return true;
	}

	// Goal 15: Tiles 1,2,3,4,5,6,7,8,9,13 in position, 14,10 in setup
	private Boolean isGoal15() {
		if (!this.isGoal14()) {
			return false;
		}
		if (this.elem.get(2).get(2) != 10)
			return false;
		return true;
	}

	// Goal 16: Tiles 1,2,3,4,5,6,7,8,9,10,13,14 in position
	private Boolean isGoal16() {
		if (!this.isGoal13()) {
			return false;
		}
		if (this.elem.get(2).get(1) != 10)
			return false;
		if (this.elem.get(3).get(1) != 14)
			return false;
		return true;
	}

	// Goal 17: Tiles 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 in position
	private Boolean isOptimal() {
		if (!this.isGoal16()) {
			return false;
		}
		if (this.elem.get(2).get(2) != 11)
			return false;
		if (this.elem.get(2).get(3) != 12)
			return false;
		if (this.elem.get(3).get(2) != 15)
			return false;
		if (this.elem.get(3).get(3) != 0)
			return false;
		return true;
	}

	/*
	 * ------------------------------------------------------------------------- End
	 * of goal definition
	 * -------------------------------------------------------------------------
	 */

	/***
	 * Calculate an unique hash function for identifying the state. This helps us
	 * saving a noticeable amount of space when checking for repeated states
	 * 
	 * @return an unique value identifying the state
	 */
	public long getHash() {
		long hash = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				hash = (i + j) * hash + this.elem.get(i).get(j);
			}
		}
		return hash;
	}

	public int getInversions() {
		List<Integer> states = new LinkedList<>();
		int inversions = 0;
		// set element as array
		for (int i = 0; i < 4; i++) {
			states.addAll(this.elem.get(i));
		}
		// calculate inversions
		// When there is an element with lower index than the current one, an
		// inversion must be accounted
		for (int i = 0; i < 15; i++) {
			for (int j = i + 1; j < 16; j++) {
				if ((states.get(j) < states.get(i)) && (states.get(j) != 0)) {
					inversions++;
				}
			}
		}
		return inversions;
	}

	@Override
	// Compare all the elements of the matrix
	public boolean equals(Object obj) {
		State that;
		if (obj instanceof State) {
			that = (State) obj;
		} else {
			return false;
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.elem.get(i).get(j) != that.elem.get(i).get(j)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		// Hoping this doesn't cause overflows
		int hash = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				hash = (i + j) * hash + this.elem.get(i).get(j);
			}
		}
		return hash;
	}

	@Override
	public String toString() {
		StringBuffer s;
		s = new StringBuffer("-------------\n");
		for (int i = 0; i < 4; i++) {
			s.append("|");
			for (int j = 0; j < 4; j++) {
				if (this.elem.get(i).get(j) < 10) {
					s.append(" ");
				}
				s.append(this.elem.get(i).get(j) + "|");
			}
			s.append("\n");
		}
		s.append("-------------");
		return s.toString();
	}
}
