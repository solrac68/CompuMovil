package co.edu.udea.compumovil.gr06_20182.lab4.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.SqliteHelper;


public class AdapterRecyclerDrinkView extends RecyclerView.Adapter<AdapterRecyclerDrinkView.DishViewHolder> implements Filterable{

    List<Drink> drinks;
    List<Drink> drinksFilter;
    OnMyAdapterClickListener onMyAdapterClickListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterRecyclerDrinkView(List<Drink> drinks, OnMyAdapterClickListener onMyAdapterClickListener) {
        this.drinks = drinks;
        this.drinksFilter = drinks;
        this.onMyAdapterClickListener = onMyAdapterClickListener;
    }

    public void updateAdapter(List<Drink> drinks){
        this.drinks.clear();
        this.drinks = drinks;
        this.drinksFilter = drinks;
        //this.notifyData
        this.notifyDataSetChanged();
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
        holder.dishName.setText(drinksFilter.get(pos).getName());
        holder.dishPrice.setText(drinksFilter.get(pos).getStrPrice());
        // TODO: cambiar
        //holder.dishPhoto.setImageBitmap(SqliteHelper.getByteArrayAsBitmap(drinksFilter.get(pos).getImage()));
        if(drinksFilter.get(pos).isFavorite()){
            holder.dishFavorite.setVisibility(pos);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        // TODO: modificar
        return 0;
        //return drinksFilter.size();
    }


    //Clase necesaria para la implementación del RecyclerView
    public class DishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardView;
        public TextView dishName;
        public TextView dishPrice;
        public ImageView dishPhoto;
        public ImageView dishFavorite;

        DishViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.card_view_dish);
            dishName = (TextView) itemView.findViewById(R.id.name_dish);
            dishPrice = (TextView) itemView.findViewById(R.id.dish_price);
            dishPhoto = (ImageView) itemView.findViewById(R.id.dish_photo);
            dishFavorite = (ImageView) itemView.findViewById(R.id.dish_favorite);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();

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
                    drinksFilter = drinks;
                } else {
                    List<Drink> filteredList = new ArrayList<>();
                    for (Drink row : drinks) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getStrPrice().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    drinksFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = drinksFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                drinksFilter = (ArrayList<Drink>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
