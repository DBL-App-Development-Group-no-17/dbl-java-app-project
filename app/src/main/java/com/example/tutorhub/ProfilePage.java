package com.example.tutorhub;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Profile page for user
 */
public class ProfilePage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        Context context = this;

        //User user = getIntent().getParcelableExtra("user");
        User user = new User("username", "Firstname Surname", "password", true,
                false, "911", "email@email.com");
        User user2 = new User("username1", "Bobby Shmurda", "password", false,
                true, "911", "email1@email.com");
        User user3 = new User("username1", "Bobby(OG) Johnson", "password", false,
                true, "911", "email1@email.com");

        /** variables for page */
        TextView name = findViewById(R.id.usersName);
        TextView email = findViewById(R.id.usersEmail);
        TextView userName =  findViewById(R.id.userName);
        TextView phoneNr = findViewById(R.id.phoneNumber);
        TextView resetPass = findViewById(R.id.textNewPass);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout1);
        name.setText(user.getName());
        userName.setText(user.getUsername());
        email.setText(user.getEmail());
        phoneNr.setText(user.getPhoneNumber());
        System.out.println(user.getStudentRole().getTutorHistory().isEmpty());
        //get tutors list and itterate through it.
//        if(user.getStudentRole().getTutorHistory().size() == 0){
//            TextView tx = new TextView(this);
//            tx.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//            tx.setText("No Tutor History");
//            tx.setId((Integer)23345);
//            ((LinearLayout) layout).addView(tx);
//        }
        //else{
//            for(User x: user.getStudentRole().getTutorHistory()){
//                int i=0;
//                TextView tx = new TextView(this);
//                tx.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//                tx.setText(x.getName());
//                tx.setId(i);
//                i++;
//                ((LinearLayout) layout).addView(tx);
//            }
//        }

        /** variables for dialog view */


        LayoutInflater li = LayoutInflater.from(context);
        View newPassDialog = li.inflate(R.layout.new_password_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        String newPassword;
        String confirmPassword;
        EditText password1 = (EditText) newPassDialog.findViewById(R.id.editTextTextPassword3);
        EditText password2 = (EditText) newPassDialog.findViewById(R.id.editTextTextPassword4);
        Button set_btn = (Button) newPassDialog.findViewById(R.id.setNewPassword_btn);
        Button cancel_btn = (Button) newPassDialog.findViewById(R.id.cancelNewPass_btn);
        alert.setTitle("Reset Password").setView(newPassDialog);
        AlertDialog al = alert.create();

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al.show();
            }
        });
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fine = true;
                if (TextUtils.isEmpty(password1.getText())) {
                    password1.setError("May not be empty");
                    fine = false;
                }
                if (TextUtils.isEmpty(password2.getText())) {
                    password2.setError("May not be empty");
                    fine = false;
                }
                if (!password1.getText().toString().equals(password2.getText().toString())) {
                    password2.setError("Password does not match");
                    fine = false;
                }
                if (fine) {
                    System.out.println(user.getPassword());
                    user.resetPassword(password1.getText().toString());
                    System.out.println(user.getPassword());
                    al.dismiss();
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                al.dismiss();
            }
        });


    }
}
