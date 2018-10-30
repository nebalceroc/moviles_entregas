package com.example.nicolas.reto8_tictactoeonline;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;

import java.util.Random;

import javax.xml.transform.ErrorListener;

public class TicTacToeGame {

    private char mBoard[] = {'1','2','3','4','5','6','7','8','9'};
    public static final int BOARD_SIZE = 9;

    public char iam = ' ';
    public int turn = 1;
    public int id =-1;
    public boolean gameOver = false;
    public boolean full = false;
    //HOST
    public static final char HUMAN_PLAYER = 'X';
    //GUEST
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';

    //private Random mRand;

    //public enum DifficultyLevel {Easy, Harder, Expert};

    //private DifficultyLevel mDifficultyLevel = DifficultyLevel.Easy;

    public TicTacToeGame() {

        // Seed the random number generator
//        mRand = new Random();
//
//        char turn = HUMAN_PLAYER;    // Human starts first
//        int  win = 0;                // Set to 1, 2, or 3 when game is over

//        // Keep looping until someone wins or a tie
//        while (win == 0)
//        {
//            displayBoard();
//
//            if (turn == HUMAN_PLAYER)
//            {
//                getUserMove();
//                turn = COMPUTER_PLAYER;
//            }
//            else
//            {
//                getComputerMove();
//                turn = HUMAN_PLAYER;
//            }
//
//            win = checkForWinner();
//        }
//
//        displayBoard();
//
//        // Report the winner
//        System.out.println();
//        if (win == 1)
//            System.out.println("It's a tie.");
//        else if (win == 2)
//            System.out.println(HUMAN_PLAYER + " wins!");
//        else if (win == 3)
//            System.out.println(COMPUTER_PLAYER + " wins!");
//        else
//            System.out.println("There is a logic problem!");
    }

    public char[] getBoardState(){
        return mBoard;
    }

    public String getBoardStr() {
        String out = "";
        for(int x = 0; x < mBoard.length; x++){
            out=out+String.valueOf(mBoard[x]);
        }
        return out;
    }

    public void setBoardState(char[] _board){
        mBoard = _board.clone();
    }

    public void setmBoardStateFromStr(String str){
        for(int x = 0; x < mBoard.length; x++){
            switch (str.charAt(x)){
                case 'E':
                    mBoard[x] = OPEN_SPOT;
                    break;
                case ' ':
                    mBoard[x] = OPEN_SPOT;
                    break;
                case 'H':
                    mBoard[x] = COMPUTER_PLAYER;
                    break;
                case 'G':
                    mBoard[x] = HUMAN_PLAYER;
                    break;
            }

        }
    }

    private void displayBoard()	{
        System.out.println();
        System.out.println(mBoard[0] + " | " + mBoard[1] + " | " + mBoard[2]);
        System.out.println("-----------");
        System.out.println(mBoard[3] + " | " + mBoard[4] + " | " + mBoard[5]);
        System.out.println("-----------");
        System.out.println(mBoard[6] + " | " + mBoard[7] + " | " + mBoard[8]);
        System.out.println();
    }

