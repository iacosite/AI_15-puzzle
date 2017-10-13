package State;

/***
 * Evaluation of a state. Used in order to ease and standardize values among
 * different functions.
 * 
 * validity
 *            Indicates whether the state is valid (the empty tile is not out of
 *            bound)
 * optimality
 *            Indicates whether the state is optimal for the current goal
 * hash
 *            Indicates the result of the hash function applied to the relative
 *            state. It can be added to the duplicate set
 *cost
 *				Indicates the distance of the state evaluated to the optimal one
 * @author oliver
 *
 */
public class Evaluation implements Comparable<Evaluation>{
	private Boolean validity;
	private Boolean optimality;
	private Long hash;
	private long cost;

	public Evaluation() {
		this.validity = false;
		this.optimality = false;
		this.hash = (long) 0;
		this.cost = 0;
	}

	public Evaluation(Boolean validity, Boolean optimality, long hash) {
		super();
		this.validity = validity;
		this.optimality = optimality;
		this.hash = hash;
	}

	public Boolean isValid() {
		return validity;
	}

	public void setValidity(Boolean validity) {
		this.validity = validity;
	}

	public Boolean isOptimal() {
		return optimality;
	}

	public void setOptimality(Boolean optimality) {
		this.optimality = optimality;
	}

	public long getHash() {
		return hash;
	}

	public void setHash(long hash) {
		this.hash = hash;
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
	}
	
	public void increaseCost() {
		this.cost++;
	}
	
	public void increaseCost(double amount) {
		this.cost+= (int)amount;
	}

	@Override
	public int compareTo(Evaluation o) {
		// TODO Auto-generated method stub
		return Long.compare(this.getCost(), o.getCost());
	}

}
