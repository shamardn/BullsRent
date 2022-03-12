package com.shamardn.android.bullsrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    boolean isUserExist = false;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    ImageView signup_img;
    TextView signup_txt1,signup_txt2;
    TextInputLayout signup_fullname,signup_username,signup_email,signup_phone,signup_password;
    MaterialButton signup_signup,signup_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        init();
        signup_signin.setOnClickListener(this);
        signup_signup.setOnClickListener(this);
    }

    private void init() {
        signup_img = findViewById(R.id.signup_img);
        signup_txt1 = findViewById(R.id.signup_txt1);
        signup_txt2 = findViewById(R.id.signup_txt2);
        signup_fullname = findViewById(R.id.signup_fullname);
        signup_username = findViewById(R.id.signup_username);
        signup_email = findViewById(R.id.signup_email);
        signup_phone = findViewById(R.id.signup_phone);
        signup_password = findViewById(R.id.signup_password);
        signup_signup = findViewById(R.id.signup_signup);
        signup_signin = findViewById(R.id.signup_signin);
    }

    private boolean validateName(){
        String val = signup_fullname.getEditText().getText().toString();

        if (val.isEmpty()){
            signup_fullname.setError("Field cannot be empty");
            return false;
        }else{
            signup_fullname.setError(null);
            signup_fullname.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserName(){
        String val = signup_username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        checkUserExistence(val);

        if (val.isEmpty()){
            signup_username.setError("Field cannot be empty");
            return false;
        }else if(val.length() >= 15){
            signup_username.setError("Username is too long");
            return false;
        }else if(!val.matches(noWhiteSpace)){
            signup_username.setError("White Spaces are not allowed");
            return false;
        } else if(isUserExist){
            signup_username.setError("username is already exist");
            signup_username.getEditText().setText("");
            signup_username.requestFocus();
            isUserExist = false;
            return false;
        } else{
            signup_username.setError(null);
            signup_username.setErrorEnabled(false);
            return true;
        }
    }

    private void checkUserExistence(String val) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query checkUsername = reference.orderByChild("userName").equalTo(val);

        checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    isUserExist = true;
                }else{
                    isUserExist = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean validateEmail(){
        String val = signup_email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            signup_email.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(emailPattern)){
            signup_email.setError("Invalid Email Address");
            return false;
        } else{
            signup_email.setError(null);
            signup_email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone(){
        String val = signup_phone.getEditText().getText().toString();

        if (val.isEmpty()){
            signup_phone.setError("Field cannot be empty");
            return false;
        }else{
            signup_phone.setError(null);
            signup_phone.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String val = signup_password.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +  // at least 1 digit
                "(?=.*[a-z])" +  // at least 1 lower case letter
                "(?=.*[A-Z])" +  // at least 1 upper case letter
//                "(?=.*[a-zA-z])" + // any letter
                "(?=.*[!@#$%&*()-+=^_])" + // at least 1 special character
                "(?=\\S+$)" +  // no white space
                ".{8,20}" +  // at least 8 characters to 20
                "$";

        if (val.isEmpty()){
            signup_password.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(passwordVal)){
            signup_password.setError("password is too weak");
            return false;
        } else{
            signup_password.setError(null);
            signup_password.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signup_signup:
                saveNewUser();
                break;

            case R.id.signup_signin:
                signInTransition();
                break;
        }
    }

    private void signInTransition() {
        Pair[] pairs = new Pair[10];
        pairs[0] = new Pair<View,String>(signup_img,"trans_img");
        pairs[1] = new Pair<View,String>(signup_txt1,"trans_logo");
        pairs[2] = new Pair<View,String>(signup_txt2,"trans_slogan");
        pairs[3] = new Pair<View,String>(signup_fullname,"trans_username");
        pairs[4] = new Pair<View,String>(signup_username,"trans_username");
        pairs[5] = new Pair<View,String>(signup_email,"trans_password");
        pairs[6] = new Pair<View,String>(signup_phone,"trans_phone");
        pairs[7] = new Pair<View,String>(signup_password,"trans_password");
        pairs[8] = new Pair<View,String>(signup_signup,"trans_go");
        pairs[9] = new Pair<View,String>(signup_signin,"trans_return");

        Intent intent = new Intent(SignUp.this, Login.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
        startActivity(intent,options.toBundle());
        finish();
    }

    private void saveNewUser() {
        if(!validateName() | !validateUserName() | !validateEmail() | !validatePhone() | !validatePassword()){
            return;
        }

        String fullName = signup_fullname.getEditText().getText().toString();
        String userName = signup_username.getEditText().getText().toString();
        String email = signup_email.getEditText().getText().toString();
        String phone = signup_phone.getEditText().getText().toString();
        String password = signup_password.getEditText().getText().toString();
        User user = new User(fullName,userName,email,phone,password);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        reference.child(userName).setValue(user);

        Intent intent = new Intent(getApplicationContext(),VerifyPhoneNo.class);
        intent.putExtra("userData",user);
        startActivity(intent);

//        signInTransition();

    }
}