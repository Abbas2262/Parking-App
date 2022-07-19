package com.example.smartcarpark;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

public class FindBluetoothDevice extends AppCompatActivity {

    Switch bluetoothSwitch;
//    BluetoothAdapter bluetoothAdapter;
    private int ENABLE_REQUEST_CODE = 100;
    public static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    String name, macAddress;

    ListView pairDevicesList, discoverDevicesList;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();;
    ArrayList<BluetoothDevice> pairList, discoverList;
    ArrayList<String> pariListNames, discoverListName;
    ArrayAdapter<String> pairListAdapter, discoverListAdapter;
    Button discoverDevicesBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_bluetooth_device);

//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        discoverDevicesList = (ListView) findViewById(R.id.discoverDevicesList);
        discoverDevicesBtn = (Button) findViewById(R.id.discoverDeviceButton);

        discoverDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdapter.startDiscovery();
            }
        });

        discoverList = new ArrayList<>();
        discoverListName = new ArrayList<>();
        discoverListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, discoverListName);
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver, intentFilter);
        discoverDevicesList.setAdapter(discoverListAdapter);
        discoverDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FindBluetoothDevice.this,MainActivity.class );
                String name = discoverListName.get(position);
                String mac = discoverList.get(position).getAddress();
//                intent.putExtra("name", mac.substring(mac.length()-17));
//                intent.putExtra("mac", mac);
                Common.name = name;
                Common.mac = mac.substring(mac.length()-17);
                startActivity(intent);
            }
        });



        final SharedPreferences sharedPreferences = getSharedPreferences("smartCarParking", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        bluetoothSwitch = (Switch) findViewById(R.id.bluetoothSwitch);
        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    bluetoothON();
                    editor.putString("Status", "ON");
                }else{
                    bluetoothOFF();
                    editor.putString("Status", "OFF");
                }
            }
        });

        String status = sharedPreferences.getString("Status","");
        if(status.equals("ON") && TextUtils.isEmpty(status)){
            bluetoothSwitch.setChecked(true);
        }else if(status.equals("OFF")){
            bluetoothSwitch.setChecked(false);
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device  = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                discoverListName.add(device.getName());
                discoverList.add(device);
                discoverListAdapter.notifyDataSetChanged();
            }
        }
    };


    private void bluetoothOFF() {
        if( bluetoothAdapter != null ) {
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            }
        }
    }

    private void bluetoothON() {
        if(bluetoothAdapter == null){
            Toast.makeText(getApplicationContext(), "Device doesn't support bluetooth!", Toast.LENGTH_SHORT).show();
        }else{
            if(!bluetoothAdapter.isEnabled()){
                Intent ebIntent  = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(ebIntent, ENABLE_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == ENABLE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Bluetooth Enabled!", Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Bluetooth Enabled Canceled!", Toast.LENGTH_SHORT).show();
                bluetoothSwitch.setChecked(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();

    }
}
