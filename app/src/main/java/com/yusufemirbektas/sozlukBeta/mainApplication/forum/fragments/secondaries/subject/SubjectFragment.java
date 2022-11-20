package com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.subject;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.databinding.FragmentSubjectEntriesBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.fragments.secondaries.paging.PagerDialog;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.communication.BundleKeys;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.EntriesItemDecoration;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.PointsViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.dataModels.itemModels.Entry;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.utils.recyclerView.adapters.EntriesRvAdapter;
import com.yusufemirbektas.sozlukBeta.mainApplication.forum.viewModels.EntriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class SubjectFragment extends Fragment implements View.OnClickListener, PagerDialog.OnPageSelectedListener {
    private static final String TAG = "SubjectFragment";
    private static final int VERTICAL_ITEM_SPACE = 30;
    public static final int ENTRY_PER_PAGE = 10;
    private FragmentSubjectEntriesBinding binding;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter recycleViewAdapter;
    private List<Entry> entryModels;
    private EntriesViewModel viewModel;
    private Bundle bundle;
    private NavController navController;
    private PointsViewModel pointsViewModel;
    private boolean isDecorated = false;
    //this value keeps track of the id of the first entry in the entryModels
    private int startCommentId = 1;
    //current page value
    private int currentPage = 1;


    private boolean isUiSet = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubjectEntriesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EntriesViewModel.class);

        binding.swipeRefreshLayout.setRefreshing(false);

        bundle = getArguments();
        navController = Navigation.findNavController(view);

        Toolbar toolbar=binding.subjectToolBar;
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //initialing the id of the top-most entry
        startCommentId = bundle.getInt(BundleKeys.COMMENT_ID, 1);


        if (entryModels == null) {
            viewModel.loadSubjectEntries(bundle.getInt(BundleKeys.SUBJECT_ID, -1), startCommentId, false);
        } else {
            if (!isUiSet) {
                setUpUi();
            }
        }

        viewModel.getEntries().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> subjectEntryModels) {
                entryModels = subjectEntryModels;
                binding.swipeRefreshLayout.setRefreshing(false);
                if (isUiSet) {
                    ((EntriesRvAdapter) recycleViewAdapter).setEntries(subjectEntryModels);
                    recycleViewAdapter.notifyDataSetChanged();
                    if (bundle.getInt(BundleKeys.COMMENT_ID, 1) > 1) {
                        binding.subjectEntriesRecyclerView.scrollToPosition(recycleViewAdapter.getItemCount() - 1);
                        bundle.putInt(BundleKeys.COMMENT_ID, 1);
                    }
                } else {
                    setUpUi();
                }
            }
        });

        pointsViewModel = new ViewModelProvider(getActivity()).get(PointsViewModel.class);
        pointsViewModel.getEntryItemPosition().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != PointsViewModel.DEFAULT_POS && entryModels != null && entryModels.size() > integer) {
                    Entry updatedEntry = entryModels.get(integer);
                    updatedEntry.setLikeStatus(updatedEntry.getLikeStatus() + pointsViewModel.getEntryItemLikeStatus().getValue());
                    updatedEntry.setLikePoint(updatedEntry.getLikePoint() + pointsViewModel.getEntryItemLikeStatus().getValue());
                    entryModels.set(integer, updatedEntry);
                    recycleViewAdapter.notifyItemChanged(integer);
                }
            }
        });


        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadSubjectEntries(bundle.getInt(BundleKeys.SUBJECT_ID, -1), startCommentId, false);
            }
        });



        //paging
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.subjectEntriesRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //pager visibility check
                    int pos = layoutManager.findLastVisibleItemPosition();
                    //Log.i(TAG, "onScrollChange: pos="+pos+" size="+entryModels.size());
                    currentPage = (pos + startCommentId) / ENTRY_PER_PAGE + 1;
                    Log.i(TAG, "onScrollChange: Y="+scrollY+" oldY="+oldScrollY+" X="+scrollX+" oldX="+oldScrollX);
                    binding.pageTextView.setText(String.valueOf(currentPage));
                    if (pos == entryModels.size() - 1) {
                        //bottom of list!
                        if (entryModels.size() == 0) {
                            return;
                        }
                        if (entryModels.get(entryModels.size() - 1) == null || viewModel.getTotalEntries() <= pos + startCommentId) {
                            //if the user is waiting for update already or this is the end of the feed, return
                            return;
                        }
                        //adding a null reference to the list to make adapter return progress bar
                        entryModels.add(null);
                        int testsSize = entryModels.size();
                        recycleViewAdapter.notifyItemInserted(testsSize - 1);
                        //load the entry items
                        viewModel.loadSubjectEntries(bundle.getInt(BundleKeys.SUBJECT_ID), startCommentId + testsSize - 1, true);
                    }

                }

            });

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isUiSet = false;
        pointsViewModel.refresh();
        binding = null;

        //startCommentId=1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setUpUi() {
        setUpRecyclerView();
        binding.progressBar.setVisibility(View.GONE);
        binding.subjectTextView.setText(bundle.getString(BundleKeys.SUBJECT_NAME));
        binding.subjectTextView.setVisibility(View.VISIBLE);
        binding.subjectFragmentNewEntry.setOnClickListener(this);
        binding.pageTextView.setOnClickListener(this);
        binding.progressBar.setVisibility(View.GONE);
        isUiSet = true;
        if (startCommentId != 1) {
            RecyclerView rv = (RecyclerView) binding.subjectEntriesRecyclerView;
            rv.smoothScrollToPosition(recycleViewAdapter.getItemCount() - 1);
        }
    }

    //spacing between elements in addItemDecoration.
    // See recyclerView/EntriesItemDecoration.class for implementation. -onerayhan
    // bool is for to not add any item decoration more than one
    private void setUpRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        recycleViewAdapter = new EntriesRvAdapter(entryModels, getContext());
        binding.subjectEntriesRecyclerView.setLayoutManager(layoutManager);
        binding.subjectEntriesRecyclerView.setAdapter(recycleViewAdapter);
        binding.subjectEntriesRecyclerView.addItemDecoration(new EntriesItemDecoration(VERTICAL_ITEM_SPACE));
        binding.subjectEntriesRecyclerView.setHasFixedSize(true);
        binding.subjectEntriesRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        if (v == binding.subjectFragmentNewEntry) {
            Bundle outArgs = new Bundle();
            int subjectId = bundle.getInt(BundleKeys.SUBJECT_ID);
            String subjectName = binding.subjectTextView.getText().toString();
            outArgs.putInt(BundleKeys.SUBJECT_ID, subjectId);
            outArgs.putString(BundleKeys.SUBJECT_NAME, subjectName);
            outArgs.putInt(BundleKeys.COMMENT_ID, viewModel.getTotalEntries());
            navController.navigate(R.id.action_subjectFragment_to_newEntryFragment, outArgs);
        } else if (v == binding.pageTextView) {
            Log.i(TAG, "onClick: pagePicker");
            Bundle pageArgs = new Bundle();
            int maxPage = viewModel.getTotalEntries() / ENTRY_PER_PAGE + 1;
            pageArgs.putInt(BundleKeys.CURRENT_PAGE, currentPage);
            pageArgs.putInt(BundleKeys.MAX_PAGE, maxPage);
            PagerDialog pagerDialog = new PagerDialog();
            pagerDialog.setArguments(pageArgs);
            pagerDialog.setOnPageSelectedListener(this);
            pagerDialog.show(getActivity().getFragmentManager(), null);
        }
    }

    @Override
    public void onPageSelected(int page) {
        if (page < 1) {
            return;
        }
        int initialPage = (startCommentId - 1) / ENTRY_PER_PAGE + 1;
        if (initialPage <= page) {
            binding.subjectEntriesRecyclerView.smoothScrollToPosition((page - 1) * ENTRY_PER_PAGE);
            return;
        }
        currentPage = page;
        binding.pageTextView.setText(String.valueOf(page));
        startCommentId = (page - 1) * ENTRY_PER_PAGE + 1;
        viewModel.loadSubjectEntries(bundle.getInt(BundleKeys.SUBJECT_ID), startCommentId, false);
    }
}
