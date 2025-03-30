package com.example.basuratrack;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new aboutfrgmnt();
            case 1:
                return new collector_about();
            case 2:
                return new notice();
            case 3:
                return new accepted_wastefrgmnt();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
