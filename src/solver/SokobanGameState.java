package solver;
import java.util.Arrays;

public class SokobanGameState {
    int width, height;
    char[][] mapData, itemsData;
    int[] playerCoordinates;
    char parentMove;
    SokobanGameState parentGameState;
    int[] cratePositionArray, targetPositionArray ;
    int[] crateAndPlayerArr;
    int crate, target;

    public SokobanGameState(int width, int height, char[][] mapData, char[][] itemsData) {
        this.width = width;
        this.height = height;

        this.mapData = mapData;
        this.itemsData = itemsData;
        this.crate = 0;
        this.cratePositionArray = new int[100];
        this.targetPositionArray  = new int[100];
        this.crateAndPlayerArr = new int[100];

        int i = 0;
        int j = 0;

        while (i < height) {
            while (j < width) {
                if (itemsData[i][j] == '@') {
                    playerCoordinates = new int[]{i, j};
                    crateAndPlayerArr[0] = i;
                    crateAndPlayerArr[1] = j;
                }
                if (itemsData[i][j] == '$') {
                    cratePositionArray[crate * 2] = i;
                    cratePositionArray[crate * 2 + 1] = j;

                    crateAndPlayerArr[crate * 2 + 2] = i;
                    crateAndPlayerArr[crate * 2 + 3] = j;

                    crate++;
                }
                if (mapData[i][j] == '.') {
                    targetPositionArray [target * 2] = i;
                    targetPositionArray [target * 2 + 1] = j;
                    target++;
                }
                j++;
            }
            // Reset j and increment i
            j = 0;
            i++;
        }

    }

    public boolean checkTargetFound() {
        return Arrays.equals(cratePositionArray, targetPositionArray);
    }

    public void setParent(char move, SokobanGameState gameState) {
        this.parentMove = move;
        this.parentGameState = gameState;
    }

    public int getHeuristics() {
        int totalHeuristic = 0;

        int mincrateTotargetDistance;
        int heuristicFromPlayer;

        int[] targetCoordinatesCopy = targetPositionArray .clone();

        for (int crateIndex = 0; crateIndex < crate; crateIndex++) {
            mincrateTotargetDistance = Integer.MAX_VALUE;

            for (int targetIndex = 0; targetIndex < target; targetIndex++) {
                int crateX = cratePositionArray[crateIndex * 2 + 1];
                int targetY = targetCoordinatesCopy[targetIndex * 2 + 1];
                int crateY = cratePositionArray[crateIndex * 2];
                int targetX = targetCoordinatesCopy[targetIndex * 2];

                int crateTotargetDistance = getDistance(crateX, targetY, crateY, targetX);
                mincrateTotargetDistance = Math.min(mincrateTotargetDistance, crateTotargetDistance);
            }

            heuristicFromPlayer = getDistance(playerCoordinates[1], cratePositionArray[crateIndex * 2 + 1], playerCoordinates[0], cratePositionArray[crateIndex * 2]);

            totalHeuristic += mincrateTotargetDistance + heuristicFromPlayer;
        }

        return totalHeuristic;
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
