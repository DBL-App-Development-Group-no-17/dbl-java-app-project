package com.example.tutorhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();

        EditText password = findViewById(R.id.passwordinput);
        EditText email = findViewById(R.id.emailinput);

        Button btn_login = findViewById(R.id.login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmail = email.getText().toString().trim();
                String loginPassword = password.getText().toString().trim();
                mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra("email", loginEmail);
                        startActivity(intent);
                    }
                    else
                    {
                        String excString = task.getException().getMessage();
                        Toast.makeText(Login.this, excString, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        TextView txt_register = findViewById(R.id.register_txt);
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationPage.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        String username;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("email");
            EditText email = findViewById(R.id.emailinput);
            email.setText(username);
        }
    }
}
