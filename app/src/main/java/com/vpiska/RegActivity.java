package com.vpiska;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class RegActivity extends Activity {

    private FirebaseAuth mAuth;
    EditText editRegTextEmail, editRegTextPass, editRegTextPassConf, editRegTextName;
    Button buttonreg;
    ProgressBar progressBar;
    String email;
    String passConf,pass, userName;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_activity);
        mAuth = FirebaseAuth.getInstance();
        buttonreg = findViewById(R.id.buttonreggo);
        editRegTextEmail = findViewById(R.id.editRegTextEmail);
        editRegTextPass = findViewById(R.id.editRegTextPass);
        editRegTextPassConf = findViewById(R.id.editRegTextPassConfirm);
        editRegTextName = findViewById(R.id.editRegTextName);
        mAuth = FirebaseAuth.getInstance();



        buttonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editRegTextEmail.getText().toString().trim();
                pass = editRegTextPass.getText().toString().trim();
                passConf = editRegTextPassConf.getText().toString().trim();
                userName = editRegTextName.getText().toString();
                progressBar = findViewById(R.id.progressBar);


                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegActivity.this, "Пожалуйста введите Email", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegActivity.this, "Пожалуйста введите корректный Email", Toast.LENGTH_LONG).show();

                }
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(RegActivity.this, "Пожалуйста введите пароль", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(passConf)){
                    Toast.makeText(RegActivity.this, "Пожалуйста введите пароль еще раз", Toast.LENGTH_LONG).show();
                    return;
                }

                if (pass.length()<6){
                    Toast.makeText(RegActivity.this, "Пароль должен быть не менее 6 символов", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!pass.equals(passConf)){
                    Toast.makeText(RegActivity.this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(RegActivity.this, "Введите ваше имя", Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);


                if (pass.equals(passConf)){


                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(RegActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid()).setValue(userName);
                                        Intent intent = new Intent(RegActivity.this, Upload_image.class);
                                        startActivity(intent);
                                        Toast.makeText(RegActivity.this, "Регистрация прошла успешно!",Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(RegActivity.this, "Ошибка в регистрации!",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                }
            }
        });



    }




}
