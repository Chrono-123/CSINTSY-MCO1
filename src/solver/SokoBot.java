package solver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SokoBot {	
	// Grid dimension
	private int width;
	private int height;
	
	private char[][] goalItemsData;
	
	private State startState;
	private State goalState;
	
	
	private ArrayList<State> pathway = new ArrayList();
	private ArrayList<char[][]> storage = new ArrayList();
	
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
				if (mapData[i][j] == Tools.GOAL && itemsData[i][j] != Tools.CRATE)
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
	public static boolean checkSpace(State state, Direction direction) {
		int[] origin = Tools.getPosOfChar(state.getItemsData(), Tools.PLAYER);
		
		int lookX = origin[Tools.X];
		int lookY = origin[Tools.Y];
		
		char[][] mapData, itemsData;
		mapData = state.getMapData();
		itemsData = state.getItemsData();
		
		direction = state.getPlayerMovementDir();
		
		int[] dir = Direction.dirToPos(direction); 
		
		lookX += dir[Tools.X];
		lookY += dir[Tools.Y]; //changed to Y from X
		
		// Check if it's a space or a goal. Also check if the player
		// tries to push two crates. Whenever you can go through

		int extraX = (1 * dir[Tools.X]);
		int extraY = (1 * dir[Tools.Y]);
		
		return (// a space or a goal
				mapData[lookY][lookX] == Tools.SPACE || mapData[lookY][lookX] == Tools.GOAL &&
				// another crate or wall
				(itemsData[lookY + extraY][lookX + extraX] != Tools.CRATE ||
				itemsData[lookY + extraY][lookX + extraX] != Tools.WALL));
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
		if (goalState != null || storage.contains(state.getItemsData())) {
			return;
		}

		if (isGoalState(state)) {
			goalState = state;  // Set the goal state when found
			return;
		}

		Direction[] allDirs = {Direction.NORTH, Direction.SOUTH,
				               Direction.EAST, Direction.WEST};

		for (Direction dir : allDirs) {
			if (checkSpace(state, dir)) {
				State newState = new State(state);
				newState.setParent(state);  // Set parent state
				state.addNextState(newState);
				newState.move(dir);
				generateTree(newState);
			}
		}
	}

	public List<State> tracePath(State goalState) {
		List<State> path = new ArrayList<>();
		State currentState = goalState;
		while (currentState != null) {
			path.add(0, currentState);  // Add state at the beginning of the list
			currentState = currentState.getParent();
		}
		return path;
	}

	private void generateGoalItemsData(char[][] mapData) {
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[i].length; j++) {
				if (mapData[i][j] == Tools.GOAL) {
					goalItemsData[i][j] = Tools.CRATE;
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

			List<State> path = tracePath(goalState);
			StringBuilder pathString = new StringBuilder();
			for (State state : path) {
				pathString.append(state.getPlayerMovement());
			}
			System.out.println(pathString);
			return pathString.toString();
	}
}
