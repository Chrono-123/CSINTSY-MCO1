package solver;

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
    private int[2] playerCoordinate;
    private int numOfGoals = 0;
    private int[][] goals;
    
    for(int i = 0; i < height; i++){
      for(int j = 0; j < width; j++){
        if(itemsData[i][j] == '@'){
          playerCoordinate[0] = j;
          playerCoordinate[1] = i;
        }
        if(mapData[i][j] == '.'){
          goals[numOfGoals][0] = j;
          goals[numOfGoals][1] = i;
          numOfGoals++;
      }
    }
    
    try {
      Thread.sleep(3000);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return "lrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlr";
  }

}
