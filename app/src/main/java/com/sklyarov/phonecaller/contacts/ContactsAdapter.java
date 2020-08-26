package com.sklyarov.phonecaller.contacts;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sklyarov.phonecaller.R;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsHolder> {

    private Cursor cursor;
    private OnItemClickListener listener;


    @NonNull
    @Override
    public ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.li_mock, parent, false);

        return new ContactsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            holder.bind(new Contacts(name, id));

            holder.setListener(listener);
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor) {
        if (cursor != null && cursor != this.cursor) {

            if (this.cursor != null)
                this.cursor.close();

            this.cursor = cursor;
            notifyDataSetChanged();
        }
    }

    public void setListener(OnItemClickListener mListener) {
        this.listener = mListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }
}
