package org.licpnz.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.licpnz.R;

/**
 * Created by Ilya on 21.12.2016.
 */

public class AboutFragment extends Fragment {

    View mContent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getActivity().getLayoutInflater().inflate(R.layout.about_layout,null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContent;
    }
}
