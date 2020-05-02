package com.stk.reto.dezzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterList extends RecyclerView.Adapter<AdapterList.MyViewHolder> {

    private List<PlayList> mData;
    private OnPlayListListener mOnPlayListListener;


    public AdapterList(List<PlayList> myData,OnPlayListListener onNoteListener) {
        mData = myData;
        this.mOnPlayListListener = onNoteListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleList;
        TextView userCreator;
        TextView numberTracks;
        ImageView image;

        OnPlayListListener mOnPayListListener;

        public MyViewHolder(View v,OnPlayListListener onPlayListListener) {
            super(v);

            this.titleList=v.findViewById(R.id.titleTrack);
            this.userCreator=v.findViewById(R.id.artistTrack);
            this.numberTracks=v.findViewById(R.id.descriptionTrack);
            this.image=v.findViewById(R.id.iconListTrack);

            mOnPayListListener = onPlayListListener;
            itemView.setOnClickListener(this);
        }

        public void setData(PlayList pl){
            titleList.setText(pl.getName());
            userCreator.setText(pl.getNameUser());
            numberTracks.setText(""+ pl.getCantSongs());
            image.setImageBitmap(pl.getBm());
        }

        @Override
        public void onClick(View v) {
            mOnPayListListener.onPlayListClick(getAdapterPosition());
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_cover,null,false);
        return new MyViewHolder(v,mOnPlayListListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setData(mData.get(position));
    }

    @Override
    public int getItemCount() {
        int a;
        if(mData!=null||!mData.isEmpty()){
            a = mData.size();
        }
        else{
            a=0;
        }
        return a;
    }

    public interface OnPlayListListener{
        void onPlayListClick(int position);
    }



}
