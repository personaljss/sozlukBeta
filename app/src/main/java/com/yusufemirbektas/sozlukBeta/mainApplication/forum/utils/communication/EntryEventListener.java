package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication;

public interface EntryEventListener {
    void onLiked(String userCode,int subjectId ,int commentId, int likeStatus, int itemPosition);
    void onSubjectClicked(int subjectId, int commentId, String subjectName);
    void onProfileClicked(String userCode);
    void onLikeDetails(int subjectId, int commentId);
}
