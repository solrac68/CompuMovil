package co.edu.udea.compumovil.gr06_20182.lab3.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab3.R;
import co.edu.udea.compumovil.gr06_20182.lab3.adapter.AdapterRecyclerDrinkView;
import co.edu.udea.compumovil.gr06_20182.lab3.adapter.OnMyAdapterClickListener;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DrinkDto;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.ControllerDrinks;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.Mapper;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.MyDownloadService;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.OnMyResponse;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.SqliteHelper;

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

        mRecyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int i, int i1) {
                Toast.makeText(getContext(), "Se inicia descarga de bebidas .. ",Toast.LENGTH_SHORT).show();

                try{
                    new ControllerDrinks(new OnMyResponse<DrinkDto>() {
                        @Override
                        public void onResponse(List<DrinkDto> obj) {
                            if(obj.size() > 0){
                                new BackgroundTaskDrink().execute(obj);
                            }
                        }

                        @Override
                        public void onFailure(String msgError) {
                            Log.d("Drinks", " Falla descarga imagenes bebidas: " + msgError);
                        }
                    }).start();

                }catch(Exception e){
                    e.printStackTrace();
                }
                return false;
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
        void onFragmentDrinkInteraction(Integer id, Boolean isNew);
    }

    private class BackgroundTaskDrink extends AsyncTask<List<DrinkDto>, Void, List<Drink>> {

        SqliteHelper sqliteHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, "Iniciando descarga de imagenes de bebidas");
        }

        @Override
        protected List<Drink> doInBackground(List<DrinkDto>... drinksDto) {
            byte[] bitmap = SqliteHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.fruit));

            sqliteHelper = new SqliteHelper(getContext());
            List<Drink>  drinks = Mapper.MapDrinks(drinksDto[0]);
            for(Drink d:drinks){
                if(d.getImage() == null){
                    d.setImage(bitmap);
                }
            }
            sqliteHelper.initializationDrinks(drinks);

            return drinks;

        }

        @Override
        protected void onPostExecute(List<Drink>  result) {
            super.onPostExecute(result);
            drinks = result;
            adapter.updateAdapter(result);
            Log.d("Drinks", "Images Bebidas Descargadas Con Exito: "+ result.size());
            Toast.makeText(getContext(), result.size() + " imagenes de bébidas descargadas con éxito",Toast.LENGTH_SHORT).show();
        }
    }

}
