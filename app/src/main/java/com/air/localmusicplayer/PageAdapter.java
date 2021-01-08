package com.air.localmusicplayer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int num_of_tabs;
    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.num_of_tabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SongsFragment();
            case 1:
                return new AlbumFragment();
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return num_of_tabs;
    }
}
