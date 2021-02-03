package com.air.localmusicplayer;

import android.content.ContentUris;
import android.content.Context;
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

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {

    private Context mContext;
    private ArrayList<MusicFiles> albumFiles;
    View view;

    public AlbumAdapter(Context mContext, ArrayList<MusicFiles> albumFiles) {
        this.mContext = mContext;
        this.albumFiles = albumFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.album_item , parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
//        holder.album_name.setText(albumFiles.get(position).getTitle());

        String album = albumFiles.get(position).getAlbum();
        if (album.equals("Music")){
            holder.album_name.setText("Unknown Album");

        }else {
            holder.album_name.setText(album);
        }

        try{
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri,
                    Long.parseLong(albumFiles.get(position).getAlbum_ID()));
            if (uri != null){
                Glide.with(mContext).load(uri).placeholder(R.drawable.backmp3).into(holder.album_image);
            }else {
                Glide.with(mContext).load(R.drawable.backmp3).into(holder.album_image);
            }
        }catch (Exception e){
            Toast.makeText(mContext, "Error"+e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView album_image;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.album_image);
            album_name = itemView.findViewById(R.id.album_name);

        }
    }
}
