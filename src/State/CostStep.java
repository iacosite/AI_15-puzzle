package State;

import java.util.List;

/***
 * Used in order to merge Evaluation and a list of mStep.
 * It is used in order to keep track of a precedent evaluation regarding a sequence of steps
 * @author oliver
 *
 */
public class CostStep implements Comparable<CostStep>{
	private List<mStep> step;
	private Evaluation eval;

	public CostStep(List<mStep> step, Evaluation eval) {
		super();
		this.step = step;
		this.eval = eval;
	}

	public List<mStep> getStep() {
		return step;
	}

	public void setStep(List<mStep> step) {
		this.step = step;
	}

	public Evaluation getEval() {
		return eval;
	}

	public void setEval(Evaluation eval) {
		this.eval = eval;
	}

	@Override
	public int compareTo(CostStep o) {
		// TODO Auto-generated method stub
		return this.eval.compareTo(o.eval);
	}

}
