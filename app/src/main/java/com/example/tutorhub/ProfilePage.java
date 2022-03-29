package com.example.tutorhub;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Profile page for user
 */
public class ProfilePage extends AppCompatActivity {
    Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        Context context = this;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        databaseReference.child("users").child("test").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    //not sure what to put here
                }
                else{
                    /** User from database */
                    User user = task.getResult().getValue(User.class);

                    /** variables for page */
                    TextView name = findViewById(R.id.usersName);
                    TextView email = findViewById(R.id.usersEmail);
                    TextView phoneNr = findViewById(R.id.phoneNumber);
                    LinearLayout layout = (LinearLayout) findViewById(R.id.layout1);

                    /** set correct vaiable */
                    name.setText(user.getName() + " ("+user.getUsername()+")");
                    email.setText(user.getEmail());
                    phoneNr.setText(user.getPhoneNumber());
                    System.out.println(user.getStudentRole().getTutorHistory().isEmpty());

                    /** Tutor History cards */
                    if(user.getStudentRole().getTutorHistory().size() == 0){
                        TextView tx = new TextView(context);
                        tx.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                        tx.setText("No Tutor History");
                        tx.setPadding(10,5,10,5);

                        CardView card = new CardView(context);
                        card.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                        card.setCardElevation(10);
                        card.setRadius(20);
                        card.setPreventCornerOverlap(true);
                        card.setUseCompatPadding(true);
                        card.setCardBackgroundColor(getResources().getColor(R.color.teal_700));

                        LinearLayout lay = new LinearLayout(context);
                        lay.setOrientation(LinearLayout.VERTICAL);
                        lay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
                        lay.addView(tx);
                        ((LinearLayout) layout).addView(card);
                        card.addView(lay);


                    }
                    else {
                        ArrayList<User> tutors = new ArrayList<>();
                        for(String username: user.getStudentRole().getTutorHistory()) {
                           databaseReference.child("users").child(username).get()
                                   .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                       @Override
                                       public void onComplete(@NonNull Task<DataSnapshot> task) {
                                           if (task.isSuccessful()) {
                                               User temp = task.getResult().getValue(User.class);
                                               System.out.println(task.getResult().getValue());
                                               System.out.println(temp.getName());
                                               tutors.add(temp);
                                               System.out.println(tutors.size());
                                           }
                                           for (User tutor : tutors) {
                                               TextView tx = new TextView(context);
                                               tx.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                               tx.setText(tutor.getName());
                                               CardView card = new CardView(context);
                                               tx.setPadding(10, 5, 10, 5);

                                               card.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                               card.setCardElevation(10);
                                               card.setRadius(20);
                                               card.setPreventCornerOverlap(true);
                                               card.setUseCompatPadding(true);
                                               card.setCardBackgroundColor(getResources().getColor(R.color.teal_700));

                                               LinearLayout lay = new LinearLayout(context);
                                               lay.setOrientation(LinearLayout.VERTICAL);
                                               lay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                               lay.addView(tx);
                                               ((LinearLayout) layout).addView(card);
                                               card.addView(lay);
                                               tutors.remove(tutor);
                                           }

                                       }
                                   });


                        System.out.println(tutors.size() + "cock");


                        }

                    }
                    /** variables for dialog view */

                    TextView resetPass = findViewById(R.id.textNewPass);
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
                                databaseReference.child("users").child(user.getUsername()).setValue(user);
                                Toast.makeText(context,"Password Successfully Changed", Toast.LENGTH_SHORT).show();
                                System.out.println(user.getPassword());
                                password1.setText("");
                                password2.setText("");
                                al.dismiss();
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
                }

            }
        });

//        //User user = getIntent().getParcelableExtra("user");
//        User user = new User("username", "Firstname Surname", "password", true,
//                false, "911111111111111", "email@email.com");

//        User user3 = new User("username1", "Bobby(OG) Johnson", "password", false,
//                true, "911", "email1@email.com");






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

       ;


    }
}
