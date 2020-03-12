package com.stk.reto1_maps;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

public class FrameMap extends SupportMapFragment {

    public FrameMap() {
    }

    public static FrameMap newInstance() {
        return new FrameMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }

}
