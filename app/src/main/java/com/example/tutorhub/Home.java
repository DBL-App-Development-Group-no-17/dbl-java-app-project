package com.example.tutorhub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.icu.text.IDNA;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {
    Context context = this;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    private FusedLocationProviderClient fusedLocationClient;

    public User curUser;

    private List<User> tutorList = new ArrayList<User>();


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    return;
                } else {
                    Toast.makeText(this, "TutorHub has no location permissions!", Toast.LENGTH_LONG);
                }
            });

    String userEmail = null;
    String sortingCriteria;
    Double rangeValue;
    Double ratingValue = 0.0;
    Boolean mathIsPresent = true;
    Boolean physicsIsPresent = true;
    Boolean chemistryIsPresent = true;
    Boolean dataStructuresIsPresent = true;
    Boolean englishIsPresent = true;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Button btnInfo = findViewById(R.id.home_info_button);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (curUser.isTutor()) {
                    intent = new Intent(getApplicationContext(), tutorProfilePage.class);
                } else {
                    intent = new Intent(getApplicationContext(), ProfilePage.class);
                }
                intent.putExtra("username", curUser.getUsername());
                startActivity(intent);
            }
        });

        Button btnFilter = findViewById(R.id.filter_button);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Filter.class);
                intent.putExtra("email", curUser.getEmail());
                startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userEmail = extras.getString("email").toLowerCase();

            sortingCriteria = extras.getString("sortingCriteria");
            rangeValue = extras.getDouble("rangeValue");
            ratingValue = extras.getDouble("ratingValue");
            mathIsPresent = extras.getBoolean("mathIsPresent");
            physicsIsPresent = extras.getBoolean("physicsIsPresent");
            chemistryIsPresent = extras.getBoolean("chemistryIsPresent");
            dataStructuresIsPresent = extras.getBoolean("dataStructuresIsPresent");
            englishIsPresent = extras.getBoolean("englishIsPresent");
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            //Ask for location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Please allow TutorHub to use location", Toast.LENGTH_LONG).show();
            }
        }


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ValueEventListener query1 = ref.child("users").orderByChild("email").equalTo(userEmail)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot user : dataSnapshot.getChildren()) {
                            curUser = user.getValue(User.class);

                            TextView name = findViewById(R.id.home_text_champion);
                            name.setText("Hi " + curUser.getName().split(" ")[0] + "!");

                            getLocation();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            ValueEventListener query = ref.child("users").orderByChild("tutor").equalTo(true)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            tutorList.clear();
                                            for (DataSnapshot tutor : dataSnapshot.getChildren()) {
                                                User curTutor = tutor.getValue(User.class);
                                                tutorList.add(curTutor);
                                            }

                                            List<User> filteredTutors = applyFilters();


                                            LinearLayout layout = (LinearLayout) findViewById(R.id.layout_view_home);
                                            layout.removeAllViews();
                                            for (User tutor: filteredTutors) {
                                                TextView tx = new TextView(context);
                                                tx.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                tx.setText(tutor.getName());
                                                tx.setTextSize(20);
                                                tx.setTypeface(null, Typeface.BOLD);

                                                TextView uni = new TextView(context);
                                                uni.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                uni.setText("University: " + tutor.getUniversity());
                                                tx.setTypeface(null, Typeface.BOLD);

                                                TextView phone = new TextView(context);
                                                phone.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                phone.setText("Phone: " + tutor.getPhoneNumber());

                                                TextView email = new TextView(context);
                                                email.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                email.setText("Email: " + tutor.getEmail());

                                                TextView subjects = new TextView(context);
                                                subjects.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                String strSubjectsList = tutor.getTutorRole().getSubjectTags().toString();
                                                String strSubjects = strSubjectsList.substring(1, strSubjectsList.length() - 1);
                                                subjects.setText("Subjects: " + strSubjects);

                                                TextView rating = new TextView(context);
                                                rating.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                rating.setText("Rating: " + tutor.getTutorRole().getRating());

                                                TextView location = new TextView(context);
                                                location.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                Location loc1 = new Location("");
                                                loc1.setLatitude(curUser.getLocation().latitude);
                                                loc1.setLongitude(curUser.getLocation().longitude);
                                                Location loc2 = new Location("");
                                                loc2.setLatitude(tutor.getLocation().latitude);
                                                loc2.setLongitude(tutor.getLocation().longitude);
                                                float distance = loc1.distanceTo(loc2) / 1000;
                                                location.setText(String.format("%.1f", distance) + " km away");
                                                location.setPadding(20,10,20,10);

                                                CardView card = new CardView(context);
                                                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                card.setCardElevation(10);
                                                card.setRadius(20);
                                                card.setPreventCornerOverlap(true);
                                                card.setUseCompatPadding(true);
                                                card.setCardBackgroundColor(getResources().getColor(R.color.baby_blue));

                                                LinearLayout lay = new LinearLayout(context);
                                                lay.setOrientation(LinearLayout.VERTICAL);
                                                lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                lay.addView(tx);
                                                lay.addView(uni);
                                                lay.addView(phone);
                                                lay.addView(email);
                                                lay.addView(subjects);
                                                lay.addView(rating);
                                                lay.addView(location);
                                                lay.setPadding(10, 5, 10, 5);
                                                ((LinearLayout) layout).addView(card);
                                                card.addView(lay);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        int x = 1;
                                    }
                                });
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    int x = 1;
                }
            });
    }

    private synchronized List<User> applyFilters() {
        List<User> lstTutorsFilteredOnTags = new ArrayList<User>();
        List<User> lstTutorsFiltered = new ArrayList<User>();

        if (mathIsPresent || physicsIsPresent || chemistryIsPresent || dataStructuresIsPresent || englishIsPresent) {
            for (User tutor : tutorList) {
                List<Subject> subjectTags = tutor.getTutorRole().getSubjectTags();
                if (subjectTags.contains(Subject.Math) && mathIsPresent) {
                    lstTutorsFilteredOnTags.add(tutor);
                } else if (subjectTags.contains(Subject.Physics) && physicsIsPresent) {
                    lstTutorsFilteredOnTags.add(tutor);
                } else if (subjectTags.contains(Subject.Chemistry) && chemistryIsPresent) {
                    lstTutorsFilteredOnTags.add(tutor);
                } else if (subjectTags.contains(Subject.Data_Structures) && dataStructuresIsPresent) {
                    lstTutorsFilteredOnTags.add(tutor);
                } else if (subjectTags.contains(Subject.English) && englishIsPresent) {
                    lstTutorsFilteredOnTags.add(tutor);
                }
            }
        } else {    //No subject tags specified in filters, so don't filter on tags
            lstTutorsFilteredOnTags = tutorList;
        }

        if (sortingCriteria != null) {
            if (sortingCriteria.equals("DISTANCE")) {
                Location userLoc = new Location("");
                userLoc.setLatitude(curUser.getLocation().latitude);
                userLoc.setLongitude(curUser.getLocation().longitude);

                for (User tutor : lstTutorsFilteredOnTags) {
                    Location tutorLoc = new Location("");
                    tutorLoc.setLatitude(tutor.getLocation().latitude);
                    tutorLoc.setLongitude(tutor.getLocation().longitude);
                    if (userLoc.distanceTo(tutorLoc) / 1000 <= rangeValue) {
                        lstTutorsFiltered.add(tutor);
                    }
                }
            } else { //sortingCriteria.equals("RATING")
                for (User tutor : lstTutorsFilteredOnTags) {
                    if (tutor.getTutorRole().getRating() >= ratingValue) {
                        lstTutorsFiltered.add(tutor);
                    }
                }
            }
        } else {
            return lstTutorsFilteredOnTags;
        }
        return lstTutorsFiltered;
    }

    @SuppressLint("MissingPermission")
    private synchronized void getLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    curUser.setLocation(location.getLatitude(), location.getLongitude());
                }
                saveUser(curUser);
            }
        });
    }

    private synchronized void saveUser(User user) {
        reference.child("users").child(user.getUsername()).setValue(user);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}