package com.example.copycare;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Plist extends AppCompatActivity {
    private ListView listView;
    List fileList = new ArrayList<>();
    ArrayAdapter adapter;
    static boolean calledAlready = false;

    // TODO: Rename and change types and number of parameters
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle("간병내역");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            if (!calledAlready) {
//                FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
//                calledAlready = true;
//            }
            //listview 만들기
            ListView listview = findViewById(R.id.listview);
            adapter = new ArrayAdapter<String>(this, R.layout.activity_listitem, fileList);
            listview.setAdapter(adapter);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();

            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference patientRef = database1.getReference("Patient_info");

            patientRef.orderByChild("useremail").equalTo(email).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // 클래스 모델이 필요?
                    for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                        //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                        //하위키들의 value를 어떻게 가져오느냐???
                        String pName = fileSnapshot.child("pName").getValue(String.class);
                        String pAge = fileSnapshot.child("pAge").getValue(String.class);
                        String pGender = fileSnapshot.child("pGender").getValue(String.class);
                        String pDisease = fileSnapshot.child("pDisease").getValue(String.class);
                        String str = "환자분 성함: " + pName + "  환자분 나이: " + pAge + "세   환자분성별: " + pGender + "  환자분 병명: " + pDisease;

                        Log.i("TAG: value is ", str);
                        fileList.add(str);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TAG: ", "Failed to read value", databaseError.toException());
                }
            });
        }
        else{
            ListView  listview =findViewById(R.id.listview);
            adapter = new ArrayAdapter<String>(this, R.layout.activity_listitem, fileList);
            fileList.add("로그인 후 사용하실수 있습니다.");
            listview.setAdapter(adapter);

        }
    }

}