package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.othersProfile;

import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.imageUtils.ImageUtils.stringToBitmap;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.BundleKeys;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentOthersProfileBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.MainActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.tabs.ProfileViewPagerAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.viewModel.ProfileDataViewModel;

public class OthersProfileFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "OthersProfileFragment";
    private FragmentOthersProfileBinding binding;
    //tab titles
    private final String[] TAB_TITLES = {"TESTLER", "ENTRILER"};
    //constants of image size when uploading the profile photo
    private static final int LOW_QUALITY_IMAGE_SIZE = 30000;
    private static final int HIGH_QUALITY_IMAGE_SIZE = 120000;
    //flags to display the profile
    public static final int FIRST_TIME = 1;
    public static final int NOT_FIRST_TIME = 0;
    //Image type constant to get profile picture from server
    public static final int IMAGE_TYPE_PROFILE = 0;
    //ViewModel
    private ProfileDataViewModel viewModel;
    private Header headerUi;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOthersProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileDataViewModel.class);

        Bundle args = getArguments();
        int userCode = -1;
        if (args != null) {
            userCode = args.getInt(BundleKeys.USERCODE, -1);
            //userCode = args.getInt(ForumActivity.BundleKeys.USERCODE, -1);
        }
        viewModel.setUserCode(userCode);
        headerUi = viewModel.getHeader().getValue();
        viewModel.getHeader().observe(getViewLifecycleOwner(), new Observer<Header>() {
            @Override
            public void onChanged(Header header) {
                headerUi = header;
                setUpHeaderUi(header);
            }
        });

        //if it is the first time that this page is opened, load the contents
        if (headerUi == null) {
            setProgressBarsVisible();
            viewModel.loadProfileData();
        } else {
            setUpHeaderUi(headerUi);
        }

        FragmentManager fm = getChildFragmentManager();
        Lifecycle lifecycle = getViewLifecycleOwner().getLifecycle();
        binding.viewPagerProfile.setAdapter(new ProfileViewPagerAdapter(fm, lifecycle));

        new TabLayoutMediator(binding.tabLayout, binding.viewPagerProfile, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(TAB_TITLES[position]);
            }
        }).attach();


        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadProfileData();
                /*
                NavController navController=Navigation.findNavController(view);
                navController.navigate(R.id.action_othersProfileFragment_self,args);

                 */
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setOnClickListeners() {
        binding.homeButtonImageView.setOnClickListener(this);
        binding.profilePpImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.homeButtonImageView) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else if (v == binding.profilePpImageView) {
            NavController navController = Navigation.findNavController(v);
            Bundle args = new Bundle();
            args.putInt(BundleKeys.USERCODE, viewModel.getUserCode().getValue());
            navController.navigate(R.id.action_othersProfileFragment_to_showPpFragment, args);
        }
    }


    private void setProgressBarsVisible() {
        binding.profileProgressBar.setVisibility(View.VISIBLE);
        binding.profilePpProgressBar.setVisibility(View.VISIBLE);
    }

    private void setProgressBarsGone() {
        binding.profileProgressBar.setVisibility(View.GONE);
        binding.profilePpProgressBar.setVisibility(View.GONE);
    }

    private void setUpHeaderUi(Header header) {
        //the page is loaded so progress bar is gone
        setProgressBarsGone();
        //onclicks
        setOnClickListeners();
        //swipe-refresh
        binding.swipeRefreshLayout.setRefreshing(false);
        binding.profileNickNameTextView.setText(header.getNickName());
        binding.profileTestsTextView.setText(String.valueOf(header.getTotalTests()));
        binding.profileChallengesTextView.setText(String.valueOf(header.getTotalChallenges()));
        binding.profilePointsTextView.setText(String.valueOf(header.getTotalPoints()));
        binding.profileSocialsTextView.setText(String.valueOf(header.getSocialEarned()));
        //setting the profile photo
        if (!(header.getProfilePhoto().equals("1") || header.getProfilePhoto().equals("0"))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.profilePpImageView.setImageBitmap(stringToBitmap(header.getProfilePhoto()));
            } else {
                //do something
            }
        }

    }

}
