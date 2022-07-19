package com.example.smartcarpark;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private static final int CHOOSE_IMAGE = 101;
    CircleImageView imageView;
    EditText editText;
    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl;
    TextView profileName, profileEmail, profileCarNumber;


    private Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userRef = FirebaseDatabase.getInstance().getReference("UserInfo");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

//        editText = (EditText) findViewById(R.id.etUserName);
        imageView = (CircleImageView) findViewById(R.id.profileImage);
        progressBar = findViewById(R.id.progressbar);
        profileName = findViewById(R.id.tvProfileName);
        profileEmail = findViewById(R.id.tvEmailAddress);
        profileCarNumber = findViewById(R.id.tvCarNumber);



        DatabaseReference databaseReference = firebaseDatabase.getReference("UserInfo").child(mAuth.getCurrentUser().getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                        UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                                                        userProfile.setuId(dataSnapshot.getKey());
                                                        Common.currentUser = userProfile;

                                                        profileName.setText("User Name : "+userProfile.getUserName());
                                                        profileEmail.setText("User Email : "+userProfile.getUserEmail());
                                                        profileCarNumber.setText("Car No : " + userProfile.getUserCar());
//                                                        if(userProfile.getUserImage().equals(""))
//                                                             Glide.with(ProfileActivity.this).load(userProfile.getUserImage()).into(imageView);

                                                        if(!Common.currentUser.getUserImage().equals("")){
                                                            Glide.with(ProfileActivity.this).load(Common.currentUser.getUserImage()).into(imageView);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        Toast.makeText(ProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                loadUserInformation();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showImageChooser();
            }
        });

        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                saveUserInformation();
                startActivity(new Intent(getApplicationContext(), SlotActivity.class));
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageView);
            }
//            if (user.getDisplayName() != null) {
//                editText.setText(user.getDisplayName());
//            }
        }
    }

//    private void saveUserInformation() {
//        String displayName = editText.getText().toString();
//
//        if(displayName.isEmpty()){
//            editText.setError("Name Required");
//            editText.requestFocus();
//            return;
//        }
//
//        FirebaseUser user = mAuth.getCurrentUser();
//
//        if(user != null && profileImageUrl != null){
//            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(displayName)
//                    .setPhotoUri(Uri.parse(profileImageUrl))
//                    .build();
//
//            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//
//                    if(task.isSuccessful()){
//                        Toast.makeText(ProfileActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {

        final StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jgp");

        if(uriProfileImage != null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);

                            profileImageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            HashMap<String, Object> update = new HashMap<>();
                            update.put("userImage", profileImageUrl);
                            userRef.child(Common.currentUser.getuId()).updateChildren(update);

                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.connection:
                Intent intent = new Intent(this, FindBluetoothDevice.class);
                startActivity(intent);
                break;
            case R.id.menuSlots:
                startActivity(new Intent(this,SlotActivity.class));
                break;


            case R.id.updateProfile:
                Intent intent1 = new Intent(this, UpdateProfile.class);
                startActivity(intent1);
                break;
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;

        }

            return true;
    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image" ), CHOOSE_IMAGE);  // create file chooser
    }

}
