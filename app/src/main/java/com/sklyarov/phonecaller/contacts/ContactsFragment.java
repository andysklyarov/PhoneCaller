package com.sklyarov.phonecaller.contacts;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sklyarov.phonecaller.R;

import java.util.Objects;

public class ContactsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final int READ_AND_CALL_REQUEST_CODE = 1;
    private RecyclerView recycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View fragmentView;
    private View errorView;

    private final ContactsAdapter contactsAdapter = new ContactsAdapter();

    private ContactsAdapter.OnItemClickListener clickListener;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ContactsAdapter.OnItemClickListener) {
            clickListener = (ContactsAdapter.OnItemClickListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState == null)
            fragmentView = inflater.inflate(R.layout.fr_recycler, container, false);

        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler = view.findViewById(R.id.recycler);
        swipeRefreshLayout = view.findViewById(R.id.refresher);
        swipeRefreshLayout.setOnRefreshListener(this);
        errorView = view.findViewById(R.id.error_view);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState == null) {
            recycler.setAdapter(contactsAdapter);
            recycler.addItemDecoration(new CardDecoration());
        }
        contactsAdapter.setListener(clickListener);

        onRefresh();
    }

    @Override
    public void onRefresh() {

        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE},
                READ_AND_CALL_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == READ_AND_CALL_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                errorView.setVisibility(View.GONE);
                recycler.setVisibility(View.VISIBLE);


                LoaderManager.getInstance(this).restartLoader(0, null, this);
            } else {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);

                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getActivity(), "Permission READ CONTACTS denied!", Toast.LENGTH_SHORT).show();

                if (grantResults[1] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(getActivity(), "Permission CALL PHONE denied!", Toast.LENGTH_SHORT).show();

                errorView.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(
                Objects.requireNonNull(getActivity()),
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                null, null, ContactsContract.Contacts._ID);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        contactsAdapter.swapCursor(data);

        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void onDetach() {
        clickListener = null;
        super.onDetach();
    }
}
