package solver;

import gui.GamePanel;

public class SokoBot {
	// Array indexes for position
	private final int X = 0;
	private final int Y = 1;
	
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
							   int[] position) {
		int x = position[X];
		int y = position[Y];
		// Check if it's a space or a goal. Whatever you can go through
		if (mapData[y][x] == ' ' || mapData[y][x] == '.')
			return true;
		// It's a wall
		else 
			return false;
	}
	
	public String search(int width, int height, 
						 char[][] mapData, char[][] itemsData,
						 int[] playerPos, String output) {		
		// array directions
		int[] upPos = playerPos;
		int[] rightPos = playerPos;
		int[] downPos = playerPos;
		int[] leftPos = playerPos;
		
		upPos[Y] -= 1;
		rightPos[X] += 1;
		downPos[Y] += 1;
		leftPos[X] -= 1;
		
		if (checkSpace(mapData, itemsData, upPos))
			search(width, height, mapData, itemsData, upPos, output + "u");
		if (checkSpace(mapData, itemsData, rightPos))
			search(width, height, mapData, itemsData, rightPos, output + "r");
		if (checkSpace(mapData, itemsData, downPos))
			search(width, height, mapData, itemsData, downPos, output + "d");
		if (checkSpace(mapData, itemsData, leftPos))
			search(width, height, mapData, itemsData, leftPos, output + "l");
		
		return output;
	}
	
	public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
		int[] playerPos = getPosOfChar(itemsData, '@');
		
		return search(width, height, mapData, itemsData, playerPos, "");
	}
}
