package com.shamardn.android.bullsrent;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 911;
    ImageView login_img;
    TextView login_txt1,login_txt2;
    TextInputLayout login_username,login_password;
    MaterialButton login_forget,login_signIn,login_signUp;
    SignInButton login_google ;
    GoogleSignInClient mGoogleSignInClient;

    FirebaseAuth mAuth;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            updateUI(user);
        }
    }

    private void updateUI(FirebaseUser user) {
        Intent signInIntent = new Intent(getApplicationContext(),UserProfile.class);
        startActivity(signInIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        init();
        mAuth = FirebaseAuth.getInstance();
        createGoogleRequest();

        login_signUp.setOnClickListener(this);
        login_signIn.setOnClickListener(this);
        login_google.setOnClickListener(this);
    }

    private void init() {
        login_img = findViewById(R.id.login_img);
        login_txt1 = findViewById(R.id.login_txt1);
        login_txt2 = findViewById(R.id.login_txt2);
        login_username = findViewById(R.id.login_username);
        login_password = findViewById(R.id.login_password);
        login_forget = findViewById(R.id.login_forget);
        login_signIn = findViewById(R.id.login_signIn);
        login_signUp = findViewById(R.id.login_signUp);
        login_google = findViewById(R.id.login_google_btn);
        login_google.setSize(SignInButton.SIZE_STANDARD);

    }

    private boolean validateUsername(){
        String val = login_username.getEditText().getText().toString().trim();

        if (val.isEmpty() | val == null){
            login_username.setError("Field cannot be empty");
            return false;
        }else{
            login_username.setError(null);
            login_username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String val = login_password.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            login_password.setError("Field cannot be empty");
            return false;
        } else{
            login_password.setError(null);
            login_password.setErrorEnabled(false);
            return true;
        }
    }

    private void loginUsername(){
        if (!validateUsername() | !validatePassword()){
            return;
        }else{
            isUser();
        }
    }

    private void isUser() {
        String usernameEntered = login_username.getEditText().getText().toString().trim();
        String passwordEntered = login_password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUsers = reference.orderByChild("userName").equalTo(usernameEntered);

        checkUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   login_username.setError(null);
                   login_username.setErrorEnabled(false);
                   String passwordFromDB = snapshot.child(usernameEntered).child("password").getValue(String.class);
                   if ( passwordFromDB.equals(passwordEntered)) {
                       login_password.setError(null);
                       login_password.setErrorEnabled(false);

                       String usernameFromDB = snapshot.child(usernameEntered).child("userName").getValue(String.class);
                       String fullNameFromDB = snapshot.child(usernameEntered).child("fullName").getValue(String.class);
                       String phoneFromDB = snapshot.child(usernameEntered).child("phone").getValue(String.class);
                       String emailFromDB = snapshot.child(usernameEntered).child("email").getValue(String.class);

                       User user = new User(fullNameFromDB, usernameFromDB, emailFromDB, phoneFromDB, passwordFromDB);

                       Intent signInIntent = new Intent(getApplicationContext(),UserProfile.class);
                       signInIntent.putExtra("userData",user);
                       startActivity(signInIntent);
                       finish();
                   }else{
                       login_password.setError("Wrong password");
                       login_password.requestFocus();
                   }
               }else{
                   login_username.setError("User name is not Exist");
                   login_username.requestFocus();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_signIn:
                loginUsername();
                break;

            case R.id.login_signUp:
                signUpTransition();
                break;

            case R.id.login_google_btn:
                signIn();
                break;
        }
    }

    private void createGoogleRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //noinspection deprecation
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
               firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
              }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            } else {
                            Toast.makeText(Login.this, "error", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
    }

    private void signUpTransition() {
        Pair[] pairs = new Pair[8];
        pairs[0] = new Pair<View,String>(login_img,"trans_img");
        pairs[1] = new Pair<View,String>(login_txt1,"trans_logo");
        pairs[2] = new Pair<View,String>(login_txt2,"trans_slogan");
        pairs[3] = new Pair<View,String>(login_username,"trans_username");
        pairs[4] = new Pair<View,String>(login_password,"trans_password");
        pairs[5] = new Pair<View,String>(login_forget,"trans_phone");
        pairs[6] = new Pair<View,String>(login_signIn,"trans_go");
        pairs[7] = new Pair<View,String>(login_signUp,"trans_return");

        Intent signUpIntent = new Intent(Login.this, SignUp.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
        startActivity(signUpIntent,options.toBundle());
    }
}