package com.air.localmusicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MusicFiles> mFiles;

    MusicAdapter(Context mContext, ArrayList<MusicFiles> mFiles){
        this.mContext = mContext;
        this.mFiles = mFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_items,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.file_name.setText(mFiles.get(position).getTitle());

        String artist = mFiles.get(position).getArtist();
        if (artist.equals("<unknown>")){
            holder.file_artist.setText("Unknown Artist");

        }else {
                holder.file_artist.setText(artist);


        }

        String album = mFiles.get(position).getAlbum();
        if (album.equals("Music")){
            holder.file_album.setText("Unknown Album");

        }else {
            holder.file_album.setText(album);
        }

        try{
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri,
                   Long.parseLong(mFiles.get(position).getAlbum_ID()));
        if (uri != null){
            Glide.with(mContext).load(uri).placeholder(R.drawable.backmp3).into(holder.album_art);
        }else {
            Glide.with(mContext).load(R.drawable.backmp3).into(holder.album_art);
        }
        }catch (Exception e){
            Toast.makeText(mContext, "Error"+e, Toast.LENGTH_SHORT).show();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PlayerActivity.class);
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView file_name;
        TextView file_artist;
        TextView file_album;
        ImageView album_art;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            file_artist = itemView.findViewById(R.id.music_file_artist);
            file_album = itemView.findViewById(R.id.music_file_album);
            album_art = itemView.findViewById(R.id.music_img);

        }
    }

}
