package solver;

import java.util.ArrayList;

public class SokobanMoveChecker {
    public static ArrayList<Character> checkMoves(SokobanGameState gameState) {
        ArrayList<Character> list = new ArrayList<>();
        list.add('u');
        list.add('d');
        list.add('l');
        list.add('r');

        if (gameState.mapData[gameState.playerCoordinates[0]][gameState.playerCoordinates[1] + 1] == '#' ||
                (gameState.itemsData[gameState.playerCoordinates[0]][gameState.playerCoordinates[1] + 1] == '$' &&
                        (gameState.mapData[gameState.playerCoordinates[0]][gameState.playerCoordinates[1] + 2] == '#' ||
                                gameState.itemsData[gameState.playerCoordinates[0]][gameState.playerCoordinates[1] + 2] == '$')))
            list.remove(Character.valueOf('r'));

        if (gameState.mapData[gameState.playerCoordinates[0]][gameState.playerCoordinates[1] - 1] == '#' ||
                (gameState.itemsData[gameState.playerCoordinates[0]][gameState.playerCoordinates[1] - 1] == '$' &&
                        (gameState.mapData[gameState.playerCoordinates[0]][gameState.playerCoordinates[1] - 2] == '#' ||
                                gameState.itemsData[gameState.playerCoordinates[0]][gameState.playerCoordinates[1] - 2] == '$')))
            list.remove(Character.valueOf('l'));

        if (gameState.mapData[gameState.playerCoordinates[0] + 1][gameState.playerCoordinates[1]] == '#' ||
                (gameState.itemsData[gameState.playerCoordinates[0] + 1][gameState.playerCoordinates[1]] == '$' &&
                        (gameState.mapData[gameState.playerCoordinates[0] + 2][gameState.playerCoordinates[1]] == '#' ||
                                gameState.itemsData[gameState.playerCoordinates[0] + 2][gameState.playerCoordinates[1]] == '$')))
            list.remove(Character.valueOf('d'));

        if (gameState.mapData[gameState.playerCoordinates[0] - 1][gameState.playerCoordinates[1]] == '#' ||
                (gameState.itemsData[gameState.playerCoordinates[0] - 1][gameState.playerCoordinates[1]] == '$' &&
                        (gameState.mapData[gameState.playerCoordinates[0] - 2][gameState.playerCoordinates[1]] == '#' ||
                                gameState.itemsData[gameState.playerCoordinates[0] - 2][gameState.playerCoordinates[1]] == '$')))
            list.remove(Character.valueOf('u'));

        return SokobanDeadlockChecker.cornerDeadlock(list, gameState);
    }
}
