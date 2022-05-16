package com.example.copycare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment2 extends Fragment {
    private ListView listview;
    int count;
    String selectName;
    List fileList = new ArrayList<>();
    List fileList2 = new ArrayList<>();
    List fileList3 = new ArrayList<>();
    Button btn_review;
    ArrayAdapter adapter;
    static boolean calledAlready = false;
    String pName,pAge,pGender,pDisease,email,pNameEqual,uphone1,username;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragement2.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment2 newInstance(String param1, String param2) {
        TabFragment2 fragment = new TabFragment2();
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
    void showDialog(String mesg) {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("케어메이트가"+mesg+"환자의 케어를 신청하였습니다")
                .setMessage("확인하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent2 = new Intent(getActivity(), TalkList.class);
                        intent2.putExtra("uemail",email);
                        intent2.putExtra("pName",mesg);
                        intent2.putExtra("uphone",uphone1);
                        startActivity(intent2);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "취소", Toast.LENGTH_SHORT).show();
                    } });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tab_fragment2, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setSubtitle("간병내역");
        btn_review = view.findViewById(R.id.btn_review);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            btn_review.setVisibility(btn_review.VISIBLE);
//            if (!calledAlready) {
//                FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
//                calledAlready = true;
//            }
            //listview 만들기
            listview = view.findViewById(R.id.listview);
            adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listitem, fileList);
            listview.setAdapter(adapter);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            email = user.getEmail();

            FirebaseDatabase database2 = FirebaseDatabase.getInstance();
            DatabaseReference patientRef2 = database2.getReference("matching");

            patientRef2.orderByChild("User_email").equalTo(email).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                        pNameEqual = fileSnapshot.child("Patient_name").getValue(String.class);
                        fileList3.add(pNameEqual);
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
            patientRef.orderByChild("useremail").equalTo(email).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;

                    // 클래스 모델이 필요?
                    for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                        //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                        //하위키들의 value를 어떻게 가져오느냐???
                        i++;
                        username= fileSnapshot.child("username").getValue(String.class);
                        pName = fileSnapshot.child("pName").getValue(String.class);
                        pAge=fileSnapshot.child("pAge").getValue(String.class);
                        pGender = fileSnapshot.child("pGender").getValue(String.class);
                        pDisease = fileSnapshot.child("pDisease").getValue(String.class);
                        uphone1 = fileSnapshot.child("userphone").getValue(String.class);

                        if (fileList3.contains(pName)){
                            count=i-1;
                            showDialog(pName);
                            selectName=pName;

                        }
                        String str = "환자분 성함: " + pName + "  환자분 나이: " + pAge + "세   환자분성별: " + pGender + "  환자분 병명: " + pDisease;

                        Log.i("TAG: value is ", str);
                        fileList.add(str);
                        fileList2.add(pName);

                    }
                    adapter.notifyDataSetChanged();
//                    listView.getChildAt(count).setBackgroundColor(Color.GREEN);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("TAG: ", "Failed to read value", databaseError.toException());
                }
            });

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    adapterView.getItemAtPosition(pos);

                    Intent intent = new Intent(getActivity(), TalkList.class);
                    intent.putExtra("uemail",email);
                    intent.putExtra("pName",fileList2.get(pos).toString());
                    intent.putExtra("uphone",uphone1);
                    startActivity(intent);

                }
            });
            btn_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent3 = new Intent(getActivity(), Review.class);
                    intent3.putExtra("uemail",email);
                    intent3.putExtra("username",username);
                    startActivity(intent3);
                }
            });
        }
        else{
            listview = view.findViewById(R.id.listview);
            adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listitem, fileList);
            fileList.add("로그인 후 사용하실수 있습니다.");
            listview.setAdapter(adapter);
            btn_review.setVisibility(btn_review.INVISIBLE);
        }

        return view;
    }


}