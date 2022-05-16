package com.example.firebase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AlertActivity extends AppCompatActivity {

    private String CHAT_NAME;
    private ArrayAdapter adapter;
    private List fileList = new ArrayList<>();
    private String care_result;
    private List careList2 = new ArrayList<>();
    private List nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        // 툴바 생성
        Toolbar alert_toolbar = findViewById(R.id.alert_toolbar);
        setSupportActionBar(alert_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setTitle("채팅방 목록"); // 툴바 제목 설정

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            //listview 만들기
            ListView listview = findViewById(R.id.alert_listview);
            adapter = new ArrayAdapter<String>(AlertActivity.this, R.layout.activity_listitem, fileList);
            listview.setAdapter(adapter);

            Intent intent1 = getIntent();
            care_result = intent1.getStringExtra("care_result");
            String phone = care_result;



            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference patientRef2 = database1.getReference("matching");

            patientRef2.orderByChild("CareGiver_phone").equalTo(phone).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // 클래스 모델이 필요?
                    for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                        //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                        //하위키들의 value를 어떻게 가져오느냐???
                        String CareGiver_phone = fileSnapshot.child("CareGiver_phone").getValue(String.class);
                        String Patient_name = fileSnapshot.child("Patient_name").getValue(String.class);
                        String User_email = fileSnapshot.child("User_email").getValue(String.class);
                        String Where = fileSnapshot.child("Where").getValue(String.class);
                        String phone = fileSnapshot.child("phone").getValue(String.class);

                        String str = Where +  "\n" + Patient_name + "\n" + User_email + "\n" + phone;
                        String chat_str = phone + "&" + Patient_name + "&" + CareGiver_phone + "&";

                        fileList.add(str);
                        careList2.add(chat_str);

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
                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(AlertActivity.this);
                    alertdialog.setMessage("채팅방 입장을 하시겠습니까?");

                    alertdialog.setPositiveButton("입장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

//                            Object vo = parent.getAdapter().getItem(position);
                            String[] result = careList2.get(position).toString().split("&");
                            String insert = result[0]+result[1]+result[2];
                            String uphone = result[0];
                            String name = result[1];
                            String care_phone = result[2];
                            String match = uphone + name + care_phone;

                            Log.d("리스트 : ", insert);

                            Toast.makeText(AlertActivity.this, "채팅방으로 입장하였습니다.", Toast.LENGTH_SHORT).show();

                            String phone = care_result;
                            Intent intent2 = new Intent(AlertActivity.this, ChatActivity.class);
                            intent2.putExtra("chatName",match);
                            startActivity(intent2);
                        }
                    });

                    alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            Toast.makeText(AlertActivity.this, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alert = alertdialog.create();
                    alert.show();
                }
            });
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                // 액티비티 이동
                finish();
                overridePendingTransition(0,0);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}