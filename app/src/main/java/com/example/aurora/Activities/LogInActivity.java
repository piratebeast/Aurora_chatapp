package com.example.aurora.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aurora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    TextInputEditText user_email, user_password;
    AppCompatButton login_btn;
    TextView do_not_have_account_text;
    FirebaseAuth firebaseAuth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(LogInActivity.this);
        dialog.setMessage("Please wait..");

        user_password = findViewById(R.id.user_password);
        user_email = findViewById(R.id.user_email);
        login_btn = findViewById(R.id.login_btn);
        do_not_have_account_text = findViewById(R.id.do_not_have_account_text);

        do_not_have_account_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
                finish();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString().trim();
                String pass = user_password.getText().toString().trim();

                if (email.isEmpty()) {
                    ShowAlart("Email can't be empty");
                } else if (pass.isEmpty()) {
                    ShowAlart("Password can't not be empty");
                } else if (pass.length() < 6) {
                    ShowAlart("Password must be 6 or more");
                }else {

                    dialog.show();
                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            dialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(LogInActivity.this, "SingIn is successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LogInActivity.this,MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    private void ShowAlart(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
        builder.setTitle("Empty");
        builder.setMessage(s);
        builder.setIcon(R.drawable.ic_baseline_error_24);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}