package com.example.copycare;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyInfo extends AppCompatActivity {
    EditText ed1,ed2,ed3,ed4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("내 정보 설정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        ed1=findViewById(R.id.ed1);
        ed2=findViewById(R.id.ed2);
        ed3=findViewById(R.id.ed3);
        ed4=findViewById(R.id.ed4);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();

            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference patientRef = database1.getReference("Users");

            patientRef.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // 클래스 모델이 필요?
                    for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                        //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                        //하위키들의 value를 어떻게 가져오느냐???
                        String name = fileSnapshot.child("name").getValue(String.class);
                        String email = fileSnapshot.child("email").getValue(String.class);
                        String birth = fileSnapshot.child("birth").getValue(String.class);
                        String phone = fileSnapshot.child("phone").getValue(String.class);
                        ed1.setText(name);
                        ed2.setText(email);
                        ed3.setText(birth);
                        ed4.setText(phone);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TAG: ", "Failed to read value", databaseError.toException());
                }
            });
        } else {

            Toast.makeText(this,"로그인 하셔야 이용하실수 있습니다",Toast.LENGTH_LONG).show();
        }



    }
        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                    finish();
                    return true;
                }
            }
            return super.onOptionsItemSelected(item);
        }

}