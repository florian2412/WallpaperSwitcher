package com.wallpaperswitcher.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallpaperswitcher.R;

/**
 * Created by FlorianXPS on 16/07/2015.
 */
public class StorageFragment extends Fragment {

    private View rootView;
    private Activity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_storage, container, false);
        this.activity = this.getActivity();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity.setTitle("Photos");
    }
}