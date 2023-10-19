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
	
	public void addNextState(State nextState) {
		nextStates.add(nextState);
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
