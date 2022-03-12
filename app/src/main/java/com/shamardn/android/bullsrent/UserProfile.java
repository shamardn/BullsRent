package com.shamardn.android.bullsrent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    TextView tv_profile_fullname, tv_profile_username;
    TextInputLayout et_profile_fullname, et_profile_email, et_profile_phone, et_profile_password;
    MaterialButton btn_profile_update, btn_profile_signOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);

        init();
        updateUI();
        showAllUserData();
        btn_profile_signOut.setOnClickListener(this);
    }

    private void updateUI() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String fullname = account.getDisplayName();
            String username = account.getFamilyName();
            String personEmail = account.getEmail();

            tv_profile_fullname.setText(fullname);
            tv_profile_username.setText(username);
            et_profile_fullname.getEditText().setText(fullname);
            et_profile_email.getEditText().setText(personEmail);

        }
    }

    private User getUserData() {
        Intent userData = getIntent();
        if (userData != null){
            User user = (User) userData.getSerializableExtra("userData");
            return user;
        }else
          return null;
    }

    private void showAllUserData() {
        User user = getUserData();
        if (user != null){

            String fullName = user.getFullName();
            String userName = user.getUserName();
            String email = user.getEmail();
            String phone = user.getPhone();
            String password = user.getPassword();

            tv_profile_fullname.setText(fullName);
            tv_profile_username.setText(userName);
            et_profile_fullname.getEditText().setText(fullName);
            et_profile_email.getEditText().setText(email);
            et_profile_phone.getEditText().setText(phone);
            et_profile_password.getEditText().setText(password);
        }
    }

    private void init() {
        tv_profile_fullname = findViewById(R.id.tv_profile_fullname);
        tv_profile_username = findViewById(R.id.tv_profile_username);
        et_profile_fullname = findViewById(R.id.et_profile_fullname);
        et_profile_email = findViewById(R.id.et_profile_email);
        et_profile_phone = findViewById(R.id.et_profile_phone);
        et_profile_password = findViewById(R.id.et_profile_password);
        btn_profile_update = findViewById(R.id.btn_profile_update);
        btn_profile_signOut = findViewById(R.id.btn_profile_signOut);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_profile_update:

                break;

            case R.id.btn_profile_signOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}