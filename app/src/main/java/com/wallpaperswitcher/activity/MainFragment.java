package com.wallpaperswitcher.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.pwittchen.networkevents.library.ConnectivityStatus;
import com.wallpaperswitcher.R;

import butterknife.ButterKnife;

/**
 * Created by FlorianXPS on 12/07/2015.
 */
public class MainFragment extends Fragment {
    private View rootView;
    private Activity activity;

    private ConnectivityStatus connectivityStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.activity = this.getActivity();
        ButterKnife.bind(this, activity);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity.setTitle("Wallpaper Switcher");
    }
}
