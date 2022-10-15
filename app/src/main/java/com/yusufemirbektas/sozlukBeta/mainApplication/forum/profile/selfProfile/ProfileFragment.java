package com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.selfProfile;

import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.imageUtils.ImageUtils.stringToBitmap;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentProfileBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.MainActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.ForumActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.PointsViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.tabs.ProfileViewPagerAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.dataModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.profile.viewModel.ProfileDataViewModel;

public class ProfileFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    private ProfileDataViewModel viewModel;
    private PointsViewModel pointsViewModel;
    //tab titles
    private final String[] TAB_TITLES = {"TESTLER", "ENTRILER"};
    //Image type constant to get profile picture from server
    public static final int IMAGE_TYPE_PROFILE = 0;
    private NavController navController;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileDataViewModel.class);
        navController= Navigation.findNavController(view);

        /*
        viewModel.getReload().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    setProgressBarsVisible();
                    viewModel.loadProfileData();
                    viewModel.setReload(false);
                }
            }
        });

         */
        pointsViewModel=new ViewModelProvider(getActivity()).get(PointsViewModel.class);
        pointsViewModel.getPointsAvailable().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer!=PointsViewModel.DEFAULT_POINTS){
                    if(viewModel.getHeader().getValue()!=null){
                        viewModel.getHeader().getValue().setTotalPoints(integer);
                    }
                }
            }
        });

        int userCode = UserData.getUserCode();
        viewModel.setUserCode(userCode);
        //if it is the first time that this page is opened, load the contents
        if (viewModel.getHeader().getValue() == null ) {
            setProgressBarsVisible();
            viewModel.loadProfileData();
        } else {
            setUpHeaderUi(viewModel.getHeader().getValue());
        }

        viewModel.getHeader().observe(getViewLifecycleOwner(), new Observer<Header>() {
            @Override
            public void onChanged(Header header) {
                setUpHeaderUi(header);
            }
        });

        //binding.viewPagerProfile.setAdapter(new ProfileViewPagerAdapter(this));
        FragmentManager fm = getChildFragmentManager();
        Lifecycle lifecycle = getViewLifecycleOwner().getLifecycle();
        ProfileViewPagerAdapter adapter=new ProfileViewPagerAdapter(fm, lifecycle);
        adapter.setSelf(true);

        binding.viewPagerProfile.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPagerProfile, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(TAB_TITLES[position]);
            }
        }).attach();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int actionId=navController.getCurrentDestination().getId();
                NavOptions navOptions=new NavOptions.Builder().setPopUpTo(actionId,true).build();
                navController.navigate(R.id.action_profileFragment_self,null,navOptions);
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

    @Override
    public void onClick(View v) {
        if (v == binding.homeButtonImageView) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }else if(v==binding.profilePpImageView){
            Bundle args=new Bundle();
            args.putInt(BundleKeys.USERCODE,viewModel.getUserCode().getValue());
            navController.navigate(R.id.action_profileFragment_to_showPpFragment,args);
        }else if(v==binding.settingsImageButton){
            navController.navigate(R.id.action_profileFragment_to_settingsFragment);
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

    private void setOnClickListeners(){
        binding.profilePpImageView.setOnClickListener(this);
        binding.homeButtonImageView.setOnClickListener(this);
        binding.settingsImageButton.setOnClickListener(this);
    }

    private void setUpHeaderUi(Header header) {
        //the page is loaded so progress bar is gone
        setProgressBarsGone();
        setOnClickListeners();
        binding.profileNickNameTextView.setText(String.valueOf(header.getNickName()));
        binding.profileTestsTextView.setText(String.valueOf(header.getTotalTests()));
        binding.profileChallengesTextView.setText(String.valueOf(header.getTotalChallenges()));
        binding.profilePointsTextView.setText(String.valueOf(header.getTotalPoints()));
        binding.profileSocialsTextView.setText(String.valueOf(header.getSocialEarned()));
        //setting the profile photo

        if (!header.getProfilePhoto().equals("1")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.profilePpImageView.setImageBitmap(stringToBitmap(header.getProfilePhoto()));
            } else {
                //do something
            }
        }

    }

}
