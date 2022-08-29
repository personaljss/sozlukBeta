package com.yusufemirbektas.sozlukBeta.mainApplication.profile.activity.fragments.tests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.profile.dataModels.Test;

import java.util.List;

public class TestViewAdapter extends RecyclerView.Adapter<TestViewAdapter.TestViewHolder> {
    private List<Test> tests;

    public static class TestViewHolder extends RecyclerView.ViewHolder{
        public TextView dateTextView;
        public TextView courseTextView;
        public TextView subjectTextView;
        public TextView correctsTextView;
        public TextView wrongsTextView;
        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView=itemView.findViewById(R.id.date_textView);
            courseTextView=itemView.findViewById(R.id.course_TextView);
            subjectTextView=itemView.findViewById(R.id.subject_textView);
            correctsTextView=itemView.findViewById(R.id.corrects_textView);
            wrongsTextView=itemView.findViewById(R.id.wrongs_textView);
        }
    }

    public TestViewAdapter(List<Test> tests) {
        this.tests = tests;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_test_item,parent,false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Test currentItem=tests.get(position);
        holder.dateTextView.setText(currentItem.getDate());
        holder.courseTextView.setText("ders:"+currentItem.getLessonNo());
        holder.subjectTextView.setText("konu: "+currentItem.getTestNo());
        holder.correctsTextView.setText("doğru:"+currentItem.getCorrectAnswers());
        holder.wrongsTextView.setText("yanlış:"+currentItem.getWrongAnswers());
    }


    @Override
    public int getItemCount() {
        return tests.size();
    }
}