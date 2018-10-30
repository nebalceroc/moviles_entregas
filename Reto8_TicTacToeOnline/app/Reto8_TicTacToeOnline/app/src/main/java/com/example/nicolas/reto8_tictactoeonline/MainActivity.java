package com.example.nicolas.reto8_tictactoeonline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.nicolas.reto8_tictactoeonline.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GoogleApiClient client;
    private GameListAdapter mAdapter;
    private static final String TAG = "MainActivity";
    private TicTacToeGame game = null;

    private void refreshGames(){
        ConnectionManager cm = new ConnectionManager();
        if (!cm.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "No hay conexion a internet disponible", Toast.LENGTH_LONG).show();
        } else {
            GameAPIService game_service = new GameAPIService(getApplicationContext());
            game_service.get_games(new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    handleJsonResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleVolleryError(error);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(getApplicationContext());

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
            SessionManager.getInstance(getApplicationContext()).setToken(newToken);
        });
        mAdapter = new GameListAdapter();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter.getItemCount() <= 0) {
            ((TextView) findViewById(R.id.empty_text)).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.empty_text)).setVisibility(View.GONE);
        }
        mRecyclerView.addOnItemTouchListener(
                new RecyclerGameClickListener(this, new RecyclerGameClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView _id = (TextView) view.findViewById(R.id.serial_text);
                        GameAPIService game_service = new GameAPIService(getApplication());
                        Log.e(TAG,"AA");
                        game_service.join(Integer.valueOf(_id.getText().toString().trim()), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject json = new JSONObject(response);
                                    Log.e(TAG,response);
                                    if(json.has("error_msg")){
                                        Toast.makeText(getApplicationContext(), "Juego Lleno.", Toast.LENGTH_LONG).show();
                                        refreshGames();
                                    }else{
                                        TicTacToeGame newGame = new TicTacToeGame();
                                        newGame.setmBoardStateFromStr(json.getString("board"));
                                        newGame.iam='2';
                                        newGame.turn=1;
                                        newGame.id=json.getInt("id");
                                        newGame.full=true;
                                        SessionManager.getInstance(getApplicationContext()).setGame(newGame);
                                        Intent intent = new Intent(getApplicationContext(), TicTacToeActivity.class);
                                        startActivityForResult(intent, Constants.START_TTT_ACTIVITY_JOIN);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                handleVolleryError(error);
                            }
                        });
                    }
                })
        );
        ConnectionManager cm = new ConnectionManager();
        if (!cm.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(this, "No hay conexion a internet disponible, las citas no se podran actualizar", Toast.LENGTH_LONG).show();
        } else {
            GameAPIService game_service = new GameAPIService(getApplicationContext());
            game_service.get_games(new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    handleJsonResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    handleVolleryError(error);
                }
            });
        }
        Button refreshButton = (Button) findViewById(R.id.rbutton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                refreshGames();
            }
        });

        Button createButton = (Button) findViewById(R.id.cbutton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ConnectionManager cm = new ConnectionManager();
                if (!cm.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(), "No hay conexion a internet disponible", Toast.LENGTH_LONG).show();
                } else {
                    GameAPIService game_service = new GameAPIService(getApplicationContext());
                    game_service.create("", "", "", "", "", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG,response);

                                    try {
                                        JSONObject json = new JSONObject(response);
                                        TicTacToeGame newGame = new TicTacToeGame();
                                        newGame.setmBoardStateFromStr(json.getString("board"));
                                        newGame.iam='1';
                                        newGame.id=json.getInt("id");
                                        newGame.turn=1;
                                        SessionManager.getInstance(getApplicationContext()).setGame(newGame);
                                        Intent intent = new Intent(getApplicationContext(), TicTacToeActivity.class);
                                        //intent.putExtra("str",response);
                                        startActivityForResult(intent, Constants.START_TTT_ACTIVITY);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();

                                }
                            });
                }
            }
        });
        game = SessionManager.getInstance(getApplicationContext()).getGame();
        if(game != null){
            Intent intent = new Intent(getApplicationContext(), TicTacToeActivity.class);
            startActivityForResult(intent, Constants.START_TTT_ACTIVITY);
        }
    }


    private void handleJsonResponse(JSONArray response) {
        List<Game> games = new ArrayList<>();
        try {
            for (int x = 0; x < response.length(); x++) {
                JSONObject obj = response.getJSONObject(x);
                Iterator<String> i = obj.keys();
                Game new_game = new Game();
                while (i.hasNext()) {
                    String k = i.next();
                    switch (k) {
                        case "id":
                            new_game.setId(Integer.parseInt(obj.getString(k)));
                            break;
                        case "host":
                            new_game.setHost(obj.getString(k));
                            break;
                        case "guest":
                            new_game.setGuest(obj.getString(k));
                            break;
                        case "board":
                            new_game.setBoard(obj.getString(k));
                            break;
                        case "state":
                            new_game.setState(obj.getString(k));
                            break;
                        case "turn":
                            new_game.setTurn(obj.getString(k));
                            break;
                    }
                }
                games.add(new_game);
            }
            mAdapter.updateFromWeb(games, getApplicationContext(), 2);
            if (games.size() <= 0) {
                (findViewById(R.id.empty_text)).setVisibility(View.VISIBLE);
            } else {
                (findViewById(R.id.empty_text)).setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleStringResponse(String response) {

    }

    private void handleVolleryError(VolleyError error) {
        if (error instanceof NoConnectionError) {
            Toast.makeText(getApplicationContext(), "No se pudo conectar al servidor, intenta más tarde.", Toast.LENGTH_LONG).show();
        } else if (error instanceof TimeoutError) {

        } else if (error instanceof AuthFailureError) {
            //TODO
        } else if (error instanceof ServerError) {
            try {
                Log.e(TAG, "eror:" + error.getMessage());
                String json = new String(error.networkResponse.data);
                String errors = new String();
                Log.e(TAG, "json:" + json);
                JSONObject obj = new JSONObject(json);
                Iterator<String> i = obj.keys();

                while (i.hasNext()) {
                    String k = i.next();
                    Log.e(TAG, "list:" + obj.getString(k));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException ne) {
                Log.e(TAG, ne.getLocalizedMessage());
            }
        } else if (error instanceof NetworkError) {
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
        }
    }
}
