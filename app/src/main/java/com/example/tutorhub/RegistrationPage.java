package com.example.tutorhub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Console;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * To add:
 * valid phone number verification
 * valid email verification
 */
public class RegistrationPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    private DatabaseReference mDb;

    EditText userName;
    EditText name;
    EditText email;
    EditText educationalInstitution;
    EditText phoneNumber;
    EditText password;
    ToggleButton student;
    ToggleButton tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);
        mAuth = FirebaseAuth.getInstance();

        // variables
        userName = findViewById(R.id.usernameInput);
        name = findViewById(R.id.nameInput);
        email = findViewById(R.id.emailInput);
        educationalInstitution = findViewById(R.id.educ_inst);
        phoneNumber = findViewById(R.id.phonenr_input);
        password = findViewById(R.id.passwordInput);
        EditText passwordCheck = findViewById(R.id.password2Input);
        student = findViewById(R.id.toggle_student);
        tutor = findViewById(R.id.toggle_tutor);
        CheckBox checkBox = findViewById(R.id.checkBox_loc);

        Button btnRegister = findViewById(R.id.reg_button);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fine = true;
                String userEmail = email.getText().toString().trim();
                String userPassword= password.getText().toString().trim();
                /** if required field are empty, signal to user */
                if(TextUtils.isEmpty(name.getText())){
                    name.setError("Name required");
                    fine = false;
                }
                if(TextUtils.isEmpty(email.getText())){
                    email.setError("Email required");
                    fine = false;
                }
                if(TextUtils.isEmpty(phoneNumber.getText())){
                    phoneNumber.setError("PhoneNumber required");
                    fine = false;
                }
                if(TextUtils.isEmpty(password.getText())){
                    password.setError("Password required");
                    fine = false;
                }
                if(TextUtils.isEmpty(passwordCheck.getText())){
                    passwordCheck.setError("please re-enter password");
                    fine = false;
                }
                if(TextUtils.isEmpty(userName.getText())){
                    userName.setError("Username required");
                    fine = false;
                }
                if(!passwordCheck.getText().toString()
                        .equals(password.getText().toString())){
                    passwordCheck.setError("Passwords do not match");
                    fine = false;
                }
                if(!(student.isChecked() || tutor.isChecked())){
                    Toast.makeText(getApplicationContext(),
                            "Please Set a role",
                            Toast.LENGTH_SHORT)
                            .show();
                    fine = false;
                }
                if(!checkBox.isChecked()){
                    checkBox.setError("Must be checked to create account");
                    fine = false;
                }

                ArrayList<Boolean> boolArr = new ArrayList<>();
                boolArr.add(fine);
                FirebaseDatabase.getInstance().getReference().child("usernames")
                        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            String s = task.getResult().getValue().toString();
                            String[] ss = s.split(", ");
                            boolean matches = false;
                            List<String> ss2 = new ArrayList<>();
                            ss[0] = ss[0].substring(1);
                            ss[ss.length-1] = ss[ss.length-1].substring(0,ss[ss.length-1].length()-1);
                            for (int i = 0; i != ss.length; i++) {
                                if (ss[i].equals(userName.getText().toString())){
                                    matches = true;
                                    break;
                                }
                                ss2.add(ss[i]);
                            }
                            if (!matches && boolArr.get(0)) {
                                ss2.add(userName.getText().toString());
                                mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            System.out.println(ss2);
                                            String email = writeNewUser(ss2);
                                            Toast.makeText(RegistrationPage.this,"You are successfully Registered", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                            intent.putExtra("email", email);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            String excString = task.getException().getMessage();
                                            Toast.makeText(RegistrationPage.this, excString, Toast.LENGTH_LONG).show();
                                        }
                                    }

                                });
                            }
                            else {
                                userName.setError("Username already used");
                            }
                        }
                    }
                });
            }
        });
    }

    public String writeNewUser(List<String> users) {
        LastLocation userLocation = new LastLocation(0.0,0.0);
        mDb = FirebaseDatabase.getInstance().getReference();
        User user;
        if (TextUtils.isEmpty(educationalInstitution.getText())){
            user = new User(userName.getText().toString(),
                    name.getText().toString(),
                    password.getText().toString(),
                    student.isChecked(),
                    tutor.isChecked(),
                    phoneNumber.getText().toString(),
                    email.getText().toString(),
                    userLocation);
        }
        else {
            user = new User(userName.getText().toString(),
                    name.getText().toString(),
                    password.getText().toString(),
                    student.isChecked(),
                    tutor.isChecked(),
                    phoneNumber.getText().toString(),
                    educationalInstitution.getText().toString(),
                    email.getText().toString(),
                    userLocation);
        }


        mDb.child("users").child(user.getUsername()).setValue(user);
        mDb.child("usernames").setValue(users);
        return user.getEmail();

    }
}
