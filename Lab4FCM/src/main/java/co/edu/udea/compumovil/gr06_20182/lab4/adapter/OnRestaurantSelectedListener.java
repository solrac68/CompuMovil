package co.edu.udea.compumovil.gr06_20182.lab4.adapter;

import com.google.firebase.firestore.DocumentSnapshot;

public interface OnRestaurantSelectedListener {
    void onRestaurantSelected(DocumentSnapshot restaurant);
}
