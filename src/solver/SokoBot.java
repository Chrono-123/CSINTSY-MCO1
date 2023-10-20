package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SokoBot {
	// Array indexes for position
	public final static int X = 0;
	public final static int Y = 1;
	
	// Character representation of each object
	public final static char PLAYER = '@';
	public final static char CRATE = '$';
	public final static char WALL = '#';
	public final static char GOAL = '.';
	
	// Grid dimension
	private int width;
	private int height;
	
	private char[][] goalItemsData;
	
	private State startState;
	private State goalState;
	
	
	private ArrayList<State> pathway = new ArrayList();
	private HashMap<char[][], String> storage;
	
	private String output = "";
	
//	private boolean isCompleted(char[][] mapData, char[][] itemsData) {
//		for (int i = 0; i < mapData.length; i++) {
//			for (int j = 0; j < mapData[i].length; j++) {
//				if (mapData[i][j] == GOAL && itemsData[i][j] != CRATE)
//					return false;
//			}
//		}
//		return true;
//	}
	private boolean isGoalState(State state) {
		char[][] mapData = state.getMapData();
		char[][] itemsData = state.getItemsData();
		
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {
				if (mapData[i][j] == GOAL && itemsData[i][j] != CRATE)
					return false;
			}
		}
		return true;
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
	public static boolean checkSpace(char[][] mapData, char[][] itemsData, 
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
		if (itemsData[y][x] == CRATE) {
			return checkSpace(mapData, itemsData, direction, newPosition);			
		}
		
		// Check if it's a space or a goal. Whatever you can go through
		return (mapData[y][x] == ' ' || mapData[y][x] == '.');
	}
	
	/**
	 * Moves an item. Usually used with player. Only time the item is a crate
	 * is when it is called recursively. The recursion only occurs when the
	 * player pushes the block.
	 * */
	
	
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
	
	private void generateTree(State state) {
		if (goalState != null || storage.containsKey(state.getItemsData())) {
			return;
		}
		
		State upState = new State
		
		state.addNextState();
	}
	
	private void generateGoalItemsData(char[][] mapData) {
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {
				if (mapData[i][j] == GOAL) {
					goalItemsData[i][j] = CRATE;
				}
			}
		}
	}
	
	public String solveSokobanPuzzle(int width, int height, char[][] mapData, 
									 char[][] itemsData) {
		this.width = width;
		this.height = height;
		startState = new State(itemsData, mapData);
		
		goalItemsData = itemsData;
		generateGoalItemsData(mapData);
		
		generateTree(startState);
		return "lrlrlrlrlrlr";
	}
}
