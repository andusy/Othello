/* 
 File Name: Othello.java
 Name: Andus Yu
 Class: ICS3U1
 Date: January, 14, 2014
 Description: This class represents a Othello (TM)
  game, which allows two players to place
  pieces onto a board.  Each move can 
  result in outflanking 0 or more opponent's 
  piece.
 Input:Mouse Clicks
*/

    public class Othello {
   
      final int EMPTY = -1;
      final int NUMPLAYER;
      final int NUMROW;
      final int NUMCOL;
      final int MAXGAME;
      final int PLAYER1 = 0;
      final int PLAYER2 = 1;
      final int TIE = -1;
   
      OthelloGUI gui;
      int numMove;
      int curPlayer;
      int board[][];
      int score[];
		//Number of wins each player has
   	int [] player_win;
   /**
   * Constructor:  Othello
   */
       public Othello(OthelloGUI gui) {
         this.gui = gui;
         NUMPLAYER = gui.NUMPLAYER;
         NUMROW = gui.NUMROW;
         NUMCOL = gui.NUMCOL;
         MAXGAME = gui.MAXGAME;
      
         board = new int [NUMROW][NUMCOL];
         score = new int [NUMPLAYER]; 
			//Sets the scores to the original values
			score[0] = 2;
			gui.setPlayerScore(PLAYER1, score[0]);
			score[1] = 2;
			gui.setPlayerScore(PLAYER2, score[1]);
			//Initializes the board
         initBoard(); 
			
			player_win = new int [NUMPLAYER];     
      }
		   
       public void play (int row, int column) {
         boolean validmove = false;
         int outflank = 0;
         int winner;
      
      //Checks to see if the square the user clicked is a valid move
         validmove = validMove (row, column);
      //If it is a valid move the piece will be placed on the square
         if (validmove){
            board[row][column] = curPlayer;
         	
         	//Checks for the total number of outflanked opponents including vertical, horizontal, 
         	//And diagonal outflanks. Also changes the outflanked pieces
            outflank += outFlankHori (row, column, curPlayer);
            outflank += outFlankVert (row, column, curPlayer);
				outflank += outFlankDiag (row, column, curPlayer);
         //Displays the message showing the number of opponents outflanked
            if (outflank > 0){
               gui.showOutflankMessage (curPlayer, outflank);
            }
         //Changes the turn of the players (Only changes turns if it is a valid move)
            if (curPlayer == 0){
               curPlayer = 1;
            } 
            else{
               curPlayer = 0;
            }   
         } 
         else if (!validmove) {
         //Displays an invalid move message
            gui.showInvalidMoveMessage ();
         }
      	
      	//Changes the icon for the next player's turn
         gui.setNextPlayer(curPlayer);
      	
			score[0] = 0;
			score[1] = 0;
			
      	//Displayes all the changed piece
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               if (board[i][j] == PLAYER1){
                  gui.setPiece (i,j,PLAYER1);
						score[0]++;
               } 
               else if (board[i][j] == PLAYER2){
                  gui.setPiece (i,j,PLAYER2);
						score[1]++;
               }
            }
         }	
         
         //Checks for the winner of the game
         winner = checkWinner(board);
         
         //Displays a pop-up window of the winner of the game
         if (winner == PLAYER1){
            gui.showWinnerMessage (PLAYER1);
            player_win[0] ++;
            initBoard ();
            //Resets the score after the board has been initialized
            score[0] = 2;
            score[1] = 2;
         } else if (winner == PLAYER2){
            gui.showWinnerMessage (PLAYER2);
            player_win[1] ++;
            initBoard ();
            //Resets the score after the board has been initialized
            score[0] = 2;
            score[1] = 2;
         } else if (winner == TIE){
            gui.showTieGameMessage ();
            initBoard ();
            //Resets the score after the board has been initialized
            score[0] = 2;
            score[1] = 2;
         }
         
         //Displays a pop-up window of the winner of the match
         if (player_win[0] == MAXGAME){
            gui.showFinalWinnerMessage (PLAYER1);
         }else if (player_win[1] == MAXGAME){
            gui.showFinalWinnerMessage (PLAYER2);
         }
         
         //Updates the score of the 2 players
			gui.setPlayerScore (PLAYER1, score[0]);
			gui.setPlayerScore (PLAYER2, score[1]);
         
      } 
   
   //Method that initializes the game board to the initial set up
       public void initBoard (){
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               board [i][j] = EMPTY;
            }  
         } 
      	
         //Clears all of the pieces on the board
         gui.resetGameBoard ();
         
      	//Places player 1's pieces
         gui.setPiece (4,3,PLAYER1);
         gui.setPiece (3,4,PLAYER1);
         board[4][3] = PLAYER1;
         board[3][4] = PLAYER1;
      	
      	//Places player 2's pieces
         gui.setPiece (3,3,PLAYER2);
         gui.setPiece (4,4,PLAYER2);  
         board[3][3] = PLAYER2;
         board[4][4] = PLAYER2;
      }
   
   //Method that checks if it is a valid move
       public boolean validMove (int row, int column){
         int rowstart = row - 1;
         int rowend = row + 1;
         int colstart = column -1;
         int colend = column + 1;
      	
      //Checks to see if there was already a piece on the selected row and column      
         if (board[row][column] != EMPTY){
            return false;
         }   
      
      //Checks for the rows that would make the array go out of bounds
         if (row == 0){
            rowstart = 0;
         } 
         else if (row == NUMROW - 1){
            rowend = NUMROW - 1;
         }
      //Checks for the columns that would make the array go out of bounds
         if (column == 0){
            colstart = 0;
         } 
         else if (column == NUMCOL - 1){
            colend = NUMCOL - 1;
         }
      //Returns true if there is atleast 1 piece within the 8 spaces around the selected space
         for (int i = rowstart ; i <= rowend; i++){
            for (int j = colstart; j <= colend ; j++){
               if (board[i][j] != EMPTY){
                  return true;
               }
            }
         }
      	//Returns false if none of the conditions above are satisfied
         return false;
      }
   
   //Method that checks for horizontal outflanks
       public int outFlankHori (int row, int column, int curPlayer){
         int piece1 = column;//The first piece to be changed in the row
         int piece2 = column;//The last piece to be changed in the row
         boolean empty = false;
         boolean piecefound = false;
         int count = column - 1;
         int outflank = 0;
         int [][] tempboard = new int [NUMROW][NUMCOL];
      	
      	//Assigns the values of tempboard
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               tempboard[i][j] = board[i][j];
            }
         }
      	
      	//Checks for the first piece to be changed in the row
         while (count >= 0 && empty == false && piecefound == false){
         	//If the space is empty the loop ends
            if (board[row][count] == EMPTY){
               empty = true;
            }
         	//Checks for the first piece that matches the current player's piece
            if (board[row][count] == curPlayer){
               piece1 = count;
               piecefound = true;
            }
            count --;
         }
      	
         piecefound = false;
         empty = false;
         count = column + 1;
      	
      	//Checks for the last piece to be changed in the row
         while (count < NUMCOL && empty == false && piecefound == false){
         //The loops ends if the current space being checked is empty
            if (board[row][count] == EMPTY){
               empty = true;
            }
         	//Checks for the first piece that matches the current player's piece
            if (board[row][count] == curPlayer){
               piece2 = count;
               piecefound = true;
            }
            count ++;
         }
      	
      	//Changes all the pieces in the row between piece1 and piece2 to the 
      	//current player's piece
         for (int i = piece1; i <= piece2; i++){
            board[row][i] = curPlayer;
         }
      	
      	//Checks for the number of outflanked pieces
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               if (board[i][j] != tempboard[i][j]){
                  outflank++;
               }
            }
         }   
         return outflank;	
      }
   
   //Method that checks for vertical outflanks
       public int outFlankVert (int row, int column, int curPlayer){
         int piece1 = row;//The first piece to be changed in the column
         int piece2 = row;//The last piece to be changed in the column
         boolean empty = false;
         boolean piecefound = false;
         int count = row - 1;
         int outflank = 0;
         int [][] tempboard = new int [NUMROW][NUMCOL];
      	
      	//Assigns the values of tempboard
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               tempboard[i][j] = board[i][j];
            }
         }
      	
      	//Checks for the first piece to be changed in the column
         while (count >= 0 && empty == false && piecefound == false){
         	//If the space is empty the loop ends
            if (board[count][column] == EMPTY){
               empty = true;
            }
         	//Checks for the first piece that matches the current player's piece
            if (board[count][column] == curPlayer){
               piece1 = count;
               piecefound = true;
            }
            count --;
         }
      	
         piecefound = false;
         empty = false;
         count = row + 1;
      	
      	//Checks for the last piece to be changed in the column
         while (count < NUMROW && empty == false && piecefound == false){
         //The loops ends if the current space being checked is empty
            if (board[count][column] == EMPTY){
               empty = true;
            }
         	//Checks for the first piece that matches the current player's piece
            if (board[count][column] == curPlayer){
               piece2 = count;
               piecefound = true;
            }
            count ++;
         }  	
      	//Changes all the pieces in the column between piece1 and piece2 to the 
      	//current player's piece
         for (int i = piece1; i <= piece2; i++){
            board[i][column] = curPlayer;
         }
      	
      	//Checks for the number of outflanked pieces
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               if (board[i][j] != tempboard[i][j]){
                  outflank++;
               }
            }
         }  	            	
         return outflank;
      }  
   
   //Method that checks for diagonal outflanks
       public int outFlankDiag (int row, int column, int curPlayer){
         int piece1_row = row;
         int col = column;
         int piece2_row = row;
			int outflank = 0;
         boolean empty = false;
         boolean piecefound = false;
   		int [][] tempboard = new int [NUMROW][NUMCOL];
      	
      	//Assigns the values of tempboard
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               tempboard[i][j] = board[i][j];
            }
         }
    		
         int count_row = row - 1;
			int count_col = column - 1;
       	
			//Checks for the first piece to be changed in the diagonal starting from the top left
			//Hand corner to the bottom right hand corner
			while (count_row >= 0 && count_col >= 0 && piecefound == false && empty == false){
			if (board[count_row][count_col] == curPlayer){
				piece1_row = count_row;
				col = count_col;
				piecefound = true;
			} else if (board[count_row][count_col] == EMPTY){
				empty = true;
			}
			count_row --;
			count_col --;
			}
       	
			count_row = row + 1;
			count_col = column + 1;
			empty = false;
			piecefound = false;
			
			//Checks for the second piece to be changed in the diagonal starting from the top left
			//Hand corner to the bottom right hand corner
			while (count_row < NUMROW && count_col < NUMCOL && piecefound == false && empty == false){
			if (board[count_row][count_col] == curPlayer){
				piece2_row = count_row;
				piecefound = true;
			} else if (board[count_row][count_col] == EMPTY){
				empty = true;
			}
			count_row ++;
			count_col ++;
			}
			
			//Changes the outflanked pieces
			for (int i = piece1_row; i < piece2_row; i++){
				 board[i][col] = curPlayer;
				 col++;
				
			}
			
			//Initializes the variables again in order to check for the diagonal going from the 
			//Top right to the bottom left corners
         count_row = row - 1;
			count_col = column + 1;
			empty = false;
			piecefound = false;
         
         piece1_row = row;
         col = column;
         piece2_row = row;
         
         //Checks for the first piece to be changed in the diagonal starting from the top right
			//Hand corner to the bottom left hand corner
			while (count_row >= 0 && count_col < NUMCOL && piecefound == false && empty == false){
			if (board[count_row][count_col] == curPlayer){
				piece1_row = count_row;
				col = count_col;
				piecefound = true;
			} else if (board[count_row][count_col] == EMPTY){
				empty = true;
			}
			count_row --;
			count_col ++;
			}
    		
			count_row = row + 1;
			count_col = column - 1;
			empty = false;
			piecefound = false;
			
			//Checks for the second piece to be changed in the diagonal starting from the top right
			//Hand corner to the bottom left hand corner
			while (count_row < NUMROW && count_col >= 0 && piecefound == false && empty == false){
			if (board[count_row][count_col] == curPlayer){
				piece2_row = count_row;
				piecefound = true;
			} else if (board[count_row][count_col] == EMPTY){
				empty = true;
			}
			count_row ++;
			count_col --;
			}
			
			//Changes the outflanked pieces
			for (int i = piece1_row; i < piece2_row; i++){
				 board[i][col] = curPlayer;
				 col--;
				
			}
         
			//Checks for the number of outflanked pieces
         for (int i = 0; i < NUMROW; i++){
            for (int j = 0; j < NUMCOL; j++){
               if (board[i][j] != tempboard[i][j]){
                  outflank++;
               }
            }
         } 
			return outflank;
      }
     
     //Method that checks for the winner
     public int checkWinner(int board[][]){
     int incomp_board = -2;//The integer returned if the board is not yet complete
     int player1_score = 0;
     int player2_score = 0;
     
     //Checks to see if the board has been completed     
     for (int i = 0; i < NUMROW; i++){
      for (int j = 0; j < NUMCOL; j++){
         if (board[i][j] == EMPTY){
            return incomp_board;
            //If the board has been completed, the scores of the two players will add up
         } else if (board[i][j] == PLAYER1){
            player1_score ++;
         } else if (board[i][j] == PLAYER2){
            player2_score ++;
         }
      }
     }
     
     //Checks for the winner of the game
     if (player1_score > player2_score){
      return PLAYER1;
     } else if (player2_score > player1_score){
      return PLAYER2;
     } else if (player1_score == player2_score){
      return TIE;
     }
     
     return incomp_board;
     }  
     
   }