package com.example.copycare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Registration extends AppCompatActivity {
    Button btn_1,btn_2,btn_3,btn_4,btn_next;
    CheckBox checkBox1,checkBox2,checkBox3,checkBox4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("공고 등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);

        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_next = findViewById(R.id.btn_next);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox1.setChecked(true);
                checkBox2.setChecked(false);
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox2.setChecked(true);
                checkBox1.setChecked(false);
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox3.setChecked(true);
                checkBox4.setChecked(false);
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox4.setChecked(true);
                checkBox3.setChecked(false);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration.this, Registration2.class);

                if (checkBox1.isChecked()) {
                    if (checkBox3.isChecked()) {
                        intent.putExtra("TorP", "시간제");
                        intent.putExtra("HorH", "병,의원");
                        startActivity(intent);
                    } else if (checkBox4.isChecked()) {
                        intent.putExtra("TorP", "시간제");
                        intent.putExtra("HorH", "집");
                        startActivity(intent);
                    } else{
                        Toast.makeText(Registration.this,"원하시는 간병장소를 선택해주세요",Toast.LENGTH_LONG).show();

                    }
                }else if (checkBox2.isChecked()) {
                    if (checkBox3.isChecked()) {
                        intent.putExtra("TorP", "기간제");
                        intent.putExtra("HorH", "병,의원");
                        startActivity(intent);
                    } else if (checkBox4.isChecked()) {
                        intent.putExtra("TorP", "기간제");
                        intent.putExtra("HorH", "집");
                        startActivity(intent);
                    }else{
                        Toast.makeText(Registration.this,"원하시는 간병장소를 선택해주세요",Toast.LENGTH_LONG).show();
                    }
                    }else{
                        Toast.makeText(Registration.this,"원하시는 서비스를 선택해주세요",Toast.LENGTH_LONG).show();
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
}

