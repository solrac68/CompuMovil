package co.edu.udea.compumovil.gr06_20182.lab3.tools;

import java.util.List;
import co.edu.udea.compumovil.gr06_20182.lab3.model.Drink;

public interface OnMyDrinksResponse {
    void onResponse(List<Drink> drinks);
}
