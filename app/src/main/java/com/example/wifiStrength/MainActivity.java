package com.example.wifiStrength;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView txtWifiInfo;
    Button btnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtWifiInfo = findViewById(R.id.idTxt);
        btnInfo = findViewById(R.id.idBtn);
    }

    public void openExplorer(View view){

        Intent i = new Intent(this.getApplicationContext(), TestActivity.class);
        i.putExtra("Value3", "file://"+this.getExternalFilesDir(null)+"/timeStamp.txt");
//        Toast.makeText(getApplicationContext(), "file://"+this.getExternalFilesDir(null)+"/timeStamp.txt" , Toast.LENGTH_LONG).show();
        // Set the request code to any code you like, you can identify the
        // callback via this code
        startActivity(i);

    }

    public void getWifiInfo(View view) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (String.valueOf(wifiInfo.getSupplicantState()).equals("COMPLETED")) {
                String SSId = wifiInfo.getSSID();

                Toast.makeText(this,  SSId, Toast.LENGTH_SHORT).show();
                int rssid = wifiInfo.getRssi();
                String ssid = wifiInfo.getSSID();
                String bssid = wifiInfo.getBSSID();
// wifiInfo.getFrequency();
                int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 6);
                int ip = wifiInfo.getIpAddress();
                String ipaddress = Formatter.formatIpAddress(ip);
                int linkspeed = wifiInfo.getLinkSpeed();
                String info = "Ipaddress: " + ipaddress +
                        "\n" + "BSSID: " + bssid +
                        "\n" + "RSSID: " + rssid + "\nSSID:" + ssid;
                String signal_strength = "Signal Strength: " + level + "/5";
                txtWifiInfo.setText(signal_strength);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                String currentTime = simpleDateFormat.format(c.getTime());

                try {
// Creates a file in the primary external storage space of the
// current application.
// If the file does not exists, it is created.
                    File testFile = new File(this.getExternalFilesDir(null), "timeStamp.txt");
                    if (!testFile.exists())
                        testFile.createNewFile();

// Adds a line to the file
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testFile, true

                            /*append*/));

                    bufferedWriter.write(currentTime + " --> " + info + "\n");
                    bufferedWriter.close();
// Refresh the data so it can seen when the device is plugged in a
// computer. You may have to unplug and replug the device to see the
// latest changes. This is not necessary if the user should not modify
// the files.
                    MediaScannerConnection.scanFile(this,
                            new String[]{testFile.toString()},
                            null,
                            null);
                } catch (IOException e) {
                    Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
                }

            }
        } else {
            wifiManager.setWifiEnabled(true);
        }
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new
                    OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
