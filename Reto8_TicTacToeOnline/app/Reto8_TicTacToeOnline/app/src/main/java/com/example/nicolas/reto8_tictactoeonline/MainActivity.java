package com.example.nicolas.reto8_tictactoeonline;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.nicolas.reto8_tictactoeonline.R;

public class MainActivity extends FragmentActivity implements GameListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
