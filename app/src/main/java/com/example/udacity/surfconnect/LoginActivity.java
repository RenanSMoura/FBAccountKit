package com.example.udacity.surfconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

public class LoginActivity extends AppCompatActivity {

    private final int APP_REQUEST_CODE = 1;

    private Button loginPhone;
    private Button loginEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        loginPhone = findViewById(R.id.phone_login_button);
        loginPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin(LoginType.PHONE);
            }
        });

        loginEmail = findViewById(R.id.email_login_button);
        loginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin(LoginType.EMAIL);
            }
        });

        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if(accessToken != null){
            launchAccountActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == APP_REQUEST_CODE){
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(loginResult.getError() != null){
                int toastMessage = loginResult.getError().getErrorType().getCode();
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            }else{
                launchAccountActivity();
            }
        }
    }

    private  void onLogin(final LoginType loginType){
        final Intent intent = new Intent(this, AccountKitActivity.class);

        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,AccountKitActivity.ResponseType.TOKEN);


        final AccountKitConfiguration configuration = configurationBuilder.build();

        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,configuration);
        startActivityForResult(intent,APP_REQUEST_CODE);
    }

    private void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

}
