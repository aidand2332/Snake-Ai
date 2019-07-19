import java.util.ArrayList;


//Controls all the game logic .. most important class in this project.
public class ThreadsController extends Thread {
	 ArrayList<ArrayList<DataOfSquare>> Squares= new ArrayList<ArrayList<DataOfSquare>>();
	 Tuple headSnakePos;
	 Tuple tailSnakePos;
	 int score = 0;
	 int sizeSnake=3;
	 long speed = 50;
	 String lastTurn = "";
	 int randomY = (int)(Math.random()*(Window.height));
	 int randomX = (int)(Math.random()*(Window.width));
	 int direction = 0;
	 public static int directionSnake ;

	 ArrayList<Tuple> positions = new ArrayList<Tuple>();
	 Tuple foodPosition;
	 
	 //Constructor of ControlleurThread 
	 ThreadsController(Tuple positionDepart){
		//Get all the threads
		Squares=Window.Grid;
		
		headSnakePos=new Tuple(positionDepart.x,positionDepart.y);
		directionSnake = 1;

		//!!! Pointer !!!!
		Tuple headPos = new Tuple(headSnakePos.getX(),headSnakePos.getY());
		positions.add(headPos);
		foodPosition= new Tuple(randomY, randomX);
		spawnFood(foodPosition);

	 }

	 //Important part :
	 public void run() {
		 while(true){
			 moveInterne(directionSnake);

			 moveExterne();
			 deleteTail();
			 pauser();

			 computerChose();

		 }
	 }
	 private void computerChose(){

	 	int xDifference = randomX - headSnakePos.getX();
	 	int yDifference = randomY - headSnakePos.getY();
		if(xDifference == 0){
			if(yDifference > 0){
				if(ThreadsController.directionSnake == 3){
					direction = 1;
				}else{
				direction = 4;
			}}
			if(yDifference < 0){
				if(ThreadsController.directionSnake == 4){
					direction = 2;
				} else{
				direction = 3;
			}}
		} else{
			if(xDifference > 0){
				if(ThreadsController.directionSnake == 2){
					direction = 3;
				}else{
				direction = 1;
			}}
			if(xDifference < 0){
				if(ThreadsController.directionSnake == 1){
					direction = 4;
				}else{
				direction = 2;
			}}
		}

		if(aiCheckCollisoin()){

			if(ThreadsController.directionSnake == 1){
				if(lastTurn.equals("left")){
					ThreadsController.directionSnake = 4;
					direction = 4;
				}
				if(lastTurn.equals("right")){
					ThreadsController.directionSnake = 3;
					direction = 3;
				}
				if(aiCheckCollisoin()){
					if(lastTurn.equals("left")){
						ThreadsController.directionSnake = 3;
						direction = 3;
					}
					if(lastTurn.equals("right")){
						ThreadsController.directionSnake = 4;
						direction = 4;
					}
				}
				if(aiCheckCollisoin()){
					ThreadsController.directionSnake = 2;
					direction = 2;

				}

			}
			if(ThreadsController.directionSnake == 2){
				if(lastTurn.equals("left")){

					direction = 3;
				}
				if(lastTurn.equals("right")){

					direction = 4;
				}
				if(aiCheckCollisoin()){
					if(lastTurn.equals("left")){

						direction = 4;
					}
					if(lastTurn.equals("right")){

						direction = 3;
					}
				}
				if(aiCheckCollisoin()){

					direction = 1;

				}
			}
			if(ThreadsController.directionSnake == 3){
				if(lastTurn.equals("left")){

					direction = 1;
				}
				if(lastTurn.equals("right")){

					direction = 2;
				}
				if(aiCheckCollisoin()){
					if(lastTurn.equals("left")){

						direction = 2;
					}
					if(lastTurn.equals("right")){

						direction = 1;
					}
				}
				if(aiCheckCollisoin()){

					direction = 4;

				}
			}
			if(ThreadsController.directionSnake == 4){
				if(lastTurn.equals("left")){

					direction = 2;
				}
				if(lastTurn.equals("right")){

					direction = 1;
				}
				if(aiCheckCollisoin()){
					if(lastTurn.equals("left")){

						direction = 1;
					}
					if(lastTurn.equals("right")){

						direction = 2;
					}
				}
				if(aiCheckCollisoin()){

					direction = 3;

				}

			}

			System.out.println(direction);
		}
		goDir();
	 }
	 private void goDir() {
	 	int oldDirection = ThreadsController.directionSnake;

			 switch(direction){
				 case 1:	// -> Right
					 //if it's not the opposite direction
					 if(ThreadsController.directionSnake!=2)
						 ThreadsController.directionSnake=1;
					 break;
				 case 2: 	// -> Left
					 if(ThreadsController.directionSnake!=1)
						 ThreadsController.directionSnake=2;
					 break;
				 case 3:	// -> Top
					 if(ThreadsController.directionSnake!=4)
						 ThreadsController.directionSnake=3;
					 break;
				 case 4:	// -> Bottom
					 if(ThreadsController.directionSnake!=3)
						 ThreadsController.directionSnake=4;
					 break;
				 default: 	break;

			 }
		if(oldDirection != ThreadsController.directionSnake){
			if(oldDirection == 1){
				if(ThreadsController.directionSnake == 3){
					lastTurn = "left";
				}
				if(ThreadsController.directionSnake == 4){
					lastTurn = "right";
				}
			}
			if(oldDirection == 2){
				if(ThreadsController.directionSnake == 4){
					lastTurn = "left";
				}
				if(ThreadsController.directionSnake == 3){
					lastTurn = "right";
				}
			}
			if(oldDirection == 3){
				if(ThreadsController.directionSnake == 2){
					lastTurn = "left";
				}
				if(ThreadsController.directionSnake == 1){
					lastTurn = "right";
				}
			}
			if(oldDirection == 4){
				if(ThreadsController.directionSnake == 1){
					lastTurn = "left";
				}
				if(ThreadsController.directionSnake == 2){
					lastTurn = "right";
				}
			}
		}
		 checkCollision();
}

	 
	 //delay between each move of the snake
	 private void pauser(){
		 try {
				sleep(speed);
		 } catch (InterruptedException e) {
				e.printStackTrace();
		 }
	 }
	 
