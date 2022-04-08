package com.example.tutorhub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.IDNA;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    Context context = this;

//    Task<DataSnapshot> database = FirebaseDatabase.getInstance().getReference().child("users").get().addOnCompleteListener(
//            new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        String s = task.getResult().getValue().toString();
//                        String[] ss = s.split(", ");
//                    }
//                }
//            });

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    ValueEventListener query = reference.child("users").orderByChild("tutor").equalTo(true)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot is the "issue" node with all children with id 0
                        for (DataSnapshot user : dataSnapshot.getChildren()) {
                            System.out.println("USER:" + user.toString());
                            User curUser = user.getValue(User.class);

                            System.out.println(curUser.getUsername());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    int x = 1;
                }
            });

    private FusedLocationProviderClient fusedLocationClient;

    public User curUser;

    private List<User> tutorList = new ArrayList<User>();
    private List<User> tutorListWithFilters = new ArrayList<User>();

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    return;
                } else {
                    Toast.makeText(this, "TutorHub has no location permissions!", Toast.LENGTH_LONG);
                }
            });



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Button btnInfo = findViewById(R.id.home_info_button);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                //intent.putExtra("username", curUser.getUsername());
                startActivity(intent);
            }
        });

        Button btnFilter = findViewById(R.id.filter_button);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Filter.class);
                startActivity(intent);
            }
        });

        String userEmail;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userEmail = extras.getString("email");
            ValueEventListener query = reference.child("users").orderByChild("email").equalTo(userEmail)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                curUser = dataSnapshot.getValue(User.class);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            int x = 1;
                        }
                    });
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            //Ask for location permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
            getLocation();
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        ValueEventListener query = reference.child("users").orderByChild("tutor").equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                User curUser = user.getValue(User.class);
                                tutorList.add(curUser);

                                System.out.println("FROM DATABASE");
                                System.out.println(curUser.getTutorRole().getContactInf());
                            }

                            LinearLayout layout = (LinearLayout) findViewById(R.id.layout_view_home);

                            System.out.println(tutorList.size()+"sizespace");
                            for (User tutor: tutorList) {
                                System.out.println(tutorList+"space");
                                TextView tx = new TextView(context);
                                tx.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                tx.setText(tutor.getName());
                                TextView uni = new TextView(context);
                                uni.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                uni.setText(tutor.getUniversity());
                                TextView phone = new TextView(context);
                                phone.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                phone.setText("Contact: " + tutor.getPhoneNumber());
                                TextView email = new TextView(context);
                                email.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                email.setText("Email: " + tutor.getEmail());
                                CardView card = new CardView(context);
                                tx.setPadding(10, 5, 10, 5);
                                uni.setPadding(10, 5, 10, 5);
                                phone.setPadding(10, 5, 10, 5);
                                email.setPadding(10, 5, 10, 5);

                                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                card.setCardElevation(10);
                                card.setRadius(20);
                                card.setPreventCornerOverlap(true);
                                card.setUseCompatPadding(true);
                                card.setCardBackgroundColor(getResources().getColor(R.color.teal_700));

                                LinearLayout lay = new LinearLayout(context);
                                lay.setOrientation(LinearLayout.VERTICAL);
                                lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                lay.addView(tx);
                                lay.addView(uni);
                                lay.addView(phone);
                                lay.addView(email);
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

    @SuppressLint("MissingPermission")
    private void getLocation() {
//        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    System.out.println("FROM DEVICE");
//                    System.out.println("LAT: " + location.getLatitude());
//                    System.out.println("LON: " + location.getLongitude());
//                    curUser.setLocation(location);
//                }
//                saveUser(curUser);
//            }
//        });
    }

    private void saveUser(User user) {
        reference.child("users").child(user.getUsername()).setValue(user);
    }

    private void getDistance() {
        //Location.distanceBetween();
    }

    @Override
    public void onResume() {
        super.onResume();
        tutorListWithFilters.clear();

        String sortingCriteria;
        Double rangeValue;
        Boolean mathIsPresent;
        Boolean physicsIsPresent;
        Boolean chemistryIsPresent;
        Boolean dataStructuresIsPresent;
        Boolean englishIsPresent;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sortingCriteria = extras.getString("sortingCriteria");
            rangeValue = extras.getDouble("rangeValue");
            mathIsPresent = extras.getBoolean("mathIsPresent");
            physicsIsPresent = extras.getBoolean("physicsIsPresent");
            chemistryIsPresent = extras.getBoolean("chemistryIsPresent");
            dataStructuresIsPresent = extras.getBoolean("dataStructuresIsPresent");
            englishIsPresent = extras.getBoolean("englishIsPresent");

            for (User tutor: tutorList) {
                if (tutor.getTutorRole().getSubjectTags().contains(Subject.Math) && mathIsPresent) {
                    tutorListWithFilters.add(tutor);
                } else if (tutor.getTutorRole().getSubjectTags().contains(Subject.Physics) && physicsIsPresent) {
                    tutorListWithFilters.add(tutor);
                } else if (tutor.getTutorRole().getSubjectTags().contains(Subject.Chemistry) && chemistryIsPresent) {
                    tutorListWithFilters.add(tutor);
                } else if (tutor.getTutorRole().getSubjectTags().contains(Subject.Data_Structures) && dataStructuresIsPresent) {
                    tutorListWithFilters.add(tutor);
                } else if (tutor.getTutorRole().getSubjectTags().contains(Subject.English) && englishIsPresent) {
                    tutorListWithFilters.add(tutor);
                }
            }

            LinearLayout layout = (LinearLayout) findViewById(R.id.layout_view_home);

            for (User tutor: tutorListWithFilters) {
                TextView tx = new TextView(context);
                tx.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tx.setText(tutor.getName());
                TextView uni = new TextView(context);
                uni.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                uni.setText(tutor.getUniversity());
                TextView phone = new TextView(context);
                phone.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                phone.setText("Contact: " + tutor.getPhoneNumber());
                TextView email = new TextView(context);
                email.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                email.setText("Email: " + tutor.getEmail());
                CardView card = new CardView(context);
                tx.setPadding(10, 5, 10, 5);
                uni.setPadding(10, 5, 10, 5);
                phone.setPadding(10, 5, 10, 5);
                email.setPadding(10, 5, 10, 5);

                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                card.setCardElevation(10);
                card.setRadius(20);
                card.setPreventCornerOverlap(true);
                card.setUseCompatPadding(true);
                card.setCardBackgroundColor(getResources().getColor(R.color.teal_700));

                LinearLayout lay = new LinearLayout(context);
                lay.setOrientation(LinearLayout.VERTICAL);
                lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lay.addView(tx);
                ((LinearLayout) layout).addView(card);
                card.addView(lay);
            }
        }
    }
}