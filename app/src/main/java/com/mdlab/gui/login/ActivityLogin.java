package com.mdlab.gui.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.mdlab.R;

public class ActivityLogin extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        Button login = findViewById(R.id.login);
        AscoltatoreActivityLogin ascoltatoreActivityLogin = new AscoltatoreActivityLogin(this);
        login.setOnClickListener(ascoltatoreActivityLogin);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public EditText getUsernameText() {
        return usernameText;
    }

    public EditText getPasswordText() {
        return passwordText;
    }
}
