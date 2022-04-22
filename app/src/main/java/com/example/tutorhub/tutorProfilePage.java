package com.example.tutorhub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class tutorProfilePage extends AppCompatActivity {
    Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_profile_page);
        Context context = this;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        Bundle extras = getIntent().getExtras();
        String value = "";
        if(extras != null) {
            value = extras.getString("username");
        }

        databaseReference.child("users").child(value).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(context,
                                    "Unexpected error occured, please leave and re-open the page.",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                        else{
                            /** User from database */
                            User user = task.getResult().getValue(User.class);

                            /** variables for page */
                            TextView name = findViewById(R.id.usersNametut);
                            TextView email = findViewById(R.id.usersEmailtut);
                            TextView phoneNr = findViewById(R.id.phoneNumbertut);
                            LinearLayout layout = (LinearLayout) findViewById(R.id.linLayouttut);
                            CheckBox[] checkBoxes = new CheckBox[5];
                            checkBoxes[0] = findViewById(R.id.checkBox);
                            checkBoxes[1] = findViewById(R.id.checkBox2);
                            checkBoxes[2] = findViewById(R.id.checkBox3);
                            checkBoxes[3] = findViewById(R.id.checkBox4);
                            checkBoxes[4] = findViewById(R.id.checkBox5);
                            // update checkboxes in accordance to set tags
                            if(!user.getTutorRole().getSubjectTags().isEmpty()){
                                for(Subject x: user.getTutorRole().getSubjectTags()){
                                    for(int i = 0; i != checkBoxes.length; i ++){
                                        if(checkBoxes[i].getText().toString().equalsIgnoreCase(x.toString())){
                                            checkBoxes[i].setChecked(true);
                                        }
                                        if(checkBoxes[i].getText().toString().equalsIgnoreCase("Data Structures") &&
                                        x.toString().equalsIgnoreCase("Data_Structures")) {
                                            checkBoxes[i].setChecked(true);
                                        }
                                    }

                                }
                            }

                            /** set correct vaiable */
                            name.setText(user.getName() + " ("+user.getUsername()+")");
                            email.setText(user.getEmail());
                            phoneNr.setText(user.getPhoneNumber());

                            /** Tutor History cards */
                            // Display no history if there is none
                            if(user.getTutorRole().getStudentHistory().size() == 0){
                                TextView tx = new TextView(context);
                                tx.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                tx.setText("No Student History");
                                tx.setPadding(10,5,10,5);

                                CardView card = new CardView(context);
                                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                card.setCardElevation(10);
                                card.setRadius(20);
                                card.setPreventCornerOverlap(true);
                                card.setUseCompatPadding(true);
                                card.setCardBackgroundColor(getResources().getColor(R.color.white));

                                LinearLayout lay = new LinearLayout(context);
                                lay.setOrientation(LinearLayout.VERTICAL);
                                lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                lay.addView(tx);
                                ((LinearLayout) layout).addView(card);
                                card.addView(lay);


                            }
                            else {
                                // Show student history otherwise
                                ArrayList<User> students = new ArrayList<>();
                                for(String username: user.getTutorRole().getStudentHistory()) {
                                    databaseReference.child("users").child(username).get()
                                            .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        User temp = task.getResult().getValue(User.class);
                                                        students.add(temp);
                                                    }
                                                    // create Student cards
                                                    for (User student : students) {
                                                        TextView tx = new TextView(context);
                                                        tx.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                        tx.setText(student.getName());
                                                        CardView card = new CardView(context);
                                                        tx.setPadding(10, 5, 10, 5);

                                                        card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                        card.setCardElevation(10);
                                                        card.setRadius(20);
                                                        card.setPreventCornerOverlap(true);
                                                        card.setUseCompatPadding(true);
                                                        card.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                        card.setClickable(true);
                                                        // Display information on click
                                                        card.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                                                alert.setTitle(student.getName());
                                                                alert.setMessage("University: " + student.getUniversity() +
                                                                        "\nEmail: "+student.getEmail() +
                                                                        "\nPhone Nunmber: "+student.getPhoneNumber());

                                                                AlertDialog al = alert.create();
                                                                al.show();
                                                            }
                                                        });
                                                        LinearLayout lay = new LinearLayout(context);
                                                        lay.setOrientation(LinearLayout.VERTICAL);
                                                        lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                        lay.addView(tx);
                                                        ((LinearLayout) layout).addView(card);
                                                        card.addView(lay);
                                                        students.remove(student);
                                                    }

                                                }
                                            });
                                }

                            }
                            TextView save = findViewById(R.id.SaveTexttut);
                            // Save changes to tags
                            save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(!user.getTutorRole().getSubjectTags().isEmpty()){
                                        user.getTutorRole().emptyTags();
                                    }
                                    String ds = "Data_Structures";
                                    for(int i = 0; i != checkBoxes.length; i++) {
                                        if(checkBoxes[i].isChecked()) {
                                            for(Subject x: Subject.values()){
                                                if(checkBoxes[i].getText().toString().equalsIgnoreCase(x.toString())){
                                                    user.getTutorRole().setSubjectTag(x);
                                                }
                                                else if(checkBoxes[i].getText().toString().equalsIgnoreCase("data structures") && x.toString().equalsIgnoreCase(ds)){
                                                    user.getTutorRole().setSubjectTag(x);
                                                }
                                            }
                                        }
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("users").child(user.getUsername()).setValue(user);
                                    Toast.makeText(context, "Changes saved", Toast.LENGTH_SHORT).show();
                                }
                            });


                            // Variables for reset Password Dialog
                            TextView resetPass = findViewById(R.id.textNewPasstut);
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
                                        user.resetPassword(password1.getText().toString());
                                        databaseReference.child("users").child(user.getUsername()).setValue(user);
                                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                        mAuth.getCurrentUser().updatePassword(password1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(context,"Password Successfully Changed", Toast.LENGTH_SHORT).show();
                                                    password1.setText("");
                                                    password2.setText("");
                                                    al.dismiss();
                                                }
                                                else{
                                                    Toast.makeText(context,"Error occured (there may are less than 6 characters).", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                            cancel_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    password1.setText("");
                                    password2.setText("");
                                    al.dismiss();
                                }
                            });

                            // Variables for new name dialog
                            LayoutInflater li2 = LayoutInflater.from(context);
                            View newNameDialog = li2.inflate(R.layout.new_name_dialog, null);
                            EditText name1 = (EditText) newNameDialog.findViewById(R.id.editTextTextName3);
                            Button setName_btn = (Button) newNameDialog.findViewById(R.id.setNewName_btn);
                            Button cancelName_btn = (Button) newNameDialog.findViewById(R.id.cancelNewName_btn);
                            alert.setTitle("Reset Name").setView(newNameDialog);
                            AlertDialog al2 = alert.create();

                            name.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    al2.show();
                                }
                            });


                            cancelName_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    name1.setText("");
                                    al2.dismiss();

                                }
                            });

                            setName_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    boolean fine = true;
                                    if (TextUtils.isEmpty(name1.getText())) {
                                        name1.setError("May not be empty");
                                        fine = false;
                                    }
                                    if (fine) {
                                        databaseReference.child("users").child(user.getUsername()).child("name").setValue(name1.getText().toString());
                                        Toast.makeText(context,"Name Successfully Changed", Toast.LENGTH_SHORT).show();
                                        name1.setText("");
                                        al2.dismiss();


                                    }
                                }
                            });

                            // Variables for new contact information dialog
                            LayoutInflater li3 = LayoutInflater.from(context);
                            View newPhoneNumberDialog = li3.inflate(R.layout.new_contact_information_dialog, null);
                            EditText phoneNumber = (EditText) newPhoneNumberDialog.findViewById(R.id.editTextTextContactInformation);
                            Button setPhoneNumber_btn = (Button) newPhoneNumberDialog.findViewById(R.id.setNewPhoneNumber_btn);
                            Button cancelPhoneNumber_btn = (Button) newPhoneNumberDialog.findViewById(R.id.cancelNewPhoneNumber_btn);
                            alert.setTitle("Reset Phone Number").setView(newPhoneNumberDialog);
                            AlertDialog al3 = alert.create();

                            phoneNr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    al3.show();
                                }
                            });


                            cancelPhoneNumber_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    phoneNumber.setText("");
                                    al3.dismiss();

                                }
                            });

                            setPhoneNumber_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    boolean fine = true;
                                    if (TextUtils.isEmpty(phoneNumber.getText())) {
                                        phoneNumber.setError("May not be empty");
                                        fine = false;
                                    }
                                    if (fine) {
                                        databaseReference.child("users").child(user.getUsername()).child("phoneNumber").setValue(phoneNumber.getText().toString());
                                        Toast.makeText(context,"Contact Information Successfully Changed", Toast.LENGTH_SHORT).show();
                                        phoneNumber.setText("");
                                        al3.dismiss();


                                    }
                                }
                            });





                        }

                    }
                });
        // Log out
        TextView logOut = findViewById(R.id.logoutTXTtut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}
