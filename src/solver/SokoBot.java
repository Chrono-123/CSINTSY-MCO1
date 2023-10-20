package solver;

import java.util.*;
import java.util.logging.Logger;

import gui.BotThread;




public class SokoBot {
	private static final char PLAYER = '@';
	private static final char CRATE = '$';
	private static final char GOAL = '.';
	private static final char WALL = '#';
	private static final char EMPTY = ' ';
	private static final char CRATE_ON_GOAL = '*';
	private static final char PLAYER_ON_GOAL = '+';

	private static final Logger logger = Logger.getLogger(BotThread.class.getName());

	
	private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
	private static final char[] DIRECTION_CHARS = {'r', 'd', 'l', 'u'};

	// Represents the state of the game
	class State {
		int playerX, playerY;
		Set<String> cratePositions; // "x,y"
		String moves;
		int score; // Add a score field

		State(int playerX, int playerY, Set<String> cratePositions, String moves, int score) {
			this.playerX = playerX;
			this.playerY = playerY;
			this.cratePositions = cratePositions;
			this.moves = moves;
			this.score = score;
		}

		// Equals and hashCode methods to compare states
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

	private int calculateMoveScore(State state, char[][] mapData) {
		int score = 0;

		// Crate Positioning:
		for (String cratePos : state.cratePositions) {
			int crateX = Integer.parseInt(cratePos.split(",")[0]);
			int crateY = Integer.parseInt(cratePos.split(",")[1]);

			// Calculate the distance of the crate to the nearest goal
			int distanceToGoal = calculateDistanceToNearestGoal(crateX, crateY, mapData);

			// Penalize moves that take crates farther from goals and reward moves that bring them closer
			score += distanceToGoal;
		}

		// Bottleneck Clearing:
		// You can implement logic to identify bottlenecks in the puzzle and prioritize moves that help clear bottlenecks.
		// This may involve analyzing the map to detect narrow passages and choosing moves that widen them.

		// Goal-Reaching Moves:
		// You can prioritize moves that bring the player closer to crates and goals or moves that push crates towards goals.

		// Add any other criteria that are important for your solving strategy.

		return score;
	}

	private int manhattanDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	

	private int calculateDistanceToNearestGoal(int x, int y, char[][] mapData) {
		int shortestDistance = Integer.MAX_VALUE;

		for (int goalY = 0; goalY < mapData.length; goalY++) {
			for (int goalX = 0; goalX < mapData[0].length; goalX++) {
				if (mapData[goalY][goalX] == GOAL) {
					int distance = manhattanDistance(x, y, goalX, goalY);
					shortestDistance = Math.min(shortestDistance, distance);
				}
			}
		}

		return shortestDistance;
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
	

	private int heuristic(State state, char[][] mapData) {
		int totalDistance = 0;
	
		// Calculate the player's distance to the nearest goals
		int playerDistance = Integer.MAX_VALUE;
		for (int y = 0; y < mapData.length; y++) {
			for (int x = 0; x < mapData[0].length; x++) {
				if (mapData[y][x] == GOAL) {
					int distance = manhattanDistance(state.playerX, state.playerY, x, y);
					playerDistance = Math.min(playerDistance, distance);
				}
			}
		}
	
		totalDistance += playerDistance;
	
		// Calculate the crates' distance to the nearest goals
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
	
		// Calculate the player's distance to the nearest crate
		int playerToCrateDistance = calculateDistanceToNearestCrate(state.playerX, state.playerY, state.cratePositions);
	
		// Calculate distances between crates (optional)
		int crateToCrateDistance = calculateDistanceBetweenCrates(state.cratePositions);
	
		// Add these additional factors to the total heuristic score
		totalDistance += playerToCrateDistance;
		totalDistance += crateToCrateDistance;
	
		return totalDistance;
	}
	

	private int calculateDistanceToNearestCrate(int playerX, int playerY, Set<String> cratePositions) {
		int shortestDistance = Integer.MAX_VALUE;

		for (String cratePos : cratePositions) {
			String[] parts = cratePos.split(",");
			int crateX = Integer.parseInt(parts[0]);
			int crateY = Integer.parseInt(parts[1]);
			int distance = manhattanDistance(playerX, playerY, crateX, crateY);
			shortestDistance = Math.min(shortestDistance, distance);
		}

		return shortestDistance;
	}

	private int calculateDistanceBetweenCrates(Set<String> cratePositions) {
		int totalDistance = 0;

		// Iterate through pairs of crates and calculate their Manhattan distances
		for (String cratePos1 : cratePositions) {
			for (String cratePos2 : cratePositions) {
				if (!cratePos1.equals(cratePos2)) {
					String[] parts1 = cratePos1.split(",");
					String[] parts2 = cratePos2.split(",");
					int crateX1 = Integer.parseInt(parts1[0]);
					int crateY1 = Integer.parseInt(parts1[1]);
					int crateX2 = Integer.parseInt(parts2[0]);
					int crateY2 = Integer.parseInt(parts2[1]);
					int distance = manhattanDistance(crateX1, crateY1, crateX2, crateY2);
					totalDistance += distance;
				}
			}
		}

		return totalDistance;
	}

	private List<State> generateMoves(State currentState, char[][] mapData) {
		List<State> nextStates = new ArrayList<>();
		List<State> potentialStates = new ArrayList<>();

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

			// Create a new State for the potential move
			Set<String> newCratePositions = new HashSet<>(currentState.cratePositions);
			int crateNextX = nextX + dir[0];
			int crateNextY = nextY + dir[1];
			char crateNextTile = mapData[crateNextY][crateNextX];

			if (currentState.cratePositions.contains(nextX + "," + nextY)) {
				if (crateNextTile != WALL && !currentState.cratePositions.contains(crateNextX + "," + crateNextY)) {
					newCratePositions.remove(nextX + "," + nextY);
					newCratePositions.add(crateNextX + "," + crateNextY);
				}
			}

			// Calculate a score for the potential move based on factors like crate positioning, bottleneck clearing, and goal-reaching moves
			int score = calculateMoveScore(new State(nextX, nextY, newCratePositions, currentState.moves + moveChar, 0), mapData);

			State potentialState = new State(nextX, nextY, newCratePositions, currentState.moves + moveChar, score);

			potentialStates.add(potentialState);
		}

		// Sort potential states by their scores in descending order
		potentialStates.sort(Comparator.comparingInt(s -> -s.score));

		// Add the potential states to the list of next states
		for (State potentialState : potentialStates) {
			nextStates.add(potentialState);
		}

		return nextStates;
	}

