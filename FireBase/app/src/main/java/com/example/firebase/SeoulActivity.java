package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeoulActivity extends AppCompatActivity {
    String seoul = "서울";
    List fileList = new ArrayList<>();
    List careList = new ArrayList<>();
    List intentList = new ArrayList<>();
    ArrayAdapter adapter;
    String choice = "";
    List nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seoul);

        Toolbar sign_toolbar = findViewById(R.id.seoul_toolbar);
        setSupportActionBar(sign_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setTitle(seoul); // 툴바 제목 설정

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            //listview 만들기
            ListView listview = findViewById(R.id.listview);
            adapter = new ArrayAdapter<String>(SeoulActivity.this, R.layout.activity_listitem, fileList);
            listview.setAdapter(adapter);

            FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String uid = user.getUid();

            FirebaseDatabase database2 = FirebaseDatabase.getInstance();
            DatabaseReference patientRef1 = database2.getReference("CareGiver");

            patientRef1.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // 클래스 모델이 필요?
                    for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                        //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                        //하위키들의 value를 어떻게 가져오느냐???
                        String phone = fileSnapshot.child("phone").getValue(String.class);

                        String care_str = phone;

                        Log.i("TAG: value is ", choice);
                        careList.add(care_str);

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TAG: ", "Failed to read value", databaseError.toException());
                }
            });





            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference patientRef = database1.getReference("Patient_info");

            patientRef.orderByChild("pWhere").equalTo(seoul).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // 클래스 모델이 필요?
                    for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                        //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                        //하위키들의 value를 어떻게 가져오느냐???
                        String pAge = fileSnapshot.child("pAge").getValue(String.class);
                        String pGender = fileSnapshot.child("pGender").getValue(String.class);
                        String startdate = fileSnapshot.child("startdate").getValue(String.class);
                        String pPhone = fileSnapshot.child("userphone").getValue(String.class);
                        String pPhone2 = fileSnapshot.child("userphone").getValue(String.class);
                        String pUseremail = fileSnapshot.child("useremail").getValue(String.class);
                        String pName = fileSnapshot.child("pName").getValue(String.class);
                        choice = pPhone + pName;
                        String str = pName +"\n환자 성별 : " + pGender + "\n간병 시작일 : " + startdate + "\n환자 나이 : " + pAge + "\n";
                        String info_str = pName + "&" + pGender + "&" + startdate + "&" + pAge + "&" + choice + "&" + pUseremail + "&" + pPhone2 + "&";

                        Log.i("TAG: value is ", choice);
                        fileList.add(str);
                        nameList.add(info_str);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TAG: ", "Failed to read value", databaseError.toException());
                }
            });

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(SeoulActivity.this);
                    alertdialog.setMessage("간병 제안을 하시겠습니까?");

                    alertdialog.setPositiveButton("제안", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

//                            Object vo = parent.getAdapter().getItem(position);

                            String[] result = nameList.get(position).toString().split("&");
                            String insert = result[0]+result[1]+result[2]+result[3]+result[4]+result[5]+result[6];
                            String pName = result[0];
                            String match = result[4];
                            String User_email = result[5];
                            String pPhone2 = result[6];


                            FirebaseAuth firebaseAuth =  FirebaseAuth.getInstance();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String care_result = careList.get(0).toString();

                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("CareGiver_email",email);
                            hashMap.put("CareGiver_phone",care_result);
                            hashMap.put("User_email", User_email);
                            hashMap.put("Where", seoul);
                            hashMap.put("Patient_name", pName);
                            hashMap.put("phone", pPhone2);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("matching");
                            reference.child(match+care_result).updateChildren(hashMap);


                            Toast.makeText(SeoulActivity.this, "간병 제안 요청 및 채팅방을 개설 하였습니다.", Toast.LENGTH_SHORT).show();

                            Intent intent2 = new Intent(SeoulActivity.this, AlertActivity.class);
                            intent2.putExtra("chatName",match+care_result);
                            intent2.putExtra("care_result",care_result);
                            startActivity(intent2);
                        }
                    });

                    alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            Toast.makeText(SeoulActivity.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alert = alertdialog.create();
                    alert.show();
                }
            });

        }


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