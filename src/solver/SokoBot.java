package solver;

public class SokoBot {
	// Array indexes for position
	private final int X = 0;
	private final int Y = 1;
	
	private final char PLAYER = '@';
	private final char CRATE = '$';
	private final char WALL = '#';
	private final char GOAL = '.';
	
	private String output;
	
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

	private boolean checkSpace(char[][] mapData, char[][] itemsData, 
							   Direction direction, int[] origin) {
		int x = origin[X];
		int y = origin[Y];
		
		// Check again if there is a crate in the same direction
		if (itemsData[y][x] == CRATE) {
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
			checkSpace(mapData, itemsData, direction, newPosition);
		}
		
		// Check if it's a space or a goal. Whatever you can go through
		return (mapData[y][x] == ' ' || mapData[y][x] == '.');
	}
	
	private char[][] movePlayer(char[][] mapData, char[][] itemsData, 
								Direction direction) {
		char[][] newItemData = itemsData;
		
		int[] playerPos = getPosOfChar(itemsData, PLAYER);
		
		// Moving
		checkSpace(mapData, itemsData, direction, playerPos);
		
		switch (direction) {
		case NORTH:
			
			break;
		case EAST:
			break;
		case SOUTH:
			break;
		case WEST:
			break;
		}
		
		return newItemData;
	}
	
	public String solveSokobanPuzzle(int width, int height, char[][] mapData, 
									 char[][] itemsData) {
		int[] playerPos = getPosOfChar(itemsData, '@');
		output = "";
		return output;
	}
}
