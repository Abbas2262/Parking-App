package com.example.smartcarpark;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SlotActivity extends AppCompatActivity {

    List<Slot> slotList;
    DatabaseReference slotRef, bookHistoryRef;
    ToggleButton toggleButton1, toggleButton2, toggleButton3, toggleButton4;
    ImageView image1, image2, image3, image4;

    boolean isParked = false;
    private ProgressDialog progress;
    BluetoothAdapter bluetoothAdapter;
    private int ENABLE_REQUEST_CODE = 100;
    public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot);


        if(!TextUtils.isEmpty(Common.name) && !TextUtils.isEmpty(Common.mac)) {
            new ConnectBT().execute();
        }

        toggleButton1 = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButton2 = (ToggleButton) findViewById(R.id.toggleButton2);
        toggleButton3 = (ToggleButton) findViewById(R.id.toggleButton3);
        toggleButton4 = (ToggleButton) findViewById(R.id.toggleButton4);

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);

        slotRef = FirebaseDatabase.getInstance().getReference("SlotInfo");
        bookHistoryRef = FirebaseDatabase.getInstance().getReference("CarBookingHistory");

        slotList = new ArrayList<>();
        slotRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                slotList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Slot slot = dataSnapshot1.getValue(Slot.class);
                    slotList.add(slot);
