package State;

/***
 * The representation of the action we need to apply to a state A in order to
 * reach a state B
 * 
 * @author oliver
 *
 */
public enum mStep {
	Up, Down, Left, Right, NOP;

	@Override
	public String toString() {
		switch (this) {
		case Up:
			return "Up";
		case Down:
			return "Down";
		case Left:
			return "Left";
		case Right:
			return "Right";
		case NOP:
			return "No needed steps";
		default:
			throw new IllegalArgumentException();
		}
	}
}
