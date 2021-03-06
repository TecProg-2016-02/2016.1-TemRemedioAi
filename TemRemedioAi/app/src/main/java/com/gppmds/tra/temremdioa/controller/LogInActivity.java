/*
 * File: LogInActivity.java
 * Purpose: Log the user in the app (Facebook and Email)
 */

package com.gppmds.tra.temremdioa.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.tra.gppmds.temremdioa.R;

import static junit.framework.Assert.*;

public class LogInActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    CallbackManager callbackManager;

    // res references
    private static TextView info = (TextView)findViewById(R.id.info);
    private static EditText editTextUsernameView = (EditText) findViewById(R.id.log_in_username_field);
    private static EditText editTextPasswordView = (EditText) findViewById(R.id.log_in_password_field);
    private static Button mUsernameSignInButton = (Button) findViewById(R.id.log_in_button);
    private static Button mRegisterButton = (Button) findViewById(R.id.log_in_sign_in_button);
    private LoginButton mFacebookButton = (LoginButton) findViewById(R.id.login_button_fb);
    private ProgressBar progressiveBarProgressView = (ProgressBar) findViewById(R.id.log_in_progress_bar);
    private View viewLoginFormView = (View) findViewById(R.id.log_in_form);
    private View focusView = null;

    //Keep track of the login task to ensure we can cancel it if requested.
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_log_in);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Button actions
        setListener();
    }

    protected void facebookSDKInitialize() {

        callbackManager = CallbackManager.Factory.create();
    }

    public ParseUser getCurrentUser() {

        assert 
        ParseUser currentUser = ParseUser.getCurrentUser();

        return currentUser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("data",data.toString());
    }

    private boolean validateError(String word, String phrase, EditText text) {

        String phraseValited = TextUtils.isEmpty(word) ? phrase : null;
        text.setError(phraseValited);

        focusView = phraseValited != null ? text : null;

        return TextUtils.isEmpty(word);
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException exception) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                editTextPasswordView.setError(getString(R.string.error_incorrect_password));
                editTextPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {

            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                /* Show primary email addresses first. Note that there won't be
                    a primary email address if the user hasn't specified one. */
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        finish();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        finish();
    }


    private void setListener() {

        mFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                finish();
            }

            @Override
            public void onCancel() {

                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {

                info.setText("Login attempt failed.");
            }
        });

        editTextPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptLogin();
            }
        });

        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpActivity = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(signUpActivity);
            }
        });
    }

    // Attempts to login in the system if the entries email and password are valid
    private void attemptLogin(){

        // Checking return value
        assertFalse(editTextUsernameView.getText().toString() == null);
        assertFalse(editTextPasswordView.getText().toString() == null);

        // Store values at the time of the login attempt
        final String username = editTextUsernameView.getText().toString();
        String password = editTextPasswordView.getText().toString();

        if (mAuthTask != null){
            return;
        }

        // Checking return value
        assert validateError(username, "Ops! Campo de username esta vazio.",
                            editTextUsernameView) != null;
        assert validateError(password, "Ops! Campo do password esta vazio.",
                            editTextPasswordView) != null;

        // Check if fields are valid to continue login
        if ((validateError(username, "Ops! Campo de username esta vazio.", editTextUsernameView) ||
                validateError(password, "Ops! Campo do password esta vazio.", editTextPasswordView))) {

            // Show login errors
            focusView.requestFocus();
        } else {
            showProgress(true);
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    // Check if the variable parseUser has received the token
                    if (parseUser != null) {

                        Toast.makeText(getApplicationContext(), "Logado com sucesso, seja bem vindo " + username +"!",
                                Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Nome de usuário e/ou senha não existente!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LogInActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // Honeycombe api use to show progress bar

        progressiveBarProgressView.getIndeterminateDrawable().setColorFilter(Color.RED,
                android.graphics.PorterDuff.Mode.SRC_IN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            viewLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);


            viewLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    viewLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressiveBarProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressiveBarProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    progressiveBarProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            /* The ViewPropertyAnimator APIs are not available, so simply show
               and hide the relevant UI components.*/
            progressiveBarProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            viewLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private interface ProfileQuery {

        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}
