package com.air.localmusicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
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


        String artist = mFiles.get(position).getArtist();
        if (artist.equals("<unknown>")){
            holder.file_artist.setText("Unknown Artist");

        }else {
                holder.file_artist.setText(artist);


        }
        holder.file_name.setText(mFiles.get(position).getTitle());

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
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext , v);
                popupMenu.getMenuInflater().inflate(R.menu.popup , popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.delete :
                                Toast.makeText(mContext, "Deleting file...", Toast.LENGTH_SHORT).show();
                                deleteFile(position,v);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void deleteFile (int position , View v){
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(mFiles.get(position).getId()));
        File file =new File(mFiles.get(position).getPath());
        boolean deleted = file.delete();
        if (deleted){
            mContext.getContentResolver().delete(contentUri, null,null);
            mFiles.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFiles.size());
            Snackbar.make(v, "File Deleted", Snackbar.LENGTH_LONG).show();

        }else {
            Snackbar.make(v, "File can't be Deleted", Snackbar.LENGTH_LONG).show();
        }

    }
    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView file_name;
        TextView file_artist;
        TextView file_album;
        ImageView album_art , menuMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            file_artist = itemView.findViewById(R.id.music_file_artist);
            file_album = itemView.findViewById(R.id.music_file_album);
            album_art = itemView.findViewById(R.id.music_img);
            menuMore = itemView.findViewById(R.id.menu_More);

        }
    }

}
