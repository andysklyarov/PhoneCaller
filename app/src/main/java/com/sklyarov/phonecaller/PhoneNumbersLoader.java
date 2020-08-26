package com.sklyarov.phonecaller;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.concurrent.TimeUnit;

public class PhoneNumbersLoader extends AsyncTaskLoader<String> {
    public static final int TIMEOUT_IN_MILLIS = 2000;

    private String mId;

    public PhoneNumbersLoader(@NonNull Context context) {
        super(context);
        mId = null;
    }

    public PhoneNumbersLoader(@NonNull Context context, String iD) {
        super(context);
        mId = iD;
    }

    @Nullable
    @Override
    public String loadInBackground() {

        try {
            TimeUnit.MILLISECONDS.sleep(TIMEOUT_IN_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String number = null;

        Cursor cursor = getContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                        + ContactsContract.CommonDataKinds.Phone.TYPE + " = ?",
                new String[]{mId, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursor.close();
        }

        return number;
    }
}
