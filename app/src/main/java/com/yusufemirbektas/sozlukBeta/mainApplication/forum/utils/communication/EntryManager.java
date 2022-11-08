package com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication;

import android.os.Bundle;

import androidx.navigation.NavController;

import com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profileList.ProfileListFragment;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.R;

public class EntryManager {
    private NavController navController;

    public EntryManager(NavController navController) {
        this.navController = navController;
    }

    public void goToProfile(int userCode) {
        Bundle args=new Bundle();
        args.putInt(BundleKeys.USERCODE,userCode);
        final int locId = navController.getCurrentDestination().getId();
        switch (locId) {
            case R.id.profileFragment:
                navController.navigate(R.id.action_profileFragment_self);
                break;
            case R.id.subjectFragment:
                navController.navigate(R.id.action_subjectFragment_to_othersProfileFragment,args);
                break;
            case R.id.othersProfileFragment:
                navController.navigate(R.id.action_othersProfileFragment_self,args);
                break;
            case R.id.mainFeedFragment:
                navController.navigate(R.id.action_mainFeedFragment_to_othersProfileFragment,args);
                break;
            case R.id.profileListFragment:
                navController.navigate(R.id.action_profileListFragment_to_othersProfileFragment,args);
                break;
            default:
                break;
        }
    }

    public void goToSubject(int subjectId, int commentId, String subjectName) {
        final int locId = navController.getCurrentDestination().getId();
        Bundle args=new Bundle();
        args.putInt(BundleKeys.SUBJECT_ID,subjectId);
        args.putInt(BundleKeys.COMMENT_ID,commentId);
        args.putString(BundleKeys.SUBJECT_NAME, subjectName);
        switch (locId) {
            case R.id.profileFragment:
                navController.navigate(R.id.action_profileFragment_to_forum_subject,args);
                break;
            case R.id.subjectFragment:
                navController.navigate(R.id.action_subjectFragment_self,args);
                break;
            case R.id.othersProfileFragment:
                //!!!!!!!!dönücem!!!!!!!!
                try{
                    navController.navigate(R.id.action_othersProfileFragment_to_forum_subject,args);
                }catch (IllegalArgumentException e){
                    navController.navigate(R.id.action_othersProfileFragment_to_subjectFragment,args);
                }
                break;
            case R.id.mainFeedFragment:
                navController.navigate(R.id.action_mainFeedFragment_to_forum_subject,args);
                break;
            case R.id.newEntryFragment:
                navController.navigate(R.id.action_newEntryFragment_to_subjectFragment,args);
                break;
            default:
                break;
        }
    }

    public void goToLikeDetails(int subjectId, int commentId){
        Bundle args=new Bundle();
        args.putInt(BundleKeys.SUBJECT_ID,subjectId);
        args.putInt(BundleKeys.COMMENT_ID,commentId);
        args.putInt(BundleKeys.PROFILE_LIST_KEY, ProfileListFragment.LIKED_ONEs_CODE);
        final int locId = navController.getCurrentDestination().getId();
        switch (locId) {
            case R.id.profileFragment:
                navController.navigate(R.id.action_profileFragment_to_profileListFragment,args);
                break;
            case R.id.subjectFragment:
                navController.navigate(R.id.action_subjectFragment_to_profileListFragment, args);
                break;
            case R.id.othersProfileFragment:
                navController.navigate(R.id.action_othersProfileFragment_to_profileListFragment, args);
                break;
            case R.id.mainFeedFragment:
                navController.navigate(R.id.action_mainFeedFragment_to_profileListFragment, args);
                break;
            default:
                break;
        }
    }
}
