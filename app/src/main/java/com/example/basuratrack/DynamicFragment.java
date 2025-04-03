package com.example.basuratrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DynamicFragment  extends Fragment {
    private static final String ARG_LAYOUT_RES_ID = "layoutResId";

    public static DynamicFragment newInstance(int layoutResId) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            int layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
            return inflater.inflate(layoutResId, container, false);
        }
        return null;
    }
}