	 //Checking if the snake bites itself or is eating
	 private void checkCollision() {
		 Tuple posCritique = positions.get(positions.size()-1);
		 for(int i = 0;i<=positions.size()-2;i++){
			 boolean biteItself = posCritique.getX()==positions.get(i).getX() && posCritique.getY()==positions.get(i).getY();
			 if(biteItself){
				stopTheGame();
			 }
		 }

		 
		 boolean eatingFood = posCritique.getX()==foodPosition.y && posCritique.getY()==foodPosition.x;
		 if(eatingFood){

			 score ++;
			 System.out.println("Score: "+ score);
			 sizeSnake=sizeSnake+1;
			 	foodPosition = getValAleaNotInSnake();

			 spawnFood(foodPosition);	
		 }
	 }
	 private boolean aiCheckCollisoin(){
		 Tuple posCritique = positions.get(positions.size()-1);

		 switch(direction){
			 case 1:if(headSnakePos.x+1 > 19){
			 	return true;
			 }
			 	break;
			 case 2:if(headSnakePos.x-1<0){
				 return true;
			 }
			 	break;
			 case 3:if(headSnakePos.y-1<0){
				 return true;
			 }
				 break;
			 case 4:if(headSnakePos.y+1>19){
				 return true;
			 }
				 break;
		 }
		 for(int i = 0;i<=positions.size()-2;i++){
		 	boolean biteItself;

				if(direction == 1) {
				 biteItself = posCritique.getX() + 1 == positions.get(i).getX() && posCritique.getY() == positions.get(i).getY();
				 if (biteItself)
					 return true;
			 }
			 else if(direction == 2) {
				 biteItself = posCritique.getX() - 1 == positions.get(i).getX() && posCritique.getY() == positions.get(i).getY();
				 if (biteItself)
					 return true;
			 }
			 else if(direction == 3){
				 biteItself = posCritique.getX()==positions.get(i).getX() && posCritique.getY() - 1 == positions.get(i).getY();
				 if(biteItself)
					 return true;
			 }
			 else if(direction == 4) {
				 biteItself = posCritique.getX() == positions.get(i).getX() && posCritique.getY() + 1 == positions.get(i).getY();
				 if (biteItself)
					 return true;
			 }
		 }
		 return false;
	 }
	 