    // Check for a winner.  Return
    //  0 if no winner or tie yet
    //  1 if it's a tie
    //  2 if X won
    //  3 if O won
//    public int checkForWinner() {
//
//        // Check horizontal wins
//        for (int i = 0; i <= 6; i += 3)	{
//            if (mBoard[i] == HUMAN_PLAYER &&
//                    mBoard[i+1] == HUMAN_PLAYER &&
//                    mBoard[i+2]== HUMAN_PLAYER)
//                return 2;
//            if (mBoard[i] == COMPUTER_PLAYER &&
//                    mBoard[i+1]== COMPUTER_PLAYER &&
//                    mBoard[i+2] == COMPUTER_PLAYER)
//                return 3;
//        }
//
//        // Check vertical wins
//        for (int i = 0; i <= 2; i++) {
//            if (mBoard[i] == HUMAN_PLAYER &&
//                    mBoard[i+3] == HUMAN_PLAYER &&
//                    mBoard[i+6]== HUMAN_PLAYER)
//                return 2;
//            if (mBoard[i] == COMPUTER_PLAYER &&
//                    mBoard[i+3] == COMPUTER_PLAYER &&
//                    mBoard[i+6]== COMPUTER_PLAYER)
//                return 3;
//        }
//
//        // Check for diagonal wins
//        if ((mBoard[0] == HUMAN_PLAYER &&
//                mBoard[4] == HUMAN_PLAYER &&
//                mBoard[8] == HUMAN_PLAYER) ||
//                (mBoard[2] == HUMAN_PLAYER &&
//                        mBoard[4] == HUMAN_PLAYER &&
//                        mBoard[6] == HUMAN_PLAYER))
//            return 2;
//        if ((mBoard[0] == COMPUTER_PLAYER &&
//                mBoard[4] == COMPUTER_PLAYER &&
//                mBoard[8] == COMPUTER_PLAYER) ||
//                (mBoard[2] == COMPUTER_PLAYER &&
//                        mBoard[4] == COMPUTER_PLAYER &&
//                        mBoard[6] == COMPUTER_PLAYER))
//            return 3;
//
//        // Check for tie
//        for (int i = 0; i < BOARD_SIZE; i++) {
//            // If we find a number, then no one has won yet
//            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER)
//                return 0;
//        }
//
//        // If we make it through the previous loop, all places are taken, so it's a tie
//        return 1;
//    }
//
//    private int getRandomMove(){
//        int move;
//        // Generate random move
//        do
//        {
//            move = mRand.nextInt(BOARD_SIZE);
//        } while (mBoard[move] == HUMAN_PLAYER || mBoard[move] == COMPUTER_PLAYER);
//
//        System.out.println("Computer is moving to " + (move + 1));
//
//        mBoard[move] = COMPUTER_PLAYER;
//
//        return move;
//    }
//
//    private int getWinningMove(){
//        // First see if there's a move O can make to win
//        for (int i = 0; i < BOARD_SIZE; i++) {
//            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
//                char curr = mBoard[i];
//                mBoard[i] = COMPUTER_PLAYER;
//                if (checkForWinner() == 3) {
//                    System.out.println("Computer is moving to " + (i + 1));
//                    return i;
//                }
//                else
//                    mBoard[i] = curr;
//            }
//        }
//        return -1;
//    }
//
//    private int getBlockingMove(){
//        // See if there's a move O can make to block X from winning
//        for (int i = 0; i < BOARD_SIZE; i++) {
//            if (mBoard[i] != HUMAN_PLAYER && mBoard[i] != COMPUTER_PLAYER) {
//                char curr = mBoard[i];   // Save the current number
//                mBoard[i] = HUMAN_PLAYER;
//                if (checkForWinner() == 2) {
//                    mBoard[i] = COMPUTER_PLAYER;
//                    System.out.println("Computer is moving to " + (i + 1));
//                    return i;
//                }
//                else
//                    mBoard[i] = curr;
//            }
//        }
//        return -1;
//    }
//
//    public int getComputerMove(){
//        int move = -1;
//        if(mDifficultyLevel == DifficultyLevel.Easy){
//            move = getRandomMove();
//        } else if(mDifficultyLevel == DifficultyLevel.Harder){
//            move = getWinningMove();
//            if (move == -1){
//                move = getRandomMove();
//            }
//        } else if(mDifficultyLevel == DifficultyLevel.Expert){
//            move = getWinningMove();
//            if (move == -1){
//                move = getBlockingMove();
//            }
//            if (move == -1){
//                move = getRandomMove();
//            }
//        }
//        return move;
//    }

    public void clearBoard(){
        for(int x = 0; x < mBoard.length; x++){
            mBoard[x] = OPEN_SPOT;
        }

    }

    public int getBoardOcupant(int pos){
        return this.mBoard[pos];
    }

    public void setMove(Context context, char player, int location, Response.Listener<String> response_listener, Response.ErrorListener errorListener){
        GameAPIService game_service = new GameAPIService(context);
        game_service.set_move(this.id,player,location, response_listener, errorListener);
    }


//    public DifficultyLevel getmDifficultyLevel() {
//        return mDifficultyLevel;
//    }
//
//    public void setmDifficultyLevel(DifficultyLevel mDifficultyLevel) {
//        this.mDifficultyLevel = mDifficultyLevel;
//    }
//
//    public void setmIntDifficultyLevel(int mDifficultyLevel) {
//        this.mDifficultyLevel = DifficultyLevel.values()[mDifficultyLevel];
//    }
}
