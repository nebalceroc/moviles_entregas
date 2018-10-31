package com.example.nicolas.reto8_tictactoeonline;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;


public class SessionManager {
    private static SessionManager mInstance;



    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "DocAppPref";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_INGAME = "ingame";

    private static final String KEY_ID = "id";
    private static final String KEY_BOARD = "board";
    private static final String KEY_PLAYER = "player";
    private static final String KEY_TURN = "turn";
    private static final String KEY_GO = "gameOver";
    private static final String KEY_FULL = "full";



    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SessionManager(context);
        }
        return mInstance;
    }

    public TicTacToeGame getGame(){
        boolean ingame = pref.getBoolean(KEY_INGAME, false);
        if(ingame){
            TicTacToeGame game = new TicTacToeGame();
            game.id = pref.getInt(KEY_ID,-1);
            game.iam = pref.getString(KEY_PLAYER,"_").charAt(0);
            game.setmBoardStateFromStr(pref.getString(KEY_BOARD,"EEEEEEEEE"));
            game.turn = pref.getInt(KEY_TURN,1);
            game.gameOver = pref.getBoolean(KEY_GO,false);
            game.full = pref.getBoolean(KEY_FULL,false);
            return game;
        }else{
            return null;
        }

    }

    public void clearGame(){
        editor.putBoolean(KEY_INGAME, false);
        editor.commit();
    }

    public void setGame(TicTacToeGame game){
        editor.putBoolean(KEY_INGAME, true);
        editor.putString(KEY_BOARD, game.getBoardStr());
        editor.putString(KEY_PLAYER, String.valueOf(game.iam));
        editor.putInt(KEY_TURN, game.turn);
        editor.putBoolean(KEY_GO,game.gameOver);
        editor.putInt(KEY_ID,game.id);
        editor.putBoolean(KEY_FULL,game.full);
        editor.commit();
    }

    // Set token
    public void setToken(String token){editor.putString(KEY_TOKEN, token);; editor.commit();}

    // Get token
    public String getToken(){
        return  pref.getString(KEY_TOKEN,null);
    }


}
