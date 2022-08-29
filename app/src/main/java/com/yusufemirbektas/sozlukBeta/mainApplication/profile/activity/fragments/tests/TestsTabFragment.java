package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.tests;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.viewModel.ProfileDataViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TestsTabFragment extends Fragment {
    private static final String TAG = "TestsTabFragment";
    private ProfileDataViewModel profileDataViewModel;
    private List<Test> tests;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile_tests_tab_fragment,container,false);
        profileDataViewModel=new ViewModelProvider(getActivity()).get(ProfileDataViewModel.class);
        tests=profileDataViewModel.getTests().getValue();

        for(Test test: tests){
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("ddMMyy");
            LocalDate date=LocalDate.parse(test.getDate(),formatter);
            DateTimeFormatter formatter1=DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateString=formatter1.format(date);
            test.setDate(dateString);
        }

        adapter=new TestViewAdapter(tests);
        manager=new LinearLayoutManager(getActivity());
        recyclerView=view.findViewById(R.id.tests_tab_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        return view;
    }
}
