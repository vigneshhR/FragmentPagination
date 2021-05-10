package com.example.demofragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class DescriptionFragment extends Fragment {

    private Context mContext;


    public DescriptionFragment(int repoId) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();

        View view = inflater.inflate(R.layout.fragment_description, container, false);
        setHasOptionsMenu(true);

        return view;
    }
}
