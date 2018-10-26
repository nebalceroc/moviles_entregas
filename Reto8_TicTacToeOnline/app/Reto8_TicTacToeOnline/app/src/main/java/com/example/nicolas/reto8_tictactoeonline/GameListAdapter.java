package com.example.nicolas.reto8_tictactoeonline;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder>{
    @NonNull
    @Override
    public GameListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GameListAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ViewHolder(View v){
            super(v);
            mView = v;
        }
    }
}
