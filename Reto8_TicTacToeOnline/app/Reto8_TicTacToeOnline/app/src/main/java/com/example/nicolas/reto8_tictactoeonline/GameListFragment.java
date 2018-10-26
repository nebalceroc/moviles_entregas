package com.example.nicolas.reto8_tictactoeonline;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GameListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private GameListAdapter mAdapter;

    private String TAG = "GameListFragment";
    private static View view;
    List<Game> games;

    public GameListFragment() {
        // Required empty public constructor
    }

    public static GameListFragment newInstance(String param1, String param2) {
        GameListFragment fragment = new GameListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addGame(Game game) {

        Log.e(TAG,"APP ADD");
        if(mAdapter==null) {
            mAdapter = new GameListAdapter();
        }
        mAdapter.addItem(game);
        ((TextView) view.findViewById(R.id.empty_text)).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_game_list, container, false);

            mAdapter = new GameListAdapter();
            RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mRecyclerView.setAdapter(mAdapter);
            if(mAdapter.getItemCount()<=0){
                ((TextView) view.findViewById(R.id.empty_text)).setVisibility(View.VISIBLE);
            }else{
                ((TextView) view.findViewById(R.id.empty_text)).setVisibility(View.GONE);
            }
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerGameClickListener(getActivity(), new RecyclerGameClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
    //                        TextView text = (TextView) view.findViewById(R.id.serial_text);
    //                        Intent intent = new Intent(context, DoctorAppointmentActivity.class);
    //                        intent.putExtra("appointment_id",Integer.valueOf(text.getText().toString().trim()));
    //                        startActivityForResult(intent, Constants.APPOINTMENT_ACTIVITY_REQUEST);
                        }
                    })
            );
            ConnectionManager cm = new ConnectionManager();
            if(!cm.isNetworkAvailable(getActivity())){
                Toast.makeText(getActivity(),"No hay conexion a internet disponible, las citas no se podran actualizar",Toast.LENGTH_LONG).show();
            }else {
                GameAPIService game_service = new GameAPIService(getActivity());
                game_service.get_games(new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Game> games = new ArrayList<>();
                        try {
                            for (int x = 0; x < response.length(); x++) {
                                JSONObject obj = response.getJSONObject(x);
                                Iterator<String> i = obj.keys();
                                Game new_game = new Game();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                Location location = new Location("0");
                                while (i.hasNext()) {
                                    String k = i.next();
                                    switch (k){
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
                            mAdapter.updateFromWeb(games,getActivity(),2);
                            if(games.size()<=0){
                                ((TextView) view.findViewById(R.id.empty_text)).setVisibility(View.VISIBLE);
                            }else{
                                ((TextView) view.findViewById(R.id.empty_text)).setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(), "No se pudo conectar al servidor, intenta más tarde.", Toast.LENGTH_LONG).show();
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
                });
            }
        }catch (InflateException e){
            e.printStackTrace();
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