//                    if(slot.getCarNumber().equals(Common.currentUser.getUserCar())){
//                        Log.d("slot", ""+slot.getCarNumber());
////                        if(slot.getSlotNumber().equals("1")){
////                            if(slot.getSlotStatus().equals("true")) {
////                                toggleButton1.setChecked(true);
////
////                            }else{
////                                toggleButton2.setChecked(false);
////                                toggleButton3.setChecked(false);
////                                toggleButton4.setChecked(false);
////                            }
////                        }else if(slot.getSlotNumber().equals("2")){
////                            if(slot.getSlotStatus().equals("true")) {
////                                toggleButton2.setChecked(true);
////
////                            }else{
////                                toggleButton1.setChecked(false);
////                                toggleButton3.setChecked(false);
////                                toggleButton4.setChecked(false);
////
////                            }
////
////
////                        }else if(slot.getSlotNumber().equals("3")){
////                            if(slot.getSlotStatus().equals("true")) {
////                                toggleButton3.setChecked(true);
////
////                            }else{
////                                toggleButton1.setChecked(false);
////                                toggleButton2.setChecked(false);
////                                toggleButton4.setChecked(false);
////
////                            }
////
////
////                        }else if(slot.getSlotNumber().equals("4")) {
////                            if(slot.getSlotStatus().equals("true")) {
////                                toggleButton4.setChecked(true);
////
////                            }else{
////                                toggleButton1.setChecked(false);
////                                toggleButton2.setChecked(false);
////                                toggleButton3.setChecked(false);
////
////                            }
////
////
////                        }
//                    }else{
//
//                        toggleButton2.setChecked(false);
//                        toggleButton1.setChecked(false);
//                        toggleButton4.setChecked(false);
//                        toggleButton3.setChecked(false);
//                    }
                    if(slot.getCarNumber().equals(Common.currentUser.getUserCar())){
                        isParked = true;
                        if(slot.getSlotNumber().equals("1")){
                            toggleButton1.setChecked(true);
                            image1.setVisibility(View.VISIBLE);
                            toggleButton2.setEnabled(false);
                            toggleButton3.setEnabled(false);
                            toggleButton4.setEnabled(false);
                        }else if(slot.getSlotNumber().equals("2")){
                            toggleButton2.setChecked(true);
                            image2.setVisibility(View.VISIBLE);
                            toggleButton1.setEnabled(false);
                            toggleButton3.setEnabled(false);
                            toggleButton4.setEnabled(false);
                        }else if(slot.getSlotNumber().equals("3")){
                            toggleButton3.setChecked(true);
                            image3.setVisibility(View.VISIBLE);
                            toggleButton1.setEnabled(false);
                            toggleButton2.setEnabled(false);
                            toggleButton4.setEnabled(false);
                        }else if(slot.getSlotNumber().equals("4")){
                            toggleButton4.setChecked(true);
                            image4.setVisibility(View.VISIBLE);
                            toggleButton1.setEnabled(false);
                            toggleButton2.setEnabled(false);
                            toggleButton3.setEnabled(false);
                        }
                    }else if(slot.getUserName().equals(Common.currentUser.getUserName())){
                        isParked = true;
                        if(slot.getSlotNumber().equals("1")){
                            toggleButton1.setChecked(true);
                            image1.setVisibility(View.VISIBLE);
                            toggleButton2.setEnabled(false);
                            toggleButton3.setEnabled(false);
                            toggleButton4.setEnabled(false);
                        }else if(slot.getSlotNumber().equals("2")){
                            toggleButton2.setChecked(true);
                            image2.setVisibility(View.VISIBLE);
                            toggleButton1.setEnabled(false);
                            toggleButton3.setEnabled(false);
                            toggleButton4.setEnabled(false);
                        }else if(slot.getSlotNumber().equals("3")){
                            toggleButton3.setChecked(true);
                            image3.setVisibility(View.VISIBLE);
                            toggleButton1.setEnabled(false);
                            toggleButton2.setEnabled(false);
                            toggleButton4.setEnabled(false);
                        }else if(slot.getSlotNumber().equals("4")){
                            toggleButton4.setChecked(true);
                            image4.setVisibility(View.VISIBLE);
                            toggleButton1.setEnabled(false);
                            toggleButton2.setEnabled(false);
                            toggleButton3.setEnabled(false);
                        }
                    }
                    else if(slot.getSlotStatus().equals("true")){
                        isParked = false;
                        if(slot.getSlotNumber().equals("1")){
                            toggleButton1.setChecked(true);
                            image1.setVisibility(View.VISIBLE);
                            toggleButton1.setEnabled(false);

                        }else if(slot.getSlotNumber().equals("2")){
                            toggleButton2.setChecked(true);
                            image2.setVisibility(View.VISIBLE);
                            toggleButton2.setEnabled(false);

                        }else if(slot.getSlotNumber().equals("3")){
                            toggleButton3.setChecked(true);
                            image3.setVisibility(View.VISIBLE);
                            toggleButton3.setEnabled(false);

                        }else if(slot.getSlotNumber().equals("4")){
                            toggleButton4.setChecked(true);
                            image4.setVisibility(View.VISIBLE);
                            toggleButton4.setEnabled(false);

                        }

                    }else if(slot.getSlotStatus().equals("false")){
                        if(slot.getSlotNumber().equals("1")){
                            toggleButton1.setChecked(false);
                            image1.setVisibility(View.INVISIBLE);

                        }else if(slot.getSlotNumber().equals("2")){
                            toggleButton2.setChecked(false);
                            image2.setVisibility(View.INVISIBLE);

                        }else if(slot.getSlotNumber().equals("3")){
                            toggleButton3.setChecked(false);
                            image3.setVisibility(View.INVISIBLE);

                        }else if(slot.getSlotNumber().equals("4")){
                            toggleButton4.setChecked(false);
                            image4.setVisibility(View.INVISIBLE);
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        Log.d("slotList", ""+slotList.size());

        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton1.isChecked()){
                    saveSlotInfo("1", Common.currentUser, "true" );
                    sendDataToArdino("A");
                    Toast.makeText(SlotActivity.this, "On", Toast.LENGTH_SHORT).show();
                }else{
                    saveSlotInfo("1", Common.currentUser, "false" );
                    sendDataToArdino("E");
                    Toast.makeText(SlotActivity.this, "off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton2.isChecked() && !isParked){
//                    saveSlotInfo("2", Common.currentUser );
                    saveSlotInfo("2", Common.currentUser, "true" );
                    sendDataToArdino("B");
                    Toast.makeText(SlotActivity.this, "On", Toast.LENGTH_SHORT).show();
                }else{
                    saveSlotInfo("2", Common.currentUser, "false" );
                    sendDataToArdino("F");
                    Toast.makeText(SlotActivity.this, "off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton3.isChecked() && !isParked){
//                    saveSlotInfo("3", Common.currentUser );
                    sendDataToArdino("C");
                    saveSlotInfo("3", Common.currentUser, "true" );
                    Toast.makeText(SlotActivity.this, "On", Toast.LENGTH_SHORT).show();
                }else{
                    saveSlotInfo("3", Common.currentUser, "false" );
                    sendDataToArdino("G");
                    Toast.makeText(SlotActivity.this, "off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toggleButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton4.isChecked() && !isParked){
//                    saveSlotInfo("4", Common.currentUser );
                    saveSlotInfo("4", Common.currentUser, "true" );
                    sendDataToArdino("D");
                    Toast.makeText(SlotActivity.this, "On", Toast.LENGTH_SHORT).show();
                }else{
                    saveSlotInfo("4", Common.currentUser, "false" );
                    sendDataToArdino("H");
                    Toast.makeText(SlotActivity.this, "off", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void saveSlotInfo(final String slotNo, UserProfile currentUser, String status) {




        slotRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                slotList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Slot slot = dataSnapshot1.getValue(Slot.class);
                    slotList.add(slot);
//
                    if(slot.getCarNumber().equals(Common.currentUser.getUserCar()) && slot.getUserName().equals(Common.currentUser.getUserName())){
                        isParked = true;
                        if(slot.getSlotNumber().equals("1")){
                            toggleButton2.setEnabled(false);
                            toggleButton3.setEnabled(false);
                            toggleButton4.setEnabled(false);
                        }else if(slot.getSlotNumber().equals("2")){
                            toggleButton1.setEnabled(false);
                            toggleButton3.setEnabled(false);
                            toggleButton4.setEnabled(false);
                        }else if(slot.getSlotNumber().equals("3")){
                            toggleButton1.setEnabled(false);
                            toggleButton2.setEnabled(false);
                            toggleButton4.setEnabled(false);
                        }else if(slot.getSlotNumber().equals("4")){
                            toggleButton1.setEnabled(false);
                            toggleButton2.setEnabled(false);
                            toggleButton3.setEnabled(false);
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        final String strDate = sdf.format(c.getTime());

        if(status.equals("false")){
            slotRef.child(slotNo).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final Slot slot = dataSnapshot.getValue(Slot.class);

                    bookHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                                SlotHistory slotHistory = dataSnapshot1.getValue(SlotHistory.class);
                                Log.d("SlotHistory", "" + slotHistory.getBookTime());
                                if(slot.getCarNumber().equals(slotHistory.getUserCarNo()) && slot.getSlotTime().equals(slotHistory.getBookTime())){
                                    Log.d("Find", "Card Found");

                                    HashMap<String, Object> update = new HashMap<>();
                                    update.put("cancelTime",strDate );
                                    bookHistoryRef.child(dataSnapshot1.getKey()).updateChildren(update);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            final Slot slot = new Slot(slotNo, "", "", status, "");
            slotRef.child(slotNo).setValue(slot);
//            String pushKey = bookHistoryRef.push().getKey();
//
//            bookHistoryRef.push().setValue(history);




        }else {

            Slot slot = new Slot(slotNo, currentUser.getUserCar(), strDate, status, currentUser.getUserName());
            slotRef.child(slotNo).setValue(slot);
            String pushKey = bookHistoryRef.push().getKey();
            Log.d("push", ""+pushKey);
            SlotHistory history = new SlotHistory(Common.currentUser.getUserName(), Common.currentUser.getUserCar(), slotNo, Common.currentUser.getUserEmail(), strDate, "", pushKey);
            bookHistoryRef.push().setValue(history);
        }


    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(SlotActivity.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = bluetoothAdapter.getRemoteDevice(Common.mac);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    private void msg(String s) {
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
    }


    private void sendDataToArdino(String data){
        if(btSocket != null) {
            try {
                btSocket.getOutputStream().write(data.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                msg("Error");
            }
        }
    }
}
