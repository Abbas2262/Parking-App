package com.example.smartcarpark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingActivity extends AppCompatActivity {

    EditText editTextName;
    Button buttonAdd;
    Spinner spinnerGenres;

    FirebaseDatabase database;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);



        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("CustomerBook");

        editTextName = (EditText) findViewById(R.id.editTextBook);
        buttonAdd = (Button) findViewById(R.id.buttonAddBooking);
        spinnerGenres = (Spinner) findViewById(R.id.spinnerGenre);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void addBooking(){

        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenres.getSelectedItem().toString();


        if(!TextUtils.isEmpty(name)){

            String id = databaseReference.push().getKey();

            Booker booker = new Booker(id,name, genre);

            databaseReference.setValue(booker);

            Toast.makeText(this, "Booking Successful", Toast.LENGTH_SHORT).show();


        }else{

            Toast.makeText(this, "You should enter a name", Toast.LENGTH_SHORT).show();
        }


    }

}
