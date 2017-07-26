package com.esh_tech.aviram.barbershop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserEmailActivity extends AppCompatActivity implements View.OnClickListener{

    Button sendVerify;
    Button save;
    EditText password;
    EditText rePassword;


    EditText emailValidate ;
    String emailPattern;
    String email;
    TextView tvValidError;
    boolean emailValid;
    boolean passwordValid;


    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_email);

        init();
    }

    private void init() {
        sendVerify = (Button)findViewById(R.id.btVerify);
        emailValid =false;
        passwordValid = false;
        password = (EditText)findViewById(R.id.etPassword);
        rePassword = (EditText)findViewById(R.id.etRePassword);
        rePassword = (EditText)findViewById(R.id.etRePassword);
        save = (Button) findViewById(R.id.btSave);

        sendVerify.setOnClickListener(this);
        save.setOnClickListener(this);

        settings = PreferenceManager.getDefaultSharedPreferences(this);


        emailValidate = (EditText)findViewById(R.id.etEmail);
        tvValidError = (TextView)findViewById(R.id.tvErrorLable);
        setTextValid();
        emailValidate.setText(settings.getString(UserDBConstants.USER_EMAIL,""));

    }

    private void setTextValid() {
//        Email
        email = emailValidate.getText().toString().trim();
        emailPattern = "[a-z A-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        emailValidate.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                Pattern pattern = Pattern.compile(emailPattern);
                Matcher matcher = pattern.matcher(s);


                if (matcher.matches() && s.length() > 0)
                {
//                    Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                    // or
                    tvValidError.setVisibility(View.INVISIBLE);
                    tvValidError.setText("valid email");
                    emailValid =true;
                }
                else
                {
//                    Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                    //or
                    if(s.length()>0) {
                        tvValidError.setVisibility(View.VISIBLE);
                        tvValidError.setText("invalid email");
                        emailValid = false;
                    }else{
                        tvValidError.setVisibility(View.INVISIBLE);
                    }

                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });

//        Password
        final TextView passError = (TextView)findViewById(R.id.tvErrorLablePassword);
        passError.setText(R.string.passwordsTooShort);

//        RePassword
        final TextView rePassError = (TextView)findViewById(R.id.tvErrorLableRePassword);
        rePassError.setText(R.string.passwordsDoNotMatch);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable userInput) {
                if(userInput.length()<4 && userInput.length()>0)
                    passError.setVisibility(View.VISIBLE);
                else passError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });
//        RePassword
        rePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable userInput) {

                if(password.getText().toString().equals(userInput.toString())){
                    rePassError.setVisibility(View.INVISIBLE);
                    passwordValid =true;

                }else{
                    rePassError.setVisibility(View.VISIBLE);
                    passwordValid =false;
                }

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btVerify:
                sendVerifyCode();
                break;
            case R.id.btSave:
                testVerifyCode();
                break;

            default:
                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void sendVerifyCode() {

//        TODO Send Verification code to user email ,Thread time Email Handler

        if(passwordValid && emailValid){

            save.setVisibility(View.VISIBLE);
            sendVerify.setBackgroundResource(R.color.background);
            passwordValid =false;
            Toast.makeText(UserEmailActivity.this, R.string.verification_code_send_to_your_email, Toast.LENGTH_SHORT).show();
            //TODO This solution will leak memory!  Don't use!!!
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendVerify.setBackgroundResource(R.color.colorAccent);
                    passwordValid =true;
                }
            }, 20000);


        }
    }

    private void testVerifyCode() {


//        TODO if verify code match continue activity

        if(emailValid) {

            editor = settings.edit();
            editor.putString(UserDBConstants.USER_EMAIL,emailValidate.getText().toString());
            editor.putString(UserDBConstants.USER_EMAIL_PASSWORD,password.getText().toString());
            editor.apply();

            Intent passwordIntent = new Intent(this, UserPasswordActivity.class);
            startActivity(passwordIntent);
            this.finish();
        }
    }
}
