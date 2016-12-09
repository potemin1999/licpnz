package org.licpnz.fragments;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.licpnz.R;

/**
 * Created by Ilya on 09.12.2016.
 */

public class MenuFragment extends Fragment {
    CoordinatorLayout mContainer;
    LinearLayout mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer = (CoordinatorLayout) getActivity().getLayoutInflater().inflate(R.layout.main_menu_layout,null);
        mContent = (LinearLayout) mContainer.findViewById(R.id.drawer_menu_linear_container);
        mContent.addView(getMenuElement(0,null,"News"));
        mContent.addView(getMenuElement(1,null,"Settings"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContainer;
    }

    public View getMenuElement(int id, Drawable image,String s){
        LinearLayout ll = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.main_menu_element,null);
        ImageView iv = (ImageView) ll.findViewById(R.id.main_menu_element_image);
        TextView tv = (TextView) ll.findViewById(R.id.main_menu_element_title);
        if (image!=null)
            iv.setImageDrawable(image);
        if (s!=null)
            tv.setText(s);
        return ll;
    }
}
