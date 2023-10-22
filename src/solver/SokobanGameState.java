package solver;
import java.util.Arrays;

public class SokobanGameState {
    int width, height;
    char[][] mapData, itemsData;
    int[] playerPos;
    char parentMove;
    SokobanGameState parentGameState;
    int[] boxArr, goalArr;
    int[] boxPlayerArr;
    int boxes, goals;
    int depth;

    public SokobanGameState(int width, int height, char[][] mapData, char[][] itemsData, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;

        this.mapData = mapData;
        this.itemsData = itemsData;

        this.boxArr = new int[50];
        this.boxes = 0;
        this.goalArr = new int[50];
        this.goals = 0;
        this.boxPlayerArr = new int[50];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (itemsData[i][j] == '@') {
                    playerPos = new int[]{i, j};
                    boxPlayerArr[0] = i;
                    boxPlayerArr[1] = j;
                }

                if (itemsData[i][j] == '$') {
                    int[] temp = {i, j};

                    boxArr[boxes * 2] = i;
                    boxArr[boxes * 2 + 1] = j;

                    boxPlayerArr[boxes * 2 + 2] = i;
                    boxPlayerArr[boxes * 2 + 3] = j;

                    boxes++;
                }
                if (mapData[i][j] == '.') {
                    int[] temp = {i, j};

                    goalArr[goals * 2] = i;
                    goalArr[goals * 2 + 1] = j;
                    goals++;
                }
            }
        }
    }

    public boolean checkGoalFound() {
        return Arrays.equals(boxArr, goalArr);
    }

    public void setParent(char move, SokobanGameState gameState) {
        this.parentMove = move;
        this.parentGameState = gameState;
    }

    public int getHeuristics() {
        int minDist, h = 0;
        int[] goalTemp = goalArr.clone();
        for (int i = 0; i < boxes; i++) {
            minDist = 99999;
            for (int j = 0; j < goals; j++) {
                int dist = getDistance(boxArr[i * 2 + 1], goalTemp[j * 2 + 1], boxArr[i * 2], goalTemp[j * 2]);
                minDist = Math.min(minDist, dist);
            }
            h += minDist;
            h += getDistance(playerPos[1], boxArr[i * 2 + 1], playerPos[0], boxArr[i * 2]);
        }
        return h + depth;
    }

    public int getDistance(int x1, int x2, int y1, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public char[][] copyItemData() {
        char[][] copy = new char[height][];
        for (int i = 0; i < height; i++) {
            copy[i] = new char[width];
            System.arraycopy(itemsData[i], 0, copy[i], 0, width);
        }
        return copy;
    }

    public void setDepth(int d) {
        this.depth = d;
    }
}
