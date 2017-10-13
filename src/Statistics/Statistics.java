package Statistics;

import java.util.LinkedList;
import java.util.List;

import State.State;

/***
 * Contains statistics divided by goal moment. The memory size is calculated
 * when the goal is created, hence a history of the program memory size at
 * different moments is kept.
 * 
 * @author oliver
 *
 */
public class Statistics {
	private int goal;
	private List<Stat> totals;
	private long time;
	private double memoryKB;
	private String name;

	public Statistics(String name, int goal) {
		super();
		this.name = name;
		this.goal = goal;
		this.time = 0;
		this.totals = new LinkedList<>();
		this.memoryKB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
	}

	public String getName() {
		return this.name;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public void addGoal(int goal) {
		this.goal = goal;
		if ((goal < totals.size()) && (totals.get(goal) != null)) {
			totals.remove(goal);
		}
		totals.add(goal, new Stat());
		totals.get(goal).setMemoryKB((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
	}

	public long getnIterations() {
		return totals.get(this.goal).getnIterations();
	}

	public void increaseIterations() {
		this.totals.get(goal).increaseIterations();
		;
	}

	public long getQueueSize() {
		return totals.get(goal).getQueueSize();
	}

	public void setQueueSize(long queueSize) {
		this.totals.get(goal).setQueueSize(queueSize);
	}

	public long getVisitedStates() {
		return totals.get(goal).getVisitedStates();
	}

	public void setVisitedStates(long visitedStates) {
		this.totals.get(goal).setVisitedStates(visitedStates);
	}

	public void increaseVisitedStates() {
		this.totals.get(goal).increaseVisitedStates();
	}

	public long getDuplicatedStates() {
		return totals.get(goal).getDuplicatedStates();
	}

	public void setDuplicatedStates(long duplicatedStates) {
		this.totals.get(goal).setDuplicatedStates(duplicatedStates);
	}

	public void increaseDuplicateStates() {
		this.totals.get(goal).increaseDuplicateStates();
	}

	public long getInvalidStates() {
		return totals.get(goal).getInvalidStates();
	}

	public void setInvalidStates(long invalidStates) {
		this.totals.get(goal).setInvalidStates(invalidStates);
	}

	public void increaseInvalidStates() {
		this.totals.get(goal).increaseDuplicateStates();
	}

	public State getState() {
		return this.totals.get(goal).getState();
	}

	public void setState(State state) {
		this.totals.get(goal).setState(state);
	}

	public long getTime() {
		return time;
	}

	public List<Stat> getTotals() {
		return this.totals;
	}

	public double getMemoryKB() {
		return memoryKB;
	}

	public void setTime(long time) {
		this.time = time;
	}

	/***
	 * @return the current memory usage of the program
	 */
	public double getActualMemoryUsageKB() {
		this.memoryKB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
		return this.memoryKB;
	}

	/***
	 * @return the history of the
	 */
	public List<Stat> getHistory() {
		return this.totals;
	}

	@Override
	public String toString() {
		long totiterations = 0;
		for (Stat s : this.totals) {
			totiterations += s.getnIterations();
		}
		StringBuffer str = new StringBuffer();
		str.append(this.name + " statistics:\n");
		str.append("Actual memory: " + this.getActualMemoryUsageKB() + "Kb\n");
		str.append("Total Time: " + this.getTime() + "ms\n");
		str.append("Total Iterations: " + totiterations);
		str.append("\n{\n");
		for (Stat s : this.totals) {
			str.append(this.totals.indexOf(s) + "\n");
			str.append(s.getState().toString() + "\n");
			str.append("Iterations: " + s.getnIterations() + ", \n\t");
			str.append("Used memory in that moment: " + s.getMemoryKB() + "Kb, \n\t");
			str.append("Visited states: " + s.getVisitedStates() + ", \n\t");
			str.append("Invalid states: " + s.getInvalidStates() + ", \n\t");
			str.append("Duplicate states: " + s.getDuplicatedStates() + ", \n\t");
			str.append("Max frontier size: " + s.getQueueSize() + "\n");
		}
		str.append("}");
		return str.toString();
	}

	public void debugInfo() {
		long totiterations = 0;
		long totVisited = 0;
		long totdup = 0;
		long totinv = 0;

		for (Stat s : this.totals) {
			totiterations += s.getnIterations();
			totVisited += s.getVisitedStates();
			totdup += s.getDuplicatedStates();
			totinv += s.getInvalidStates();
		}
		System.out.println("nIterations: " + totiterations + " goal: " + this.goal + " visited: " + totVisited
				+ " duplicated: " + totdup + " invalid: " + totinv);
	}

	/***
	 * Compares two execution methods in term of: -Time -Memory usage -Visited nodes
	 * -Iterations -Duplicate nodes
	 * 
	 * @param other
	 *            The other Statistics we want to compare with the current one
	 * @return A table containing the detailed information about the comparison
	 */
	public String versus(Statistics other) {
		StringBuffer str = new StringBuffer("\t[" + this.name + " | " + other.name + "]\n");
		long thisTotIterations = 0;
		long thisTotVisited = 0;
		long thisTotDup = 0;
		//long thisTotInv = 0;
		long otherTotIterations = 0;
		long otherTotVisited = 0;
		long otherTotDup = 0;
		//long otherTotInv = 0;

		// Collect the cumulative totals, since in Statistics Stat is taken separately
		// for each goal
		for (Stat s : this.totals) {
			thisTotIterations += s.getnIterations();
			thisTotVisited += s.getVisitedStates();
			thisTotDup += s.getDuplicatedStates();
			//thisTotInv += s.getInvalidStates();
		}
		for (Stat s : other.totals) {
			otherTotIterations += s.getnIterations();
			otherTotVisited += s.getVisitedStates();
			otherTotDup += s.getDuplicatedStates();
			//otherTotInv += s.getInvalidStates();
		}

		// Create the string buffer
		str.append("Total time:\t\t" + this.time + "ms\t|\t" + other.time + "ms\n");
		str.append("Memory:\t\t" + this.memoryKB + "Kb\t|\t" + other.memoryKB + "Kb\n");
		str.append("Iterations:\t\t" + thisTotIterations + "\t|\t" + otherTotIterations + "\n");
		str.append("Visited states:\t\t" + thisTotVisited + "\t|\t" + otherTotVisited + "\n");
		str.append("Duplicate states:\t" + thisTotDup + "\t|\t" + otherTotDup + "\n");
		// str.append("Invalid states:\t\t"+thisTotInv+"\t|\t"+otherTotInv+"\n");
		return str.toString();
	}
}
