package com.harvey.mvvmsample.viewmodel;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.harvey.mvvmsample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by hanhui on 2018/5/4 0004 16:06
 */

public class LoginViewModel extends Observable implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    public Context mContext;
    public UserLoginTask mAuthTask = null;
    public ObservableField<String> email = new ObservableField<>(null);
    public ObservableField<String> password = new ObservableField<>(null);

    public ObservableField<String> emailError = new ObservableField<>(null);
    public ObservableField<String> passwordError = new ObservableField<>(null);

    public ObservableBoolean showProgress = new ObservableBoolean(false);

    public ObservableList<String> emailsList = new ObservableArrayList<>();

    public Animator.AnimatorListener loginFormAnimator = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {

        }
    };

    public Animator.AnimatorListener loginProgressAnimator = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
        }
    };

    public View.OnClickListener loginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            attemptLogin();
        }
    };

    public TextView.OnEditorActionListener passwordEditorAction = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int id, KeyEvent event) {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        }
    };

    public LoginViewModel(Context context) {
        this.mContext = context;
        ((Activity) context).getLoaderManager().initLoader(0, null, this);
    }


    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        emailError.set(null);
        passwordError.set(null);

        if (TextUtils.isEmpty(email.get())) {
            emailError.set(mContext.getString(R.string.error_field_required));
            return;
        }

        if (!isEmailValid(email.get())) {
            emailError.set(mContext.getString(R.string.error_invalid_email));
            return;
        }

        if (TextUtils.isEmpty(password.get()) || !isPasswordValid(password.get())) {
            passwordError.set(mContext.getString(R.string.error_invalid_password));
            return;
        }

        showProgress(true);
        mAuthTask = new UserLoginTask(email.get(), password.get());
        mAuthTask.execute((Void) null);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        showProgress.set(show);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(mContext,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        emailsList.addAll(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    return pieces[1].equals(mPassword);
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                ((Activity) mContext).finish();
            } else {
                passwordError.set(mContext.getString(R.string.error_incorrect_password));
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


}
