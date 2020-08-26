package com.sklyarov.phonecaller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.sklyarov.phonecaller.contacts.ContactsAdapter;
import com.sklyarov.phonecaller.contacts.ContactsFragment;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.OnItemClickListener, LoaderManager.LoaderCallbacks<String> {

    public static final int MY_LOADER_ID = 42;
    public static final String NUMBER_ID_KEY = "NUMBER_ID_KEY";

    private LoaderManager mLoaderManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction()
                    .replace(R.id.container, ContactsFragment.newInstance())
                    .commit();
        }

        mLoaderManager = LoaderManager.getInstance(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_stop_call) {

            if (mLoaderManager.hasRunningLoaders()) {
                Loader<String> loader = mLoaderManager.getLoader(MY_LOADER_ID);

                if (loader != null && loader.cancelLoad()) {
                    Toast.makeText(this, "Request canceled!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(String iD) {

        Bundle args = new Bundle();
        args.putString(NUMBER_ID_KEY, iD);

        mLoaderManager.initLoader(MY_LOADER_ID, args, this);
        Loader<String> loader = mLoaderManager.getLoader(MY_LOADER_ID);

        if (loader != null)
            loader.forceLoad();
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        if (args != null)
            return new PhoneNumbersLoader(this, args.getString(NUMBER_ID_KEY));
        else
            return new PhoneNumbersLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        if (data != null) {
//            Toast.makeText(this, "Number calling " + data, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + data)));
        } else
            Toast.makeText(this, "There is no such number!!!", Toast.LENGTH_SHORT).show();

        mLoaderManager.destroyLoader(MY_LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }
}