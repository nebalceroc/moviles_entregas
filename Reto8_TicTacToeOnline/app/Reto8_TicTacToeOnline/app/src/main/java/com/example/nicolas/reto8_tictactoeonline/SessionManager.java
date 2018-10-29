package com.example.nicolas.reto8_tictactoeonline;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


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
            return null;
        }else{
            return null;
        }

    }

    public void setGame(TicTacToeGame game){

    }

    // Set token
    public void setToken(String token){editor.putString(KEY_TOKEN, token);; editor.commit();}

    // Get token
    public String getToken(){
        return  pref.getString(KEY_TOKEN,null);
    }


}
