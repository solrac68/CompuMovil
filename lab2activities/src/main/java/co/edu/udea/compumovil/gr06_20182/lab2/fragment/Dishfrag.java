package co.edu.udea.compumovil.gr06_20182.lab2.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab2.R;
import co.edu.udea.compumovil.gr06_20182.lab2.adapter.AdapterRecyclerView;
import co.edu.udea.compumovil.gr06_20182.lab2.adapter.OnMyAdapterClickListener;
import co.edu.udea.compumovil.gr06_20182.lab2.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab2.tools.SqliteHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dishfrag extends Fragment {
    List<Dish> dishes;
    SqliteHelper sqlh;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private Boolean isNew;
    private static final String ARG_NEW = "estado";

    private static final String ARG_NAME = "Dishes";

    private OnFragmentListenerDish mListener;


    public Dishfrag() {
        // Required empty public constructor
    }

    public static Dishfrag newInstance(Boolean isNew) {
        Dishfrag fragment = new Dishfrag();
        Bundle args = new Bundle();
        args.putBoolean(ARG_NEW, isNew);
        fragment.setArguments(args);
        return fragment;
    }


    public void onButtonPressed(Integer id,Boolean isNew) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id,isNew);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw = inflater.inflate(R.layout.fragment_dishes, container, false);
        sqlh = new SqliteHelper(getContext());
        dishes = sqlh.getDishes();

        mRecyclerView = vw.findViewById(R.id.rv_content);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        AdapterRecyclerView adapter = new AdapterRecyclerView(dishes, new OnMyAdapterClickListener() {
            @Override
            public void onItemClick(Integer position) {
                //Toast.makeText(getContext(), "Hello: "+ dishes.get(position).getId().toString(), Toast.LENGTH_SHORT).show();
                onButtonPressed(dishes.get(position).getId(),false);
            }
        });
        mRecyclerView.setAdapter(adapter);

        fab = vw.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "Hola", Toast.LENGTH_SHORT).show();
                // Para crear un nuevo registro de platos..
                onButtonPressed(-1,true);
            }
        });

        return vw;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListenerDish) {
            mListener = (OnFragmentListenerDish) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentListenerDish {
        // TODO: Update argument type and name
        void onFragmentInteraction(Integer id,  Boolean isNew);
    }

}
