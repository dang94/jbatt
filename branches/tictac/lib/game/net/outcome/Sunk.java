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
	
	@Override
	public String getOppMessage() {
		return "You sunk a " + ship.getName() + "!";
	}
	
	@Override
	public String getSelfMessage() {
		return "Your " + ship.getName() + " was sunk!";
	}
}
