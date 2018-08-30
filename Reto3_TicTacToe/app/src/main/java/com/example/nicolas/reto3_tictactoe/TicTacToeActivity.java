package com.example.nicolas.reto3_tictactoe;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class TicTacToeActivity extends AppCompatActivity {

    private TicTacToeGame mGame;
    private Button mBoardButtons[];
    private TextView mInfoTextView;
    private TextView mScoreTextView;
    private boolean mGameOver;
    private int human_wins;
    private int android_wins;
    private int ties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.b1);
        mBoardButtons[1] = (Button) findViewById(R.id.b2);
        mBoardButtons[2] = (Button) findViewById(R.id.b3);
        mBoardButtons[3] = (Button) findViewById(R.id.b4);
        mBoardButtons[4] = (Button) findViewById(R.id.b5);
        mBoardButtons[5] = (Button) findViewById(R.id.b6);
        mBoardButtons[6] = (Button) findViewById(R.id.b7);
        mBoardButtons[7] = (Button) findViewById(R.id.b8);
        mBoardButtons[8] = (Button) findViewById(R.id.b9);

        mInfoTextView = (TextView) findViewById(R.id.info_text);
        mScoreTextView = (TextView) findViewById(R.id.score_text);

        mGame = new TicTacToeGame();
        mGameOver = false;

        human_wins = 0;
        android_wins = 0;
        ties = 0;

        updateScore();

        startNewGame();
    }

    private void updateScore(){
        mScoreTextView.setText("Score H:"+String.valueOf(human_wins)+" T:"+String.valueOf(ties)+" A:"+String.valueOf(android_wins));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        startNewGame();
        return true;
    }

    private void startNewGame() {
        mGame.clearBoard();
        mGameOver = false;
        for(int i =0; i <mBoardButtons.length; i++){
            mBoardButtons[i].setText(" ");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        Random rand = new Random();
        int n = rand.nextInt(100);
        if(n < 50){
            mInfoTextView.setText(R.string.turn_computer);
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            mInfoTextView.setText(R.string.first_human);
        }else{
            mInfoTextView.setText(R.string.turn_human);
        }
    }

    private class ButtonClickListener implements View.OnClickListener {

        int location;

        public ButtonClickListener(int location){
            this.location = location;
        }

        @Override
        public void onClick(View v) {
            if(mBoardButtons[location].isEnabled() && !mGameOver){
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                int winner = mGame.checkForWinner();
                if (winner == 0){
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if(winner == 0){
                    mInfoTextView.setText(R.string.turn_human);
                } else if(winner == 1) {
                    mInfoTextView.setText(R.string.result_tie);
                    mGameOver = true;
                    ties+=1;
                    updateScore();
                } else if (winner == 2){
                    mInfoTextView.setText(R.string.result_human_wins);
                    mGameOver = true;
                    human_wins+=1;
                    updateScore();
                } else {
                    mInfoTextView.setText(R.string.result_android_wins);
                    mGameOver = true;
                    android_wins+=1;
                    updateScore();
                }
            }
        }
    }

    private void setMove(char player, int location){
        mGame.setMove(player,location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER) {
            mBoardButtons[location].setTextColor(Color.rgb(0,200,0));
        } else {
            mBoardButtons[location].setTextColor(Color.rgb(200,0,0));
        }
    }

}
