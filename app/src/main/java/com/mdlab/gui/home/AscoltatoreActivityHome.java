package com.mdlab.gui.home;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mdlab.R;
import com.mdlab.utility.RequestAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AscoltatoreActivityHome implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ActivityHome activity;

    AscoltatoreActivityHome(ActivityHome activity){
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exec:
                JSONObject postDataParams = new JSONObject();
                try {
                    postDataParams.put("tipo_operazione", "cmd");
                    postDataParams.put("dispositivo", activity.getSpinnerDevices().getSelectedItem().toString());
                    postDataParams.put("comando", activity.getSpinnerCommands().getSelectedItem().toString());
                    RequestAsync req = new RequestAsync("https://casamelo-mdlab.pagekite.me/api/net", postDataParams.toString(), activity.getSession());
                    req.execute().get();
                    JSONObject response = new JSONObject(req.getResponse());
                    activity.getResult().setText(activity.getResources().getString(R.string.cmd_result, response.getString("output"), response.getString("result")));
                } catch (JSONException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            break;
            case R.id.scan:
                postDataParams = new JSONObject();
                try {
                    postDataParams.put("tipo_operazione", "scan");
                    RequestAsync req = new RequestAsync("https://casamelo-mdlab.pagekite.me/api/net", postDataParams.toString(), activity.getSession());
                    req.execute().get();
                    JSONObject response = new JSONObject(req.getResponse());
                    activity.getResultScan().setText(activity.getResources().getString(R.string.scan_result, response.getString("find_device"), response.getString("new_device"), response.getString("updated_device")));
                } catch (JSONException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedItem = adapterView.getItemAtPosition(i).toString();
        JSONObject postDataParams = new JSONObject();
        JSONObject device = null;
        for (int j=0; j < activity.getDevices().length(); j++){
            try {
                device = new JSONObject(activity.getDevices().get(j).toString());
                if (device.getString("net_code").equals(selectedItem))
                    break;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            postDataParams.put("tipo_operazione", "command");
            postDataParams.put("net_type", Objects.requireNonNull(device).getString("net_type"));
            RequestAsync req = new RequestAsync("https://casamelo-mdlab.pagekite.me/api/net", postDataParams.toString(), activity.getSession());
            req.execute().get();
            List<String> payloadSpinnerCommands =  new ArrayList<>();
            JSONArray commands = new JSONObject(req.getResponse()).getJSONArray("commands");
            System.out.println(commands.length());
            for (int j=0; j < commands.length(); j++) {
                System.out.println(new JSONObject(commands.get(j).toString()).getString("cmd_str"));
                payloadSpinnerCommands.add(new JSONObject(commands.get(j).toString()).getString("cmd_str"));
            }
            ArrayAdapter<String> adapterCommands = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, payloadSpinnerCommands);
            adapterCommands.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            activity.getSpinnerCommands().setAdapter(adapterCommands);
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
