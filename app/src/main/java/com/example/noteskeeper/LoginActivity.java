package com.example.noteskeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    ProgressBar LoginProgressBar;
    EditText EmailET, PassET;
    Button LoginBtn;
    TextView createAccTV,ForgotPsswrdTV;


    public  Boolean LoginValidator(){

        if (TextUtils.isEmpty(EmailET.getText().toString())){
            EmailET.setError("Enail can`t be empty");
            return false;
        }


        if (PassET.getText().toString().trim()==null){
            PassET.setError("password can`t be empty");
            return false;
        }


        if ((PassET.getText().toString().length()<8)){
            PassET.setError("Password must be atleast 8 characters");
            return false;
        }



        return true;
    }




    public void LoginUserWithFirebase(){
        {
            LoginProgressBar.setVisibility(View.VISIBLE);
            try {
                //try getting forms data and also try validating if credentilas are correct
                //get data. Trim removes trailing and ending whitespaces
                final String Email=EmailET.getText().toString().trim();
                final String Pass=PassET.getText().toString().trim();
                //FIREBASE LOGIN LOGIC HERE
                //use firebase signin method and also call the oncomplete listener to listen if login is successful and complete

                fAuth.getInstance().signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if login success move user to drawer activity
                        //HIDE PROGRESSBAR AFTER SUCCESSFUL LOGIN
                        LoginProgressBar.setVisibility(View.GONE);


                        if(task.isSuccessful()){

                            //tel user
                            Toast.makeText(getApplicationContext(),"Login Successful...WELCOME BACK",Toast.LENGTH_LONG).show();




                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);

                            startActivity(intent);
                            finish();


                        }else {

                            Toast.makeText(getApplicationContext(), "Student Login Failed due to: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            LoginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });





            }catch (Exception e){
//handle exception incase firebase login fails
                Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
            }

        }

    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        PassET=(EditText)findViewById(R.id.ETloginPswd) ;
        EmailET=(EditText)findViewById(R.id.ETLoginEmail) ;
        createAccTV=(TextView) findViewById(R.id.UsrNotRegTv);
        ForgotPsswrdTV=(TextView) findViewById(R.id.forgotPasswrdTv);
        LoginBtn=(Button) findViewById(R.id.btnLogin);
        LoginProgressBar =(ProgressBar) findViewById(R.id.LoginProgBar);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (LoginValidator()==true){


                    LoginUserWithFirebase();
                }

         // startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });



       createAccTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

            }
        });



      ForgotPsswrdTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));


            }
        });





    }
}
