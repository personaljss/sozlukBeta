package com.yusufemirbektas.sozlukBeta.mainApplication.forum.trends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.ForumSubject;

import java.util.List;

public class TrendsViewModel extends ViewModel {
    MutableLiveData<List<ForumSubject>> forumSubjects=new MutableLiveData<>();

    public void setForumSubjects(List<ForumSubject> forumSubjects) {
        this.forumSubjects.setValue(forumSubjects);
    }

    public LiveData<List<ForumSubject>> getForumSubjects() {
        return forumSubjects;
    }

}
