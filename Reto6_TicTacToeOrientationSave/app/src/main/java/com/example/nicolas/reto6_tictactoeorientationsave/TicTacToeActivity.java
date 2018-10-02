package com.example.nicolas.reto6_tictactoeorientationsave;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private char first;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 2;
    static final int DIALOG_ABOUT_ID = 1;

    private BoardView mBoardView;

    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPLayer;

    private int turn;
    private int d;


    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = getSharedPreferences("ttt_prefs",MODE_PRIVATE);

        human_wins = mPrefs.getInt("human_wins",0);
        android_wins = mPrefs.getInt("android_wins",0);
        ties = mPrefs.getInt("ties",0);
        d = mPrefs.getInt("mdif",1);

        //ties = mPrefs.getInt("ties",0);

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPLayer = MediaPlayer.create(getApplicationContext(), R.raw.computer);

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

        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);
        if(savedInstanceState==null){
            startNewGame();
        }else{
            mGame.setBoardState(savedInstanceState.getCharArray("board"));
            mGameOver = savedInstanceState.getBoolean("mGameOver");
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            //human_wins = savedInstanceState.getInt("human_wins");
            //android_wins = savedInstanceState.getInt("android_wins");
            //ties = savedInstanceState.getInt("ties");
            first = savedInstanceState.getChar("first");
            turn = savedInstanceState.getInt("turn");
        }
        mGame.setmIntDifficultyLevel(d);
        updateScore();
    }

    private void resetScores(){
        ties = 0;
        human_wins = 0;
        android_wins = 0;
        updateScore();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putCharArray("board", mGame.getBoardState());
        outState.putBoolean("mGameOver",mGameOver);
        //outState.putInt("human_wins",human_wins);
        //outState.putInt("android_wins",android_wins);
        //outState.putInt("ties",ties);
        outState.putCharSequence("info", mInfoTextView.getText());
        outState.putChar("first",first);
        outState.putInt("turn",turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        mGame.setBoardState(savedInstanceState.getCharArray("board"));
        mGameOver = savedInstanceState.getBoolean("mGameOver");
        mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
        human_wins = savedInstanceState.getInt("human_wins");
        android_wins = savedInstanceState.getInt("android_wins");
        //ties = savedInstanceState.getInt("ties");
        //first = savedInstanceState.getChar("first");
        //turn = savedInstanceState.getInt("turn");
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

                //int selected = d+1;
                //mGame.setmIntDifficultyLevel(d+1);
                Log.e("D create",String.valueOf(d));
                builder.setSingleChoiceItems(levels, mGame.getmDifficultyLevel().ordinal(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();
                                //mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.valueOf(String.valueOf(levels[item])));
                                mGame.setmIntDifficultyLevel(item);
                                SharedPreferences.Editor ed = mPrefs.edit();
                                ed.putInt("mdif",item);
                                ed.apply();
                                d=item;
                                Log.e("D set",String.valueOf(item));
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
                                TicTacToeActivity.this.finish();

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
    protected void onStop(){
        super.onStop();

        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("human_wins",human_wins);
        ed.putInt("android_wins",android_wins);
        ed.putInt("ties",ties);
        ed.putInt("mdif",d);
        ed.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.reset_scores:
                resetScores();
                return true;
            case R.id.about:
                showDialog(DIALOG_ABOUT_ID);
                return true;
        }
        return true;
    }

    private void startNewGame() {
        mGame.clearBoard();
        mBoardView.invalidate();
        mGameOver = false;
        //for(int i =0; i <mBoardButtons.length; i++){
        //    mBoardButtons[i].setText(" ");
        //    mBoardButtons[i].setEnabled(true);
        //    mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        //}
        Random rand = new Random();
        int n = rand.nextInt(100);
        if(n < 50){
            mInfoTextView.setText(R.string.turn_computer);
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
            first = 'A';
        }else{
            first = 'H';
        }
        mInfoTextView.setText(R.string.turn_human);
        turn = TicTacToeGame.HUMAN_PLAYER;
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

    private boolean setMove(char player, int location){
        if(player==TicTacToeGame.HUMAN_PLAYER){
            mHumanMediaPlayer.start();
        }else{
            mComputerMediaPLayer.start();
        }
        if(mGame.setMove(player,location)){
            mBoardView.invalidate();
            return true;
        }
        return false;
        //mBoardButtons[location].setEnabled(false);
        //mBoardButtons[location].setText(String.valueOf(player));
        //if (player == TicTacToeGame.HUMAN_PLAYER) {
        //    mBoardButtons[location].setTextColor(Color.rgb(0,200,0));
        //} else {
        //    mBoardButtons[location].setTextColor(Color.rgb(200,0,0));
        //}
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(turn == TicTacToeGame.HUMAN_PLAYER) {
                int col = (int) event.getX() / mBoardView.getBoardCelWidth();
                int row = (int) event.getY() / mBoardView.getBoardCelHeight();
                int pos = row * 3 + col;
                if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {
                    int winner = mGame.checkForWinner();
                    if (winner == 0) {
                        mInfoTextView.setText(R.string.turn_computer);
                        turn = TicTacToeGame.COMPUTER_PLAYER;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int move = mGame.getComputerMove();
                                setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                                int winner = mGame.checkForWinner();
                                if (winner == 0) {
                                    mInfoTextView.setText(R.string.turn_human);
                                    turn = TicTacToeGame.HUMAN_PLAYER;
                                } else if (winner == 1) {
                                    mInfoTextView.setText(R.string.result_tie);
                                    mGameOver = true;
                                    ties += 1;
                                    updateScore();
                                } else if (winner == 2) {
                                    mInfoTextView.setText(R.string.result_human_wins);
                                    mGameOver = true;
                                    human_wins += 1;
                                    updateScore();
                                } else {
                                    mInfoTextView.setText(R.string.result_android_wins);
                                    mGameOver = true;
                                    android_wins += 1;
                                    updateScore();
                                }
                            }
                        }, 1000);
                    } else if (winner == 1) {
                        mInfoTextView.setText(R.string.result_tie);
                        mGameOver = true;
                        ties += 1;
                        updateScore();
                    } else if (winner == 2) {
                        mInfoTextView.setText(R.string.result_human_wins);
                        mGameOver = true;
                        human_wins += 1;
                        updateScore();
                    } else {
                        mInfoTextView.setText(R.string.result_android_wins);
                        mGameOver = true;
                        android_wins += 1;
                        updateScore();
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(), "Not your turn!",Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPLayer = MediaPlayer.create(getApplicationContext(), R.raw.computer);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPLayer.release();
    }
}
