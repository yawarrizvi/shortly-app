package com.shortly.shortlyapp.UI.Activities.Login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shortly.shortlyapp.BuildConfig;
import com.shortly.shortlyapp.Interfaces.ProgressLoaderInterface;
import com.shortly.shortlyapp.Interfaces.SyncInterface;
import com.shortly.shortlyapp.Logic.ProgressHandler.ProgressHandler;
import com.shortly.shortlyapp.R;
import com.shortly.shortlyapp.Sync.APICalls;
import com.shortly.shortlyapp.UI.Activities.BaseActivity;
import com.shortly.shortlyapp.UI.Activities.MainActivity.ShortlyTabViewActivity;
import com.shortly.shortlyapp.utils.Constants;
import com.shortly.shortlyapp.utils.Utilities;

import butterknife.Bind;

public class LoginActivity extends BaseActivity implements SyncInterface {

    @Bind(R.id.login_layout)
    LinearLayout mLoginLayout;

    @Bind(R.id.btn_login)
    Button mBtnLogin;

    @Bind(R.id.btn_register)
    Button mBtnRegister;

    @Bind(R.id.input_layout_email)
    TextInputLayout mInputLayoutEmail;

    @Bind(R.id.input_layout_password)
    TextInputLayout mInputLayoutPassword;

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    private Activity mActivity;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toggleLoginButton(true);

        if (mHandler == null) {
            mHandler = new Handler(LoginActivity.this.getMainLooper());
        }

        mEditTextEmail = mInputLayoutEmail.getEditText();
        mEditTextPassword = mInputLayoutPassword.getEditText();

        if (BuildConfig.DEBUG) {
//            mEditTextEmail.setText("shortlytest@gmail.com");
//            mEditTextPassword.setText("shortly1234");
//
            mEditTextEmail.setText("asi589@gmail.com");
            mEditTextPassword.setText("qwerty123");
        }

        mEditTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || actionId == EditorInfo.IME_ACTION_DONE) {
                    performLogin();
                }
                return false;
            }
        });
        mActivity = this;
        mLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        mEditTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mInputLayoutEmail.setError(null);
                }
            }
        });

        mEditTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mInputLayoutPassword.setError(null);
                }
            }
        });
    }

    private void registerUser() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void performLogin() {
        if (mEditTextEmail != null && mEditTextPassword != null) {
            toggleLoginButton(false);
            String email = mEditTextEmail.getText().toString();
            String password = mEditTextPassword.getText().toString();
            checkUserCredentials(email, password);
        }
    }

    private void checkUserCredentials(String email, String password) {
        boolean isValidCredential = true;
        if (!isEmailValid(email)) {
            isValidCredential = false;
        }
        if (password.isEmpty()) {
            isValidCredential = false;
        }

        hideKeyboard();
        if (isValidCredential) {
            toggleLoginButton(false);
            email = email.toLowerCase();
            authenticateUser(email, password);
        } else {
            toggleLoginButton(true);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.key_error_invalid_user_credentials))
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        }

    }

    private boolean isEmailValid(String email) {
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void hideKeyboard() {
        Utilities.hideSoftKeyboard(mActivity);
        mInputLayoutPassword.clearFocus();
        mInputLayoutEmail.clearFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * Authenticate user
     *
     * @param email    employee username/email
     * @param password employee password
     */
    private void authenticateUser(final String email, final String password) {
        ProgressHandler.setProgLoaderInterface(new ProgressLoaderInterface() {
            @Override
            public void onAlertDialogActionResult(int result, String positiveButtonTitle, String negativeButtonTitle, String message) {
                ProgressHandler.hideProgressDialogue();
            }
        });
        ProgressHandler.showProgressDialog(this, getString(R.string.app_name), getString(R.string.key_authentication_title), 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
        new Thread() {
            public void run() {
                APICalls.setSyncInterface(new SyncInterface() {
                    @Override
                    public void onAPIResult(int result, Object resultObject, int totalRecords) {
                        switch (result) {
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SUCCESS:
                                ProgressHandler.hideProgressDialogue();
                                showVideoList();
                                break;
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                            case Constants.ServiceResponseCodes.RESPONSE_CODE_UNAUTHORIZED_USER:
                                showError(result);
                                break;
                            default:
                                showError(Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR);
                                break;
                        }
                    }
                });
                APICalls.authenticateUser(email, password, LoginActivity.this);
            }


        }.start();
    }

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();

    }

    private void showVideoList() {
        Intent intent = new Intent(LoginActivity.this, ShortlyTabViewActivity.class);
        startActivity(intent);
        finish();
    }

    private void showError(int errorType) {
        toggleLoginButton(true);
        final String message;
        switch (errorType) {
            case Constants.ServiceResponseCodes.RESPONSE_CODE_NO_CONNECTIVITY:
                message = getString(R.string.key_error_no_connectivity);
                break;
            case Constants.ServiceResponseCodes.RESPONSE_CODE_ERROR:
                message = getString(R.string.key_error_invalid_credentials);
                break;
            case Constants.ServiceResponseCodes.RESPONSE_CODE_SERVICE_FAILURE:
                message = getString(R.string.key_error_server_connection);
                break;
            default:
                message = "";
                break;
        }

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ProgressHandler.upDateProgressDialog(LoginActivity.this, "", message, 0, Constants.ProgressBarStyles.PROGRESS_BAR_NONE, getString(R.string.button_title_ok), "");
                }
            }
        };
        timerThread.start();
//        ProgressHandler.upDateProgressDialog(this, "", message, 0, Constants.ProgressBarStyles.PROGRESS_BAR_NONE, getString(R.string.button_title_ok), "");
//        ProgressHandler.showProgressDialog(this, getString(R.string.app_name), getString(R.string.key_authentication_title), 0, Constants.ProgressBarStyles.PROGRESS_BAR_ANIMATED, "", "");
    }


    private void toggleLoginButton(final boolean enabled) {
        Handler mainHandler = new Handler(this.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mBtnLogin.setEnabled(enabled);
            }
        };
        mainHandler.post(myRunnable);
    }


    @Override
    public void onAPIResult(int result, Object resultObject, int totalRecords) {

    }
}
