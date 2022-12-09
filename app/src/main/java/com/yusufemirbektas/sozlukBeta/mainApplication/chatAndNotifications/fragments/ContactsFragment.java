package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.fragments;

import android.content.BroadcastReceiver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yusufemirbektas.sozlukBeta.databinding.FragmentContactsBinding;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.activities.ContactsNotificationActivity;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.ContactsViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.models.Contact;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils.ContactsRvAdapter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


public class ContactsFragment extends Fragment implements ContactsNotificationActivity.EventListener {
    private FragmentContactsBinding binding;
    private ContactsViewModel viewModel;
    private List<Contact> contacts;
    private ContactsRvAdapter rvAdapter;
    private boolean isUiSet=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentContactsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel=new ViewModelProvider(getActivity()).get(ContactsViewModel.class);
        ((ContactsNotificationActivity) getActivity()).setEventListener(this);

        contacts=viewModel.getContacts().getValue();
        if(contacts==null){
            int date=0;
            viewModel.fetchContacts(date);
        }
        viewModel.getContacts().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                if(contacts==null){
                    return;
                }
                ContactsFragment.this.contacts=contacts;
                if(!isUiSet){
                    setUpUi();
                }else {
                    rvAdapter.setContacts(contacts);
                    rvAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void setUpUi() {
        rvAdapter=new ContactsRvAdapter();
        rvAdapter.setContext(getContext());
        rvAdapter.setContacts(contacts);
        binding.contactsRecyclerView.setAdapter(rvAdapter);
        binding.contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.contactsRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ContactsNotificationActivity) getActivity()).setEventListener(null);
        isUiSet=false;
    }

    @Override
    public void onStartTapped() {
        binding.contactsRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onUnseenMessageSeen(int index) {
        Contact contact=contacts.get(index);
        contact.setUs0ls(contact.getTotalMessages());
        contacts.set(index,contact);
        viewModel.setContacts(contacts);
    }
}