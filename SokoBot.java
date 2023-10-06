package solver;

public class SokoBot {

   private int[2] playerCoordinate;
    private int numOfGoals = 0;
    private int[][] goals;
    private int numOfCrates = 0;
    private int[][] crates;
    private int numOfWalls = 0;
    private int[][] walls;

  /*Responsible for the movement of player*/
  public String movement(){
     while(numOfGoals > 0){
        for(int i = 0; i < numOfWalls; i++){ 
            if (playerCoordinate[0]+1 != walls[i][0]){ //up
               playerCoordinate[0]++;
            }
           else if (playerCoordinate[1]+1 != walls[i][1]){ //right
              playerCoordinate[1]++;
           }
           else if (playerCoordinate[0]-1 != walls[i][0]){ //down
              playerCoordinate[0]--;
        }
           else if (playerCoordinate[1]-1 != walls[i][1]){ //left
              playerCoordinate[1]--;
     }
  }
  public String solveSokobanPuzzle(int width, int height, char[][] mapData, char[][] itemsData) {
    /*
     * YOU NEED TO REWRITE THE IMPLEMENTATION OF THIS METHOD TO MAKE THE BOT SMARTER
     */
    /*
     * Default stupid behavior: Think (sleep) for 3 seconds, and then return a
     * sequence
     * that just moves left and right repeatedly.
     */
   
    /*To identify the coordinates of all solid objects that is not an empty space*/
    for(int i = 0; i < height; i++){ 
      for(int j = 0; j < width; j++){
        if(itemsData[i][j] == '@'){ //coordinates of player at start
          playerCoordinate[0] = j;
          playerCoordinate[1] = i;
        }
        if(itemsData[i][j] == '$'){ //coordinates of crates/boxes
          crates[numOfCrates][0] = j;
          crates[numOfCrates][1] = i;
          numOfCrates++;
        }
        if(mapData[i][j] == '.'){ //coordinates of goals
          goals[numOfGoals][0] = j;
          goals[numOfGoals][1] = i;
          numOfGoals++;
        }
        if(mapData[i][j] == '#'){ //coordinates of walls
          walls[numOfWalls][0] = j;
          walls[numOfWalls][1] = i;
          numOfWalls++;
        }
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
