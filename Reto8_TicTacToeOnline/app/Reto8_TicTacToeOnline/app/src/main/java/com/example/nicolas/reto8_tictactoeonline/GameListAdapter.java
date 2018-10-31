package com.example.nicolas.reto8_tictactoeonline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.nicolas.reto8_tictactoeonline.R;

class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder>{

    private String TAG = "GameListAdapter";
    private List<Game> mDataset;
    private List<Integer> news;

    public GameListAdapter(List<Game> myDataset, Context context) {
        mDataset = myDataset;
        news = new ArrayList<Integer>();
    }

    public GameListAdapter() {
        mDataset = new ArrayList<Game>();
    }

    public void updateFromWeb(List<Game> games, Context context, int type) {
        mDataset.clear();
        List<Game> update_set = new ArrayList<>();
        for(int x=0; x<games.size(); x++){
            boolean is_new = true;
            Game g1 = games.get(x);
            update_set.add(g1);
//            for(int y=0; y<mDataset.size(); y++){
//                Game g2 = mDataset.get(y);
//                if(g1.getId()==g2.getId()){
//                    Log.e(TAG,"old game");
////                    if(!g1.getState().equals(g2.getState())){
////                        //STATUS CHANGE
////                        if(db.udpateAppointmentStatus(ap1.getId(),ap1.getStatus())){
////                            //STATUS CHANGE DONE
////                            if(ap1.getStatus().equals("2")){
////                                db.updateAppointmentDoctor(ap1.getId(),ap1.getDoctor().getId());
////                            }else if(ap1.getStatus().equals("3")){
////                                db.udpateAppointmentDiagnostic(ap1.getId(), ap1.getDiagnostic());
////                            }
////                            Log.e(TAG,"DB OK"+ap1.getCauses()+ap1.getId());
////                            mDataset.set(y,appointments.get(x));
////                            notifyItemChanged(y);
////                        }else{
////                            Log.e(TAG,"DB FAIL");
////                        }
////                        news.add(appointments.get(x).getId());
////                    }else{
////                        if(ap1.getStatus().equals("3")){
////                            db.udpateAppointmentDiagnostic(ap1.getId(), ap1.getDiagnostic());
////                            mDataset.set(y,appointments.get(x));
////                        }
////                    }
//                    is_new=false;
//                }
//            }
//            if(is_new){
//
//                Log.e(TAG,"new game "+String.valueOf(g1.getId()));
//            }
        }
        mDataset.addAll(update_set);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GameListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.game_list_item, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TextView text = (TextView) viewHolder.mView.findViewById(R.id.serial_text);
        text.setText("ID: "+String.valueOf(mDataset.get(i).getId()));
        text = (TextView) viewHolder.mView.findViewById(R.id.target_id);
        int players =2;
        if(mDataset.get(i).getGuest().equals("empty")){
            players--;
        }
        text.setText(String.valueOf(players)+"/2");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addItem(Game game){
        mDataset.add(game);
        notifyDataSetChanged();
    }



    public static  class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ViewHolder(View v){
            super(v);
            mView = v;
        }
    }
}
