package com.example.admin.kinglaw;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText loginUser;
    private EditText loginPass;
    private TextView loginLogin;
    private FirebaseAuth loginAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
            }
        };
        loginAuth.addAuthStateListener(mAuthListener);
    }
    private void init() {
        loginUser=(EditText)findViewById(R.id.login_user);
        loginPass=(EditText)findViewById(R.id.login_pass);
        loginLogin=(TextView)findViewById(R.id.login_login);
        loginAuth = FirebaseAuth.getInstance();
        loginLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login:{
                if(loginUser.getText().length()>0&&loginPass.getText().length()>0) {
                    if (isOnline()) {
                        final ProgressDialog pd = new ProgressDialog(this);
                        pd.setMessage("Loading");
                        pd.show();
                        loginAuth.signInWithEmailAndPassword(loginUser.getText().toString(), loginPass.getText().toString())
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            pd.dismiss();
                                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(LoginActivity.this, "Authentication failed...\nPlease! try again later.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else {
                        Toast.makeText(this, "Please! Turn on your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Fill the fields...", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
