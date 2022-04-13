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
//
//    ValueEventListener query = reference.child("users").orderByChild("tutor").equalTo(true)
//            .addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()) {
//                        // dataSnapshot is the "issue" node with all children with id 0
//                        for (DataSnapshot user : dataSnapshot.getChildren()) {
//                            System.out.println("USER:" + user.toString());
//                            User curUser = user.getValue(User.class);
//
//                            System.out.println(curUser.getUsername());
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    int x = 1;
//                }
//            });

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

        System.out.println("CREATED");

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
                startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userEmail = extras.getString("email");

            sortingCriteria = "";//extras.getString("sortingCriteria");
            rangeValue = extras.getDouble("rangeValue");
            ratingValue = extras.getDouble("ratingValue");
            mathIsPresent = extras.getBoolean("mathIsPresent");
            physicsIsPresent = extras.getBoolean("physicsIsPresent");
            chemistryIsPresent = extras.getBoolean("chemistryIsPresent");
            dataStructuresIsPresent = extras.getBoolean("dataStructuresIsPresent");
            englishIsPresent = extras.getBoolean("englishIsPresent");
            System.out.println(sortingCriteria);
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
                                getLocation();

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                ValueEventListener query = ref.child("users").orderByChild("tutor").equalTo(true)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot tutor : dataSnapshot.getChildren()) {
                                                    User curTutor = tutor.getValue(User.class);
                                                    tutorList.add(curTutor);

                                                    //System.out.println("FROM DATABASE");
                                                   // System.out.println(curTutor.getTutorRole().getContactInf());
                                                }

                                                List<User> filteredTutors = applyFilters();


                                                LinearLayout layout = (LinearLayout) findViewById(R.id.layout_view_home);

                                                System.out.println(tutorList.size()+"sizespace");

                                                for (User tutor: filteredTutors) {
                                                    System.out.println(tutor.getUsername());
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

                                                    TextView location = new TextView(context);
                                                    location.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                    Location loc1 = new Location("");
                                                    loc1.setLatitude(curUser.getLocation().latitude);
                                                    loc1.setLongitude(curUser.getLocation().longitude);
                                                    Location loc2 = new Location("");
                                                    loc2.setLatitude(tutor.getLocation().latitude);
                                                    loc2.setLongitude(tutor.getLocation().longitude);
                                                    int distance = (int) loc1.distanceTo(loc2) / 1000;
                                                    location.setText(distance + " km away");
                                                    location.setPadding(20,10,20,10);

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
                                                    lay.addView(location);
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
        List<User> tutorListWithFilters = new ArrayList<User>();
        System.out.println(ratingValue + ",  " + rangeValue);
        for (User tutor: tutorList) {
            List<Subject> subjectTags = tutor.getTutorRole().getSubjectTags();
            if (subjectTags.isEmpty()) {
                tutorListWithFilters = tutorList;
            } else if (tutor.getTutorRole().getSubjectTags().contains(Subject.Math) && mathIsPresent) {
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

        if (sortingCriteria == "DISTANCE") {
            Location userLoc = new Location("");
            userLoc.setLatitude(curUser.getLocation().latitude);
            userLoc.setLongitude(curUser.getLocation().longitude);

            for (User tutor : tutorListWithFilters) {
                Location tutorLoc = new Location("");
                tutorLoc.setLatitude(tutor.getLocation().latitude);
                tutorLoc.setLongitude(tutor.getLocation().longitude);
                if (userLoc.distanceTo(tutorLoc) > rangeValue) {
                    tutorListWithFilters.remove(tutor);
                }
            }
        } else { //sortingCriteria == "RATING"
            for (User tutor : tutorListWithFilters) {
                if (tutor.getTutorRole().getRating() < ratingValue) {
                    tutorListWithFilters.remove(tutor);
                }
            }
        }
        return tutorListWithFilters;
    }

    @SuppressLint("MissingPermission")
    private synchronized void getLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                   System.out.println("FROM DEVICE");
                   System.out.println("LAT: " + location.getLatitude());
                   System.out.println("LON: " + location.getLongitude());
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
        //System.out.println("RESUMED");
//        tutorListWithFilters.clear();
//
//        String sortingCriteria;
//        Double rangeValue;
//        Boolean mathIsPresent;
//        Boolean physicsIsPresent;
//        Boolean chemistryIsPresent;
//        Boolean dataStructuresIsPresent;
//        Boolean englishIsPresent;
//
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            sortingCriteria = extras.getString("sortingCriteria");
//            rangeValue = extras.getDouble("rangeValue");
//            mathIsPresent = extras.getBoolean("mathIsPresent");
//            physicsIsPresent = extras.getBoolean("physicsIsPresent");
//            chemistryIsPresent = extras.getBoolean("chemistryIsPresent");
//            dataStructuresIsPresent = extras.getBoolean("dataStructuresIsPresent");
//            englishIsPresent = extras.getBoolean("englishIsPresent");
//
//            for (User tutor: tutorList) {
//                if (tutor.getTutorRole().getSubjectTags().contains(Subject.Math) && mathIsPresent) {
//                    tutorListWithFilters.add(tutor);
//                } else if (tutor.getTutorRole().getSubjectTags().contains(Subject.Physics) && physicsIsPresent) {
//                    tutorListWithFilters.add(tutor);
//                } else if (tutor.getTutorRole().getSubjectTags().contains(Subject.Chemistry) && chemistryIsPresent) {
//                    tutorListWithFilters.add(tutor);
//                } else if (tutor.getTutorRole().getSubjectTags().contains(Subject.Data_Structures) && dataStructuresIsPresent) {
//                    tutorListWithFilters.add(tutor);
//                } else if (tutor.getTutorRole().getSubjectTags().contains(Subject.English) && englishIsPresent) {
//                    tutorListWithFilters.add(tutor);
//                }
//            }
//
//            LinearLayout layout = (LinearLayout) findViewById(R.id.layout_view_home);
//
//            for (User tutor: tutorListWithFilters) {
//                TextView tx = new TextView(context);
//                tx.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                tx.setText(tutor.getName());
//                TextView uni = new TextView(context);
//                uni.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                uni.setText(tutor.getUniversity());
//                TextView phone = new TextView(context);
//                phone.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                phone.setText("Contact: " + tutor.getPhoneNumber());
//                TextView email = new TextView(context);
//                email.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                email.setText("Email: " + tutor.getEmail());
//
//                TextView location = new TextView(context);
//                location.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                Location loc1 = new Location("");
//                loc1.setLatitude(curUser.getLocation().latitude);
//                loc1.setLongitude(curUser.getLocation().longitude);
//                Location loc2 = new Location("");
//                loc2.setLatitude(tutor.getLocation().latitude);
//                loc2.setLongitude(tutor.getLocation().longitude);
//                location.setText(loc1.distanceTo(loc2) + " km away");
//                location.setPadding(20,10,20,10);
//                tx.setText(location.getText().toString());
//
//                CardView card = new CardView(context);
//                tx.setPadding(10, 5, 10, 5);
//                uni.setPadding(10, 5, 10, 5);
//                phone.setPadding(10, 5, 10, 5);
//                email.setPadding(10, 5, 10, 5);
//
//                card.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                card.setCardElevation(10);
//                card.setRadius(20);
//                card.setPreventCornerOverlap(true);
//                card.setUseCompatPadding(true);
//                card.setCardBackgroundColor(getResources().getColor(R.color.teal_700));
//
//                LinearLayout lay = new LinearLayout(context);
//                lay.setOrientation(LinearLayout.VERTICAL);
//                lay.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                lay.addView(tx);
//                lay.addView(location);
//                ((LinearLayout) layout).addView(card);
//                card.addView(lay);
//            }
//        }
    }
}