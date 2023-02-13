package com.example.uitscuisine;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentAddRecipe extends Fragment {

    Button addRecipe;
    ImageView addRecipeImage;
    EditText recipeName, serves, duration, ingredients, steps;
    Spinner category, difficulty;
    ArrayAdapter<CharSequence> categoryAdapter, difficultyAdapter;
    String choosedCategory, choosedDifficulty;
    int h, m;

    MainActivity mainActivity;
    LottieDialog Dialog;

    Uri choosedImage;
    String posterName, posterAvatar;
    int recipeId = (int) Math.floor(((Math.random() * 899999) + 100000));

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        mainActivity = (MainActivity) getActivity();
        Dialog = new LottieDialog(getActivity())
                .setAnimation(R.raw.pan)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setMessage("Loading...")
                .setMessageTextSize(30)
                .setDialogBackground(Color.WHITE)
                .setAnimationViewHeight(400)
                .setCancelable(false);

        checkPhone();

        mapping(view);
        addRecipeImage();

        selectDuration();
        selectDifficulty();
        selectCategory();

        addRecipe();

        return view;
    }

    private void mapping(View view) {
        addRecipe = (Button) view.findViewById(R.id.addRecipeButton);
        recipeName = (EditText) view.findViewById(R.id.postRecipeNameEdt);
        serves = (EditText) view.findViewById(R.id.postServesEdt);
        duration = (EditText) view.findViewById(R.id.postDurationEdt);
        difficulty = (Spinner) view.findViewById(R.id.postDifficultyEdt);
        category = (Spinner) view.findViewById(R.id.postCategoryEdt);
        ingredients = (EditText) view.findViewById(R.id.postIngredientsEdt);
        steps = (EditText) view.findViewById(R.id.postStepsEdt);
        addRecipeImage = (ImageView) view.findViewById(R.id.addRecipeImage);
    }

    public void addRecipeImage() {
        addRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        Uri selectedImage = data.getData();
                        addRecipeImage.setImageURI(selectedImage);
                        choosedImage = selectedImage;
                    }
                }
            }

    );

//    public void upPost() {
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Posts");
//        addRecipe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Post post = new Post(recipeName.getText()+"", serves.getText()+"", duration.getText()+"", difficulty.getText()+"", category.getText()+"", ingredients.getText()+"", steps.getText()+"", addRecipeImage.get);
//                database.child(mainActivity.getMobile()).setValue(post);
//            }
//        });
//    }

    private void upPost(Uri imageUri, int recipeId) {
        Dialog.show();
        assert mainActivity != null;
        String phone_check_login = mainActivity.getMobile();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
        Query checkUser = database.orderByKey();
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String phoneFromDB = dataSnapshot.child("phone").getValue().toString();
                    if (phone_check_login.equals(phoneFromDB)) {
                        posterName = dataSnapshot.child("name").getValue().toString();
                        posterAvatar = dataSnapshot.child("imageURl").getValue().toString();
                        Dialog.dismiss();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        String posterPhone = mainActivity.getMobile();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("images/"+imageUri.getLastPathSegment());
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageURL = uri.toString();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Posts");
                        Post post = new Post(recipeName.getText()+"", serves.getText()+""
                                , duration.getText()+"", choosedDifficulty
                                , choosedCategory, ingredients.getText()+""
                                , steps.getText()+"", imageURL, posterPhone
                                , posterName, posterAvatar, recipeId);
                        database.child("Recipe ID: "+recipeId).setValue(post);
                        Toast.makeText(getActivity(), "SUCCESSFULLY!!", Toast.LENGTH_SHORT).show();
                        Dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkPhone() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        }
        else{
            try {
                Dialog.show();
                assert mainActivity != null;
                String phone_check_login = mainActivity.getMobile();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Profile");
                Query checkUser = database.orderByKey();
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String phoneFromDB = dataSnapshot.child("phone").getValue().toString();
                            if (phone_check_login.equals(phoneFromDB)) {
                                posterName = dataSnapshot.child("name").getValue().toString();
                                posterAvatar = dataSnapshot.child("imageURl").getValue().toString();
                                Dialog.dismiss();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectDuration() {
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        h = hourOfDay;
                        m = minute;
                        String time = h + ":" + m;
                        SimpleDateFormat format24 = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = format24.parse(time);
                            duration.setText(format24.format(date).replace(":", "h").concat("m"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 0, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(h, m);
                timePickerDialog.show();
            }
        });
    }

    public void selectDifficulty() {
        difficultyAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.difficulty_items, android.R.layout.simple_spinner_dropdown_item);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(difficultyAdapter);
        difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choosedDifficulty = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    public void selectCategory() {
        categoryAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.category_items, android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                choosedCategory = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    public void addRecipe() {
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.show();
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("Posts");
                Query check = database.orderByKey();
                check.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            upPost(choosedImage, recipeId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Dialog.dismiss();
            }
        });
    }
}
