package co.edu.udea.compumovil.gr06_20182.lab2.adapter;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab2.R;
import co.edu.udea.compumovil.gr06_20182.lab2.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab2.tools.SqliteHelper;


public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.DishViewHolder> implements Filterable{

    List<Dish> dishes;
    List<Dish> dishesFilter;
    OnMyAdapterClickListener onMyAdapterClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerView(List<Dish> dishes, OnMyAdapterClickListener onMyAdapterClickListener) {
        this.dishes = dishes;
        this.dishesFilter = dishes;
        this.onMyAdapterClickListener = onMyAdapterClickListener;
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

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.dishName.setText(dishesFilter.get(pos).getName());
        holder.dishPrice.setText(dishesFilter.get(pos).getStrPrice());
        holder.dishPreparationTime.setText(dishesFilter.get(pos).getStrTime_preparation());
        holder.dishPhoto.setImageBitmap(SqliteHelper.getByteArrayAsBitmap(dishesFilter.get(pos).getImage()));
        if(dishesFilter.get(pos).isFavorite()){
            holder.dishFavorite.setVisibility(pos);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dishesFilter.size();
    }


    //Clase necesaria para la implementación del RecyclerView
    public class DishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        public TextView dishName;
        public TextView dishPrice;
        public TextView dishPreparationTime;
        public ImageView dishPhoto;
        public ImageView dishFavorite;

        DishViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.card_view_dish);
            dishName = (TextView) itemView.findViewById(R.id.name_dish);
            dishPrice = (TextView) itemView.findViewById(R.id.dish_price);
            dishPhoto = (ImageView) itemView.findViewById(R.id.dish_photo);
            dishPreparationTime = (TextView) itemView.findViewById(R.id.dish_preparation);
            dishFavorite = (ImageView) itemView.findViewById(R.id.dish_favorite);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            //Log.d("AdapterRecyclerView", "onClick: " + pos + "  Name: "+ dishName.getText() );

            onMyAdapterClickListener.onItemClick(pos);
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dishesFilter = dishes;
                } else {
                    List<Dish> filteredList = new ArrayList<>();
                    for (Dish row : dishes) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getStrPrice().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    dishesFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dishesFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dishesFilter = (ArrayList<Dish>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
