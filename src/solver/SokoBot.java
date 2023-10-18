package solver;

import java.util.ArrayList;

public class SokoBot {
	// Array indexes for position
	private final int X = 0;
	private final int Y = 1;
	
	// Character representation of each object
	private final char PLAYER = '@';
	private final char CRATE = '$';
	private final char WALL = '#';
	private final char GOAL = '.';
	
	
	// Grid dimension
	private int width;
	private int height;
	
	ArrayList<int[]> listGoals = new ArrayList();
	
	private boolean isCompleted(char[][] mapData, char[][] itemsData) {
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {
				if (mapData[i][j] == GOAL && itemsData[i][j] != CRATE)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Get the position of a character in either a mapData or itemsData
	 * 
	 * @param data    mapData or itemsData
	 * @param target  what character to search
	 * 
	 * @return array of size 2 that stores position data
	 * 		   position[0] = x coordinate 
	 * 		   position[1] = y coordinate
	 * 		   
	 * 		   Use constants for index:
	 * 				X = 0
	 * 				Y = 1
	 * 			
	 * 		   e.g.
	 * 				position[X], position[Y]
	 * */
	private int[] getPosOfChar(char[][] data, char target) {
		int position[] = new int[2];

		/*****************************
		 * Note:
		 * Going through *rows* means changing *y* coordinates, and
		 * Going through *columns* means changing *x* coordinates.
		 * 
		 * Therefore, accessing array element with x and y goes like this:
		 * 		data[y][x]
		 ***************************** 
		 */
		// Rows (y)
		for (int y = 0; y < data.length; y++) {
			// Columns (x)
			for (int x = 0; x < data[y].length; x++) {
				if (data[y][x] == target) {
					position[X] = x;
					position[Y] = y;
					break;
				}
			}
		}
		
		return position;
	}

	private void listAllGoals(char[][] mapData) {
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {
				int[] goalPos = new int[2];
				goalPos[X] = j;
				goalPos[Y] = i;
				listGoals.add(goalPos);
			}
		}
	}
	
	/**
	 * Check for a space in a direction
	 * 
	 * @param mapData   map data
	 * @param itemsData item data
	 * @param direction what direction the player is facing
	 * @param origin    position of the player or something else
	 * 
	 * @return distance to a nearest goal
	 * */
	private boolean checkSpace(char[][] mapData, char[][] itemsData, 
							   Direction direction, int[] origin) {
		int x = origin[X];
		int y = origin[Y];
		
		int[] newPosition = origin;
		switch (direction) {
		case NORTH:
			newPosition[Y] -= 1;
			break;
		
		case SOUTH:
			newPosition[Y] += 1;
			break;
		
		case EAST:
			newPosition[X] -= 1;
			break;
		
		case WEST:
			newPosition[X] += 1;
			break;
		}
		
		x = newPosition[X];
		y = newPosition[Y];
		
		// Check again if there is a crate in the same direction
		if (itemsData[y][x] == CRATE)
			checkSpace(mapData, itemsData, direction, newPosition);
		
		// Check if it's a space or a goal. Whatever you can go through
		return (mapData[y][x] == ' ' || mapData[y][x] == '.');
	}
	
	
	/**
	 * This is going to be our heuristic function ;)))
	 * @param posCrate  position of specific crate
	 * 
	 * @return distance to a nearest goal
	 * */
	private double distCrate(int[] posCrate) {
		// Find nearest goal
		double distance = 9999;
		
		// ArrayList<int[]> listGoals = new ArrayList();
		for (int[] goalPos : listGoals) {
			// Pythagorean, bby
			double measureDist = 
					Math.sqrt(Math.pow(posCrate[X] - goalPos[X], 2) + 
							  Math.pow(posCrate[Y] - goalPos[Y], 2));
			if (measureDist < distance) {
				distance = measureDist;
			}
		}
		
		return distance;
	}
	
	/**
	 * Moves an item. Usually used with player. Only time the item is a crate
	 * is when it is called recursively. The recursion only occurs when the
	 * player pushes the block.
	 * */
	private char[][] moveItem(char[][] itemsData, char[][] mapData
							 int[] dest, int[] origin, char item) {
		int destX =   dest[X];
		int destY=   dest[Y];
		int originX = origin[X];
		int originY = origin[Y];
		
		int[][] newItemsData = itemsData;
		
		if (itemsData[destY][destX] == GOAL) {
			int[] newPos = destPosition;
			newPos[X] += destX - originX;
			newPos[Y] += destY - originY;
			moveItem(itemsData, mapData, newPos, dest, GOAL);
		}
		
		// Check if it is a wall or outside of the board
		if (mapData[destY][destX] == WALL && !withinBoundary(destY, destX))
			return newItemsData;
		
		newItemsData[originY][originX] = ' ';
		newItemsData[destY][destX] = item;
		
		return newItemsData;
	}
	
	/**
	 * Check whether the position is within the board
	 * 
	 * @return true - within board
	 * 		   false - outside board
	 * */
	private boolean withinBoundary(int row, int col) {
		return (row > 0) && (col > 0) &&
			   (row < height) && (col < width);
	}
	
	
	
	public String solveSokobanPuzzle(int width, int height, char[][] mapData, 
									 char[][] itemsData) {
		this.width = width;
		this.height = height;
		
		int[] playerPos = getPosOfChar(itemsData, '@');
		listAllGoals(mapData);
		output = "";
		return output;
	}
}
