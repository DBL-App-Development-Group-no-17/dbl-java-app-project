package com.example.tutorhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

// to implement: Save Filters, subject addition, sorting strategy
public class Filter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        TextView cancel = findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });

        TextView reset = findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // reset the filters to default values
            }
        });

        ToggleButton distance = findViewById(R.id.distance_button);
        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set the sorting of results by Distance
            }
        });

        ToggleButton rating = findViewById(R.id.rating_button);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set the sorting of results by Rating
            }
        });

        SeekBar range = findViewById(R.id.range_bar);
        range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set the distance/rating selected
            }
        });

        TextView progress = findViewById(R.id.range_progress);
        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // output the progress of the distance/rating bar as in km or points
            }
        });

        ToggleButton math = findViewById(R.id.math_button);
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add Math to the list of subjects
            }
        });
        ToggleButton english = findViewById(R.id.english_button);
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add English to the list of subjects
            }
        });
        ToggleButton physics = findViewById(R.id.physics_button);
        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add Physics to the list of subjects
            }
        });
        ToggleButton chemistry = findViewById(R.id.chemistry_button);
        chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add Chemistry to the list of subjects
            }
        });
        ToggleButton data_structures = findViewById(R.id.data_structures_button);
        data_structures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add Data_Structures to the list of subjects
            }
        });

        Button saveFilters = findViewById(R.id.save_filters_button);
        saveFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save the filters to set values
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });

        /*Button dm = findViewById(R.id.dm_button);
        dm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DM.class));
            }
        });*/

        /*Button home = findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });*/

        /*Button map = findViewById(R.id.map_button);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Map.class));
            }
        });*/
    }
}
