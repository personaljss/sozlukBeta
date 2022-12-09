package com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yusufemirbektas.sozlukBeta.R;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.ContactsViewModel;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.models.Contact;
import com.yusufemirbektas.sozlukBeta.mainApplication.chatAndNotifications.utils.viewHolder.ContactViewHolder;

import java.util.List;

public class ContactsRvAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Contact> contacts;
    public static final int DONE=1;
    public static final int LOADING=0;

    public void setContext(Context context) {
        this.context = context;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view;
        final RecyclerView.ViewHolder holder;
        if(viewType==DONE){
            view=LayoutInflater.from(context).inflate(R.layout.contact_item,parent,false);
            holder=new ContactViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= holder.getAdapterPosition();
                    Contact contact= contacts.get(pos);
                    ((ContactViewHolder) holder).unseenMessages.setText("");
                    ((ContactViewHolder.OnContactEventListener) context).onNavigateToChat(contact.getUserCode());
                }
            });
        }else {
            //will be implemented later
            holder=null;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Contact contact=contacts.get(position);
        if(holder.getItemViewType()==DONE){
            ((ContactViewHolder) holder).text.setText(contact.getLastMessage());
            ((ContactViewHolder) holder).unseenMessages.setText(String.valueOf(contact.getTotalMessages()-contact.getUs0ls()));
            ((ContactViewHolder) holder).date.setText(contact.stringDate("dd/MM/yyyy HH:mm"));
            ((ContactViewHolder) holder).userName.setText(contact.getNickName());
        }else{
            //will be implemented later!!!!!!!!!!!
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (contacts.get(position)==null)?LOADING:DONE;
    }
}
