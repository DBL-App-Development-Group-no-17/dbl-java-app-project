package com.example.tutorhub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

public class Home extends AppCompatActivity {

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

    private final int REQUEST_ACCESS_COARSE_LOCATION = 1;



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

//        ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
//                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
//                    if (coarseLocationGranted != null && coarseLocationGranted) {
//                        // Only approximate location access granted.
//                    } else {
//                        // No location access granted.
//                        Toast.makeText(this, "TutorHub has no location permissions!", Toast.LENGTH_LONG);
//                    }
//                }
//        );
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

                                System.out.println("FROM DATABASE");
                                System.out.println(curUser.getTutorRole().getContactInf());
                                System.out.println(curUser.location.latitude);
                                System.out.println(curUser.location.longitude);
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
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    System.out.println("FROM DEVICE");
                    System.out.println("LAT: " + location.getLatitude());
                    System.out.println("LON: " + location.getLongitude());
                }
            }
        });
    }

    private void getDistance() {
        //Location.distanceBetween();
    }

    @Override
    public void onResume() {
        super.onResume();

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


        }
    }
}