package solver;

public enum Direction {
	NORTH,
	EAST,
	SOUTH,
	WEST;
	
	/**
	 * @returns array as a 2D normalized vector.
	 * 					  X   Y
	 * 			NORTH = { 0,  1}
	 * 			SOUTH = { 0, -1}
	 * 			EAST  = { 1, -0}
	 * 			WEST  = {-1,  0}
	 * @param direction
	 * */
	
	public static int[] dirToPos(Direction direction) {
		int pos[] = new int[2];
		
		switch(direction) {
		case NORTH:
			pos[Tools.X] = 0;
			pos[Tools.Y] = -1;
			break;
		case EAST:
			pos[Tools.X] = 1;
			pos[Tools.Y] = 0;
			break;
		case SOUTH:
			pos[Tools.X] = 0;
			pos[Tools.Y] = 1;
			break;
		case WEST:
			pos[Tools.X] = -1;
			pos[Tools.Y] = 0;
			break;
		}
		
		return pos;
	}
	
	public static String dirToStr(Direction direction) {
		String dir = "";
		
		switch(direction) {
		case NORTH:
			dir = "u";
			break;
		case EAST:
			dir = "l";
			break;
		case SOUTH:
			dir = "d";
			break;
		case WEST:
			dir = "r";
			break;
		}
		
		return dir;
	}
}
