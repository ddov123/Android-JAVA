package com.example.copycare;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import android.os.Handler ;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment extends Fragment {
    List fileList2 = new ArrayList<>();
    String username,message;
    ArrayAdapter adapter;
    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;
    private CircleIndicator3 mIndicator;
    private List fileList = new ArrayList<>();
    int count,many;
    private ListView listview;
    PieChart mPieChart,mPieChart2;
    TextView nation1,nation2,gender1,gender2,tv_many;;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Handler  sliderHandler = new Handler () ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragment newInstance(String param1, String param2) {
        TabFragment fragment = new TabFragment();
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
        View view = inflater.inflate(R.layout.tab_fragment, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        Button btn_login = view.findViewById(R.id.btn_login);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            btn_login.setVisibility(btn_login.INVISIBLE);
        }
        else{btn_login.setVisibility(btn_login.VISIBLE);}
        ViewPager2 viewPager = view.findViewById(R.id.viewpager);

//        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//
//        BannerAdapter BannerAdapter = new BannerAdapter(new Banner(), 3);
//        viewPager.setAdapter(BannerAdapter);
//ViewPager2
        mPager = view.findViewById(R.id.viewpager);
//Adapter
        FragmentManager fm = getChildFragmentManager();
        Lifecycle lifecycle = getViewLifecycleOwner().getLifecycle();
        pagerAdapter = new BannerAdapter(getChildFragmentManager(),lifecycle,num_page);
        mPager.setAdapter(pagerAdapter);
//Indicator
        mIndicator = view.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page, 0);
//ViewPager Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
/**
 * 이 부분 조정하여 처음 시작하는 이미지 설정.
 * 2000장 생성하였으니 현재위치 1002로 설정하여
 * 좌 우로 슬라이딩 할 수 있게 함. 거의 무한대로
 */
        mPager.setCurrentItem(900); //시작 지점
        mPager.setOffscreenPageLimit(3); //최대 이미지 수
        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position % num_page);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(),Login.class);
                startActivity(intent);
            }
        });
        Button btn_applyservice = view.findViewById(R.id.btn_applyservice);
        btn_applyservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    Intent intent = new Intent(getActivity(), Registration.class);
                    startActivity(intent);
                }
                else{Toast.makeText(getActivity(),"로그인 후 이용해주세요",Toast.LENGTH_SHORT).show();}
            }
        });

        nation1=view.findViewById(R.id.nation1);
        nation2=view.findViewById(R.id.nation2);
        gender1 = view.findViewById(R.id.gender1);
        gender2 = view.findViewById(R.id.gender2);

        mPieChart = view.findViewById(R.id.piechart);
        makechart("nation","내국인", Color.parseColor("#FE6DA8"),mPieChart,nation1);
        fileList=new ArrayList<>();
        makechart("nation","외국인",Color.parseColor("#56B7F1"),mPieChart,nation2);
        mPieChart.startAnimation();
        fileList=new ArrayList<>();

        mPieChart2 = view.findViewById(R.id.piechart2);
        makechart("sex","남성", Color.parseColor("#CBFF75"),mPieChart2,gender1);
        fileList=new ArrayList<>();
        makechart("sex","여성",Color.parseColor("#FFE65A"),mPieChart2,gender2);
        mPieChart2.startAnimation();
        tv_many = view.findViewById(R.id.tv_many);

        listview = view.findViewById(R.id.listview);
        fileList2=new ArrayList<>();
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listitem, fileList2);
        listview.setAdapter(adapter);

        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
        DatabaseReference patientRef = database1.getReference("matching");
        patientRef.child("chat").child("review").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    username = fileSnapshot.child("userName").getValue(String.class);
                    message = fileSnapshot.child("message").getValue(String.class);
                    String str = username + ":" + message;
                    Log.i("TAG: value is ", str);
                    Log.i("TAG: value is ", str);
                    fileList2.add(str);
                }
                adapter.notifyDataSetChanged();
            }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("TAG: ", "Failed to read value", databaseError.toException());
        }

        });
        return view;

    }

    public void makechart(String inchild,String inequl,int color,PieChart mPieChart,TextView textView){
    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
    DatabaseReference patientRef = database1.getReference("CareGiver");

        patientRef.orderByChild(inchild).equalTo(inequl).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // 클래스 모델이 필요?
            fileList=new ArrayList<>();
            for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                //하위키들의 value를 어떻게 가져오느냐???
                String nation = fileSnapshot.child(inchild).getValue(String.class);
                fileList.add(nation);
            }
            count=fileList.size();
            many+=count;
            tv_many.setText((many/2)+"명");
            mPieChart.addPieSlice(new PieModel("",count,color));
            textView.setText(String.valueOf(count));
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("TAG: ", "Failed to read value",error.toException());
        }
    });}
    @Override
    public void onPause () {
        super.onPause () ;
        sliderHandler.removeCallbacks (sliderRunnable) ;
    }

    @Override
    public void onResume () {
        super.onResume () ;
        sliderHandler.postDelayed (sliderRunnable , 2000 );
    }
    private Runnable sliderRunnable = new Runnable () {
        @Override
        public void run () {
            mPager.setCurrentItem (mPager.getCurrentItem () + 1 ) ;
        }
    } ;

}