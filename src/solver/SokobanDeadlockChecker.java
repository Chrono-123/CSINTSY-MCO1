package solver;

import java.util.ArrayList;

public class SokobanDeadlockChecker {
    public static ArrayList<Character> cornerDeadlock(ArrayList<Character> moves, SokobanGameState gameState) {
        for (char move : new ArrayList<>(moves)) {
            if (isCornerDeadlock(move, gameState)) {
                moves.remove(Character.valueOf(move));
            }
        }
        return moves;
    }

    private static boolean isCornerDeadlock(char move, SokobanGameState gameState) {
        int x = gameState.playerPos[0];
        int y = gameState.playerPos[1];

        // Define the offsets based on the move direction
        int xOff = 0;
        int yOff = 0;
        switch (move) {
            case 'r':
                yOff = 1;
                break;
            case 'l':
                yOff = -1;
                break;
            case 'd':
                xOff = 1;
                break;
            case 'u':
                xOff = -1;
                break;
        }

        int nextX = x + xOff;
        int nextY = y + yOff;

        // Check for corner deadlock conditions
        if (gameState.itemsData[nextX][nextY] == '$' &&
            gameState.mapData[x + xOff * 2][y + yOff * 2] != '.' &&
            gameState.mapData[x + xOff * 3][y + yOff * 3] == '#') {
            if (gameState.mapData[x + xOff][y + yOff + 1] == '#' ||
                gameState.mapData[x + xOff][y + yOff - 1] == '#') {
                return true;
            }
        }

        return false;
    }
}
