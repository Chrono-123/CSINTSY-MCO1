package solver;

public class SokoBot {

   private int[2] playerCoordinate;
   private int numOfGoals = 0;
   private int[][] goals;
   private int numOfCrates = 0;
   private int[][] crates;
   private int numOfWalls = 0;
   private int[][] walls;
   private boolean up = true, down = true, left = true, right = true;
   private int move = 1;

   /*checks if player can move*/
   public void checkWall(){
      for (int checking = 0; checking < walls.length(); checking++){
         if (playerCoordinate[0]+move == walls[checking][0]){
            right = false;
         }
         if (playerCoordinate[1]+move == walls[checking][1]){
            up = false;
         }
         if (playerCoordinate[0]-move == walls[checking][0]){
            left = false;
         }
         if (playerCoordinate[1]-move == walls[checking][1]){
            down = false;
         }
      }

      /*Checks if crates can move*/
      public void checkCrate(){
         for (int checking = 0; checking < crates.length(); checking++){
         if (playerCoordinate[0]+move == crates[checking][0]){
            move = 2;
         }
         if (playerCoordinate[1]+move == crates[checking][1]){
            move = 2;
         }
         if (playerCoordinate[0]-move == crates[checking][0]){
            move = 2;
         }
         if (playerCoordinate[1]-move == crates[checking][1]){
            move = 2;
         }
         switch(move){
            case 2: checkWall();
         }
      }
      

  /*Responsible for the movement of player*/
  public String movement(){
     while(numOfGoals > 0){
        checkWall();
        checkCrate();
        
        
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
