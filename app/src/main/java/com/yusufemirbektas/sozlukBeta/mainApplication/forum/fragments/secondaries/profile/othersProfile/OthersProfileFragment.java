package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.othersProfile;

import static com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.image.ImageUtils.stringToBitmap;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

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
import com.yusufemirbektas.sozlukBeta.data.UserData;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentOthersProfileBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profile.tabs.ProfileViewPagerAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.profileList.ProfileListFragment;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.PointsViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.homePage.MainActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Header;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.ProfileDataViewModel;

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
    private NavController navController;
    private PointsViewModel pointsViewModel;

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
        pointsViewModel=new ViewModelProvider(getActivity()).get(PointsViewModel.class);
        navController=Navigation.findNavController(view);

        Bundle args = getArguments();
        int userCode = -1;
        if (args != null) {
            userCode = args.getInt(BundleKeys.USERCODE, -1);
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

        viewModel.following.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==1){
                    binding.followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.unFollowUser(viewModel.getUserCode().getValue());
                        }
                    });
                }else if(integer==0){
                    binding.followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.followUser(viewModel.getUserCode().getValue());
                        }
                    });
                }
            }
        });

        viewModel.getFollowResult().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer!=-1){
                    Toast.makeText(getContext(), viewModel.getFollowComment(), Toast.LENGTH_SHORT).show();
                    if(integer==0){
                        binding.followButton.setText("takip ediyorsun");
                    }else{
                        binding.followButton.setText("takip et");
                    }
                    viewModel.setFollowResult(-1);
                }
            }
        });

        //if it is the first time that this page is opened, load the contents
        if (headerUi == null) {
            setProgressBarsVisible();
            viewModel.loadProfileData();
        } else {
            setUpHeaderUi(headerUi);
        }

        pointsViewModel.getEntryItemLikeStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer!=PointsViewModel.DEFAULT_STATUS){
                    if(viewModel.getHeader().getValue()!=null){
                        //!!!!!!!!!!!!!!DEBUG HERE !!!!!!!!!!!!!!!!
                        int points=viewModel.getHeader().getValue().getTotalPoints();
                        viewModel.getHeader().getValue().setTotalPoints(points+integer);
                        binding.profilePointsTextView.setText(String.valueOf(points+integer));
                    }
                }
            }
        });

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
        binding.profileSocialsLayout.setOnClickListener(this);
        //binding.followButton.setOnClickListener(this);
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
        }else if(v==binding.profileSocialsLayout){
            PopupMenu menu=new PopupMenu(getContext(),v);
            menu.getMenuInflater().inflate(R.menu.forum_socials_menu,menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Bundle args=new Bundle();
                    args.putInt(BundleKeys.USERCODE, viewModel.getUserCode().getValue());
                    switch (item.getItemId()){
                        case R.id.followers:
                            args.putInt(BundleKeys.PROFILE_LIST_KEY, ProfileListFragment.FOLLOWERS_CODE);
                            break;
                        case R.id.followedBys:
                            args.putInt(BundleKeys.PROFILE_LIST_KEY, ProfileListFragment.FOLLOWED_BYs_CODE);
                            break;
                        default:
                            break;
                    }
                    navController.navigate(R.id.action_othersProfileFragment_to_profileListFragment,args);
                    return true;
                }
            });
            menu.show();
        }else if(v==binding.followButton){
            /*
            if(viewModel.getHeader().getValue().getFollowing()==1){
                viewModel.unFollowUser(viewModel.getUserCode().getValue());
            }else {
                viewModel.followUser(viewModel.getUserCode().getValue());
            }
             */
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
        //follow button check
        if(header.getFollowing()==1){
            binding.followButton.setText("takip ediyorsun");
        }else if(header.getFollower()==1){
            binding.followButton.setText("sen de takip et");
        }
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
