package solver;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class SokoBot {	
	private static int count = 0;
	
	// Grid dimension
	private static int width;
	private static int height;
	
	private char[][] goalItemsData;
	
	private State startState;
	private State goalState;
	
	
	private ArrayList<State> pathway = new ArrayList<>();
	private ArrayList<char[][]> storage = new ArrayList<>();
	
	private String output = "";
	
	/**
	 * Check if the state has all the crates in all the goals.
	 * */
	private boolean isGoalState(State state) {
		char[][] mapData = state.getMapData();
		char[][] itemsData = state.getItemsData();
		
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {
				if (mapData[i][j] == Tools.GOAL && itemsData[i][j] != Tools.CRATE) {
//					System.out.print("isGoalState(): ");
//					System.out.println("IS NOT GOAL");
					return false;
				}
			}
		}
//		System.out.print("isGoalState(): ");
//		System.out.println("IS GOAL");
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
	private boolean checkSpace(State state, Direction direction) {
		int[] origin = Tools.getPosOfChar(state.getItemsData(), Tools.PLAYER).get(0);
		
		int lookX = origin[Tools.X];
		int lookY = origin[Tools.Y];
		
		char[][] mapData, itemsData;
		mapData = state.getMapData();
		itemsData = state.getItemsData();
		
		int[] dir = Direction.dirToPos(direction); 
		
		lookX += dir[Tools.X];
		lookY += dir[Tools.Y];
		
		String s = Direction.dirToStr(direction);
		
		// Check if it's a space or a goal. Also check if the player
		// tries to push two crates. Whenever you can go through
		
		int extraX = lookX + dir[Tools.X];
		int extraY = lookY + dir[Tools.Y];
		
		System.out.println("checkSpace(" + s + "): originXY = (" + origin[0] + "," + origin[1] + ")");
		
		boolean isSpaceNear = (mapData[lookY][lookX] != Tools.WALL && 
							   (itemsData[lookY][lookX] == Tools.SPACE ||
							   itemsData[lookY][lookX] == Tools.CRATE));
		
		System.out.println("checkSpace(" + s + "): lookXY = (" + lookX + "," + lookY + ")");
		System.out.println("checkSpace(" + s + "): itemsData[lookY][lookX] = \'" + itemsData[lookY][lookX] + "\'");
		System.out.println("checkSpace(" + s + "): mapData[lookY][lookX] = \'" + mapData[lookY][lookX] + "\'");
		
		if (extraX < 0 || extraY < 0 || extraX >= width || extraY >= height) {
			return isSpaceNear;
		}
		
		System.out.println("checkSpace(" + s + "): extraXY = (" + extraX + "," + extraY + ")");
		System.out.println("checkSpace(" + s + "): itemsData[extraY][extraX] = \'" + itemsData[extraY][extraX] + "\'");
		System.out.println("checkSpace(" + s + "): mapData[extraY][extraX] = \'" + mapData[extraY][extraX] + "\'");
		
		boolean isSpaceNext = 
				(itemsData[extraY][extraX] == Tools.SPACE &&
				   mapData[extraY][extraX] != Tools.WALL);
				   
		boolean isSolidNext =
				(itemsData[lookY][lookX] == Tools.CRATE || 
				   mapData[lookY][lookX] == Tools.WALL);
				
		System.out.println("checkSpace(" + s + "): " + 
			"(" + isSpaceNear + " && " + isSpaceNext + ") || " +
			"(" + (itemsData[lookY][lookX] != Tools.CRATE) + " && " + !isSolidNext + ")");
		
		System.out.println("checkSpace(" + s + "): return " + 
			((isSpaceNear && isSpaceNext) || 
		    (itemsData[lookY][lookX] != Tools.CRATE && !isSolidNext)));
		
		return (isSpaceNear && isSpaceNext) || 
			   (itemsData[lookY][lookX] != Tools.CRATE && !isSolidNext);
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
	
	private void printBoard(State state) {
		char[][] itemsData = state.getItemsData();
		char[][] mapData = state.getMapData();
		
		System.out.print("\n");
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {
				if (itemsData[i][j] == ' ') {
					System.out.print(mapData[i][j]);
				}
				else {
					System.out.print(itemsData[i][j]);
				}
			}
			System.out.print("\n");
		}
	}
	
	private void printArray(char[][] data) {
		System.out.print("\n");
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				System.out.print(data[i][j]);
			}
			System.out.print("\n");
		}
	}
	
	private boolean isExists(char[][] data) {
		if (storage.isEmpty()) return false;
		
		
		for (char[][] search : storage) {
			if (Arrays.deepEquals(search, data)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	private void printStorage() {
		System.out.println("\n-------------------");
		for (char[][] data : storage) {
			printArray(data);
			System.out.println("-------------------");
		}
		if (storage.isEmpty()) {
			System.out.println("is empty");
			System.out.println("-------------------");
		}
	}
	
	/**
	 * Generate a state tree
	 * @param state the parent state.
	 * */
	private void generateTree(State state, int level) {
		if (level > 2) return;
//		printBoard(state);
		
		Direction[] allDirs = {Direction.NORTH, Direction.SOUTH, 
							   Direction.EAST, Direction.WEST};
		
		for (Direction dir : allDirs) {
			String s = Direction.dirToStr(dir);
			State newState = new State(state);
			
			printBoard(state);
			
			System.out.println("generateTree(): Player looked at " + s);
			if (checkSpace(state, dir)) {
				newState.move(dir);
				state.addNextState(newState);
				s = newState.getPlayerMovement();
				
				if (isExists(newState.getItemsData())) {
					System.out.println("generateTree(): Already moved there!");
					return;
				}
				else {
					System.out.println("generateTree(): Player moved " + s);
					
					char[][] copyPos = new char[height][width];
					copyPos = Tools.copyArray(newState.getItemsData());
					storage.add(copyPos);
					
					printBoard(newState);
					generateTree(newState, level + 1);
				}
			}
		}
	}

//	public List<State> tracePath(State goalState) {
//		List<State> path = new ArrayList<>();
//		State currentState = goalState;
//		while (currentState != null) {
//			path.add(0, currentState);  // Add state at the beginning of the list
//			currentState = currentState.getParent();
//		}
//		return path;
//	}

	
	/**
	 * Create a special itemsData where all crates are in the goals' positions
	 * */

	private void generateGoalItemsData(char[][] mapData) {
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {
				if (mapData[i][j] == Tools.GOAL) {
					goalItemsData[i][j] = Tools.CRATE;
				}
			}
		}
	}
	
	private String generatePath(State startState) {
		String output = "";
		
		State target = startState;
		int tries = 0;
		while (!isGoalState(target) && tries < 10) {
			State nextState = target;
			
			// Pick one with lowest heuristic value
			int minHeuristic = Integer.MAX_VALUE;
			for (State whichState: startState.getNextStates()) {
				System.out.print("generatePath(): ");
				System.out.println("heuristic: " + whichState.getHeuristic());
				if (whichState.getHeuristic() < minHeuristic) {
					nextState = whichState;
				}
			}
			
			// Output that shit
			output += nextState.getPlayerMovement();
			
			target = nextState;
			
//			System.out.print("generatePath(): ");
//			System.out.println(": " + whichState.getHeuristic());
			
			System.out.print("generatePath(): ");
			System.out.println(tries);
			tries += 1;
		}
		
		return output;
	}
	
	public static int getWidth() { return width; }
	public static int getHeight() { return height; }
	
	/**
	 * Fun part.
	 * 
	 * @return a string of player's movements.
	 * @param width     board's width
	 * @param height    board's height
	 * @param mapData   board with just walls and the goals
	 * @param itemsData board with the player and the crates
	 * */
	public String solveSokobanPuzzle(int width, int height, char[][] mapData, 

									 char[][] itemsData) {
		this.width = width;
		this.height = height;
		startState = new State(itemsData, mapData);
		
		// Define a goal data
//		goalItemsData = itemsData;
//		generateGoalItemsData(mapData);
		
		System.out.println("Generating tree...");
		// Generate a tree
		generateTree(startState, 0);
		printStorage();
		
		System.out.println("Generating path...");
		
		System.out.println("output: " + output);
		output = generatePath(startState);
//		List<State> path = tracePath(goalState);
//		StringBuilder pathString = new StringBuilder();
//		for (State state : path) {
//			pathString.append(state.getPlayerMovement());
//		}
//		System.out.println(pathString);
		System.out.println(output);
		return output;
	}
}
