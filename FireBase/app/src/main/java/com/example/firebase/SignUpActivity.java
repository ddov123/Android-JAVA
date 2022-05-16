package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    EditText mEmailText, mPasswordText, mPasswordcheckText, mName, phoneEt, careerEt, appealEt;
    Button mregisterBtn;
    private FirebaseAuth firebaseAuth;
    CheckBox allcheck, c1, c2, c3, c4, c5, c6, c7, c8;
    RadioGroup sexRG, nationRG;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //액션 바 등록하기
        Toolbar sign_toolbar = findViewById(R.id.sign_toolbar);
        setSupportActionBar(sign_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        getSupportActionBar().setTitle("회원가입"); // 툴바 제목 설정



        //파이어베이스 접근 설정
        firebaseAuth =  FirebaseAuth.getInstance();

        mEmailText = findViewById(R.id.emailEt);
        mPasswordText = findViewById(R.id.passwordEdt);
        mPasswordcheckText = findViewById(R.id.passwordcheckEdt);
        mregisterBtn = findViewById(R.id.register2_btn);
        mName = findViewById(R.id.nameEt);
        allcheck = findViewById(R.id.allcheck);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);
        c7 = findViewById(R.id.c7);
        c8 = findViewById(R.id.c8);
        sexRG = findViewById(R.id.sexRG);
        nationRG = findViewById(R.id.nationRG);
        phoneEt = findViewById(R.id.phoneEt);
        careerEt = findViewById(R.id.careerEt);
        appealEt = findViewById(R.id.appealEt);

        allcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCheckChanged();
            }
        });

        //가입버튼 클릭리스너   -->  firebase에 데이터를 저장한다.
        mregisterBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                //가입 정보 가져오기
                final String email = mEmailText.getText().toString().trim();
                String pwd = mPasswordText.getText().toString().trim();
                String pwdcheck = mPasswordcheckText.getText().toString().trim();

                if(c1.isChecked() != true){
                    Toast.makeText(SignUpActivity.this, "이용약관 동의는 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(c2.isChecked() != true){
                    Toast.makeText(SignUpActivity.this, "간병인 계약서 동의는 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(c3.isChecked() != true){
                    Toast.makeText(SignUpActivity.this, "개인정보처리방침 동의는 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }else if(c4.isChecked() != true){
                    Toast.makeText(SignUpActivity.this, "개인정보 수집 및 이용 동의서 동의는 필수입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pwd.equals(pwdcheck)) {
                    Log.d(TAG, "등록 버튼 " + email + " , " + pwd);
                    final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
                    mDialog.setMessage("가입중입니다...");
                    mDialog.show();

                    //파이어베이스에 신규계정 등록하기
                    firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //가입 성공시
                            if (task.isSuccessful()) {
                                mDialog.dismiss();

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();
                                String name = mName.getText().toString().trim();
                                String phone = phoneEt.getText().toString().trim();
                                String career = careerEt.getText().toString().trim();
                                String appeal = appealEt.getText().toString();
                                String c1_str = "동의";
                                String c2_str = "동의";
                                String c3_str = "동의";
                                String c4_str = "동의";

                                int sex_id = sexRG.getCheckedRadioButtonId();
                                RadioButton sex_rb = findViewById(sex_id);
                                String sex = sex_rb.getText().toString().trim();

                                int nation_id = nationRG.getCheckedRadioButtonId();
                                RadioButton nation_rb = findViewById(nation_id);
                                String nation = nation_rb.getText().toString().trim();

                                //해쉬맵 테이블을 파이어베이스 데이터베이스에 저장
                                HashMap<Object,String> hashMap = new HashMap<>();

                                hashMap.put("uid",uid);
                                hashMap.put("email",email);
                                hashMap.put("name",name);
                                hashMap.put("phone",phone);
                                hashMap.put("career",career);
                                hashMap.put("appeal",appeal);
                                hashMap.put("sex",sex);
                                hashMap.put("nation",nation);
                                hashMap.put("이용약관",c1_str);
                                hashMap.put("간병인 계약서",c2_str);
                                hashMap.put("개인정보처리방침",c3_str);
                                hashMap.put("개인정보 수집 및 이용 동의서",c4_str);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("CareGiver");
                                reference.child(phone + name).setValue(hashMap);

                                //가입이 이루어져을시 가입 화면을 빠져나감.
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(SignUpActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            }else {
                                mDialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "이미 존재하는 아이디 입니다.", Toast.LENGTH_SHORT).show();
                                return;  //해당 메소드 진행을 멈추고 빠져나감.

                            }

                        }
                    });


                }else{
                    //비밀번호 오류시
                    Toast.makeText(SignUpActivity.this, "비밀번호가 틀렸습니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
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

    public void onCheckChanged(){
        if (allcheck.isChecked()){
            c1.setChecked(true);
            c2.setChecked(true);
            c3.setChecked(true);
            c4.setChecked(true);
            c5.setChecked(true);
            c6.setChecked(true);
            c7.setChecked(true);
            c8.setChecked(true);
        }else {
            c1.setChecked(false);
            c2.setChecked(false);
            c3.setChecked(false);
            c4.setChecked(false);
            c5.setChecked(false);
            c6.setChecked(false);
            c7.setChecked(false);
            c8.setChecked(false);
        }
    }


}