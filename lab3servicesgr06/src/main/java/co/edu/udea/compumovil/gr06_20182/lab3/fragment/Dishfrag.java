package co.edu.udea.compumovil.gr06_20182.lab3.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab3.R;
import co.edu.udea.compumovil.gr06_20182.lab3.adapter.AdapterRecyclerView;
import co.edu.udea.compumovil.gr06_20182.lab3.adapter.OnMyAdapterClickListener;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DishDto;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.ControllerDishes;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.Mapper;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.OnMyResponse;
import co.edu.udea.compumovil.gr06_20182.lab3.tools.SqliteHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dishfrag extends Fragment {
    List<Dish> dishes;
    SqliteHelper sqlh;
    private RecyclerView mRecyclerView;
    private AdapterRecyclerView adapter;
    private FloatingActionButton fab;
    private Boolean isNew;
    private static final String ARG_NEW = "estado";

    private static final String ARG_NAME = "Dishes";

    private OnFragmentListenerDish mListener;

    private SearchView searchView;


    public Dishfrag() {
        // Required empty public constructor
    }

    void Initialization(){

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
        View vw = inflater.inflate(R.layout.fragment_dishes, container, false);

        sqlh = new SqliteHelper(getContext());
        this.dishes = sqlh.getDishes();

        mRecyclerView = vw.findViewById(R.id.rv_content);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AdapterRecyclerView(dishes, new OnMyAdapterClickListener() {
            @Override
            public void onItemClick(Integer position) {
                //Toast.makeText(getContext(), "Hello: "+ dishes.get(position).getId().toString(), Toast.LENGTH_SHORT).show();
                //dishes = sqlh.getDishes();
                Log.d("DISHFRAG", "Id: " + dishes.get(position).getId());
                onButtonPressed(dishes.get(position).getId(),false);
            }
        });
        mRecyclerView.setAdapter(adapter);

        //mRecyclerView.removeViewAt(1);
        //adapter.notifyItemChanged();

        fab = vw.findViewById(R.id.fab);
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
                Log.d("onFling", "onFling: " + Integer.toString(i) + Integer.toString(i1));
                Toast.makeText(getContext(), "Se inicia descarga de platos .. ",Toast.LENGTH_SHORT).show();

                ///----
                try {
                    new ControllerDishes(new OnMyResponse<DishDto>() {
                        @Override
                        public void onResponse(List<DishDto> obj) {

                            if(obj.size() > 0){
                                new BackgroundTaskDish().execute(obj);
                            }
                        }

                        @Override
                        public void onFailure(String msgError) {
                            Log.d("DishFrag", " Falla descarga imagenes platos: " + msgError);
                        }
                    }).start();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                ///----
                return false;
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

    public AdapterRecyclerView getAdapter() {
        return adapter;
    }

    public interface OnFragmentListenerDish {
        // TODO: Update argument type and name
        void onFragmentInteraction(Integer id, Boolean isNew);
    }

    private class BackgroundTaskDish extends AsyncTask<List<DishDto>, Void, List<Dish>> {

        SqliteHelper sqliteHelper;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, "Iniciando descarga de imagenes de platos");
        }

        @Override
        protected List<Dish> doInBackground(List<DishDto>... dishesDto) {
            //int count = urls.length;
            byte[] bitmap = SqliteHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.fish));
            sqliteHelper = new SqliteHelper(getContext());
            List<Dish>  dishes = Mapper.MapDishes(dishesDto[0]);
            for(Dish d:dishes){
                if(d.getImage() == null){
                    d.setImage(bitmap);
                }
            }

            sqliteHelper.initializationDishes(dishes);
            return dishes;

        }

        @Override
        protected void onPostExecute(List<Dish>  result) {
            super.onPostExecute(result);
            dishes = result;
            adapter.updateAdapter(result);
            Log.d("DishFrag", "Images Platos descargadas Con Exito: "+ result.size());
            Toast.makeText(getContext(), result.size() + " imagenes de platos descargadas con éxito",Toast.LENGTH_SHORT).show();
        }
    }


}
