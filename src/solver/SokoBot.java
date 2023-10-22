package solver;

import java.util.*;

public class SokoBot {

  private SokobanMoveHandler moveHandler = new SokobanMoveHandler();

  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    /*
     * YOU NEED TO REWRITE THE IMPLEMENTATION OF THIS METHOD TO MAKE THE BOT SMARTER
     */
    /*
     * Default stupid behavior: Think (sleep) for 3 seconds, and then return a
     * sequence
     * that just moves left and right repeatedly.
     */
    SokobanGameState currentStateSokobanGameState = new SokobanGameState(width, height, mapData, itemsData);

    SokobanGameState goal = aSearch(currentStateSokobanGameState);

    String pathing = pathTracing(currentStateSokobanGameState, goal);
    System.out.println(pathing);
    return pathing;
  }

  public static class heuristic implements Comparator<SokobanGameState>{
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

  public SokobanGameState aSearch(SokobanGameState startState) {
    PriorityQueue<SokobanGameState> openSokobanGameStates = new PriorityQueue<>(new heuristic());
    HashMap<String, SokobanGameState> closedSokobanGameStates = new HashMap<>();

    openSokobanGameStates.offer(startState);

    while (!openSokobanGameStates.isEmpty()) {
      SokobanGameState currentState = openSokobanGameStates.remove();
      String currentStatePos = Arrays.toString(currentState.crateAndPlayerArr);

      if (currentState.checkTargetFound()) {
        return currentState; // Goal found
      }

      SokobanMoveChecker moveChecker = new SokobanMoveChecker();
      ArrayList<Character> listMoves = moveChecker.checkMoves(currentState);

      for (char move : listMoves) {
        SokobanGameState temp = createNextState(currentState, move);
        String tempPos = Arrays.toString(temp.crateAndPlayerArr);

        if (!closedSokobanGameStates.containsKey(tempPos)) {
          temp.setParent(move, currentState);
          openSokobanGameStates.offer(temp);
        }
      }

      closedSokobanGameStates.put(currentStatePos, currentState);
    }
    return null; // Goal not found
  }

  private SokobanGameState createNextState(SokobanGameState currentState, char move) {
    switch (move) {
      case 'u':
        return new SokobanGameState(currentState.width, currentState.height, currentState.mapData, moveUp(currentState));
      case 'd':
        return new SokobanGameState(currentState.width, currentState.height, currentState.mapData, moveDown(currentState));
      case 'l':
        return new SokobanGameState(currentState.width, currentState.height, currentState.mapData, moveLeft(currentState));
      case 'r':
        return new SokobanGameState(currentState.width, currentState.height, currentState.mapData, moveRight(currentState));
      default:
        return null;
    }
  }


  public String pathTracing(SokobanGameState initial, SokobanGameState end){
    if (initial == null || end == null) {
      System.out.println("Unsolvable");
      return " ";
    }

    StringBuilder sb = new StringBuilder();
    while (end != null && !end.equals(initial)) {
      sb.insert(0, end.parentMove);
      end = end.parentGameState;
    }
    return sb.toString();
  }


  public char[][] moveDown(SokobanGameState state) {
    return moveHandler.moveDown(state);
  }

  public char[][] moveUp(SokobanGameState state) {
    return moveHandler.moveUp(state);
  }

  public char[][] moveLeft(SokobanGameState state) {
    return moveHandler.moveLeft(state);
  }

  public char[][] moveRight(SokobanGameState state) {
    return moveHandler.moveRight(state);
  }



}