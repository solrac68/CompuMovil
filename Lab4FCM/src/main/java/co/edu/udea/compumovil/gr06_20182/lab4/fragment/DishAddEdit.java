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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import co.edu.udea.compumovil.gr06_20182.lab4.activities.RegisterActivity;
import co.edu.udea.compumovil.gr06_20182.lab4.model.Dish;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.ImageHelper;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.Mapper;
import co.edu.udea.compumovil.gr06_20182.lab4.tools.SqliteHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DishAddEdit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DishAddEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DishAddEdit extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";
    private static final String ARG_NEW = "estado";
    private static String uniqueBlob;
    private String id;
    private Boolean isNew;
    private EditText txtNameDish,txtTimePreparation,txtPrice;
    private CheckBox checkFavorite;
    private ImageView imgDish;
    private Button btnOk;
    private Dish dish;
    public static SqliteHelper sqliteHelper;
    final int REQUEST_CODE_GALLERY = 999;
    final int REQUEST_CODE_PHONE = 998;
    final String TAG = "DishAddEdit";
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;
    private String currentImage;


    private OnFragmentListenerDishAddEdit mListener;

    public DishAddEdit() {
        // Required empty public constructor
        initFirestone();
    }

    private void initFirestone(){
        mFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("dishes");
    }


    public static DishAddEdit newInstance(String id, Boolean isNew) {
        DishAddEdit fragment = new DishAddEdit();
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
        sqliteHelper = new SqliteHelper(getContext());
    }

    private void init(View view){
        txtNameDish = view.findViewById(R.id.txtNameDish);
        txtTimePreparation = view.findViewById(R.id.txtTimePreparation);
        txtPrice = view.findViewById(R.id.txtPrice);
        checkFavorite =  view.findViewById(R.id.checkFavorite);
        imgDish = view.findViewById(R.id.imgDish);
        btnOk = view.findViewById(R.id.btnOk);
    }

    private void setData(){
        final File file;

        txtNameDish.setText(dish.getName());
        txtTimePreparation.setText(dish.getTime_preparation().toString());
        txtPrice.setText(dish.getPrice().toString());
        checkFavorite.setChecked(dish.isFavorite());


        try {
            file = File.createTempFile(dish.getImage(), "jpg");
            storageReference.child(dish.getImage()).getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Glide.with(imgDish.getContext())
                                    .load(file.getAbsolutePath())
                                    .into(imgDish);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("DishAddEdit", "Ocurrio un error al mostrar la imagen");
                    e.printStackTrace();
                }
            });
        }catch (Exception e){
            Log.e("DishAddEdit", "Ocurrió un error en la descarga de imágenes");
            e.printStackTrace();
        }
    }

    private void getData(){
        dish.setName(txtNameDish.getText().toString().trim());
        dish.setFavorite(checkFavorite.isChecked());
        dish.setPrice(Integer.parseInt(txtPrice.getText().toString().trim()));
        dish.setTime_preparation(Integer.parseInt(txtTimePreparation.getText().toString().trim()));
        dish.setImage(currentImage);
    }

    private void addDish(){
        CollectionReference platos = mFirestore.collection("dishes");
        platos.add(dish)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Dish add with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error agregando un plato", e);
                    }
                });
    }

    private void updateDish(){
        CollectionReference platos = mFirestore.collection("dishes");
        platos.document(id)
                .set(dish)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Dish update with ID: " );
                        Toast.makeText(getContext(), getString(R.string.ok_insert), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing dish", e);
                        Toast.makeText(getContext(), getString(R.string.ok_update), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void viewDataOnScreenToUpdate(){
        // TODO: cambiar
        CollectionReference db = mFirestore.collection("dishes");
        DocumentReference docRef = db.document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        dish = document.toObject(Dish.class);
                        setData();

                    } else {
                        Log.d(TAG, "No existe el plato");
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
        View view = inflater.inflate(R.layout.fragment_dish_add_edit, container, false);

        init(view);

        // If data exists, this is to show on Screen.
        if(!isNew){
            viewDataOnScreenToUpdate();
        }

        imgDish.setOnClickListener(new View.OnClickListener() {
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
                    dish = new Dish();
                    // TODO: cambiar
                    dish.setType("H");
                }

                try{
                    img = ImageHelper.imageViewToByte(imgDish);
                }catch (Exception e){
                    img = ImageHelper.getBitmapAsByteArray(BitmapFactory.decodeResource(getResources(),R.drawable.octopus));
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

                        // Continue with the task to get the download URL
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
                                addDish();
                            }else{
                                updateDish();
                            }

                            //Toast.makeText(getContext(), isNew?getString(R.string.ok_insert):getString(R.string.ok_update), Toast.LENGTH_SHORT).show();
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
            mListener.onFragmentInteraction(isNew);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListenerDishAddEdit) {
            mListener = (OnFragmentListenerDishAddEdit) context;
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

    public interface OnFragmentListenerDishAddEdit {
        // TODO: Update argument type and name
        void onFragmentInteraction(Boolean isNew);
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
                imgDish.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
