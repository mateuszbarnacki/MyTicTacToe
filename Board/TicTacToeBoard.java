package TicTacToe.Board;

// This class contains the tic tac toe board.
// It stores O as 1 and X as 2. The blank spaces
// are equal to zero.
public class TicTacToeBoard {
    // Variable saves the space in memory for data.
    private final short[] board = new short[9];
    // Counter counts the number of written fields.
    private int counter = 0;

    // Constructor create the "blank" game board.
    public TicTacToeBoard(){
        for(int i = 0; i < 9; ++i){
            this.board[i] = 0;
        }
    }

    // Getter for the counter
    public int getCounter(){
        return counter;
    }

    // We pass to this method coordinates information and the value of O or X.
    // We get the information about the succeed of the process.
    public boolean changeFieldValue(int firstCoordinate, int secondCoordinate, short number){
        if(board[3*secondCoordinate + firstCoordinate] == 0){
            board[3*secondCoordinate + firstCoordinate] = number;
            // If the value is written, we increment the counter.
            counter++;
            return true;
        }
        return false;
    }

    // This function return the information about the end of the game.
    public boolean isEndOfTheGame(){
        boolean isEnd = false;
        int counter = 0;

        // Check all raws.
        for(int type = 1; type <= 2; ++type) {
            for (int y = 0; y < 3; ++y) {
                for (int x = 0; x < 3; ++x) {
                    if(board[y*3+x] == type){
                        counter++;
                    }
                }
                if (counter == 3){
                    isEnd = true;
                }
                counter = 0;
            }
        }

        // Check all columns.
        for(int type = 1; type <= 2; ++type) {
            for (int x = 0; x < 3; ++x) {
                for (int y = 0; y < 3; ++y) {
                    if(board[y*3+x] == type){
                        counter++;
                    }
                }
                if (counter == 3){
                    isEnd = true;
                }
                counter = 0;
            }
        }

        // Check diagonals.
        if((board[0] == board[4]) && (board[0] == board[8]) && (board[0] != 0)){
            isEnd = true;
        }

        if((board[2] == board[4]) && (board[2] == board[6]) && (board[2] != 0)){
            isEnd = true;
        }

        return isEnd;
    }

}
