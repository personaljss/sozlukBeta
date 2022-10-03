package com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yusufemirbektas.sozlukBeta.mainApplication.forum.showEntries.dataModel.SubjectEntryModel;

import java.util.List;

public class SubjectEntriesViewModel extends ViewModel {
    private MutableLiveData<Integer> subjectId=new MutableLiveData<>();
    private MutableLiveData<Integer> commentId=new MutableLiveData<>();
    private MutableLiveData<List<SubjectEntryModel>> subjectEntries=new MutableLiveData<>();

    public void setSubjectId(int subjectId){
        this.subjectId.setValue(subjectId);
    }
    public void setCommentId(int commentId){
        this.commentId.setValue(commentId);
    }

    public void setSubjectEntries(List<SubjectEntryModel> subjectEntries){
        this.subjectEntries.setValue(subjectEntries);
    }
    public void postSubjectEntries(List<SubjectEntryModel> subjectEntries){
        this.subjectEntries.postValue(subjectEntries);
    }



    public LiveData<Integer> getSubjectId() {
        return subjectId;
    }

    public LiveData<Integer> getCommentId() {
        return commentId;
    }

    public LiveData<List<SubjectEntryModel>> getSubjectEntries() {
        return subjectEntries;
    }
}
