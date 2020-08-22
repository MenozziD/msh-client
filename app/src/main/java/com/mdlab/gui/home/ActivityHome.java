package com.mdlab.gui.home;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mdlab.R;
import com.mdlab.utility.RequestAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ActivityHome extends AppCompatActivity {

    private Spinner spinnerDevices;
    private Spinner spinnerCommands;
    private TextView result;
    private TextView resultScan;
    private String session;
    private JSONArray devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        spinnerDevices = findViewById(R.id.device);
        spinnerCommands = findViewById(R.id.command);
        Button exec = findViewById(R.id.exec);
        Button scan = findViewById(R.id.scan);
        result = findViewById(R.id.result);
        resultScan = findViewById(R.id.result_scan);
        session = Objects.requireNonNull(getIntent().getExtras()).getString("session");
        AscoltatoreActivityHome ascoltatoreActivityHome = new AscoltatoreActivityHome(this);
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("tipo_operazione", "list");
            RequestAsync req = new RequestAsync("https://casamelo-mdlab.pagekite.me/api/net", postDataParams.toString(), session);
            req.execute().get();
            devices = new JSONObject(req.getResponse()).getJSONArray("devices");
            List<String> payloadSpinnerDevices = new ArrayList<>();
            for (int i=0; i < devices.length(); i++)
                payloadSpinnerDevices.add(new JSONObject(devices.get(i).toString()).getString("net_code"));
            ArrayAdapter<String> adapterDevices = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, payloadSpinnerDevices);
            adapterDevices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDevices.setAdapter(adapterDevices);
            spinnerDevices.setOnItemSelectedListener(ascoltatoreActivityHome);
            exec.setOnClickListener(ascoltatoreActivityHome);
            scan.setOnClickListener(ascoltatoreActivityHome);

        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String getSession() {
        return session;
    }

    public JSONArray getDevices() {
        return devices;
    }

    public Spinner getSpinnerCommands() {
        return spinnerCommands;
    }

    public Spinner getSpinnerDevices() {
        return spinnerDevices;
    }

    public TextView getResult() {
        return result;
    }

    public TextView getResultScan() {
        return resultScan;
    }
}
