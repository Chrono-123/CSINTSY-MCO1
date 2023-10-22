package solver;

public class SokobanMoveHandler {

    public char[][] moveDown(SokobanGameState state) {
        int x = state.playerCoordinates[0];
        int y = state.playerCoordinates[1];
        char[][] copy = state.cloneItemData();
        if (copy[x + 1][y] == '$') {
            swap(x + 1, y, copy, 'D');
        }
        swap(x, y, copy, 'D');
        return copy;
    }

    public char[][] moveUp(SokobanGameState state) {
        int x = state.playerCoordinates[0];
        int y = state.playerCoordinates[1];
        char[][] copy = state.cloneItemData();
        if (copy[x - 1][y] == '$') {
            swap(x - 1, y, copy, 'U');
        }
        swap(x, y, copy, 'U');
        return copy;
    }

    public char[][] moveLeft(SokobanGameState state) {
        int x = state.playerCoordinates[0];
        int y = state.playerCoordinates[1];
        char[][] copy = state.cloneItemData();
        if (copy[x][y - 1] == '$') {
            swap(x, y - 1, copy, 'L');
        }
        swap(x, y, copy, 'L');
        return copy;
    }

    public char[][] moveRight(SokobanGameState state) {
        int x = state.playerCoordinates[0];
        int y = state.playerCoordinates[1];
        char[][] copy = state.cloneItemData();
        if (copy[x][y + 1] == '$') {
            swap(x, y + 1, copy, 'R');
        }
        swap(x, y, copy, 'R');
        return copy;
    }

    public void swap(int x, int y, char[][] itemsData, char move) {
        char temp;
        switch (move) {
            case 'D':
                temp = itemsData[x + 1][y];
                itemsData[x + 1][y] = itemsData[x][y];
                itemsData[x][y] = temp;
                break;
            case 'U':
                temp = itemsData[x - 1][y];
                itemsData[x - 1][y] = itemsData[x][y];
                itemsData[x][y] = temp;
                break;
            case 'L':
                temp = itemsData[x][y - 1];
                itemsData[x][y - 1] = itemsData[x][y];
                itemsData[x][y] = temp;
                break;
            case 'R':
                temp = itemsData[x][y + 1];
                itemsData[x][y + 1] = itemsData[x][y];
                itemsData[x][y] = temp;
                break;
        }
    }
}
