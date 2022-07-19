package com.example.smartcarpark;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;
    DatabaseReference userRef;
    CircleImageView imageView;
    TextView forgetPasswordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userRef = FirebaseDatabase.getInstance().getReference("UserInfo");

        editTextEmail = (EditText) findViewById(R.id.etEmails);
        editTextPassword = (EditText) findViewById(R.id.etPasswords);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        imageView = (CircleImageView) findViewById(R.id.profileImage);
        forgetPasswordTv = (TextView) findViewById(R.id.forgetPassword);

        forgetPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgetPasswordDialog();
            }
        });


        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);

    }

    private void showForgetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("FORGET PASSWORD");
        builder.setMessage("Please enter your email address.");

        View pass_rest_layout = LayoutInflater.from(MainActivity.this).inflate(R.layout.forget_password_layout, null );

        final EditText pwd_email = (EditText) pass_rest_layout.findViewById(R.id.edtForgetUserEmail);

        builder.setView(pass_rest_layout);
        builder.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                final AlertDialog waitingDialog = new SpotsDialog.Builder()
                        .setContext(MainActivity.this)
                        .setMessage("PLEASE WAIT").build();
                waitingDialog.show();

                if(!TextUtils.isEmpty(pwd_email.getText())) {
                    mAuth.sendPasswordResetEmail(pwd_email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            waitingDialog.dismiss();

                            Toast.makeText(MainActivity.this, "Reset Password link has been sent!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            waitingDialog.dismiss();
                            Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(MainActivity.this, "Kindly Enter Email to reset password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void userLogin()
    {
        String emails = editTextEmail.getText().toString().trim();
        String passwords = editTextPassword.getText().toString().trim();

        //String email = "alyabbbasss@gmail.com";
        //String password = "1234567";


        if(emails.isEmpty()){
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emails).matches()){
            editTextEmail.setError("Please Enter a valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if(passwords.isEmpty()){
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        if(passwords.length()<6){
            editTextPassword.setError("Password length should be 6");
            editTextPassword.requestFocus();
            return;
        }



        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(emails, passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                 if(task.isSuccessful()){
                     userRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserProfile profile = dataSnapshot.getValue(UserProfile.class);
                            profile.setuId(dataSnapshot.getKey());
                            Common.currentUser = profile;
                             finish();              //added
                             Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                             startActivity(intent);
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });


                 }else{
                     Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                 }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.buttonLogin:

                userLogin();
                break;

            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, RegistrationActivity.class));
                break;

        }

    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    } */

}
