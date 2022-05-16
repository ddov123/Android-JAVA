package com.example.copycare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.List;

public class TalkList extends AppCompatActivity {
    private ListView listView;
    List fileList = new ArrayList<>();
    List fileList2 = new ArrayList<>();
    ArrayAdapter adapter;
    String pName,uemail,CareGiver_email,uphone,email,cphone;
    static boolean calledAlready = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.talklist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("케어리스트와 채팅");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);


                ListView listview = findViewById(R.id.listview);
                adapter = new ArrayAdapter<String>(this, R.layout.activity_listitem, fileList);
                listview.setAdapter(adapter);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                email = user.getEmail();

                FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                DatabaseReference patientRef = database1.getReference("matching");
                patientRef.orderByChild("User_email").equalTo(email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        patientRef.orderByChild("Patient_name").equalTo(pName).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                                    //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                                    //하위키들의 value를 어떻게 가져오느냐???
                                    CareGiver_email = fileSnapshot.child("CareGiver_email").getValue(String.class);
                                    pName=fileSnapshot.child("Patient_name").getValue(String.class);
                                    uemail = fileSnapshot.child("User_email").getValue(String.class);
                                    cphone = fileSnapshot.child("CareGiver_phone").getValue(String.class);
                                    String str = "환자분 성함: " + pName +"   케어메이트: " + CareGiver_email +"   보호자: " + uemail;
                                    Log.i("TAG: value is ", str);
                                    fileList.add(str);
//                                    fileList2.add(CareGiver_email);
                                    fileList2.add(cphone);
                                }
                                adapter.notifyDataSetChanged();
                                                                                                   }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TAG: ", "Failed to read value", databaseError.toException());
                            }});
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG: ", "Failed to read value", databaseError.toException());
                    }
                });
        Intent Regintent = getIntent();
        uemail=Regintent.getStringExtra("uemail");
        pName=Regintent.getStringExtra("pName");
        uphone=Regintent.getStringExtra("uphone");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                adapterView.getItemAtPosition(pos);
                Intent intent = new Intent(TalkList.this, ChatRoom.class);
                cphone=fileList2.get(pos).toString();
                intent.putExtra("chatName",uphone+pName+cphone);
                intent.putExtra("userName",email);
                startActivity(intent);
            }
        });
            }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
