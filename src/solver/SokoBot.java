package solver;
import java.util.ArrayList;
import java.util.List;

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
		
		// Check if it's a space or a goal. Also check if the player
		// tries to push two crates. Whenever you can go through
		
		int extraX = (1 * dir[Tools.X]);
		int extraY = (1 * dir[Tools.Y]);
		
//		System.out.print("Sokobot.checkSpace(): ");
//		System.out.println("mapData[lookY][lookX] = \"" + mapData[lookY][lookX] + "\"");
		
		if (extraX < 0 || extraY < 0 || extraX > width || extraY > height) {
			return (// a space or a goal
					(mapData[lookY][lookX] == Tools.SPACE || 
					 mapData[lookY][lookX] == Tools.GOAL));
		}
		
//		System.out.print("Sokobot.checkSpace(): ");
//		System.out.println("mapData[lookY + extraY][lookX + extraX] = \"" + mapData[lookY + extraY][lookX + extraX] + "\"");
//		System.out.print("Sokobot.checkSpace(): ");
//		System.out.println("itemsData[lookY + extraY][lookX + extraX] = \"" + itemsData[lookY + extraY][lookX + extraX] + "\"");
		
		return (// a space or a goal
				(mapData[lookY][lookX] == Tools.SPACE || 
				 mapData[lookY][lookX] == Tools.GOAL) &&
				// another crate or wall
				(itemsData[lookY + extraY][lookX + extraX] != Tools.CRATE &&
				   mapData[lookY + extraY][lookX + extraX] != Tools.WALL)
		);
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
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[i].length; j++) {
					if (search[i][j] != data[i][j]) {
						return false;
					}
				}
			}
		}
		return true;
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
		if (level > 100) return;
		
//		System.out.print("Sokobot.generateTree(state, " + level + "): ");
//		int[] playerPos = Tools.getPosOfChar(state.getItemsData(), Tools.PLAYER).get(0);
//		System.out.println("playerPos = (" +  playerPos[Tools.X] + ", " + playerPos[Tools.Y] + ")");
//		
//		System.out.print("Sokobot.generateTree(state, " + level + "): ");
//		System.out.println("state.direction: " + state.getPlayerMovement());
		
		System.out.print("Sokobot.generateTree(state, " + level + "): ");
		printBoard(state);
		
//		System.out.print("Sokobot.generateTree(state, " + level + "): ");
		
//		printStorage();
		
//		if (storage.contains(state.getItemsData())) {
		if (isExists(state.getItemsData())) {
//				System.out.print("Sokobot.generateTree(state, " + level + "): ");
//				System.out.println("IT EXISTS");
				return;
		}
		else {
//			System.out.print("Sokobot.generateTree(state, " + level + "): ");
//			System.out.println("IT DOES NOT EXIST");
			
			char[][] copyPos;
			copyPos = Tools.copyArray(state.getItemsData());
			storage.add(copyPos);
			
			printStorage();
		}
		
		Direction[] allDirs = {Direction.NORTH, Direction.SOUTH, 
							   Direction.EAST, Direction.WEST};
		
		for (Direction dir : allDirs) {
//			System.out.print("Sokobot.generateTree(state, " + level + "): ");
//			System.out.println("Checking: " + Direction.dirToStr(dir));
			if (checkSpace(state, dir)) {
				State newState = new State(state);

				newState.move(dir);
				state.addNextState(newState);
				
				generateTree(newState, level + 1);
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
