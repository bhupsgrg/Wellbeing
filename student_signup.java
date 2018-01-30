package dp015507.reading.uk.ac.surveyapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by User on 23/11/2016.
 */

public class student_signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_sign_up);

        Spinner day = (Spinner) findViewById(R.id.dob_day);
        Spinner month = (Spinner) findViewById(R.id.dob_month);
        Spinner year = (Spinner) findViewById(R.id.dob_year);

        addItemsOnSpinner(month);

    }

    private void addItemsOnSpinner(Spinner spinner){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.monthList, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }
}
