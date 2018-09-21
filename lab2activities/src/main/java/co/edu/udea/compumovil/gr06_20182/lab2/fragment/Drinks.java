package co.edu.udea.compumovil.gr06_20182.lab2.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab2.R;
import co.edu.udea.compumovil.gr06_20182.lab2.adapter.AdapterRecyclerDrinkView;
import co.edu.udea.compumovil.gr06_20182.lab2.adapter.OnMyAdapterClickListener;
import co.edu.udea.compumovil.gr06_20182.lab2.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab2.tools.SqliteHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class Drinks extends Fragment {
    List<Drink> drinks;
    SqliteHelper sqlh;
    private RecyclerView mRecyclerView;
    private AdapterRecyclerDrinkView adapter;
    private FloatingActionButton fab;
    private Boolean isNew;
    private static final String ARG_NEW = "estado";

    private static final String ARG_NAME = "Drinks";

    private OnFragmentListenerDrink mListener;

    private SearchView searchView;


    public Drinks() {
        // Required empty public constructor
    }

    void Initialization(){

    }


    public static Drinks newInstance(Boolean isNew) {
        Drinks fragment = new Drinks();
        Bundle args = new Bundle();
        args.putBoolean(ARG_NEW, isNew);
        fragment.setArguments(args);
        return fragment;
    }


    public void onButtonPressed(Integer id,Boolean isNew) {
        if (mListener != null) {
            mListener.onFragmentDrinkInteraction(id,isNew);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        filter(menu,adapter);

    }

    void filter(Menu menu,final Filterable filter){
        //getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                filter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                filter.getFilter().filter(query);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
//        if(t.onOptionsItemSelected(item))
//            return true;

        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw = inflater.inflate(R.layout.fragment_drinks, container, false);
        sqlh = new SqliteHelper(getContext());
        drinks = sqlh.getDrinks();

        mRecyclerView = vw.findViewById(R.id.rv_content_drink);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AdapterRecyclerDrinkView(drinks, new OnMyAdapterClickListener() {
            @Override
            public void onItemClick(Integer position) {

                onButtonPressed(drinks.get(position).getId(),false);
            }
        });
        mRecyclerView.setAdapter(adapter);

        fab = vw.findViewById(R.id.fab_drink);
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
        if (context instanceof OnFragmentListenerDrink) {
            mListener = (OnFragmentListenerDrink) context;
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


    public interface OnFragmentListenerDrink {
        // TODO: Update argument type and name
        void onFragmentDrinkInteraction(Integer id,  Boolean isNew);
    }

}
