package com.example.nicolas.reto8_tictactoeonline;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

public class Game implements Parcelable {

    private int id;
    private String host;
    private String guest;
    private String board;
    private String turn;
    private String state;

    public Game(){}

    public Game(String host, String guest, String board, String turn, String state){
        this.setHost(host);
        this.setGuest(guest);
        this.setBoard(board);
        this.setTurn(turn);
        this.setState(state);
    }

    public Game(Parcel in) {
        String dummy;
        id = in.readInt();
        Log.e("PARC id",String.valueOf(id));
        host = in.readString();
        Log.e("PARC host",String.valueOf(host));
        dummy = in.readString();
        guest = in.readString();
        Log.e("PARC guest",String.valueOf(guest));
        dummy = in.readString();
        board = in.readString();
        Log.e("PARC board",String.valueOf(board));
        state = in.readString();
        Log.e("PARC state",String.valueOf(state));
        turn = in.readString();
        Log.e("PARC turn",String.valueOf(turn));
    }

    public void set_move(final Context context, int id, char player, int location,
                     Response.Listener response_listener,
                     Response.ErrorListener error_listener){
        try {
            final String my_fcm = SessionManager.getInstance(context).getToken();
            Log.e("GAME API", "SET MOVE");

            StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.API_URL_GAME_SET_MOVE_VIEWSET,
                    response_listener,
                    error_listener
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("game_id", String.valueOf(id));
                    params.put("location", String.valueOf(location));
                    params.put("player", String.valueOf(player));
                    return params;
                }
            };
            Constants.mRequestQueue.getInstance(context).addToRequestQueue(postRequest);
        }catch(Exception e){
            Toast.makeText(context, "There was a problem.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void join(final Context context, int id,
                       Response.Listener response_listener,
                       Response.ErrorListener error_listener){
        try {
            final String my_fcm = SessionManager.getInstance(context).getToken();
            Log.e("GAME API", "REQUEST JOIN");

            StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.API_URL_GAME_ENTER_VIEWSET,
                    response_listener,
                    error_listener
            ) {
                @Override
                protected Map<String, String> getParams()
                {

                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("game_id", String.valueOf(id));
                    params.put("guest_fcm_token", my_fcm);

                    return params;
                }
            };
            Constants.mRequestQueue.getInstance(context).addToRequestQueue(postRequest);
        }catch(Exception e){
            Toast.makeText(context, "There was a problem.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void create(final Context context,
                       Response.Listener response_listener,
                       Response.ErrorListener error_listener){
        try {
            final SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            final String my_fcm = SessionManager.getInstance(context).getToken();
//            JSONObject newGame = new JSONObject();
//            newGame.accumulate("host", "");
//            newGame.accumulate("host_fcm_token", my_fcm);
            Log.e("GAME API", "REQUEST CREATE INIT");

            StringRequest postRequest = new StringRequest(Request.Method.POST, Constants.API_URL_GAME_VIEWSET,
                    response_listener,
                    error_listener
            ) {
                @Override
                protected Map<String, String> getParams()
                {

                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("host", "");
                    params.put("host_fcm_token", my_fcm);

                    return params;
                }
            };
            Constants.mRequestQueue.getInstance(context).addToRequestQueue(postRequest);
        }catch(Exception e){
            Toast.makeText(context, "There was a problem.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //Parcelable

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(host);
        dest.writeString(guest);
        dest.writeString(board);
        dest.writeString(state);
        dest.writeString(turn);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

