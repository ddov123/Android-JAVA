package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    MainFragment mainFragment;
    WorkFragment workFragment;
    CommFragment commFragment;
    MyFragment   myFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainFragment = new MainFragment();
        workFragment = new WorkFragment();
        commFragment = new CommFragment();
        myFragment = new MyFragment();

        // BottomNavigationView 기능 구현
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation_view);
        // 초기화면은 MainFragment로 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, mainFragment).commit();

        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.main:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, mainFragment).commit();
                        return true;
                    case R.id.work:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, workFragment).commit();
                        return true;
                    case R.id.comm:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, commFragment).commit();
                        return true;
                    case R.id.my:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, myFragment).commit();
                        return true;
                }

                return false;
            }
        });



    }
}