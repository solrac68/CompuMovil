package co.edu.udea.compumovil.gr06_20182.lab2.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab2.R;
import co.edu.udea.compumovil.gr06_20182.lab2.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab2.tools.SqliteHelper;


public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.PersonViewHolder>{

    List<Dish> dishes;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerView(List<Dish> dishes) {
        this.dishes = dishes;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dishe, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(view);
        return pvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PersonViewHolder holder, int pos) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.dishName.setText(dishes.get(pos).getName());
        holder.dishPrice.setText(dishes.get(pos).getStrPrice());
        holder.dishPhoto.setImageBitmap(SqliteHelper.getByteArrayAsBitmap(dishes.get(pos).getImage()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dishes.size();
    }


    //Clase necesaria para la implementación del RecyclerView
    public static class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        public TextView dishName;
        public TextView dishPrice;
        public TextView dishPreparationTime;
        public ImageView dishPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.card_view_dish);
            dishName = (TextView) itemView.findViewById(R.id.name_dish);
            dishPrice = (TextView) itemView.findViewById(R.id.dish_price);
            dishPhoto = (ImageView) itemView.findViewById(R.id.dish_photo);
            dishPreparationTime = (TextView) itemView.findViewById(R.id.dish_preparation);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Log.d("AdapterRecyclerView", "onClick: " + pos + "  Name: "+ dishName.getText() );
            Toast.makeText(itemView.getContext(), "Hello: "+ dishName.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
