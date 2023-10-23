package solver;
import java.util.ArrayList;
import java.util.HashMap;
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
	
	/**
	 * Check if the state has all the crates in all the goals.
	 * */
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
	private boolean checkSpace(State state, Direction direction) {
		int[] origin = Tools.getPosOfChar(state.getItemsData(), Tools.PLAYER).get(0);
		
		int lookX = origin[Tools.X];
		int lookY = origin[Tools.Y];
		
		char[][] mapData, itemsData;
		mapData = state.getMapData();
		itemsData = state.getItemsData();
		
		direction = state.getPlayerMovementDir();
		
		int[] dir = Direction.dirToPos(direction); 
		
		lookX += dir[Tools.X];
		lookY += dir[Tools.Y];
		
		// Check if it's a space or a goal. Also check if the player
		// tries to push two crates. Whenever you can go through

		int extraX = (1 * dir[Tools.X]);
		int extraY = (1 * dir[Tools.Y]);
		
		return (// a space or a goal
				(mapData[lookY][lookX] == Tools.SPACE || 
					mapData[lookY][lookX] == Tools.GOAL) &&
				// another crate or wall
				(itemsData[lookY + extraY][lookX + extraX] != Tools.CRATE ||
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
	
	/**
	 * Generate a state tree
	 * @param state the parent state.
	 * */
	private void generateTree(State state) {
		if (goalState != null || storage.contains(state.getItemsData())) {
			return;
		}
		
		Direction[] allDirs = {Direction.NORTH, Direction.SOUTH, 
							   Direction.EAST, Direction.WEST};
		
		for (Direction dir : allDirs) {
			System.out.println("Checking: " + Direction.dirToStr(dir));
			if (checkSpace(state, dir)) {
				State newState = new State(state);
				state.addNextState(newState);
				newState.move(dir);
				
				System.out.println(newState.getPlayerMovement());
				
				generateTree(newState);
			}
		}
	}
	
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
		while (!isGoalState(target) && tries < 100) {
			State nextState = target;
			
			// Pick one with lowest heuristic value
			int minHeuristic = Integer.MAX_VALUE;
			for (State whichState: startState.getNextStates()) {
				if (whichState.getHeuristic() < minHeuristic) {
					nextState = whichState;
				}
			}
			
			// Output that shit
			output += nextState.getPlayerMovement();
			
			target = nextState;
			tries += 1;
		}
		
		return output;
	}
	
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
		goalItemsData = itemsData;
		generateGoalItemsData(mapData);
		
		System.out.println("Generating tree...");
		// Generate a tree
		generateTree(startState);
		
		System.out.println("Generating path...");
		// Cook up string
		output = generatePath(startState);
		System.out.println("output: " + output);
		return output;

	}
}
