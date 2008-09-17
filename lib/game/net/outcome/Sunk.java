package game.net.outcome;

import game.layout.Ship;

public class Sunk extends Outcome {
	
	private Ship ship;
	
	public Sunk (int x, int y, Ship ship) {
		super(x, y);
		this.ship = ship;
	}
	
	public Ship getShip () {
		return ship;
	}
}
