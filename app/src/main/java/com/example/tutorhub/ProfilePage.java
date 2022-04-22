package com.example.tutorhub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
        Bundle extras = getIntent().getExtras();
        String value = "";
        if(extras != null) {
            value = extras.getString("username");
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

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
                            // User from database
                            User user = task.getResult().getValue(User.class);

                            // variables for page
                            TextView name = findViewById(R.id.usersName);
                            TextView email = findViewById(R.id.usersEmail);
                            TextView phoneNr = findViewById(R.id.phoneNumber);
                            LinearLayout layout = (LinearLayout) findViewById(R.id.linLayout);

                            // set correct variables
                            name.setText(user.getName() + " ("+user.getUsername()+")");
                            email.setText(user.getEmail());
                            phoneNr.setText(user.getPhoneNumber());

                            /** Tutor History cards */
                            // If empty tell the user that
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
                                card.setCardBackgroundColor(getResources().getColor(R.color.white));

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
                                                        tutors.add(temp);
                                                    }
                                                    // Show tutors from student history
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
                                                        card.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                        card.setClickable(true);
                                                        // Display information if tutor is clicked
                                                        card.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                                                alert.setTitle(tutor.getName());
                                                                String tags = "Subjects: ";
                                                                tags = tags +tutor.getTutorRole().getSubjectTags().toString();
                                                                alert.setMessage("University: " + tutor.getUniversity() +
                                                                        "\nEmail: "+tutor.getEmail() +
                                                                        "\nPhone Nunmber: "+tutor.getPhoneNumber()+ "\n" +
                                                                        tags);

                                                                AlertDialog al = alert.create();
                                                                al.show();
                                                            }
                                                        });
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
                                }

                            }
                            // Variables for reset Password Dialog View
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






                            // Variables for New Name Dialog View
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
                            // Variables for Contact Information Dialog View
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

                            // Variables for Add Tutor Dialog View
                            TextView pst = findViewById(R.id.textView5);
                            LayoutInflater linf = LayoutInflater.from(context);
                            View sel_tut = linf.inflate(R.layout.select_tutor, null);
                            AlertDialog.Builder alert4 = new AlertDialog.Builder(context);
                            alert4.setTitle("Add Tutor").setView(sel_tut);
                            alert4.setCancelable(false);
                            LinearLayout laytut = sel_tut.findViewById(R.id.linLayouttut);
                            Button search = sel_tut.findViewById(R.id.searchpfp);
                            EditText tutName = sel_tut.findViewById(R.id.editTextTextPersonName2);
                            Button cancel = sel_tut.findViewById(R.id.button15);
                            Button add = sel_tut.findViewById(R.id.button16);
                            RatingBar rating = sel_tut.findViewById(R.id.ratingBar2);
                            AlertDialog al4 = alert4.create();
                            List<Boolean> changes = new ArrayList<>();

                            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                    changes.add(true);
                                }
                            });
                            pst.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    al4.show();
                                    ArrayList<User> tutors = new ArrayList<>();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    List<User> selTutor = new ArrayList<>();
                                    List<CardView> cards = new ArrayList<>();
                                    ValueEventListener query1 = ref.child("users").orderByChild("tutor").equalTo(true)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        // Display tutors if not in student history
                                                        for (DataSnapshot tutor : snapshot.getChildren()) {
                                                            boolean found = false;
                                                            User curTutor = tutor.getValue(User.class);
                                                            for(String usrn: user.getStudentRole().getTutorHistory()){
                                                                if(usrn.equals(curTutor.getUsername())){
                                                                    found = true;
                                                                    break;
                                                                }
                                                            }
                                                            if(!found){
                                                                tutors.add(curTutor);
                                                                TextView tx = new TextView(context);
                                                                tx.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                                                tx.setText(curTutor.getName());
                                                                CardView card = new CardView(context);
                                                                tx.setPadding(10, 5, 10, 5);

                                                                card.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                                                card.setCardElevation(10);
                                                                card.setRadius(20);
                                                                card.setPreventCornerOverlap(true);
                                                                card.setUseCompatPadding(true);
                                                                card.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                                cards.clear();
                                                                card.setClickable(true);
                                                                cards.add(card);

                                                                // Select tutor if card is clicked
                                                                card.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {
                                                                        selTutor.clear();
                                                                        selTutor.add(curTutor);
                                                                        // Change card color if clicked
                                                                        for(CardView c: cards){
                                                                            if(c.getCardBackgroundColor().equals(getResources().getColorStateList(R.color.baby_blue))){
                                                                                c.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                                                break;
                                                                            }
                                                                        }
                                                                        if(card.getCardBackgroundColor().equals(getResources().getColorStateList(R.color.baby_blue))){
                                                                            card.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                                        }
                                                                        else{
                                                                            card.setCardBackgroundColor(getResources().getColorStateList(R.color.baby_blue));
                                                                        }
                                                                    }
                                                                });

                                                                LinearLayout lay = new LinearLayout(context);
                                                                lay.setOrientation(LinearLayout.VERTICAL);
                                                                lay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                                                lay.addView(tx);
                                                                ((LinearLayout) laytut).addView(card);
                                                                card.addView(lay);
                                                            }

                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(context, "Unexpected error occured, please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            tutName.setText("");
                                            rating.setRating(0);
                                            laytut.removeAllViews();
                                            selTutor.clear();
                                            al4.dismiss();

                                        }
                                    });
                                    add.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            boolean fine = true;
                                            if(selTutor.isEmpty()){
                                                fine = false;
                                                Toast.makeText(context,"Please select a tutor or cancel action.", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(changes.isEmpty()){
                                                Toast.makeText(context,"Please add a rating to the tutor or cancel action", Toast.LENGTH_SHORT).show();
                                                fine = false;
                                            }
                                            if(fine){
                                                user.getStudentRole().addTutor(selTutor.get(0).getUsername());
                                                selTutor.get(0).getTutorRole().addStudent(user.getUsername());
                                                selTutor.get(0).getTutorRole().addRating(rating.getRating());
                                                FirebaseDatabase.getInstance().getReference().child("users").child(user.getUsername()).setValue(user);
                                                FirebaseDatabase.getInstance().getReference().child("users").child(selTutor.get(0).getUsername()).setValue(selTutor.get(0));
                                                tutName.setText("");
                                                selTutor.clear();
                                                rating.setRating(0);
                                                laytut.removeAllViews();
                                                al4.dismiss();
                                                Toast.makeText(context,"Successfully added Tutor (will be updated on refresh)", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                    search.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String usrname = tutName.getText().toString();
                                            boolean found = false;
                                            boolean empty = false;
                                            //create cards based on tutors in database
                                            for(User t: tutors) {
                                                if(t.getUsername().equalsIgnoreCase(usrname)) {
                                                    laytut.removeAllViews();
                                                    found = true;
                                                    TextView tx = new TextView(context);
                                                    tx.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                                    tx.setText(t.getName());
                                                    CardView card = new CardView(context);
                                                    tx.setPadding(10, 5, 10, 5);

                                                    card.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                                    card.setCardElevation(10);
                                                    card.setRadius(20);
                                                    card.setPreventCornerOverlap(true);
                                                    card.setUseCompatPadding(true);
                                                    card.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                    cards.clear();
                                                    card.setClickable(true);
                                                    cards.add(card);
                                                    //add clicked selection
                                                    card.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            selTutor.clear();
                                                            selTutor.add(t);
                                                            //change color for clicked cards
                                                            for(CardView c: cards){
                                                                if(c.getCardBackgroundColor().equals(getResources().getColorStateList(R.color.baby_blue))){
                                                                    c.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                                    break;
                                                                }
                                                            }
                                                            if(card.getCardBackgroundColor().equals(getResources().getColorStateList(R.color.baby_blue))){
                                                                card.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                            }
                                                            else{
                                                                card.setCardBackgroundColor(getResources().getColorStateList(R.color.baby_blue));
                                                            }
                                                        }
                                                    });
                                                    LinearLayout lay = new LinearLayout(context);
                                                    lay.setOrientation(LinearLayout.VERTICAL);
                                                    lay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                                    lay.addView(tx);
                                                    ((LinearLayout) laytut).addView(card);
                                                    card.addView(lay);
                                                }
                                                else if(usrname.isEmpty()){
                                                    Toast.makeText(context,"No user specified", Toast.LENGTH_SHORT).show();
                                                    laytut.removeAllViews();
                                                    empty = true;
                                                    //create cards based on tutors in database
                                                    for(User t2: tutors){

                                                        TextView tx = new TextView(context);
                                                        tx.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                                        tx.setText(t2.getName());
                                                        CardView card = new CardView(context);
                                                        tx.setPadding(10, 5, 10, 5);

                                                        card.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                                        card.setCardElevation(10);
                                                        card.setRadius(20);
                                                        card.setPreventCornerOverlap(true);
                                                        card.setUseCompatPadding(true);
                                                        card.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                        cards.clear();
                                                        card.setClickable(true);
                                                        cards.add(card);

                                                        //add clicked selection
                                                        card.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                selTutor.clear();
                                                                selTutor.add(t2);
                                                               //change color for clicked cards
                                                                for(CardView c: cards){
                                                                    if(c.getCardBackgroundColor().equals(getResources().getColorStateList(R.color.baby_blue))){
                                                                        c.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                                        break;
                                                                    }
                                                                }
                                                                if(card.getCardBackgroundColor().equals(getResources().getColorStateList(R.color.baby_blue))){
                                                                    card.setCardBackgroundColor(getResources().getColor(R.color.white));
                                                                }
                                                                else{
                                                                    card.setCardBackgroundColor(getResources().getColorStateList(R.color.baby_blue));
                                                                }
                                                            }
                                                        });
                                                        LinearLayout lay = new LinearLayout(context);
                                                        lay.setOrientation(LinearLayout.VERTICAL);
                                                        lay.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                                        lay.addView(tx);
                                                        ((LinearLayout) laytut).addView(card);
                                                        card.addView(lay);
                                                    }
                                                }
                                            }
                                            if(!found && !empty){
                                                Toast.makeText(context,"Username specified does not exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

        TextView logOut = findViewById(R.id.logoutTXT);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}
