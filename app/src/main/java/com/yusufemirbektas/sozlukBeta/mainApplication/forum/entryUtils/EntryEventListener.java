package com.yusufemirbektas.sozlukBeta.mainApplication.forum.entryUtils;

public interface EntryEventListener {
    void onLiked(int userCode,int subjectId ,int commentId, int likeStatus, int itemPosition);
    void onSubjectClicked(int subjectId, int commentId, String subjectName);
    void onProfileClicked(int userCode);
}
