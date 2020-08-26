package com.sklyarov.phonecaller.contacts;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sklyarov.phonecaller.R;

public class ContactsHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView value;
    private String id;

    public ContactsHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_name);
        value = itemView.findViewById(R.id.tv_value);
    }

    public void bind(Contacts contacts) {
        name.setText(contacts.getName());
        value.setText(contacts.getValue());
        id = contacts.getValue();
    }

    public void setListener(final ContactsAdapter.OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(id);
            }
        });
    }
}
