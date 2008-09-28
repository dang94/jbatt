package game.net.request;

import java.io.Serializable;

import com.sun.org.apache.regexp.internal.recompile;

public class Request implements Serializable {
	
	public static enum RequestType {
		MOVE_REQUEST,
		CONFIRMATION_REQUEST,
		GAMEBOARD_REQUEST
	}
	
	private final RequestType type;
	
	public Request (RequestType type) {
		this.type = type;
	}
	
	public RequestType getType () {
		return type;
	}
	
}
