package Statistics;

import State.State;

/***
 * The basic statistic element. Is the container for all the data we intend to save
 * @author oliver
 *
 */
public class Stat {

	private double memoryKB;
	private long nIterations;
	private long queueSize;
	private long visitedStates;
	private long duplicatedStates;
	private long invalidStates;
	private State state;

	public Stat(){
		this.nIterations = 0;
		this.queueSize = 0;
		this.visitedStates = 0;
		this.duplicatedStates = 0;
		this.invalidStates = 0;
	}
	
	public double getMemoryKB() {
		return memoryKB;
	}

	public void setMemoryKB(double memoryKB) {
		this.memoryKB = memoryKB;
	}

	public void setnIterations(long nIterations) {
		this.nIterations = nIterations;
	}

	public long getnIterations() {
		return this.nIterations;
	}

	public void increaseIterations(){
		this.nIterations ++;
	}

	public long getQueueSize() {
		return this.queueSize;
	}

	public void setQueueSize(long queueSize) {
		this.queueSize = queueSize;
	}

	public long getVisitedStates() {
		return this.visitedStates;
	}

	public void setVisitedStates(long visitedStates) {
		this.visitedStates = visitedStates;
	}
	
	public void increaseVisitedStates(){
		this.visitedStates ++;
	}

	public long getDuplicatedStates() {
		return duplicatedStates;
	}

	public void setDuplicatedStates(long duplicatedStates) {
		this.duplicatedStates = duplicatedStates;
	}
	
	public void increaseDuplicateStates(){
		this.duplicatedStates ++;
	}

	public long getInvalidStates() {
		return this.invalidStates;
	}

	public void setInvalidStates(long invalidStates) {
		this.invalidStates = invalidStates;
	}
	
	public void increaseInvalidStates(){
		this.invalidStates ++;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	

}
