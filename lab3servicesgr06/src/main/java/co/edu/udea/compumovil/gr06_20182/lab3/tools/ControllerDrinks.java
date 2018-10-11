package co.edu.udea.compumovil.gr06_20182.lab3.tools;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DrinksDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControllerDrinks implements Callback<DrinksDto> {
    List<Drink> drinks;
    OnMyDrinksResponse onMyDrinksResponse;

    static final String BASE_URL = "http://www.mocky.io/v2/5bb69ce32e00004d00683718";

    public ControllerDrinks(OnMyDrinksResponse onMyDrinksResponse){
        this.onMyDrinksResponse = onMyDrinksResponse;
    }

    public void start(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService apiAPI = retrofit.create(ApiService.class);

        Call<DrinksDto> call = apiAPI.getDrinks();

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<DrinksDto> call, Response<DrinksDto> response) {
        if(response.isSuccessful()){
            drinks = Mapper.MapDrinks(response.body().getDrinks());
            if(this.onMyDrinksResponse != null){
                this.onMyDrinksResponse.onResponse(drinks);
            }
        }
    }

    @Override
    public void onFailure(Call<DrinksDto> call, Throwable t) {
        Log.d("ControllerDrinks", "onFailure: " + t.getMessage());
    }
}
