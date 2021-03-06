package com.vpiska.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vpiska.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddAd extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 71;
    ImageButton image_button_food;
    Uri filepath;
    EditText editTextNameAds, edit_text_more_details, edit_text_number_phone, text_adress;
    Button button_add_ad;
    String plasefromdb;
    FirebaseUser user;
    String city;
    FirebaseFirestore firebaseFirestore;
    public Map<String, Object> adressMap;
    int add;
    FirebaseStorage storage;
    StorageReference storageReference;
    byte[] Data;
    Geocoder geocoder;
    List<Place.Field> fieldList;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ad_layout);
        image_button_food = findViewById(R.id.image_button_food);
        editTextNameAds = findViewById(R.id.edit_text_name_ads);
        edit_text_more_details = findViewById(R.id.edit_text_more_details);
        edit_text_number_phone = findViewById(R.id.edit_text_number_phone);
        button_add_ad = findViewById(R.id.button_add_ad);
        Places.initialize(getApplicationContext(), "");
        text_adress = findViewById(R.id.text_adress);
        text_adress.setFocusable(false);
        storage = FirebaseStorage.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        text_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(AddAd.this);
                startActivityForResult(intent, 100);

            }
        });


        AddAd();


        image_button_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберете фотографию"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                image_button_food.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                Data = baos.toByteArray();


            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        if (requestCode == 100 && resultCode == RESULT_OK) {
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());
            Place place = Autocomplete.getPlaceFromIntent(data);
            try {
                addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                city = addresses.get(0).getLocality();
                Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            plasefromdb = place.getAddress();
            text_adress.setText(plasefromdb);
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void AddAd() {
        final Date date = new Date();
        final Date newDate = new Date(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        final String stringdate = dt.format(newDate);
        //storageReference = storageReference.child("ad_images/" + user.getUid());


        adressMap = new HashMap<>();

        button_add_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storageReference = FirebaseStorage.getInstance().getReference().child("ad_images");

                final StorageReference ImagePath = storageReference.child(user.getUid()+Objects.requireNonNull(filepath.getLastPathSegment()));

                ImagePath.putBytes(Data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                        ImagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(final Uri uri) {

                                                firebaseFirestore.collection("MyAd").document(user.getUid()).collection(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        add = queryDocumentSnapshots.size();
                                                        if (add<=2){
                                                            adressMap.put("adImage", String.valueOf(uri));
                                                            adressMap.put("Name_Ads", editTextNameAds.getText().toString());
                                                            adressMap.put("More_Details", edit_text_more_details.getText().toString());
                                                            adressMap.put("Adres", plasefromdb);
                                                            adressMap.put("Phone", edit_text_number_phone.getText().toString());
                                                            adressMap.put("Time", stringdate);
                                                            adressMap.put("User", user.getUid());
                                                            firebaseFirestore.collection("AddAd").document(city).collection(city).document().set(adressMap);
                                                            firebaseFirestore.collection("MyAd").document(user.getUid()).collection(user.getUid()).document().set(adressMap);
                                                            Intent intent = new Intent(AddAd.this, Home.class);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(AddAd.this, "Возможно добавить только 3 объявления! Удалите старые", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                /*firebaseFirestore.collection("MyAd").get()
                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                add = queryDocumentSnapshots.size();
                                                                if (queryDocumentSnapshots.isEmpty()){
                                                                    adressMap.put("adImage", String.valueOf(uri));
                                                                    adressMap.put("Name_Ads", editTextNameAds.getText().toString());
                                                                    adressMap.put("More_Details", edit_text_more_details.getText().toString());
                                                                    adressMap.put("Adres", plasefromdb);
                                                                    adressMap.put("Phone", edit_text_number_phone.getText().toString());
                                                                    adressMap.put("Time", stringdate);
                                                                    adressMap.put("User", user.getUid());
                                                                    firebaseFirestore.collection("AddAd").document(city).collection(city).document().set(adressMap);
                                                                    firebaseFirestore.collection("MyAd").document(user.getUid()).set(adressMap);
                                                                    Intent intent = new Intent(AddAd.this, Home.class);
                                                                    startActivity(intent);
                                                                } else if (add<=2){
                                                                    adressMap.put("adImage", String.valueOf(uri));
                                                                    adressMap.put("Name_Ads", editTextNameAds.getText().toString());
                                                                    adressMap.put("More_Details", edit_text_more_details.getText().toString());
                                                                    adressMap.put("Adres", plasefromdb);
                                                                    adressMap.put("Phone", edit_text_number_phone.getText().toString());
                                                                    adressMap.put("Time", stringdate);
                                                                    adressMap.put("User", user.getUid());
                                                                    firebaseFirestore.collection("MyAd").document().set(adressMap);
                                                                    firebaseFirestore.collection("AddAd").document(city).collection(city).document().set(adressMap);
                                                                    Intent intent = new Intent(AddAd.this, Home.class);
                                                                    startActivity(intent);
                                                                } else {
                                                                    Toast.makeText(AddAd.this, "Возможно добавить только 3 объявления! Удалите старые", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                        });*/

                            }
                        });

                    }
                });



            }
        });


               /* firebaseFirestore.collection("AddAd").document(user.getUid())
                        .collection(String.valueOf(1)).document(String.valueOf(1)).set(adressMap);*/
              /*  firebaseFirestore.collection("AddAd").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        add =  queryDocumentSnapshots.getDocuments().size();
                        storageReference = storageReference.child("ad_images/" + Arrays.toString(Data));
                        if (queryDocumentSnapshots.isEmpty()){
                            //storageReference = storageReference.child("ad_images/" + user.getUid()+add);
                            //adressMap.put("Image", storageReference.getDownloadUrl());
                            //storageReference.getDownloadUrl();
                            storageReference.putBytes(Data);
                            firebaseFirestore.collection("AddAd").document(user.getUid()+add).set(adressMap);
                            Intent intent = new Intent(AddAd.this, Home.class);
                            startActivity(intent);
                        } else if (add<=2){
                            //firebaseFirestore.collection("AddAd").document(user.getUid()).collection(String.valueOf(1)).document(String.valueOf(add+1)).set(adressMap);
                            firebaseFirestore.collection("AddAd").document(user.getUid()+add).set(adressMap);
                            storageReference = storageReference.child("ad_images/" + user.getUid()+add);
                            storageReference.putBytes(Data);
                            Intent intent = new Intent(AddAd.this, Home.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddAd.this, "Возможно добавить только 3 объявления! Удалите старые", Toast.LENGTH_SHORT).show();
                        }

                    }
                });*/


    }


}
