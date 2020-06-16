package com.vpiska;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    EditText emailEditText, passEditText;
    Button buttonSignIn;
    TextView textViewReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        emailEditText = findViewById(R.id.editTextEmail);
        passEditText = findViewById(R.id.editTextPass);

        buttonSignIn = findViewById(R.id.buttonSignIn);
        textViewReg = findViewById(R.id.textViewRegister);
        textViewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString().trim();
                String pass  = passEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Введите ваш Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(MainActivity.this, "Введите ваш пароль", Toast.LENGTH_LONG).show();
                    return;
                }

               mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this,
                       new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

                       if (task.isSuccessful()){
                           firebaseUser = mAuth.getCurrentUser();
                           Toast.makeText(MainActivity.this, "Успех!", Toast.LENGTH_LONG).show();
                           Intent intent = new Intent(MainActivity.this, Home.class);
                           startActivity(intent);
                       } else {
                           Toast.makeText(MainActivity.this, "Логин или пароль введен не верно", Toast.LENGTH_LONG).show();
                       }

                   }
               });

            }
        });


    }

    public void onBackPressed() {
        // super.onBackPressed();

    }

}
