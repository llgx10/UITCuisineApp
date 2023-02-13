package com.example.uitscuisine;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    EditText nameEditProfile, addressEditProfile
            , weightEditProfile, heightEditProfile, phoneEditProfile ;
    Button EditProfile1;
    Spinner genderEditProfile;
    TextView birthdayEditProfile;
    private DatePickerDialog.OnDateSetListener mDataSetListener;
    FloatingActionButton chooseImage;
    CircleImageView imageEditProfile;
    LottieDialog Dialog;
    String name, address,birthday,gender,weight,height,imageURL,phone;

    Intent intent1;
    String uploadFilepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        chooseImage   = (FloatingActionButton) findViewById(R.id.chooseImage);
        imageEditProfile = (CircleImageView)findViewById(R.id.imageEditProfile);
        EditProfile1     = (Button)findViewById(R.id.EditProfile);
        nameEditProfile = (EditText)findViewById(R.id.nameEditProfile);
        addressEditProfile= (EditText)findViewById(R.id.addressEditProfile);
        birthdayEditProfile=(TextView) findViewById(R.id.birthdayEditProfile);
        genderEditProfile= (Spinner) findViewById(R.id.genderEditProfile);
        weightEditProfile = (EditText)findViewById(R.id.weightEditProfile);
        heightEditProfile = (EditText)findViewById(R.id.heightEditProfile);
        phoneEditProfile= (EditText)findViewById(R.id.phoneEditProfile);

        Dialog = new LottieDialog(EditProfile.this)
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Update Profile Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);

        birthdayEditProfile.setOnClickListener(this::chooseBirthday);
        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                String date = day + "/" + month + "/" + year;
                birthdayEditProfile.setText(date);
            }
        };

        ArrayList<String> myAdapter = new ArrayList<String>();
        myAdapter.add("your gender");
        myAdapter.add("Others");
        myAdapter.add("Male");
        myAdapter.add("Female");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,myAdapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderEditProfile.setAdapter(adapter);
        chooseImage.setOnClickListener(this::pickImage);
        EditProfile1.setOnClickListener(this::SaveProfile);


        login_check();

    }

    public void chooseBirthday(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(
                EditProfile.this,
                R.style.DatePickerStyle,
                //android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                mDataSetListener,
                year,month,day
        );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void pickImage(View view){
        intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent1);
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK && result.getData()!=null){
                        Uri seleimage = result.getData().getData();
                        int reqHeight = imageEditProfile.getHeight();
                        int reqWidth = imageEditProfile.getWidth();
                        imageEditProfile.setImageURI(seleimage);
                        uploadFilepath = seleimage.toString();
                        uploadImage(seleimage);
                    }
                }
            }
    );

    public void SaveProfile(View view) {

        Dialog.show();
        try {
            if (!checkName() || !checkAddress() || !checkGender() || !checkWeight() || !checkHeight() || !checkBirthday()
            ||!checkPhone()) {
                Dialog.dismiss();
                return;
            }else{
                Intent intent = getIntent();
                String email_check_login = intent.getStringExtra("email");
                String phone_check_login = intent.getStringExtra("mobile");
                Toast.makeText(EditProfile.this, "Update profile successfully!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(EditProfile.this,myProfile.class);
                intent1.putExtra("mobile",phone_check_login);
                intent1.putExtra("email",email_check_login);
                startActivity(intent1);
                Dialog.dismiss();
            }
        }catch(IllegalStateException e){
            e.printStackTrace();
        }
    }
private void uploadImage(Uri imageUri){

    Dialog.show();
    if (!checkName() || !checkAddress() || !checkGender() || !checkWeight() || !checkHeight() || !checkBirthday()
            ||!checkPhone()) {
        Dialog.dismiss();
        return;}
    StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("images/"+imageUri.getLastPathSegment());
    storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    phone = phoneEditProfile.getText().toString();
                    imageURL = uri.toString();
                    name = nameEditProfile.getText().toString();
                    address = addressEditProfile.getText().toString();
                    birthday = birthdayEditProfile.getText().toString();
                    gender = genderEditProfile.getItemAtPosition(genderEditProfile.getSelectedItemPosition()).toString();
                    weight = weightEditProfile.getText().toString();
                    height = heightEditProfile.getText().toString();
                    Profile profile = new Profile(name,phone,address,birthday,gender,weight,height,imageURL);
                    DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("Profile");
                    db.child(phone).setValue(profile);
                    Toast.makeText(EditProfile.this, "SUCCESSFULLY!!", Toast.LENGTH_SHORT).show();
                    Dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });

}
public void login_check(){
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    if (accessToken != null) {

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }else {
        try {
            Dialog.show();
            Intent intent = getIntent();
            String phone_check_login = intent.getStringExtra("mobile");
            //phoneEditProfile.setText(phone_check_login);
            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
            Query checkUser = database.orderByChild("phone").equalTo(phone_check_login);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        displayDataFromDB();
                        Toast.makeText(EditProfile.this, "YOUR PROFILE", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = getIntent();
                        String phone_check_login = intent.getStringExtra("mobile");
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
                        Profile profile2 = new Profile(phone_check_login);
                        database.child(phone_check_login).setValue(profile2);
                        Dialog.dismiss();
                        Toast.makeText(EditProfile.this, "Welcome to create profile", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
public void displayDataFromDB(){
    try {
        Dialog.show();
        Intent intent = getIntent();
        String phone_check_login = intent.getStringExtra("mobile");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
        Query checkUser = database.orderByKey();
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean foundUser = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    phoneEditProfile.setError(null);
                    String phoneFromDB = dataSnapshot.child("phone").getValue().toString();
                    if(phone_check_login.equals(phoneFromDB)) {
                        String nameFromDB = dataSnapshot.child("name").getValue().toString();
                        String addressFromDB = dataSnapshot.child("address").getValue().toString();
                        String birthdayFromDB = dataSnapshot.child("birthday").getValue().toString();
                        String genderFromDB = dataSnapshot.child("gender").getValue().toString();
                        String imageFromDB = dataSnapshot.child("imageURl").getValue().toString();
                        String weightFromDB = dataSnapshot.child("weight").getValue().toString();
                        String heightFromDB = dataSnapshot.child("height").getValue().toString();
                        nameEditProfile.setText(nameFromDB);
                        addressEditProfile.setText(addressFromDB);
                        birthdayEditProfile.setText(birthdayFromDB);
                        genderEditProfile.setAutofillHints(genderFromDB);
                        weightEditProfile.setText(weightFromDB);
                        heightEditProfile.setText(heightFromDB);
                        phoneEditProfile.setText(phone_check_login);
                        ArrayList<String> myAdapter1 = new ArrayList<String>();
                        myAdapter1.add(genderFromDB);
                        myAdapter1.add("Female");
                        myAdapter1.add("your gender");
                        myAdapter1.add("Others");
                        ArrayAdapter adapter1 = new ArrayAdapter(EditProfile.this, android.R.layout.simple_list_item_1, myAdapter1);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        genderEditProfile.setAdapter(adapter1);
                        Glide.with(EditProfile.this).load(imageFromDB).into(imageEditProfile);
                        Dialog.dismiss();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }catch(IllegalStateException e){
        e.printStackTrace();
    }
}


    private Boolean checkName(){
        String val = nameEditProfile.getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";
        if(val.isEmpty()){
            nameEditProfile.setError("Field cannot be empty");
            nameEditProfile.requestFocus();
            return false;
        }
        else if(val.length() >= 15){
            nameEditProfile.setError("Name is too long");
            nameEditProfile.requestFocus();
            return false;
        }
        else{
            nameEditProfile.setError(null);
            return true;
        }
    }
    private Boolean checkAddress(){
        String val = addressEditProfile.getText().toString();
        if(val.isEmpty()){
            addressEditProfile.setError("Field cannot be empty");
            addressEditProfile.requestFocus();
            return false;
        }
        else{
            addressEditProfile.setError(null);
            return true;
        }
    }
    private Boolean checkGender(){
        String val = genderEditProfile.getItemAtPosition(genderEditProfile.getSelectedItemPosition()).toString();

        if(val.isEmpty()){
            TextView err = (TextView)genderEditProfile.getSelectedView();
            err.setError("Field cannot be empty");
            err.requestFocus();
            return false;
        }
        else{
            TextView err = (TextView)genderEditProfile.getSelectedView();
            err.setError(null);
            return true;
        }
    }
    private Boolean checkWeight(){
        String val = weightEditProfile.getText().toString();
        if(val.isEmpty()){
            weightEditProfile.setError("Field cannot be empty");
            weightEditProfile.requestFocus();
            return false;
        }
        else{
            weightEditProfile.setError(null);
            return true;
        }
    }
    private Boolean checkHeight(){
        String val = heightEditProfile.getText().toString();
        if(val.isEmpty()){
            heightEditProfile.setError("Field cannot be empty");
            heightEditProfile.requestFocus();
            return false;
        }
        else{
            heightEditProfile.setError(null);
            return true;
        }
    }
    private Boolean checkBirthday(){
        String val = birthdayEditProfile.getText().toString();
        if(val.isEmpty()){
            birthdayEditProfile.setError("Field cannot be empty");
            birthdayEditProfile.requestFocus();
            return false;
        }
        else{
            birthdayEditProfile.setError(null);
            return true;
        }
    }
    private Boolean checkPhone(){
        String val = phoneEditProfile.getText().toString();
        Intent intent = getIntent();
        String phone_check_login = intent.getStringExtra("mobile");
        if(val.isEmpty()){
            phoneEditProfile.setError("Field cannot be empty");
            phoneEditProfile.requestFocus();
            return false;
        }else if(!val.equals(phone_check_login)){
            phoneEditProfile.setError("Please use your number phone registered");
            phoneEditProfile.requestFocus();
            return false;
        }
        else{
            phoneEditProfile.setError(null);
            return true;
        }
    }
}