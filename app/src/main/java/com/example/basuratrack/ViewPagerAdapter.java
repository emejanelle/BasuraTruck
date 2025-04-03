package com.example.basuratrack;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<Integer> layoutIds;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Integer> layoutIds) {
        super(fragmentActivity);
        this.layoutIds = layoutIds;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return DynamicFragment.newInstance(layoutIds.get(position));
    }

    @Override
    public int getItemCount() {
        return layoutIds.size();
    }
}
