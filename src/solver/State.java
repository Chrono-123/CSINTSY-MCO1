package solver;

import java.util.ArrayList;

public class State {
	private char[][] cratesData;
	private char[][] goalsData;
	private ArrayList<State> nextStates;
	private int cost;
	private String playerMovement = "";
	
	State(char[][] itemsData, char[][] mapsData, String playerMovement) {
		nextStates = new ArrayList<State>();
		
		this.cratesData = itemsData;
		this.goalsData = mapsData;
		
		for (int i = 0; i < itemsData.length; i++) {
			for (int j = 0; j < itemsData[i].length; j++) {
				if (cratesData[i][j] != '$')
					cratesData[i][j] = ' ';
				
				if (goalsData[i][j] != '.') 
					cratesData[i][j] = ' ';
			}
		}
	}
	
	public void addNextState(State nextState) {
		nextStates.add(nextState);
	}
	
	public String getPlayerMovement() {
		return playerMovement;
	}
}
