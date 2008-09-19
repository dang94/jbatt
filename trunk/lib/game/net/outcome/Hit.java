package game.net.outcome;

public class Hit extends Outcome {
	
	private static final String OPP_MESSAGE = "You hit!";
	
	public Hit (int x, int y) {
		super(x, y);
	}

	@Override
	public String getOppMessage() {
		return OPP_MESSAGE;
	}
	
	@Override
	public String getSelfMessage() {
		return "You were hit at (" + getX() + "," + getY() + ")!";
	}
}
