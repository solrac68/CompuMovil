package co.edu.udea.compumovil.gr06_20182.lab4.fragment;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.activities.MainActivity;
import co.edu.udea.compumovil.gr06_20182.lab4.adapter.AdapterRecyclerView;
import co.edu.udea.compumovil.gr06_20182.lab4.adapter.OnRestaurantSelectedListener;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;



import static android.support.v4.content.ContextCompat.createDeviceProtectedStorageContext;

//import static android.support.v4.content.ContextCompat.getSystemService;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dishfrag extends Fragment {
    List<Dish> dishes;
    private RecyclerView mRecyclerView;
    private AdapterRecyclerView adapter;
    private FloatingActionButton fab;
    private Boolean isNew;
    private static final String ARG_NEW = "estado";

    private static final String ARG_NAME = "Dishes";

    private OnFragmentListenerDish mListener;

    private SearchView searchView;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    SharedPreferences pref;
    Boolean notificacion;


    public Dishfrag() {
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


    public void onButtonPressed(String id,Boolean isNew) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id,isNew);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        notificacion = pref.getBoolean("key_notification",false);
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

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("dishes")
                .orderBy("name", Query.Direction.DESCENDING);

    }

    private void initRecyclerView(){
        if (mQuery == null) {
            Log.w("DisfFrag", "No query, not initializing RecyclerView");
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AdapterRecyclerView(mQuery, new OnRestaurantSelectedListener() {
            @Override
            public void onRestaurantSelected(DocumentSnapshot restaurant) {
                Log.d("DISHFRAG", "Id: " + restaurant.getId());
                onButtonPressed(restaurant.getId(),false);
            }
        }){

            @Override
            protected void onDataChanged(String message) {

                if(notificacion)
                    showNotification(message);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
//                Snackbar.make(findViewById(android.R.id.content),
//                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }

        };

        mRecyclerView.setAdapter(adapter);
    }

    private void showNotification(String message){
        Intent intent = new Intent(getActivity(),MainActivity.class);

        intent.putExtra("Message", message);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity(),getString(R.string.channel_id))
                .setSmallIcon(R.drawable.octopus)
                .setContentTitle(getString(R.string.title_notification))
                .setContentText(message + " plato")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager
                = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(0, notificationBuilder.build());

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw = inflater.inflate(R.layout.fragment_dishes, container, false);


        mRecyclerView = vw.findViewById(R.id.rv_content);

        initFirestore();

        initRecyclerView();

        fab = vw.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed("-1",true);
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
        void onFragmentInteraction(String id, Boolean isNew);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null) {
            adapter.startListening();
        }
    }
}
