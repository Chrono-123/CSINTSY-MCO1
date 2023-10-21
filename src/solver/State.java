package solver;

import java.util.ArrayList;

public class State {
	private char[][] itemsData;
	private char[][] mapData;
	private ArrayList<State> nextStates;
	private int cost;
	private String playerMovement;
	
	private State parentState;
	
	State(State current, String playerMovement) {
		int moveX = 0;
		int moveY = 0;
		
		switch (playerMovement) {
		case "l":
			moveX = -1;
			moveY = 0;
			break;
		case "r":
			moveX = 1;
			moveY = 0;
			break;
		case "u":
			moveX = 0;
			moveY = -1;
			break;
		case "d":
			moveX = 0;
			moveY = 1;
			break;
		}
		
		moveItem(this, '$', new int[2]);
		
		this.playerMovement = playerMovement;
	}
	
	private void setParent(State parent) {
		this.parentState = parent;
	}
	
	private State getParent() {
		return parentState;
	}
	
	State(char[][] itemsData, char[][] mapData) {
		nextStates = new ArrayList<State>();
		this.cost = 0;
		this.playerMovement = "";
		this.itemsData = itemsData;
		this.mapData= mapData;
	}
	
	private boolean moveItem(State state, char item, int[] dest, int[] origin) {
		int destX =   dest[X];
		int destY =   dest[Y];
		int originX = origin[X];
		int originY = origin[Y];
		
		char[][] newItemsData = itemsData;
		
		if (itemsData[destY][destX] == GOAL) {
			int[] newPos = dest;
			newPos[X] += destX - originX;
			newPos[Y] += destY - originY;
			moveItem(itemsData, mapData, newPos, dest, GOAL);
		}
		
		// Check if it is a wall or outside of the board
		if (mapData[destY][destX] == WALL || !withinBoundary(destY, destX))
			return false;
		
		if (item == PLAYER && itemsData[destY][destX] != GOAL) {
			newItemsData[originY][originX] = ' ';
			newItemsData[destY][destX] = item;
		}
		
		return true;
	}
	
	public void addNextStates(ArrayList<State> nextStates) {
		nextStates.addAll(nextStates);
	}
	
	public ArrayList<State> getNextStates() {
		return nextStates;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getCost() {
		return cost;
	}
	
	public void setPlayerMovement(String movement) {
		this.playerMovement = movement;
	}
	
	public String getPlayerMovement() {
		return playerMovement;
	}
	
	public char[][] getItemsData() {
		return itemsData;
	}
	
	public char[][] getMapData() {
		return mapData;
	}
}
