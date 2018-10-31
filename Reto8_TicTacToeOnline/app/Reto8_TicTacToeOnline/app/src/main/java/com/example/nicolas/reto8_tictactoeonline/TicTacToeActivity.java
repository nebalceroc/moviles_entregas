package com.example.nicolas.reto8_tictactoeonline;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.ybq.android.spinkit.style.Circle;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
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

    private Circle mCircleDrawable;

    private SharedPreferences mPrefs;

    private boolean full=false;
    private ProgressDialog progressDialog;
    private boolean loading;


    private void toggleContent(int option){
        if(option==0){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String kind = intent.getStringExtra("kind");
            Log.e("KIND",kind);
            switch (kind){
                case "join":
                    progressDialog.hide();
                    mGame.full=true;
                    SessionManager.getInstance(context).setGame(mGame);
                    break;
                case "action":
                    Log.e("SSSSS",intent.getStringExtra("board"));
                    mGame.setmBoardStateFromStr(intent.getStringExtra("board"));
                    mGame.turn=Integer.valueOf(intent.getStringExtra("turn"));
                    SessionManager.getInstance(context).setGame(mGame);
                    refreshBoard();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e("ASD","CREATED");

        setContentView(R.layout.activity_tic_tac_toe);
        //mPrefs = getSharedPreferences("ttt_prefs",MODE_PRIVATE);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound",true);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.JOIN_GAME_NOTIFICATION));

        mGame = SessionManager.getInstance(this).getGame();
        if(mGame==null){
            startNewGame();
            mGame = new TicTacToeGame();
            Intent intent = getIntent();
            turn = (int) mGame.turn;
            SessionManager.getInstance(this).setGame(mGame);
        }
        color = mPrefs.getInt("color",Color.BLACK);
        //d = mPrefs.getInt("mdif",1);

        //ties = mPrefs.getInt("ties",0);

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.human);
        mComputerMediaPLayer = MediaPlayer.create(getApplicationContext(), R.raw.computer);

        mInfoTextView = (TextView) findViewById(R.id.info_text);
        mScoreTextView = (TextView) findViewById(R.id.score_text);


        mGameOver = false;
        //mGame.setmIntDifficultyLevel(d);

        mBoardView = (BoardView) findViewById(R.id.board);
        Log.e("ONCREATE",mGame.getBoardStr());
        mBoardView.setGame(mGame);
        mBoardView.setColor(color);
        mBoardView.setOnTouchListener(mTouchListener);

        updateScore();
        //OLD PROGRESS DIALOG
        progressDialog = new ProgressDialog(this, R.style.LoaderTheme);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("TicTacToe");
        progressDialog.setMessage("Esperando Oponente");
        progressDialog.setCancelable(false);

//        //NEW MATERIAL ANIMATION
//        TextView textView = (TextView) findViewById(R.id.loading_text);
//        mCircleDrawable = new Circle();
//        mCircleDrawable.setBounds(0, 0, 100, 100);
//        mCircleDrawable.setColor(Color.parseColor("#0064b4"));
//        textView.setCompoundDrawables(null, null, mCircleDrawable, null);
        if(!mGame.full){
            progressDialog.show();
        }
        //Log.e("IM", String.valueOf(mGame.iam));
        //Log.e("turn", String.valueOf(mGame.turn));
        if(String.valueOf(mGame.iam).equals(String.valueOf(mGame.turn))){
            mInfoTextView.setText(R.string.turn_human);
            //Log.e("turn", String.valueOf(mGame.turn));
        }else{
            mInfoTextView.setText("Wait for your opponent move");
        }

        human_wins = mPrefs.getInt("human_wins",0);
        android_wins = mPrefs.getInt("android_wins",0);
        ties = mPrefs.getInt("ties",0);
        mBoardView.invalidate();
    }

//        if(difficultyLevel.equals(getResources().getString(R.string.difficulty_easy))){
//            mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
//        }else if(difficultyLevel.equals(getResources().getString(R.string.difficulty_harder))){
//            mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
//        }else{
//            mGame.setmDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
//        }



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
        } else {
            String defaultMessage = getResources().getString(R.string.result_human_wins);
            Log.e("WINNER", String.valueOf(winner));
            if(winner==3){
                if(mGame.iam=='1'){
                    mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
                    mGameOver = true;
                    human_wins += 1;
                }else{
                    mInfoTextView.setText("Your opponent won!");
                    mGameOver = true;
                    android_wins += 1;
                }
            }
            if(winner==2){
                if(mGame.iam=='1'){
                    mInfoTextView.setText("Your opponent won!");
                    mGameOver = true;
                    android_wins += 1;
                }else{
                    mInfoTextView.setText(mPrefs.getString("victory_message", defaultMessage));
                    mGameOver = true;
                    human_wins += 1;
                }
            }
            updateScore();
        }
        SessionManager.getInstance(this).clearGame();
        return false;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putCharArray("board", mGame.getBoardState());
        outState.putBoolean("mGameOver",mGameOver);
        outState.putInt("human_wins",human_wins);
        outState.putInt("android_wins",android_wins);
        outState.putInt("ties",ties);
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
//        if(turn==mGame.COMPUTER_PLAYER && !mGameOver){
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    androidMove();
//                }
//            }, 2000);
//        }
    }




    private void updateScore(){
        mScoreTextView.setText("Score W:"+String.valueOf(human_wins)+" T:"+String.valueOf(ties)+" L:"+String.valueOf(android_wins));
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

            mBoardView.setColor(mPrefs.getInt("color",0));
            mBoardView.invalidate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
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
        //Random rand = new Random();
//        int n = rand.nextInt(100);
//        if(n < 50){
//            mInfoTextView.setText(R.string.turn_computer);
//            int move = mGame.getComputerMove();
//            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
//            first = 'A';
//        }else{
//            first = 'H';
//        }
        mInfoTextView.setText(R.string.turn_human);
        //turn = TicTacToeGame.HUMAN_PLAYER;
    }

    private void refreshBoard(){
        mBoardView.invalidate();
        if(String.valueOf(mGame.iam).equals(String.valueOf(mGame.turn))){
            mInfoTextView.setText(R.string.turn_human);
        }else{
            mInfoTextView.setText("Wait for your opponent move");
        }
        int winner = mGame.checkForWinner();
        checkWinner(winner);
    }

    private void setMove(char player, int location){
        mGame.setMove(getApplicationContext(), player, location, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            Log.e("TTA",response);
                            if(json.has("error_msg")){
                                Toast.makeText(getApplicationContext(), "Movimiento Invalido.", Toast.LENGTH_LONG).show();
                            }else{
                                TicTacToeGame umGame = SessionManager.getInstance(getApplicationContext()).getGame();
                                umGame.setmBoardStateFromStr(json.getString("board"));
                                umGame.turn=json.getInt("turn");
                                mGame.setmBoardStateFromStr(json.getString("board"));
                                mGame.turn=json.getInt("turn");
                                SessionManager.getInstance(getApplicationContext()).setGame(umGame);
                                refreshBoard();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e("TOUCH", String.valueOf(turn));
            if(String.valueOf(mGame.iam).equals(String.valueOf(mGame.turn))) {
                int col = (int) event.getX() / mBoardView.getBoardCelWidth();
                int row = (int) event.getY() / mBoardView.getBoardCelHeight();
                int pos = row * 3 + col;
                if(!mGameOver){
                    setMove(mGame.iam, pos);
                }else{
                    Toast.makeText(getApplicationContext(), "Game Over!",Toast.LENGTH_SHORT).show();
                }

//                if (!mGame.gameOver && ) {
//                    mInfoTextView.setText(R.string.turn_computer);
//
//                }else if(mGameOver){
//                    Toast.makeText(getApplicationContext(), "Game already ended!",Toast.LENGTH_SHORT).show();
//                }
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
