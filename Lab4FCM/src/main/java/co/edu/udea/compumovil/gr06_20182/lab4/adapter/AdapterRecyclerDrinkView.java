package co.edu.udea.compumovil.gr06_20182.lab4.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Drink;


public class AdapterRecyclerDrinkView extends FirestoreAdapter<AdapterRecyclerDrinkView.DishViewHolder>{

    OnRestaurantSelectedListener mListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerDrinkView(Query query, OnRestaurantSelectedListener mListener) {
        super(query);
        this.mListener = mListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dishe, parent, false);
        DishViewHolder pvh = new DishViewHolder(view);
        return pvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DishViewHolder holder, int pos) {

        holder.bind(getSnapshot(pos), mListener);
        Log.d("AdapterRecyclerDrink", "Inicio onBindViewHolder");

    }



    //Clase necesaria para la implementación del RecyclerView
    public class DishViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView dishName;
        public TextView dishPrice;
        public ImageView dishPhoto;
        public ImageView dishFavorite;

        StorageReference gsReference;

        DishViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_dish);
            dishName = (TextView) itemView.findViewById(R.id.name_dish);
            dishPrice = (TextView) itemView.findViewById(R.id.dish_price);
            dishPhoto = (ImageView) itemView.findViewById(R.id.dish_photo);
            dishFavorite = (ImageView) itemView.findViewById(R.id.dish_favorite);
        }

        public void bind(final DocumentSnapshot snapshot, final OnRestaurantSelectedListener listener) {

            final Drink drink = snapshot.toObject(Drink.class);
            Resources resources = itemView.getResources();

            gsReference = FirebaseStorage.getInstance().getReference().child("drinks").child(drink.getImage());

            Log.d("AdapterRecyclerDrink", gsReference.getName());

            final File file;
            try {
                file = File.createTempFile(drink.getImage(), "jpg");
                gsReference.getFile(file)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Glide.with(dishPhoto.getContext())
                                        .load(file.getAbsolutePath())
                                        .into(dishPhoto);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AdapterRecyclerDrink", "Ocurrio un error al mostrar la imagen");
                        e.printStackTrace();
                    }
                });
            }catch (Exception e){
                Log.e("Adapter", "Ocurrió un error en la descarga de imágenes");
                e.printStackTrace();
            }

            dishName.setText(drink.getName());
            dishPrice.setText(drink.getStrPrice());

            if(drink.isFavorite()){
                dishFavorite.setVisibility(View.VISIBLE);
            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onRestaurantSelected(snapshot);
                    }
                }
            });
        }


    }

}
