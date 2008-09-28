package game.net.action;

import java.io.Serializable;

public class FiredShot implements Serializable {
	
	private final int x;
	private final int y;
	
	public FiredShot (int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
		
}
