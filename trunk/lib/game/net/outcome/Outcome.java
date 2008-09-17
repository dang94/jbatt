package game.net.outcome;

public abstract class Outcome {
	
	private final int x;
	private final int y;
	
	public Outcome (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX () {
		return x;
	}
	
	public int getY () {
		return y;
	}

}
