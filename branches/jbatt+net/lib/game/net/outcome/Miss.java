package game.net.outcome;

public class Miss extends Outcome {
	
	private static final String OPP_MESSAGE = "You missed!";
	private static final String SELF_MESSAGE = "Your opponent missed!";
	
	public Miss (int x, int y) {
		super(x, y);
	}
	
	@Override
	public String getOppMessage() {
		return OPP_MESSAGE;
	}
	
	@Override
	public String getSelfMessage() {
		return SELF_MESSAGE;
	}
}
