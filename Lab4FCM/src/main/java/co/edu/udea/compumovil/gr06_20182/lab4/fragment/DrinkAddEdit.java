package co.edu.udea.compumovil.gr06_20182.lab4.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

import co.edu.udea.compumovil.gr06_20182.lab4.R;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Drink;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.ImageHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkAddEdit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrinkAddEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinkAddEdit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_NEW = "estado";
    private String id;
    private Boolean isNew;
    private EditText txtNameDrink,txtPrice;
    private CheckBox checkFavorite;
    private ImageView imgDrink;
    private Button btnOk;
    private Drink drink;
    final int REQUEST_CODE_GALLERY = 999;
    final String TAG = "DrinkAddEdit";
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private String currentImage;


    private OnFragmentListenerDrinkAddEdit mListener;

    public DrinkAddEdit() {
        // Required empty public constructor
    }

    private void initFirestone(){
        mFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("drinks");
    }


    public static DrinkAddEdit newInstance(String id, Boolean isNew) {
        DrinkAddEdit fragment = new DrinkAddEdit();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        args.putBoolean(ARG_NEW, isNew);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            isNew = getArguments().getBoolean(ARG_NEW);
        }
    }

    private void init(View view){
        txtNameDrink = view.findViewById(R.id.txtNameDrink);
        txtPrice = view.findViewById(R.id.txtPriceDrink);
        checkFavorite =  view.findViewById(R.id.checkFavoriteDrink);
        imgDrink = view.findViewById(R.id.imgDrink);
        btnOk = view.findViewById(R.id.btnOkDrink);
    }

    private void getData(){
        drink.setName(txtNameDrink.getText().toString().trim());
        drink.setFavorite(checkFavorite.isChecked());
        drink.setPrice(Float.parseFloat(txtPrice.getText().toString().trim()));
        drink.setImage(currentImage);
    }

    private void setData(){
        final File file;

        txtNameDrink.setText(drink.getName());
        txtPrice.setText(drink.getPrice().toString());
        checkFavorite.setChecked(drink.isFavorite());


        try {
            file = File.createTempFile(drink.getImage(), "jpg");
            storageReference.child(drink.getImage()).getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Glide.with(imgDrink.getContext())
                                    .load(file.getAbsolutePath())
                                    .into(imgDrink);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("DishAddEdit", "Ocurrio un error al mostrar la imagen");
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            Log.e("DrinkAddEdit", "Ocurrió un error en la descarga de imágenes");
            e.printStackTrace();
        }
    }

    private void addDrink(){
        CollectionReference platos = mFirestore.collection("drinks");
        platos.add(drink)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Drink add with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error agregando una bebda", e);
                    }
                });
    }

    private void updateDrink(){
        CollectionReference platos = mFirestore.collection("drinks");
        platos.document(id)
                .set(drink)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Drink update with ID: " );
                        Toast.makeText(getContext(), getString(R.string.ok_insert), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing Drink", e);
                        Toast.makeText(getContext(), getString(R.string.ok_update), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void viewDataOnScreenToUpdate(){
        CollectionReference db = mFirestore.collection("drinks");
        DocumentReference docRef = db.document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        drink = document.toObject(Drink.class);
                        setData();

                    } else {
                        Log.d(TAG, "No existe la bebida");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drink_add_edit, container, false);
        initFirestone();
        init(view);

        Log.d(TAG,"Id: " + id);
        // If data exists, this is to show on Screen.
        if(!isNew){
            viewDataOnScreenToUpdate();
        }

        imgDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_GALLERY);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] img = null;
                if(isNew){
                    drink = new Drink();
                    // TODO: cambiar
                }

                try{
                    img = ImageHelper.imageViewToByte(imgDrink);
                }catch (Exception e){
                    img = ImageHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.coffee));
                }

                currentImage = UUID.randomUUID().toString() + ".png";
                storageReference = storageReference.child(currentImage);

                UploadTask uploadTask = storageReference.putBytes(img);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            String url = downloadUri.getLastPathSegment();

                            Log.w(TAG, "image URL: " + url);

                            getData();

                            if(isNew){
                                addDrink();
                            }else{
                                updateDrink();
                            }

                            onButtonPressed(isNew);


                        } else {
                            Log.e(TAG, "Ocurrió un error en la subida");
                        }
                    }
                });

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Boolean isNew) {
        if (mListener != null) {
            mListener.onFragmentDrinkAddEditInteraction(isNew);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListenerDrinkAddEdit) {
            mListener = (OnFragmentListenerDrinkAddEdit) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentListenerDrinkAddEdit {
        void onFragmentDrinkAddEditInteraction(Boolean isNew);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_GALLERY);
            }
            else{
                Toast.makeText(getContext(),getString(R.string.without_access_security),Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == getActivity().RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgDrink.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
