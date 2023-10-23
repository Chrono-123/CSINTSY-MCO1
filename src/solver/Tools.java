package solver;

import java.util.ArrayList;

public class Tools {
	public final static int X = 0;
	public final static int Y = 1;
	
	public final static char PLAYER = '@';
	public final static char CRATE = '$';
	public final static char WALL = '#';
	public final static char GOAL = '.';
	public final static char SPACE = ' ';
	
	public static boolean isGoalState(State state) {
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
	public static ArrayList<int[]> getPosOfChar(char[][] data, char target) {
		ArrayList<int[]> positions = new ArrayList();
	
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
					int pos[] = new int[2];
					pos[X] = x;
					pos[Y] = y;
					positions.add(pos);
				}
			}
		}
		
		return positions;
	}
	
	public static boolean IsCharInPos(char[][] data, int[] pos, char c) {
		return data[pos[Tools.Y]][pos[Tools.X]] == c;
	}
	
	public static int getDistance(int[] source, int[] dest) {
		int xx = source[Tools.Y] - dest[Tools.Y]; 
		int yy = source[Tools.Y] - dest[Tools.Y]; 
		return Math.abs(xx) + Math.abs(yy);
	}
	
	public static char[][] copyArray(char[][] data) {
		int width = SokoBot.getWidth();
		int height = SokoBot.getHeight();
				
		char[][] result = new char[height][width];
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				result[i][j] = data[i][j];
			}
		}
		
		return result;
	}
	
}
