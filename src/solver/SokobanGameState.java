package solver;
import java.util.Arrays;

public class SokobanGameState {
    int width, height;
    char[][] mapData, itemsData;
    int[] playerCoordinates;
    char parentMove;
    SokobanGameState parentGameState;
    int[] boxPositionArray, goalPositionArray ;
    int[] boxAndPlayerArr;
    int boxes, goals;
    int depth;

    public SokobanGameState(int width, int height, char[][] mapData, char[][] itemsData, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;

        this.mapData = mapData;
        this.itemsData = itemsData;

        this.boxPositionArray = new int[50];
        this.boxes = 0;
        this.goalPositionArray  = new int[50];
        this.goals = 0;
        this.boxAndPlayerArr = new int[50];

        int i = 0;
        int j = 0;

        while (i < height) {
            while (j < width) {
                if (itemsData[i][j] == '@') {
                    playerCoordinates = new int[]{i, j};
                    boxAndPlayerArr[0] = i;
                    boxAndPlayerArr[1] = j;
                }

                if (itemsData[i][j] == '$') {
                    int[] temp = {i, j};

                    boxPositionArray[boxes * 2] = i;
                    boxPositionArray[boxes * 2 + 1] = j;

                    boxAndPlayerArr[boxes * 2 + 2] = i;
                    boxAndPlayerArr[boxes * 2 + 3] = j;

                    boxes++;
                }

                if (mapData[i][j] == '.') {
                    int[] temp = {i, j};

                    goalPositionArray [goals * 2] = i;
                    goalPositionArray [goals * 2 + 1] = j;
                    goals++;
                }

                j++;
            }

            // Reset j and increment i
            j = 0;
            i++;
        }

    }

    public boolean checkGoalFound() {
        return Arrays.equals(boxPositionArray, goalPositionArray );
    }

    public void setParent(char move, SokobanGameState gameState) {
        this.parentMove = move;
        this.parentGameState = gameState;
    }

    public int getHeuristics() {
        int totalHeuristic = 0;

        int minBoxToGoalDistance;
        int heuristicFromPlayer;

        int[] goalCoordinatesCopy = goalPositionArray .clone();

        for (int boxIndex = 0; boxIndex < boxes; boxIndex++) {
            minBoxToGoalDistance = Integer.MAX_VALUE;

            for (int goalIndex = 0; goalIndex < goals; goalIndex++) {
                int boxX = boxPositionArray[boxIndex * 2 + 1];
                int goalY = goalCoordinatesCopy[goalIndex * 2 + 1];
                int boxY = boxPositionArray[boxIndex * 2];
                int goalX = goalCoordinatesCopy[goalIndex * 2];

                int boxToGoalDistance = getDistance(boxX, goalY, boxY, goalX);
                minBoxToGoalDistance = Math.min(minBoxToGoalDistance, boxToGoalDistance);
            }

            heuristicFromPlayer = getDistance(playerCoordinates[1], boxPositionArray[boxIndex * 2 + 1], playerCoordinates[0], boxPositionArray[boxIndex * 2]);

            totalHeuristic += minBoxToGoalDistance + heuristicFromPlayer;
        }

        return totalHeuristic + depth;
    }



    public int getDistance(int x1, int x2, int y1, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public char[][] cloneItemData() {
        char[][] copy = new char[height][];
        for (int i = 0; i < height; i++) {
            copy[i] = new char[width];
            System.arraycopy(itemsData[i], 0, copy[i], 0, width);
        }
        return copy;
    }
}