	private class AStarNode {
		State state;
		AStarNode parent;
		int costSoFar;
		int estimatedTotalCost;

		AStarNode(State state, AStarNode parent, int costSoFar, int estimatedTotalCost) {
			this.state = state;
			this.parent = parent;
			this.costSoFar = costSoFar;
			this.estimatedTotalCost = estimatedTotalCost;
		}
	}

	private String reconstructPath(AStarNode goalNode) {
		StringBuilder path = new StringBuilder();
		AStarNode current = goalNode;

		while (current.parent != null) {
			State currentState = current.state;
			State previousState = current.parent.state;
		
			// Determine the move that led to the current state
			int dx = currentState.playerX - previousState.playerX;
			int dy = currentState.playerY - previousState.playerY;
			char move = ' '; // Initialize move as empty
			
			if (dx == 1) {
				move = 'l'; // Player moved left
			} else if (dx == -1) {
				move = 'r'; // Player moved right
			} else if (dy == 1) {
				move = 'u'; // Player moved up
			} else if (dy == -1) {
				move = 'd'; // Player moved down
			}
		
			path.insert(0, move);  // Insert the move at the beginning of the path
			current = current.parent;
		}
		
		return path.toString();
	}



	













	private void printMap(char[][] mapData, State state) {
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                if (state.playerX == x && state.playerY == y) {
                    System.out.print(PLAYER);
                } else {
                    char cell = mapData[y][x];
                    if (state.cratePositions.contains(x + "," + y)) {
                        if (cell == GOAL) {
                            System.out.print(CRATE_ON_GOAL);
                        } else {
                            System.out.print(CRATE);
                        }
                    } else {
                        System.out.print(cell);
                    }
                }
            }
            System.out.println();
        }
    }

    // Add this method to print the map with the current state
    private void printGameState(char[][] mapData, State state) {
        System.out.println("Current Map:");
        printMap(mapData, state);
        System.out.println("Player Position: (" + state.playerX + ", " + state.playerY + ")");
        System.out.println("Crate Positions: " + state.cratePositions);
        System.out.println("Moves: " + state.moves);
        System.out.println("Score: " + state.score);
    }


	public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
		PriorityQueue<AStarNode> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.estimatedTotalCost));
		Set<State> visited = new HashSet<>();

		int playerX = 0, playerY = 0;
		Set<String> cratePositions = new HashSet<>();

		// ... (rest of the setup code)

		AStarNode initialNode = new AStarNode(new State(playerX, playerY, cratePositions, "", 0), null, 0, heuristic(new State(playerX, playerY, cratePositions, "", 0), mapData));


		queue.add(initialNode);

		while (!queue.isEmpty()) {
			AStarNode currentNode = queue.poll();
			State currentState = currentNode.state;

			// Add this line to print the current game state
            printGameState(mapData, currentState);

			
			if (isGoalState(currentState, mapData)) {
				return reconstructPath(currentNode);
			}

			List<State> nextStates = generateMoves(currentState, mapData);

			for (State nextState : nextStates) {
				if (!visited.contains(nextState)) {
					int costSoFar = currentNode.costSoFar + 1;
					int estimatedTotalCost = costSoFar + heuristic(nextState, mapData);
					AStarNode nextNode = new AStarNode(nextState, currentNode, costSoFar, estimatedTotalCost);
					queue.add(nextNode);
					visited.add(nextState);
				}
			}
		}

		return "No solution found";
	}
}
