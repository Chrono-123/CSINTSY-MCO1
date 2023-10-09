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
   private int move = 1, prevMove;

   /*checks if player can move*/
   public void checkWall(){
      for (int checking = 0; checking < walls.size(); checking++){
         if (playerCoordinate[0]+move == walls[checking][0]){ //wall right of player
            right = false;
         }
         if (playerCoordinate[1]+move == walls[checking][1]){ //wall above player
            up = false;
         }
         if (playerCoordinate[0]-move == walls[checking][0]){ //wall left of player
            left = false;
         }
         if (playerCoordinate[1]-move == walls[checking][1]){ //wall below player
            down = false;
         }
      }

      /*Checks if crates can move*/
      public void checkCrate(){
         for (int checking = 0; checking < crates.size(); checking++){
         if (playerCoordinate[0]+move == crates[checking][0]){ //crate right of player
            move = 2;
         }
         if (playerCoordinate[1]+move == crates[checking][1]){ //crate above player
            move = 2;
         }
         if (playerCoordinate[0]-move == crates[checking][0]){ //crate left of player
            move = 2;
         }
         if (playerCoordinate[1]-move == crates[checking][1]){ //crate below player
            move = 2;
         }
         switch(move){
            case 2: checkWall();
         }
      }
      

  /*Responsible for the movement of player*/
  public String movement(int prevMove){
      checkWall();
      checkCrate();
     
      switch(prevMove){
         case 0: up = false;break;
         case 1: down = false;break;
         case 2: left = false;break;
         case 3: right = false; break;
      }
     
      if(up == true){
         playerCoordinate[1]++;
         prevMove = 0;
         movement(prevMove);
      }
      if(down == true){
         playerCoordinate[1]--;
         prevMove = 1;
         movement(prevMove);
         
      }
      if(left == true){
         playerCoordinate[0]--;
         prevMove = 2;
         movement(prevMove);
      }
      if(right == true){
         playerCoordinate[0]++;
         prevMove = 3;
         movement(prevMove);
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
          crates[crates.size()][0] = j;
          crates[crates.size()][1] = i;
        }
        if(mapData[i][j] == '.'){ //coordinates of goals
          goals[goals.size()][0] = j;
          goals[goals.size()][1] = i;
        }
        if(mapData[i][j] == '#'){ //coordinates of walls
          walls[walls.size()][0] = j;
          walls[walls.size()][1] = i;
        }
      }
    }
    prevMove = -1;
    movement(prevMove);
    try {
      Thread.sleep(3000);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return "lrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlrlr";
  }

}
