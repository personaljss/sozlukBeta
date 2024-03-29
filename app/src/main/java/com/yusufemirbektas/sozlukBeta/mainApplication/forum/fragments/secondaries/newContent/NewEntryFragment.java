package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.newContent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.yusufemirbektas.sozlukBeta.databinding.FragmentNewEntry10Binding;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentNewEntryBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.EntryEventListener;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.NewEntryViewModel;

import java.util.HashMap;


public class NewEntryFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "NewEntryFragment";
    private static final int LENGTH_LIMIT=500;
    private EntryEventListener entryEventListener;
    private FragmentNewEntry10Binding binding;
    private Bundle args;
    private NewEntryViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewEntry10Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        entryEventListener=(EntryEventListener)getContext();
        viewModel= new ViewModelProvider(this).get(NewEntryViewModel.class);
        //getting the arguments
        args = getArguments();
        setOnClickListeners();
        EditText contentEditText=binding.newEntryContentEditText;
        String subjectName=args.getString(BundleKeys.SUBJECT_NAME,"");

        //observe new entry data
        viewModel.getNewEntryLoc().observe(getViewLifecycleOwner(), new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(HashMap<String, String> stringStringHashMap) {
                if(stringStringHashMap!=null){
                    if(!stringStringHashMap.get(NewEntryViewModel.SUBJECT_ID_KEY).equals("")){
                        int subjectId=Integer.parseInt(stringStringHashMap.get(NewEntryViewModel.SUBJECT_ID_KEY));
                        int commentId=Integer.parseInt(stringStringHashMap.get(NewEntryViewModel.COMMENT_ID_KEY));
                        //int newLoc=(commentId-9<1)?1:commentId-9;
                        int newLoc=((commentId-1)/10)*10+1;
                        entryEventListener.onSubjectClicked(subjectId,newLoc,subjectName);
                    }
                }else {
                    Toast.makeText(getContext(), "yeterli puanınız yok", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.newEntrySubjectTextView.setText(subjectName);
        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(start+after>=LENGTH_LIMIT){
                    Toast.makeText(getContext(), "entryler için karakter sınırı 500'dür.", Toast.LENGTH_SHORT).show();;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        entryEventListener=null;
        binding=null;
    }


    private void setOnClickListeners(){
        binding.newEntryPublishTextView.setOnClickListener(this);
        binding.newEntryBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==binding.newEntryPublishTextView){
            int subjectId=args.getInt(BundleKeys.SUBJECT_ID,-1);
            String entry=binding.newEntryContentEditText.getText().toString();
            viewModel.postNewEntry(subjectId,entry);
        }else if(v==binding.newEntryBackButton){
            getActivity().onBackPressed();
        }
    }
}