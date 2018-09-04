package com.example.nicolas.reto4_tictactoeplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class TicTacToePlusActivity extends AppCompatActivity {

    private TicTacToeGame mGame;
    private Button mBoardButtons[];
    private TextView mInfoTextView;
    private TextView mScoreTextView;
    private boolean mGameOver;
    private int human_wins;
    private int android_wins;
    private int ties;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 2;
    static final int DIALOG_ABOUT_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_plus);

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

    @Override
    protected Dialog onCreateDialog(int id){
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id){
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                int selected = 1;
                builder.setSingleChoiceItems(levels, mGame.getmDifficultyLevel().ordinal(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();
                                mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.valueOf(String.valueOf(levels[item])));
                                Toast.makeText(getApplicationContext(), levels[item],Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_QUIT_ID:
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                TicTacToePlusActivity.this.finish();

                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
            case DIALOG_ABOUT_ID:
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog,null);
                builder.setView(layout);
                builder.setPositiveButton("Ok",null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    private void updateScore(){
        mScoreTextView.setText("Score H:"+String.valueOf(human_wins)+" T:"+String.valueOf(ties)+" A:"+String.valueOf(android_wins));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
            case R.id.about:
                showDialog(DIALOG_ABOUT_ID);
        }
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

        }
        mInfoTextView.setText(R.string.turn_human);
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
