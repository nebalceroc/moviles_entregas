package com.example.nicolas.reto8_tictactoeonline;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GameAPIService {

    private Context context;

    public GameAPIService(Context ctx){
        this.context=ctx;
    }

    public void create(String host, String guest, String board, String turn, String state,
                       Response.Listener<String> response_listener,
                       Response.ErrorListener error_listener){

        Game game = new Game(host,guest,board,turn,state);
        game.create(context,response_listener, error_listener);

    }

    public void join(int id,
                       Response.Listener<String> response_listener,
                       Response.ErrorListener error_listener){

        Game game = new Game();
        game.join(context,id,response_listener, error_listener);

    }

    public void get_games(Response.Listener<JSONArray> response_listener,
                                         Response.ErrorListener error_listener){
        JSONArray arr = new JSONArray();
        Map<String, String> headers = new HashMap<String, String>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Constants.API_URL_GAME_VIEWSET, arr, response_listener,error_listener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //SessionManager sm = new SessionManager(context.getApplicationContext());
                Log.e("APP SERVICE","");
                //params.put("Authorization", "Token " + SessionManager.getInstance(context).getToken());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Constants.mRequestQueue.getInstance(context).addToRequestQueue(request);
    }

//    public void notify_attend(int selected_appointment_id, Response.Listener<JSONObject> response_listener, Response.ErrorListener error_listener) {
//        JSONObject params = new JSONObject();
//        SessionManager sm = new SessionManager(context);
//        try {
//            params.put("doctor",sm.getUserDetails().getId());
//            JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                    (Request.Method.PATCH, Constants.API_URL_APPOINTMENT_VIEWSET+selected_appointment_id+"/"+ Constants.API_URL_ATTEND_APPOINTMENT,
//                            params,
//                            response_listener,
//                            error_listener){
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    //SessionManager sm = new SessionManager(context);
//                    params.put("Authorization", "Token " + SessionManager.getInstance(context).getToken());
//                    return params;
//                }
//            };
//            // Add the request to the RequestQueue.
//            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            Constants.mRequestQueue.getInstance(context).addToRequestQueue(jsObjRequest);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