	 //Stops The Game
	 private void stopTheGame(){
		 System.out.println("COLISION! \n");
		 while(true){
			 pauser();
		 }
	 }
	 
	 //Put food in a position and displays it
	 private void spawnFood(Tuple foodPositionIn){
		 	Squares.get(foodPositionIn.x).get(foodPositionIn.y).lightMeUp(1);
	 }
	 
	 //return a position not occupied by the snake
	 private Tuple getValAleaNotInSnake(){
		 Tuple p ;
		 randomY = (int)(Math.random()*(Window.height));;
		 randomX = (int)(Math.random()*(Window.width));
		 p=new Tuple(randomY,randomX);
		 for(int i = 0;i<=positions.size()-1;i++){
			 if(p.getY()==positions.get(i).getX() && p.getX()==positions.get(i).getY()){
				 randomY= 0 + (int)(Math.random()*Window.height);
				 randomX= 0 + (int)(Math.random()*Window.width);
				 p=new Tuple(randomY,randomX);
				 i=0;
			 }
		 }
		 return p;
	 }
	 
	 //Moves the head of the snake and refreshes the positions in the arraylist
	 //1:right 2:left 3:top 4:bottom 0:nothing
	 private void moveInterne(int dir){
		 switch(dir){
		 	case 4:
		 		if(headSnakePos.y+1>19){
		 			stopTheGame();
				}
				 headSnakePos.ChangeData(headSnakePos.x,(headSnakePos.y+1)%20);
				 positions.add(new Tuple(headSnakePos.x,headSnakePos.y));
		 		break;
		 	case 3:
		 		if(headSnakePos.y-1<0){
		 			 stopTheGame();
		 		 }
		 		else{
				 headSnakePos.ChangeData(headSnakePos.x,Math.abs(headSnakePos.y-1)%20);
		 		}
				 positions.add(new Tuple(headSnakePos.x,headSnakePos.y));
		 		break;
		 	case 2:
		 		 if(headSnakePos.x-1<0){
		 			 stopTheGame();
		 		 }
		 		 else{
		 			 headSnakePos.ChangeData(Math.abs(headSnakePos.x-1)%20,headSnakePos.y);
		 		 } 
		 		positions.add(new Tuple(headSnakePos.x,headSnakePos.y));

		 		break;
		 	case 1:
				if(headSnakePos.x+1>19){
					stopTheGame();
				}
				 headSnakePos.ChangeData(Math.abs(headSnakePos.x+1)%20,headSnakePos.y);
				 positions.add(new Tuple(headSnakePos.x,headSnakePos.y));
		 		 break;
		 }
	 }
	 
	 //Refresh the squares that needs to be 
	 private void moveExterne(){
		 for(Tuple t : positions){
			 int y = t.getX();
			 int x = t.getY();
			 Squares.get(x).get(y).lightMeUp(0);
			 
		 }
	 }
	 
	 //Refreshes the tail of the snake, by removing the superfluous data in positions arraylist
	 //and refreshing the display of the things that is removed
	 private void deleteTail(){
		 int cmpt = sizeSnake;
		 for(int i = positions.size()-1;i>=0;i--){
			 if(cmpt==0){
				 Tuple t = positions.get(i);
				 Squares.get(t.y).get(t.x).lightMeUp(2);
			 }
			 else{
				 cmpt--;
			 }
		 }
		 cmpt = sizeSnake;
		 for(int i = positions.size()-1;i>=0;i--){
			 if(cmpt==0){
				 positions.remove(i);
			 }
			 else{
				 cmpt--;
			 }
		 }
	 }
}
