package solver;

import java.util.*;

public class SokoBot {

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    /*
     * YOU NEED TO REWRITE THE IMPLEMENTATION OF THIS METHOD TO MAKE THE BOT SMARTER
     */
    /*
     * Default stupid behavior: Think (sleep) for 3 seconds, and then return a
     * sequence
     * that just moves left and right repeatedly.
     */
    SokobanGameState currSokobanGameState = new SokobanGameState(width, height, mapData, itemsData, 0);
    long startTime = System.nanoTime();

    SokobanGameState goal = aSearch(currSokobanGameState);

    String path = tracePath(currSokobanGameState, goal);
    long elapsedTime = System.nanoTime() - startTime;
    System.out.println(path);
    System.out.println("Execution Time: " + elapsedTime/1000000000 + "s");
    return path;
  }

  public static class heuristicsComparator implements Comparator<SokobanGameState>{
      @Override
      public int compare(SokobanGameState n1, SokobanGameState n2) {
          int first = n1.getHeuristics();
          int second = n2.getHeuristics();
          if(first > second)
              return 1;
          else if(first < second)
              return -1;
          return 0;
      }
  }

  PriorityQueue<SokobanGameState> openSokobanGameStates = new PriorityQueue<>(new heuristicsComparator());

  HashMap<String, SokobanGameState> closedSokobanGameStates = new HashMap<>();
  
public SokobanGameState aSearch(SokobanGameState startState){
  Scanner sc = new Scanner(System.in);
  SokobanGameState curr = startState;
  boolean goalFound = curr.checkGoalFound();
  int i=0;
  if(!goalFound){

    openSokobanGameStates.offer(curr);
    while(!goalFound && !openSokobanGameStates.isEmpty()){
      curr = openSokobanGameStates.remove();
      String currPos = Arrays.toString(curr.boxPlayerArr);
      SokobanMoveChecker moveChecker = new SokobanMoveChecker();

      goalFound = curr.checkGoalFound();
      if(!goalFound) {
        ArrayList<Character> listMoves = moveChecker.checkMoves(curr);
        if (!listMoves.isEmpty()) {
          for (char move : listMoves) {
            SokobanGameState temp = switch (move) {
              case 'u' -> new SokobanGameState(curr.width, curr.height, curr.mapData, moveUp(curr), curr.depth + 1);
              case 'd' -> new SokobanGameState(curr.width, curr.height, curr.mapData, moveDown(curr), curr.depth + 1);
              case 'l' -> new SokobanGameState(curr.width, curr.height, curr.mapData, moveLeft(curr), curr.depth + 1);
              case 'r' -> new SokobanGameState(curr.width, curr.height, curr.mapData, moveRight(curr), curr.depth + 1);
              default -> null;
            };
            assert temp != null;
            String tempos = Arrays.toString(temp.boxPlayerArr);
            if(!closedSokobanGameStates.containsKey(tempos)){
                               temp.setParent(move, curr);
                  openSokobanGameStates.offer(temp);

            }
          }
        }
      }
      closedSokobanGameStates.put(currPos, curr);
      i++;
    }
  }
  System.out.println("SokobanGameStates: " + i);
  return curr;
}

public String tracePath(SokobanGameState start, SokobanGameState current){
  StringBuilder sb = new StringBuilder();
  while(!current.equals(start)){

    sb.insert(0, current.parentMove);
    current = current.parentGameState;
  }
  return sb.toString();
}
  public char[][] moveDown(SokobanGameState state){
    int x = state.playerPos[0];
    int y = state.playerPos[1];
    char[][] copy = state.copyItemData();
    if(copy[x+1][y]=='$'){
      swap(x+1, y, copy, 'D');
    }
    swap(x, y, copy, 'D');
    return copy;
  }

  public char[][] moveUp(SokobanGameState state){
    int x = state.playerPos[0];
    int y = state.playerPos[1];
    char[][] copy = state.copyItemData();
    if(copy[x-1][y]=='$'){
      swap(x-1, y, copy, 'U');
    }
    swap(x, y, copy, 'U');
    return copy;
  }

  public char[][] moveLeft(SokobanGameState state){
    int x = state.playerPos[0];
    int y = state.playerPos[1];
    char[][] copy = state.copyItemData();
    if(copy[x][y-1]=='$'){
      swap(x, y-1, copy, 'L');
    }
    swap(x, y, copy, 'L');
    return copy;
  }

  public char[][] moveRight(SokobanGameState state){
    int x = state.playerPos[0];
    int y = state.playerPos[1];
    char[][] copy = state.copyItemData();
    if(copy[x][y+1]=='$'){
      swap(x, y+1, copy, 'R');
    }
    swap(x, y, copy, 'R');
    return copy;
  }

  public void swap(int x, int y, char[][] itemsData, char move){
    char temp;
    switch(move){
      case 'D':
        temp = itemsData[x+1][y];
        itemsData[x+1][y] = itemsData[x][y];
        itemsData[x][y] = temp;
        break;
      case 'U':
        temp = itemsData[x-1][y];
        itemsData[x-1][y] = itemsData[x][y];
        itemsData[x][y] = temp;
        break;
      case 'L':
        temp = itemsData[x][y-1];
        itemsData[x][y-1] = itemsData[x][y];
        itemsData[x][y] = temp;
        break;
      case 'R':
        temp = itemsData[x][y+1];
        itemsData[x][y+1] = itemsData[x][y];
        itemsData[x][y] = temp;
    }
  }

  public void printMap(int width, int height, char[][] mapData, char[][] itemsData){
      for(int i=0; i<height; i++){
      for(int j=0; j<width; j++){
        System.out.print(mapData[i][j]);
      }
      System.out.println();
    }

    for(int i=0; i<height; i++){
      for(int j=0; j<width; j++){
        System.out.print(itemsData[i][j]);
      }
      System.out.println();
    }
  }

}