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
	public static boolean checkSpace(State state, Direction direction, int[] origin) {
		int lookX = origin[Tools.X];
		int lookY = origin[Tools.Y];
		
		char[][] mapData, itemsData;
		mapData = state.getMapData();
		itemsData = state.getItemsData();
		
		direction = state.getPlayerMovementDir();
		
		int[] dir = Direction.dirToPos(direction); 
		
		lookX += dir[Tools.X];
		lookY += dir[Tools.X];
		
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
		
		State northState = new State(state);
		State eastState = new State(state);
		State southState = new State(state);
		State westState = new State(state);
		
		northState.move(Direction.NORTH);
		eastState.move(Direction.EAST);
		southState.move(Direction.SOUTH);
		westState.move(Direction.WEST);
		
		state.addNextState(northState);
		state.addNextState(eastState);
		state.addNextState(southState);
		state.addNextState(westState);
		
		generateTree(northState);
		generateTree(eastState);
		generateTree(southState);
		generateTree(westState);
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
		return "lrlrlrlrlrlr";

	}
}
