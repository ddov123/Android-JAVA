package com.example.copycare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.models.PieModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment4 extends Fragment {

    private DatabaseReference mDatabase;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String uname,email;
    public TabFragment4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment4 newInstance(String param1, String param2) {
        TabFragment4 fragment = new TabFragment4();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment4, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        toolbar.setSubtitle("마이페이지");
        toolbar.inflateMenu(R.menu.menu1);

        TextView frag4 = view.findViewById(R.id.frag4);
        TextView hello = view.findViewById(R.id.hello);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();

        if (user!= null){
            email = user.getEmail();
            String name = user.getDisplayName();
            if(name!=null){
                 hello.setText(name+"님 안녕하세요!");}
            frag4.setText(email+"로 로그인하셧습니다");


        DatabaseReference patientRef = database1.getReference("Users");
        patientRef.orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 클래스 모델이 필요?
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                    //하위키들의 value를 어떻게 가져오느냐???
                    uname = fileSnapshot.child("name").getValue(String.class);
                    if(uname!=null){
                        hello.setText(uname+"님 안녕하세요");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG: ", "Failed to read value",error.toException());
            }
        });
        } else{frag4.setText("로그인 하지 않았습니다");}
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.alerticon:
                        Intent intent = new Intent(getActivity(),notification.class);
                        startActivity(intent);
                        break;
                    case R.id.settingicon:
                        Intent intent2 = new Intent(getActivity(),AppSetting.class);
                        startActivity(intent2);
                        break;
                }

                return false;

            }
        });
        Button btn_myinfo = view.findViewById(R.id.btn_myinfo);
        btn_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getActivity(),MyInfo.class);
                startActivity(intent3);
            }
        });
        Button btn_plist = view.findViewById(R.id.btn_plist);
        btn_plist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getActivity(),Plist.class);
                startActivity(intent4);
            }
        });

        return view;
    }


//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu1, menu);
//    }
//    @Override
//    public boolean onMEnuItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.search:
//                Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
//                startActivity(intent);;
//        }
//        return super.onOptionsItemSelected(item);
//    }


}