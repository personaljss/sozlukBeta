package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.bottomNavHosts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentNewSubjectBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.serverResponses.NewContentServerResponse;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.pointsRelated.BoostDialog;
import com.yusufemirbektas.sozlukBeta.serverClient.ApiClientOkhttp;
import com.yusufemirbektas.sozlukBeta.serverClient.ServerAdress;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NewSubjectFragment extends Fragment implements View.OnClickListener, BoostDialog.OnPickListener {
    private static final String TAG = "NewSubjectFragment";
    private final int CHAR_LIMIT=50;
    private String points="0";
    private FragmentNewSubjectBinding binding;
    private NavController navController;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewSubjectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController=Navigation.findNavController(view);
        setOnClickListeners();

        EditText titleEditText=binding.newSubjectTitleEditText;
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(start+after>CHAR_LIMIT){
                    titleEditText.setError("başlık en fazla 50 karakter olabilir");
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

    private void publishSubject(String subjectName, String content, String points, String channel){
        //$userCode = $_POST["userCode"];
        //    $subjectName = $_POST["subjectName"];
        //    $comment = $_POST["comment"];
        //    $pointInvested = $_POST["invested"];
        //    $channel = $_POST["channel"];
        OkHttpClient client = ApiClientOkhttp.getInstance();
        RequestBody requestBody = new FormBody.Builder()
                .add("userCode",String.valueOf(UserData.getUserCode()))
                .add("subjectName", subjectName)
                .add("comment",content)
                .add("invested",points)
                .add("channel",channel)
                .build();

        Request request = new Request.Builder()
                .url(ServerAdress.SERVER_URL + ServerAdress.CREATE_SUBJECT)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Log.i(TAG, "onResponse: ");
                    handleResponse(response,subjectName);
                }
            }
        });
    }

    private boolean validatePointsInput(String points){
        try {
            int p=Integer.parseInt(points);
            if(p<0){
                Toast.makeText(getContext(), "puan kısmına negatif bir sayı giremezsiniz", Toast.LENGTH_SHORT).show();
                return false;
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "lütfen puan kısmına pozitif bir tam sayı girin", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void handleResponse(Response response, String subjectName) throws IOException {
        Gson gson=new Gson();
        String jsonResponse=response.body().string();
        NewContentServerResponse serialisedResponse=gson.fromJson(jsonResponse,NewContentServerResponse.class);
        Handler handler=new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Bundle args=new Bundle();
                String comment=serialisedResponse.getComment();
                int i=1;
                StringBuilder sb=new StringBuilder();
                while(i>=0){
                    try {
                        int digit=Integer.parseInt(comment.substring(i-1,i));
                        sb.append(digit);
                        i++;
                    }catch (NumberFormatException e){
                        i=-1;
                    }
                }
                try {
                    args.putInt(BundleKeys.SUBJECT_ID,Integer.parseInt(sb.toString()));
                    args.putInt(BundleKeys.COMMENT_ID,1);
                    args.putString(BundleKeys.SUBJECT_NAME,subjectName);
                    navController.navigate(R.id.action_newSubjectFragment_to_forum_subject,args);
                }catch (NumberFormatException e){
                    //subject ccreated before
                    Toast.makeText(getContext(), "konu daha önce oluşturulmuş.", Toast.LENGTH_LONG).show();
                    int j=6;
                    StringBuilder sb2=new StringBuilder();
                    while(j>=0){
                        try {
                            int digit=Integer.parseInt(comment.substring(j-1,j));
                            sb2.append(digit);
                            j++;
                        }catch (NumberFormatException e2){
                            j=-1;
                        }
                    }
                    args.putInt(BundleKeys.SUBJECT_ID,Integer.parseInt(sb2.toString()));
                    args.putInt(BundleKeys.COMMENT_ID,1);
                    args.putString(BundleKeys.SUBJECT_NAME,subjectName);
                    navController.navigate(R.id.action_newSubjectFragment_to_forum_subject,args);
                }
            }
        });
    }

    private void setOnClickListeners(){
        binding.newSubjectPublishTextView.setOnClickListener(this);
        binding.newSubjectBackButton.setOnClickListener(this);
        binding.likeDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==binding.newSubjectPublishTextView){
            Log.i(TAG, "onClick: publish");
            if(points.equals(null)){
                Toast.makeText(getContext(), "lütfen like miktarı seçiniz", Toast.LENGTH_SHORT).show();
                return;
            }
            String subjectName=binding.newSubjectTitleEditText.getText().toString();
            String content=binding.newSubjectContentEditText.getText().toString();
            if(validatePointsInput(points)){
                publishSubject(subjectName,content,points,"0");
            }
        }else if(v==binding.newSubjectBackButton){
            getActivity().onBackPressed();
        }else if(v==binding.likeDialog){
            BoostDialog dialog=new BoostDialog();
            dialog.setListener(this);
            dialog.show(getChildFragmentManager(),null);
        }
    }

    @Override
    public void onPick(String num) {
        this.points=num;
        binding.newSubjectPointsInvested.setText("puan: "+num);
    }
}