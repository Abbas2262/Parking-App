package com.example.smartcarpark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {

    EditText username, useremail, userpassword, usercarNo;
    Button updateBtn;

    CircleImageView circleImageView;
    DatabaseReference userRef;
    Uri uriProfileImage;
    String profileImageUrl;
    ProgressBar progressBar;
    private static final int CHOOSE_IMAGE = 101;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        userRef = FirebaseDatabase.getInstance().getReference("UserInfo");

        progressBar = new ProgressBar(this);

        circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        username = (EditText) findViewById(R.id.usernameUpdate);
        useremail = (EditText) findViewById(R.id.useremailUpdate);

        usercarNo = (EditText) findViewById(R.id.usercarnoUpdate);

        updateBtn = (Button) findViewById(R.id.buttonUpdate);

        if(Common.currentUser != null){
            username.setText(Common.currentUser.getUserName());
            useremail.setText(Common.currentUser.getUserEmail());
            usercarNo.setText(Common.currentUser.getUserCar());
//            if(!Common.currentUser.getUserImage().equals(""))
//                Glide.with(this).load(Common.currentUser.getUserImage()).into(circleImageView);
        }

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(!TextUtils.isEmpty(username.getText().toString()) && !TextUtils.isEmpty(useremail.getText().toString()) && !TextUtils.isEmpty(usercarNo.getText().toString())){
                    HashMap<String, Object> update = new HashMap<>();
                    update.put("userName", username.getText().toString());
                    update.put("userEmail", useremail.getText().toString());
                    update.put("userCar", usercarNo.getText().toString());

                    userRef.child(Common.currentUser.getuId()).updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(UpdateProfile.this, "Profile Information Updated!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Kindly enter the requried information!", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }


    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image" ), CHOOSE_IMAGE);  // create file chooser
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfileImage = data.getData();

            Uri uri = data.getData();
            if(uri != null){
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Uploading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String imageName = UUID.randomUUID().toString();
                final StorageReference imageFolder = storageReference.child("images/"+imageName);
                imageFolder.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();


                                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.d("url", ""+uri);

                                        HashMap<String, Object> update = new HashMap<>();
                                        update.put("userImage", String.valueOf(uri));
                                        userRef.child(Common.currentUser.getuId()).updateChildren(update);

                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+ progress + "%");
                    }
                });
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                circleImageView.setImageBitmap(bitmap);



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

                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                     profileImageUrl = String.valueOf(uri);

                                }
                            });
                            Log.d("url", profileImageUrl+"");
                            HashMap<String, Object> update = new HashMap<>();
                            update.put("userImage", String.valueOf(profileImageUrl));
                            userRef.child(Common.currentUser.getuId()).updateChildren(update);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(UpdateProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
