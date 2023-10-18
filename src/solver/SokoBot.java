package solver;

import java.util.*;

public class SokoBot {
	private static final char PLAYER = '@';
	private static final char CRATE = '$';
	private static final char GOAL = '.';
	private static final char WALL = '#';
	private static final char EMPTY = ' ';
	private static final char CRATE_ON_GOAL = '*';
	private static final char PLAYER_ON_GOAL = '+';

	private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
	private static final char[] DIRECTION_CHARS = {'r', 'd', 'l', 'u'};

	class State {
		int playerX, playerY;
		Set<String> cratePositions; // "x,y"
		String moves;

		State(int playerX, int playerY, Set<String> cratePositions, String moves) {
			this.playerX = playerX;
			this.playerY = playerY;
			this.cratePositions = cratePositions;
			this.moves = moves;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			State state = (State) o;
			return playerX == state.playerX &&
					playerY == state.playerY &&
					Objects.equals(cratePositions, state.cratePositions);
		}

		@Override
		public int hashCode() {
			return Objects.hash(playerX, playerY, cratePositions);
		}
	}

	private boolean isGoalState(State state, char[][] mapData) {
		for (String cratePos : state.cratePositions) {
			String[] parts = cratePos.split(",");
			int x = Integer.parseInt(parts[0]);
			int y = Integer.parseInt(parts[1]);
			if (mapData[y][x] != GOAL) {
				return false;
			}
		}
		return true;
	}

	private int manhattanDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	private int heuristic(State state, char[][] mapData) {
		int totalDistance = 0;
		for (String cratePos : state.cratePositions) {
			String[] parts = cratePos.split(",");
			int crateX = Integer.parseInt(parts[0]);
			int crateY = Integer.parseInt(parts[1]);

			int shortestDistance = Integer.MAX_VALUE;
			for (int y = 0; y < mapData.length; y++) {
				for (int x = 0; x < mapData[0].length; x++) {
					if (mapData[y][x] == GOAL) {
						int distance = manhattanDistance(crateX, crateY, x, y);
						shortestDistance = Math.min(shortestDistance, distance);
					}
				}
			}
			totalDistance += shortestDistance;
		}
		return totalDistance;
	}

	private List<State> generateMoves(State currentState, char[][] mapData) {
		List<State> nextStates = new ArrayList<>();

		for (int i = 0; i < DIRECTIONS.length; i++) {
			int[] dir = DIRECTIONS[i];
			char moveChar = DIRECTION_CHARS[i];

			int nextX = currentState.playerX + dir[0];
			int nextY = currentState.playerY + dir[1];

			if (nextX < 0 || nextX >= mapData[0].length || nextY < 0 || nextY >= mapData.length) {
				continue;
			}

			char nextTile = mapData[nextY][nextX];

			if (nextTile == WALL) {
				continue;
			}

			Set<String> newCratePositions = new HashSet<>(currentState.cratePositions);

			if (currentState.cratePositions.contains(nextX + "," + nextY)) {
				int crateNextX = nextX + dir[0];
				int crateNextY = nextY + dir[1];

				if (crateNextX < 0 || crateNextX >= mapData[0].length || crateNextY < 0 || crateNextY >= mapData.length) {
					continue;
				}

				char crateNextTile = mapData[crateNextY][crateNextX];

				if (crateNextTile == WALL || currentState.cratePositions.contains(crateNextX + "," + crateNextY)) {
					continue;
				}

				newCratePositions.remove(nextX + "," + nextY);
				newCratePositions.add(crateNextX + "," + crateNextY);
			}

			nextStates.add(new State(nextX, nextY, newCratePositions, currentState.moves + moveChar));
		}

		return nextStates;
	}

	public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
		PriorityQueue<State> queue = new PriorityQueue<>(
				Comparator.comparingInt(state -> state.moves.length() + heuristic(state, mapData))
		);
		Set<State> visited = new HashSet<>();

		int playerX = 0, playerY = 0;
		Set<String> cratePositions = new HashSet<>();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (itemsData[y][x] == PLAYER || itemsData[y][x] == PLAYER_ON_GOAL) {
					playerX = x;
					playerY = y;
				} else if (itemsData[y][x] == CRATE || itemsData[y][x] == CRATE_ON_GOAL) {
					cratePositions.add(x + "," + y);
				}
			}
		}

		State initialState = new State(playerX, playerY, cratePositions, "");
		queue.add(initialState);
		visited.add(initialState);

		while (!queue.isEmpty()) {
			State currentState = queue.poll();

			if (isGoalState(currentState, mapData)) {
				return currentState.moves;
			}

			List<State> nextStates = generateMoves(currentState, mapData);

			for (State nextState : nextStates) {
				if (!visited.contains(nextState)) {
					queue.add(nextState);
					visited.add(nextState);
				}
			}
		}

		return "No solution found";
	}
}
