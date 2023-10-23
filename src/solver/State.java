package solver;

import java.util.ArrayList;

public class State {
	private char[][] itemsData;
	private char[][] mapData;
	private ArrayList<State> nextStates;
	private int cost;
	private String playerMovement;

	private State parentState;

	void setParent(State parent) {
		this.parentState = parent;
	}

	State getParent() {
		return parentState;
	}

	State(char[][] itemsData, char[][] mapData) {
		nextStates = new ArrayList<State>();
		this.cost = 0;
		this.playerMovement = "";
		this.itemsData = itemsData;
		this.mapData= mapData;
	}

	State(State parentState) {
		nextStates = new ArrayList<State>();
		this.cost = 0;
		this.playerMovement = "";
		this.itemsData = parentState.getItemsData();
		this.mapData= parentState.getMapData();
	}

	public void move(Direction direction) {
		int[] playerPos;
		int[] dest;
		int[] dir;
    
		playerPos = Tools.getPosOfChar(itemsData, Tools.PLAYER).get(0);

		// Create an array representing as direction
		dir = Direction.dirToPos(direction);

		// Determine the new position based on the direction.
		dest = new int[2];
		dest[Tools.X] = dir[Tools.X] + playerPos[Tools.X];
		dest[Tools.Y] = dir[Tools.Y] + playerPos[Tools.Y];

		
		System.out.print("State.move(): ");
		System.out.println("direction: " + Direction.dirToStr(direction));
		
		System.out.print("State.move(): ");
		System.out.println(dest[Tools.X] + ", " + dest[Tools.Y]);
		
		// Push the crate
		if (Tools.IsCharInPos(itemsData, dest, Tools.CRATE)) {
			int[] extraPos = new int[2];

			extraPos[Tools.X] = dest[Tools.X] + (1 * dir[Tools.X]);
			extraPos[Tools.Y] = dest[Tools.Y] + (1 * dir[Tools.Y]);

			replaceChar(dest, Tools.SPACE);
			replaceChar(extraPos, Tools.CRATE);
		}

		// Move the Player
		replaceChar(playerPos, Tools.SPACE);
		replaceChar(dest, Tools.PLAYER);

		// Update the playerMovement string
		playerMovement = Direction.dirToStr(direction);
	}

	private void replaceChar(int[] pos, char newChar) {
		itemsData[pos[Tools.Y]][pos[Tools.X]] = newChar;
	}

	public void addNextStates(ArrayList<State> nextStates) {
		nextStates.addAll(nextStates);
	}

	public void addNextState(State nextState) {
		nextStates.add(nextState);
	}

	public ArrayList<State> getNextStates() {
		return nextStates;
	}

	public void setPlayerMovement(String movement) {
		this.playerMovement = movement;
	}

	public Direction getPlayerMovementDir() {
		Direction dir = Direction.NORTH;
		switch (playerMovement) { // added break statements
		case "u":
			dir = Direction.NORTH;
			break;
		case "d":
			dir = Direction.SOUTH;
			break;
		case "r":
			dir = Direction.WEST;
			break;
		case "l":
			dir = Direction.EAST;
			break;
		}

		return dir;
	}	
	public int getHeuristic() {
		// TODO: Manhattan and min distance
		ArrayList<int[]> goals = Tools.getPosOfChar(itemsData, Tools.GOAL);
		ArrayList<int[]> crates = Tools.getPosOfChar(itemsData, Tools.CRATE);
		
		int closestToGoal;
		int playerHeuristic;
		int totalHeuristic = 0;
		
		int[] playerPos = Tools.getPosOfChar(itemsData, Tools.PLAYER).get(0);
		
		for (int[] crate : crates) {
			closestToGoal = Integer.MAX_VALUE;
			for (int[] goal: goals) {
				closestToGoal = Math.min(closestToGoal, Tools.getDistance(crate, goal));
			}
			
			playerHeuristic = Tools.getDistance(playerPos, crate);
			
			totalHeuristic += playerHeuristic + closestToGoal;
		}
		
		return totalHeuristic;
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
