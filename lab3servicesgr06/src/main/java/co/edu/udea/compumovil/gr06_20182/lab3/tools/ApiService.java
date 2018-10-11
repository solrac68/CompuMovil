package co.edu.udea.compumovil.gr06_20182.lab3.tools;


import co.edu.udea.compumovil.gr06_20182.lab3.model.DrinksDto;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DishesDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiService {
    @GET("/")
    Call<DishesDto> getDishes();

    @GET("/")
    Call<DrinksDto> getDrinks();
}
