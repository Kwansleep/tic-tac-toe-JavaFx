package ca.cmpt213.asn4.tictactoe.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Board
 *
 * represents a board of tic tac toe
 */
public class Board {

    // local variables
    private final int BOARD_SIZE;
    private int[][] field;

    // variables for checking game-state
    private List<Integer> rowCounter;
    private List<Integer> columnCounter;
    private int majorDiagonalCounter;
    private int minorDiagonalCounter;
    private int moveCounter;

    public Board(int size){
        BOARD_SIZE = size;
        makeField();
        resetCounters();
        checkCondition();
    }

    // set up/reset functions
    private void makeField(){
        field = new int[BOARD_SIZE][BOARD_SIZE];
    }
    private void resetCounters(){
        // initialize to zero
        rowCounter = new ArrayList<>();
        columnCounter = new ArrayList<>();
        for(int i = 0; i < BOARD_SIZE;i++){
            rowCounter.add(0);
            columnCounter.add(0);
        }
        majorDiagonalCounter = 0;
        minorDiagonalCounter = 0;
        moveCounter = 0;
    }
    public void reset(){
        makeField();
        resetCounters();
    }

    public boolean makeAMove(int row,int col,Player player){
        if(!validMove(row,col)){
            //System.out.println("Debug: invalid move");
            return false;
        }

        switch(player){
            case ONE:
                field[row][col] = 1;
                updateCounter(row,col,1);
                return true;
            case TWO:
                field[row][col] = -1;
                updateCounter(row,col,-1);
                return true;
            default:
                throw new IllegalArgumentException("unknown player");
        }
    }
    // Sub functions for makeAMove
    private boolean validMove(int row,int col){
        // is on field
        if(row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE)
            return false;
        // has not been moved on
        if(field[row][col] != 0){
            return false;
        }
        return true;
    }
    private void updateCounter(int row,int col,int value){
        rowCounter.set(row,rowCounter.get(row) + value);
        columnCounter.set(col,columnCounter.get(col) + value);
        if(row == col){
            majorDiagonalCounter += value;
        }
        if(row == BOARD_SIZE - col - 1){
            minorDiagonalCounter += value;
        }

        moveCounter++;
    }

    public GameState checkCondition(){
        //System.out.println("colCount:"+columnCounter.toString());
        //System.out.println("rowCount:"+rowCounter.toString());
        //System.out.println(moveCounter + " , size:" + BOARD_SIZE );

        if(p1Wins()){
            return GameState.WIN_P1;
        } else if (p2Wins()){
            return  GameState.WIN_P2;
        } else if (moveCounter == (BOARD_SIZE * BOARD_SIZE)){
            return GameState.DRAW;
        } else {
            return GameState.PLAYING;
        }
    }
    // sub functions for checkCondition
    private boolean p1Wins(){
        return rowCounter.contains(BOARD_SIZE)
                || columnCounter.contains(BOARD_SIZE)
                || majorDiagonalCounter == BOARD_SIZE
                || minorDiagonalCounter == BOARD_SIZE;
    }
    private boolean p2Wins() {
        return rowCounter.contains(-BOARD_SIZE)
                || columnCounter.contains(-BOARD_SIZE)
                || majorDiagonalCounter == -BOARD_SIZE
                || minorDiagonalCounter == -BOARD_SIZE;
    }

    // enums
    public enum GameState {PLAYING,WIN_P1,WIN_P2,DRAW}
    public enum Player {ONE,TWO}

    // Debug function
    public void debugPrint(){
        System.out.println("printing field");
        for(int row = 0; row < BOARD_SIZE;row++){
            for(int col = 0; col <BOARD_SIZE; col++){
                System.out.print(field[row][col] + " ");
            }
            System.out.print('\n');
        }
    }
}
