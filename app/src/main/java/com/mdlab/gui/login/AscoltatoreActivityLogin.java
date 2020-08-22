package com.mdlab.gui.login;

import android.content.Intent;
import android.view.View;

import com.mdlab.gui.home.ActivityHome;
import com.mdlab.utility.RequestAsync;

import org.json.JSONObject;

public class AscoltatoreActivityLogin implements View.OnClickListener {

    private ActivityLogin activity;

    AscoltatoreActivityLogin(ActivityLogin activity){
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        try {
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("user", activity.getUsernameText().getText());
            postDataParams.put("password", activity.getPasswordText().getText());
            RequestAsync req = new RequestAsync("https://casamelo-mdlab.pagekite.me/api/login", postDataParams.toString());
            req.execute().get();
            Intent setPage = new Intent(activity, ActivityHome.class);
            if (req.getCookie() != null) {
                setPage.putExtra("session", req.getCookie().split(";")[0]);
                activity.startActivity(setPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
