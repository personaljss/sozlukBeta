package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.newContent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentNewEntryBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses.NewContentServerResponse;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.EntryEventListener;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.NewEntryViewModel;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NewEntryFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "NewEntryFragment";
    private static final int LENGTH_LIMIT=500;
    private EntryEventListener entryEventListener;
    private FragmentNewEntryBinding binding;
    private Bundle args;
    private NewEntryViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewEntryBinding.inflate(inflater, container, false);
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
                if(!stringStringHashMap.get(NewEntryViewModel.SUBJECT_ID_KEY).equals("")){
                    int subjectId=Integer.parseInt(stringStringHashMap.get(NewEntryViewModel.SUBJECT_ID_KEY));
                    int commentId=Integer.parseInt(stringStringHashMap.get(NewEntryViewModel.COMMENT_ID_KEY));
                    entryEventListener.onSubjectClicked(subjectId,commentId,subjectName);
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
    }

    /*
    private void postNewEntry(int subjectId, String entry) {
        //{"result":0,"comment":"Yorum yapıldı.","time":"97.834825515747 ms"}
        OkHttpClient client = ApiClientOkhttp.getInstance();
        RequestBody requestBody = new FormBody.Builder()
                .add("subjectID", String.valueOf(subjectId))
                .add("userCode", String.valueOf(UserData.getUserCode()))
                .add("comment", entry)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.NEW_COMMENT)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "onFailure: ");
                Toast.makeText(getContext(), "bir hata oluştu, lütfen internet bağlantınızı kontrol edin", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i(TAG, "onResponse: ");
                if(response.isSuccessful()){
                    handleResponse(response);
                }
            }
        });
    }

    private void handleResponse(Response response) throws IOException {
        Gson gson=new Gson();
        String jsonResponse=response.body().string();
        NewContentServerResponse serialisedResponse=gson.fromJson(jsonResponse,NewContentServerResponse.class);
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), serialisedResponse.getComment(), Toast.LENGTH_SHORT).show();
                if(serialisedResponse.getResult()==0){
                    //go to the subject
                    getActivity().onBackPressed();
                }
            }
        });
    }
     */

    private void setOnClickListeners(){
        binding.newEntryPublishTextView.setOnClickListener(this);
        binding.newEntryBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==binding.newEntryPublishTextView){
            /*
            int subjectId=args.getInt(BundleKeys.SUBJECT_ID,-1);
            String entry=binding.newEntryContentEditText.getText().toString();
            postNewEntry(subjectId,entry);
             */
            int subjectId=args.getInt(BundleKeys.SUBJECT_ID,-1);
            String entry=binding.newEntryContentEditText.getText().toString();
            viewModel.postNewEntry(subjectId,entry);
        }else if(v==binding.newEntryBackButton){
            getActivity().onBackPressed();
        }
    }
}