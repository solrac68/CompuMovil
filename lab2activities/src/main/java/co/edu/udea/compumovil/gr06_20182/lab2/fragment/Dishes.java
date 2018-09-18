package co.edu.udea.compumovil.gr06_20182.lab2.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.udea.compumovil.gr06_20182.lab2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dishes extends Fragment {


    public Dishes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dishes, container, false);
    }

}
