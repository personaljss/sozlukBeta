package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.tabs.tests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Test;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewHolders.ProgressBarViewHolder;

import java.util.List;


public class TestsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "Recycler View";
    private List<Test> tests;
    private Context context;

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    private static final int LOADING=0;
    private static final int DONE=1;

    private static class TestViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView courseTextView;
        public TextView subjectTextView;
        public TextView correctsTextView;
        public TextView wrongsTextView;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_textView);
            courseTextView = itemView.findViewById(R.id.course_TextView);
            subjectTextView = itemView.findViewById(R.id.subject_textView);
            correctsTextView = itemView.findViewById(R.id.corrects_textView);
            wrongsTextView = itemView.findViewById(R.id.wrongs_textView);
        }
    }


    public TestsRvAdapter(Context context, List<Test> tests) {
        this.tests = tests;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==LOADING){
            view=LayoutInflater.from(context).inflate(R.layout.item_recycler_view_progress_bar,parent,false);
            return new ProgressBarViewHolder(view);
        }else{
            view=LayoutInflater.from(context).inflate(R.layout.item_profile_tests, parent, false);
            return new TestViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==LOADING){
            ((ProgressBarViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
        }else{
            Test currentItem = tests.get(position);
            ((TestViewHolder) holder).dateTextView.setText(currentItem.getDate());
            ((TestViewHolder) holder).courseTextView.setText("ders:" + currentItem.getLessonNo());
            ((TestViewHolder) holder).subjectTextView.setText("konu: " + currentItem.getTestNo());
            ((TestViewHolder) holder).correctsTextView.setText("doğru:" + currentItem.getCorrectAnswers());
            ((TestViewHolder) holder).wrongsTextView.setText("yanlış:" + currentItem.getWrongAnswers());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (tests.get(position)==null)?LOADING:DONE;
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }
}