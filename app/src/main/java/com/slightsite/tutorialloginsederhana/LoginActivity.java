package com.slightsite.tutorialloginsederhana;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.slightsite.tutorialloginsederhana.controllers.UserController;
import com.slightsite.tutorialloginsederhana.utils.Database;
import com.slightsite.tutorialloginsederhana.utils.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText passwordRepeat;
    private EditText nameBox;
    private EditText phoneBox;
    private Button mEmailSignInButton;
    private Button signup_button;
    private Button register_button;
    private Button signin_button;
    private View mProgressView;
    private View mLoginFormView;

    public final static String TAG_ID = "id";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_NAME = "name";
    public final static String TAG_PHONE = "phone";
    public final static String TAG_PASSWORD = "password";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, email, name;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initiateCoreApp();
        checkSession();

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        nameBox = findViewById(R.id.nameBox);
        phoneBox = findViewById(R.id.phoneBox);

        signup_button = (Button) findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        register_button = (Button) findViewById(R.id.register_button);
        signin_button = (Button) findViewById(R.id.signin_button);
        passwordRepeat = (EditText) findViewById(R.id.passwordRepeat);
    }

    private void initiateCoreApp() {
        Database database = new DatabaseHelper(this);
        UserController.setDatabase(database);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private ContentValues admin;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            admin = UserController.getInstance().getDataByEmail(mEmail);
            if (admin != null) {
                if (mPassword.equals(admin.getAsString("password"))) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(session_status, true);
                    editor.putString(TAG_ID, admin.getAsString("_id"));
                    editor.putString(TAG_EMAIL, admin.getAsString("email"));
                    editor.putString(TAG_NAME, admin.getAsString("name"));
                    editor.putString(TAG_PHONE, admin.getAsString("phone"));
                    editor.commit();
                } else {
                    return false;
                }

                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_EMAIL, email);
                if (admin != null) {
                    intent.putExtra(TAG_NAME, admin.getAsString("name"));
                }
                finish();
                startActivity(intent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void checkSession() {
        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        email = sharedpreferences.getString(TAG_EMAIL, null);
        name = sharedpreferences.getString(TAG_NAME, null);

        if (session) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_EMAIL, email);
            intent.putExtra(TAG_NAME, name);
            finish();
            startActivity(intent);
        }
    }

    public void registerRequest(View view) {
        nameBox.setVisibility(View.VISIBLE);
        phoneBox.setVisibility(View.VISIBLE);
        mEmailSignInButton.setVisibility(View.GONE);
        signup_button.setVisibility(View.VISIBLE);
        register_button.setVisibility(View.GONE);
        signin_button.setVisibility(View.VISIBLE);
        passwordRepeat.setVisibility(View.VISIBLE);
    }

    public void signinRequest(View view) {
        nameBox.setVisibility(View.GONE);
        phoneBox.setVisibility(View.GONE);
        mEmailSignInButton.setVisibility(View.VISIBLE);
        signup_button.setVisibility(View.GONE);
        register_button.setVisibility(View.VISIBLE);
        signin_button.setVisibility(View.GONE);
        passwordRepeat.setVisibility(View.GONE);
    }

    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String password_repeat = passwordRepeat.getText().toString();
        String full_name = nameBox.getText().toString();
        String phone = phoneBox.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password_repeat) && !isPasswordValid(password_repeat)) {
            passwordRepeat.setError(getString(R.string.error_invalid_password));
            focusView = passwordRepeat;
            cancel = true;
        }

        if (!password_repeat.equals(password)) {
            passwordRepeat.setError(getString(R.string.error_invalid_password_repeat));
            focusView = passwordRepeat;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else {
            ContentValues adminData = UserController.getInstance().getDataByEmail(email);
            if (adminData != null) {
                mEmailView.setError(getString(R.string.error_unavailable_email));
                focusView = mEmailView;
                cancel = true;
            }
        }

        if (TextUtils.isEmpty(full_name)) {
            nameBox.setError(getString(R.string.error_field_required));
            focusView = nameBox;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneBox.setError(getString(R.string.error_field_required));
            focusView = phoneBox;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);

            ContentValues content = new ContentValues();
            content.put("email", email);
            content.put("name", full_name);
            content.put("password", password);
            content.put("phone", phone);
            content.put("date_added", getCurrentTime());

            int id = UserController.getInstance().register(content);
            if (id > 0) {
                mAuthTask = new UserLoginTask(email, password);
                mAuthTask.execute((Void) null);
            }
        }
    }

    /**
     * Geting current time
     * @return
     */
    private String getCurrentTime() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c);
        
        return formattedDate;
    }
}
