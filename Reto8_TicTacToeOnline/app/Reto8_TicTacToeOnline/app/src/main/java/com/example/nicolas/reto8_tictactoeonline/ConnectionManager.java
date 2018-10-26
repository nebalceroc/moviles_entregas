package com.example.nicolas.reto8_tictactoeonline;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class ConnectionManager {

    public boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void serverReachable(Response.Listener<String> request_listener, Response.ErrorListener error_listener){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.API_TEST_URL,
                request_listener,
                error_listener);
    }
}
