package com.example.nicolas.reto8_tictactoeonline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class TicTacToeActivity extends AppCompatActivity {

    private TicTacToeGame mGame;
    private TextView mInfoTextView;
    private TextView mScoreTextView;
    private boolean mGameOver;
    private int human_wins;
    private int android_wins;
    private int ties;
    private char first;

    //static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 2;
    static final int DIALOG_ABOUT_ID = 1;

    private BoardView mBoardView;

    private boolean mSoundOn;
    private MediaPlayer mHumanMediaPlayer;
    private MediaPlayer mComputerMediaPLayer;

    private int turn;
    private int color;
    private int d;


    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ASD","CREATED");

        setContentView(R.layout.activity_tic_tac_toe);
        //mPrefs = getSharedPreferences("ttt_prefs",MODE_PRIVATE);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound",true);
        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));

        mGame = new TicTacToeGame();

        if(difficultyLevel.equals(getResources().getString(R.string.difficulty_easy))){
            mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        }else if(difficultyLevel.equals(getResources().getString(R.string.difficulty_harder))){
            mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        }else{
            mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
        }

        human_wins = mPrefs.getInt("human_wins",0);
        android_wins = mPrefs.getInt("android_wins",0);
        ties = mPrefs.getInt("ties",0);
        color = mPrefs.getInt("color",0);
        //d = mPrefs.getInt("mdif",1);

        //ties = mPrefs.getInt("ties",0);

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPLayer = MediaPlayer.create(getApplicationContext(), R.raw.computer);

        mInfoTextView = (TextView) findViewById(R.id.info_text);
        mScoreTextView = (TextView) findViewById(R.id.score_text);


        mGameOver = false;
        //mGame.setmIntDifficultyLevel(d);

        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setColor(color);
        mBoardView.setOnTouchListener(mTouchListener);
        if(savedInstanceState==null){
            startNewGame();
        }
        updateScore();
    }

    private void resetScores(){
        ties = 0;
        human_wins = 0;
        android_wins = 0;
        updateScore();
    }

    private boolean checkWinner(int winner){
        if(winner == 0){
            return true;
        }else if (winner == 1) {
            mInfoTextView.setText(R.string.result_tie);
            mGameOver = true;
            ties += 1;
            updateScore();
        } else if (winner == 2) {
            String defaultMessage = getResources().getString(R.string.result_human_wins);
            mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
            mGameOver = true;
            human_wins += 1;
            updateScore();
        } else {
            mInfoTextView.setText(R.string.result_android_wins);
            mGameOver = true;
            android_wins += 1;
            updateScore();
        }
        return false;
    }

    private void androidMove(){
        int move = mGame.getComputerMove();
        setMove(TicTacToeGame.COMPUTER_PLAYER, move);
        int winner = mGame.checkForWinner();
        if (checkWinner(winner)) {
            mInfoTextView.setText(R.string.turn_human);
            turn = TicTacToeGame.HUMAN_PLAYER;
        }
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
        first = savedInstanceState.getChar("first");
        turn = savedInstanceState.getInt("turn");
        if(turn==mGame.COMPUTER_PLAYER && !mGameOver){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    androidMove();
                }
            }, 2000);
        }
    }

    /*@Override
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
*/
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
        ed.putInt("color",color);
        ed.putInt("mdif",d);
        ed.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
        if (requestCode == RESULT_CANCELED){
            mSoundOn = mPrefs.getBoolean("sound",true);

            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder));

            if(difficultyLevel.equals(getResources().getString(R.string.difficulty_easy))){
                mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
            }else if(difficultyLevel.equals(getResources().getString(R.string.difficulty_harder))){
                mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
            }else{
                mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
            }

            mBoardView.setColor(mPrefs.getInt("color",0));
            mBoardView.invalidate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
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

    private boolean setMove(char player, int location){
        if(mGame.setMove(player,location)){
            Log.e("VALID","VALID");
            if(mSoundOn) {
                if (player == TicTacToeGame.HUMAN_PLAYER) {
                    mHumanMediaPlayer.start();
                } else {
                    mComputerMediaPLayer.start();
                }
            }
            mBoardView.invalidate();
            return true;
        }
        return false;
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e("TOUCH", String.valueOf(turn));
            if(turn == TicTacToeGame.HUMAN_PLAYER) {
                int col = (int) event.getX() / mBoardView.getBoardCelWidth();
                int row = (int) event.getY() / mBoardView.getBoardCelHeight();
                int pos = row * 3 + col;
                if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {
                    int winner = mGame.checkForWinner();
                    if (checkWinner(winner)) {
                        mInfoTextView.setText(R.string.turn_computer);
                        turn = TicTacToeGame.COMPUTER_PLAYER;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                androidMove();
                            }
                        }, 2000);
                    }
                }else if(mGameOver){
                    Toast.makeText(getApplicationContext(), "Game already ended!",Toast.LENGTH_SHORT).show();
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
        mBoardView.invalidate();
        //mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        //mComputerMediaPLayer = MediaPlayer.create(getApplicationContext(), R.raw.computer);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mHumanMediaPlayer.release();
        mComputerMediaPLayer.release();
    }
}
