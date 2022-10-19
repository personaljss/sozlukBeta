package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication;

public interface EntryEventListener {
    void onLiked(int userCode,int subjectId ,int commentId, int likeStatus, int itemPosition);
    void onSubjectClicked(int subjectId, int commentId, String subjectName);
    void onProfileClicked(int userCode);
    void onLikeDetails(int subjectId, int commentId);
}
