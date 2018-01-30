package dp015507.reading.uk.ac.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * Created by User on 23/11/2016.
 */

public class Dashboard extends AppCompatActivity{

    public static final String STUDENT_ID = "student_id";
    public static final String STUDENT_NAME = "student_name";

    protected String student_fname;
    private String student_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView greeting;
        Button btnSurvey, btnInfo;
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            student_fname = savedInstanceState.getString(STUDENT_NAME);
            student_id = savedInstanceState.getString(STUDENT_ID);
        }else {
            Intent intent = getIntent();
            student_fname = intent.getStringExtra(MainActivity.FIRST_NAME);
            student_id = intent.getStringExtra(MainActivity.ID);
        }

        setContentView(R.layout.dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        greeting = (TextView) findViewById(R.id.username);

        greeting.setText(student_fname);

        btnSurvey = (Button) findViewById(R.id.btn_survey);
        btnInfo = (Button) findViewById(R.id.button_info);

        btnSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                //startActivity(new Intent(Dashboard.this, Survey_Description.class));
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STUDENT_ID, student_id);
        outState.putString(STUDENT_NAME, student_fname);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        student_fname = savedInstanceState.getString(STUDENT_NAME);
        student_id = savedInstanceState.getString(STUDENT_ID);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getData() {

        String url = Config.GET_SURVEY+student_id;

        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    if(response.equals("false")){
                        Intent intent = new Intent(Dashboard.this, Survey_Description.class);
                        startActivity(intent);
                    }else {
                        showJSON(response);
                    }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Dashboard.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject surveyData = result.getJSONObject(0);
            String survey_id = surveyData.getString(Config.SURVEY_ID);
            String questionnaire_id = surveyData.getString(Config.QUESTIONNAIRE_ID);
            String survey_title = surveyData.getString(Config.TITLE);
            String survey_des = surveyData.getString(Config.DESCRIPTION);

            Intent intent = new Intent(Dashboard.this, Survey_Description.class);
            intent.putExtra(Config.STUDENT_ID, student_id);
            intent.putExtra(Config.SURVEY_ID, survey_id);
            intent.putExtra(Config.QUESTIONNAIRE_ID, questionnaire_id);
            intent.putExtra(Config.SURVEY_TITLE, survey_title);
            intent.putExtra(Config.SURVEY_DESCRIPTION, survey_des);
            finish();
            startActivity(intent);

        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
