package co.edu.udea.compumovil.gr06_20182.lab3.tools;

import java.util.List;

public interface OnMyResponse<T> {
    void onResponse(List<T> obj);
    void onFailure(String msgError);
}
