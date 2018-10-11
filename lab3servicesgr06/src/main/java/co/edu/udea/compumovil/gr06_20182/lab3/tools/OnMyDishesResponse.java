package co.edu.udea.compumovil.gr06_20182.lab3.tools;

import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab3.model.Dish;

public interface OnMyDishesResponse {
    void onResponse(List<Dish> dishes);
}
