package co.edu.udea.compumovil.gr06_20182.lab3.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr06_20182.lab3.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DishDto;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab3.model.DrinkDto;

public class Mapper {
    public static List<Dish> MapDishes(List<DishDto> dishesdto) {

        List<Dish> dishes = new ArrayList<>();
        Dish dish;
        byte[] image;
        for(DishDto dishdto : dishesdto) {
            dish = new Dish();
            //Log.d("MAPPER", dishdto.getImage()!=null?dishdto.getImage():"");
            image = dishdto.getImage() != null ? SqliteHelper.getBitmapAsByteArrayFromUrl(dishdto.getImage()): null;
            dish.setImage(image);
            dish.setFavorite(false);
            dish.setName(dishdto.getName());
            dish.setPrice(dishdto.getPrice());
            dish.setTime_preparation(dishdto.getTime());
            dish.setType(dishdto.getType());

            dishes.add(dish);
        }
        return dishes;
    }

    public static List<Drink> MapDrinks(List<DrinkDto> drinksdto) {
        List<Drink> drinks = new ArrayList<>();
        Drink drink;
        byte[] image;
        for(DrinkDto drinkDto : drinksdto) {
            drink = new Drink();
            image = SqliteHelper.getBitmapAsByteArrayFromUrl(drinkDto.getImage());
            drink.setImage(image);
            drink.setFavorite(false);
            drink.setName(drinkDto.getName());
            drink.setPrice(Integer.parseInt(drinkDto.getPrice()));
            drinks.add(drink);
        }
        return drinks;
    }
}