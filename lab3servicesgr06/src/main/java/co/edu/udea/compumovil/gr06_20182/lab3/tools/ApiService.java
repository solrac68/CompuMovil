package co.edu.udea.compumovil.gr06_20182.lab3.tools;


import co.edu.udea.compumovil.gr06_20182.lab3.model.DrinksDto;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DishesDto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    @GET("/v2/{dishes}")
    Call<DishesDto> getDishes(@Path("dishes") String dishes);

    @GET("/v2/{drinks}")
    Call<DrinksDto> getDrinks(@Path("drinks") String drinks);
}
