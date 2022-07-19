package com.example.smartcarpark;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    EditText editTextEmails, editTextPasswords , editTextNames, editTextCars;
    private FirebaseAuth mAuth;
    String name, email , password, car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextEmails = (EditText) findViewById(R.id.editTextEmail);
        editTextPasswords = (EditText) findViewById(R.id.editTextPassword);
        editTextCars = (EditText) findViewById(R.id.editTextCar);
        editTextNames = (EditText) findViewById(R.id.editTextName);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);

    }

    private void registerUser(){
         name = editTextNames.getText().toString().trim();
         email = editTextEmails.getText().toString().trim();
         password = editTextPasswords.getText().toString().trim();
         car = editTextCars.getText().toString().trim();


        //String email = "alyabbbasss@gmail.com";
        //String password = "1234567";

        if(name.isEmpty()){
            editTextNames.setError("Name is Required");
            editTextNames.requestFocus();
            return;
        }

        if(name.length()>20){
            editTextNames.setError("Name length should be less than 20");
            editTextNames.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmails.setError("Email is Required");
            editTextEmails.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmails.setError("Please Enter a valid Email");
            editTextEmails.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPasswords.setError("Password is Required");
            editTextPasswords.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextPasswords.setError("Password length should be 6");
            editTextPasswords.requestFocus();
            return;
        }
        if(car.isEmpty()){
            editTextCars.setError("Car number is Required");
            editTextCars.requestFocus();
            return;
        }

        if(car.length()<8){
            editTextCars.setError("Car length should be 8");
            editTextCars.requestFocus();
            return;
        }




        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(RegistrationActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    sendUserData();
                    //Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                   // Intent intent = new Intent(RegistrationActivity.this,ProfileActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(intent);

                    //added
                    finish();
                    startActivity(new Intent(RegistrationActivity.this,ProfileActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Registration Failed"+ task.getException(),Toast.LENGTH_SHORT).show();

                   if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Your Already Registered",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSignUp:

                registerUser();
                break;


            case R.id.textViewLogin:
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;

        }


    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference("UserInfo").child(mAuth.getCurrentUser().getUid());
        UserProfile userProfile = new UserProfile(name, email , car,"");
        myRef.setValue(userProfile);
    }

}
