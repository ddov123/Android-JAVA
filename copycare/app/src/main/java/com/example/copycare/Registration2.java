package com.example.copycare;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import net.daum.mf.map.api.MapView;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Registration2 extends AppCompatActivity {
    DatabaseReference mDBReference = null;
    HashMap<String, Object> childUpdates = null;
    Map<String, Object> userValue = null;
    UserInfo userInfo = null;
    TextView tv_timespace;
    String a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,pwhere;
    String q1,q2,q3,q4,q5,q6,q7,q8,q9,q10,q11,q12,q13,q14,q15,q16,q17,q18;
    String email,uname,uphone;
    private FirebaseAuth firebaseAuth;
    private  DatabaseReference mDatabase;
    HashMap<Object,String> hashMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration2);

        MapView mapView = new MapView(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("공고 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        Intent Regintent = getIntent();
        String TorP=Regintent.getStringExtra("TorP");
        String HorH=Regintent.getStringExtra("HorH");
        Toast.makeText(this, (TorP+"와 "+HorH+"을 선택하셧습니다.") , Toast.LENGTH_LONG).show();
        tv_timespace = findViewById(R.id.tv_timespace);
        tv_timespace.setText("["+TorP+"와 "+HorH+"]");

        AppCompatSpinner s_corona = findViewById(R.id.s_corona);
        String[]list = {"ex)예/아니요","예","아니요"};
        SpnAdapter(s_corona,list);
        EditText Ea2=findViewById(R.id.a2);
        EditText Ea3=findViewById(R.id.a3);
        EditText Ea4=findViewById(R.id.a4);
        EditText Ea5=findViewById(R.id.a5);
        EditText Ea6=findViewById(R.id.a6);
        EditText Ea7=findViewById(R.id.a7);
        EditText Ea8=findViewById(R.id.a8);
        EditText Ea9=findViewById(R.id.a9);
        EditText Ea10=findViewById(R.id.a10);
        EditText Ea0 = findViewById(R.id.a0);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference patientRef = database1.getReference("Users");
        patientRef.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                uname=snapshot.child("name").getValue().toString();
                uphone =snapshot.child("phone").getValue().toString();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String[]list1 = {"Q1.병실(일반실/중환자실/응급실/격리병실/폐쇄병실)","일반실","중환자실","응급실","격리병실","폐쇄병실"};
        AppCompatSpinner pinfo1 = findViewById(R.id.pinfo1);
       SpnAdapter(pinfo1,list1);

        String[]list2 = {"Q2.감염성질환여부:(없음/VRE/CRE/결핵/옴/독감/기타)","없음","VRE","CRE","결핵","옴","독감","기타"};
        AppCompatSpinner pinfo2 = findViewById(R.id.pinfo2);
        SpnAdapter(pinfo2,list2);

        String[]list3 = {"Q3.전신마비/편마비/없음","전신마비","편마비","없음"};
        AppCompatSpinner pinfo3 = findViewById(R.id.pinfo3);
        SpnAdapter(pinfo3,list3);

        String[]list4 = {"Q4.거동 상태:(불가능/부축필요/스스로가능)","불가능","부축필요","스스로가능"};
        AppCompatSpinner pinfo4 = findViewById(R.id.pinfo4);
        SpnAdapter(pinfo4,list4);

        String[]list5 = {"Q5.욕창 여부:(네/아니요)","네","아니요"};
        AppCompatSpinner pinfo5 = findViewById(R.id.pinfo5);
        SpnAdapter(pinfo5,list5);

        String[]list6 = {"Q6.의식 여부:(네/아니요)","네","아니요"};
        AppCompatSpinner pinfo6 = findViewById(R.id.pinfo6);
        SpnAdapter(pinfo6,list6);

        String[]list7 = {"Q7.인지장애:(치매/섬망/없음)","치매","섬망","없음"};
        AppCompatSpinner pinfo7 = findViewById(R.id.pinfo7);
        SpnAdapter(pinfo7,list7);

        String[]list8 = {"Q8.수면장애:(네/아니요)","네","아니요"};
        AppCompatSpinner pinfo8 = findViewById(R.id.pinfo8);
        SpnAdapter(pinfo8,list8);

        String[]list9 = {"Q9.화장실 이용:(비이용/부축필요/스스로이용)","비이용","부축필요","스스로이용"};
        AppCompatSpinner pinfo9 = findViewById(R.id.pinfo9);
        SpnAdapter(pinfo9,list9);

        String[]list10 = {"Q10.배변도구:(기저귀/소변줄/없음)","기저귀","소변줄","없음"};
        AppCompatSpinner pinfo10 = findViewById(R.id.pinfo10);
        SpnAdapter(pinfo10,list10);

        String[]list11 = {"Q11.장루 여부:(네/아니요)","네","아니요"};
        AppCompatSpinner pinfo11 = findViewById(R.id.pinfo11);
        SpnAdapter(pinfo11,list11);

        String[]list12 = {"Q12.스스로 식사여부:(식사하지않음/도움필요/스스로 가능)","식사하지않음","도움필요","스스로 가능"};
        AppCompatSpinner pinfo12 = findViewById(R.id.pinfo12);
        SpnAdapter(pinfo12,list12);

        String[]list13 = {"Q13.석션 사용 여부:(사용중/사용 안함)","사용중","사용 안함"};
        AppCompatSpinner pinfo13 = findViewById(R.id.pinfo13);
        SpnAdapter(pinfo13,list13);

        String[]list14 = {"Q14.피딩 사용 여부:(사용중/사용 안함)","사용중","사용 안함"};
        AppCompatSpinner pinfo14 = findViewById(R.id.pinfo14);
        SpnAdapter(pinfo14,list14);

        String[]list15 =  {"Q15.재활치료여부:(네/아니요)","네","아니요"};
        AppCompatSpinner pinfo15 = findViewById(R.id.pinfo15);
        SpnAdapter(pinfo15,list15);

        String[]list16 = {"Q16.투석치료여부:(네/아니요)","네","아니요"};
        AppCompatSpinner pinfo16 = findViewById(R.id.pinfo16);
        SpnAdapter(pinfo16,list16);

        String[]list17 = {"Q17.케어메이트의 선호성별:(남자/여자/상관없음)","남자","여자", "상관없음"};
        AppCompatSpinner pinfo17 = findViewById(R.id.pinfo17);
        SpnAdapter(pinfo17,list17);

        String[]list18 = {"Q18.케어메이트의 선호복장:(유니폼/자유복장)","유니폼","자유복장"};
        AppCompatSpinner pinfo18 = findViewById(R.id.pinfo18);
        SpnAdapter(pinfo18,list18);

        String[]lista1 = {"확인 및 개인정보 제3자 제공 동의서:(예/아니요)","예","아니요"};
        AppCompatSpinner agreement1 = findViewById(R.id.agreement1);
        SpnAdapter(agreement1,lista1);

        String[]lista2 = {"작성 환자 정보를 환자목록에 등록여부:(예/아니요)","예","아니요"};
        AppCompatSpinner agreement2 = findViewById(R.id.agreement2);
        SpnAdapter(agreement2,lista2);

        Button btn_agree = findViewById(R.id.btn_agree);
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (agreement1.getSelectedItem().toString().trim()=="예" &&  agreement2.getSelectedItem().toString().trim()=="예"){
                    Toast.makeText(getApplicationContext(),"입력되었습니다", Toast.LENGTH_LONG).show();

//                    HashMap<Object,String> hashMap = new HashMap<>();
                    a2 = Ea2.getText().toString();
                    a3= Ea3.getText().toString();
                    a4= Ea4.getText().toString();
                    a5= Ea5.getText().toString();
                    a6= Ea6.getText().toString();
                    a7= Ea7.getText().toString();
                    a8= Ea8.getText().toString();
                    a9= Ea9.getText().toString();
                    a10= Ea10.getText().toString();
                    pwhere=Ea0.getText().toString();

//                    해쉬맵 테이블을 파이어베이스 데이터베이스에 저장

                    hashMap.put("TorP",TorP);
                    hashMap.put("HorH",HorH);
                    hashMap.put("useremail",email);
                    hashMap.put("username",uname);
                    hashMap.put("userphone",uphone);
                    hashMap.put("a1",s_corona.getSelectedItem().toString().trim());
                    hashMap.put("pWhere",pwhere);
                    hashMap.put("period",a2);
                    hashMap.put("date",a3);
                    hashMap.put("startdate",a4);
                    hashMap.put("pName",a5);
                    hashMap.put("pGender",a6);
                    hashMap.put("pAge",a7);
                    hashMap.put("pTall",a8);
                    hashMap.put("pWeight",a9);
                    hashMap.put("pDisease",a10);
                    hashMap.put("q1",pinfo1.getSelectedItem().toString().trim());
                    hashMap.put("q2",pinfo2.getSelectedItem().toString().trim());
                    hashMap.put("q3",pinfo3.getSelectedItem().toString().trim());
                    hashMap.put("q4",pinfo4.getSelectedItem().toString().trim());
                    hashMap.put("q5",pinfo5.getSelectedItem().toString().trim());
                    hashMap.put("q6",pinfo6.getSelectedItem().toString().trim());
                    hashMap.put("q7",pinfo7.getSelectedItem().toString().trim());
                    hashMap.put("q8",pinfo8.getSelectedItem().toString().trim());
                    hashMap.put("q9",pinfo9.getSelectedItem().toString().trim());
                    hashMap.put("q10",pinfo10.getSelectedItem().toString().trim());
                    hashMap.put("q11",pinfo11.getSelectedItem().toString().trim());
                    hashMap.put("q12",pinfo12.getSelectedItem().toString().trim());
                    hashMap.put("q13",pinfo13.getSelectedItem().toString().trim());
                    hashMap.put("q14",pinfo14.getSelectedItem().toString().trim());
                    hashMap.put("q15",pinfo15.getSelectedItem().toString().trim());
                    hashMap.put("q16",pinfo16.getSelectedItem().toString().trim());
                    hashMap.put("q17",pinfo17.getSelectedItem().toString().trim());
                    hashMap.put("q18",pinfo18.getSelectedItem().toString().trim());

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Patient_info");
                    reference.child(uphone+a5).setValue(hashMap);

                    Intent intent = new Intent(Registration2.this, customer.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"정보제공 동의를 하여야 등록가능 합니다", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void SpnAdapter(AppCompatSpinner spinner,String[] list){
        ArrayAdapter spnadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
        spnadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spnadapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
