package dp015507.reading.uk.ac.surveyapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 27/11/2016.
 */

public class Survey_Description extends AppCompatActivity {

    protected String intro_page_survey_title;
    protected String intro_page_survey_description;
    protected String survey_id;
    protected String questionnaire_id;
    private String response;
    private String student_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_intro);

        TextView surveyTitle = (TextView) findViewById(R.id.survey_title);
        TextView survey_Description = (TextView) findViewById(R.id.survey_description);
        Button startSurvey = (Button) findViewById(R.id.btn_start_Survey);

        Intent intent = getIntent();
        intro_page_survey_title = intent.getStringExtra(Config.SURVEY_TITLE);
        intro_page_survey_description = intent.getStringExtra(Config.SURVEY_DESCRIPTION);
        survey_id = intent.getStringExtra(Config.SURVEY_ID);
        questionnaire_id = intent.getStringExtra(Config.QUESTIONNAIRE_ID);
        student_id = intent.getStringExtra(Config.STUDENT_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(survey_id != null && intro_page_survey_title != null && intro_page_survey_description != null){
            surveyTitle.setText(intro_page_survey_title);
            survey_Description.setText(intro_page_survey_description);
        } else {
            startSurvey.setEnabled(false);
            surveyTitle.setText(" ");
            survey_Description.setText(R.string.surveys_not_available);
        }

        startSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQuestions();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_surveydescription, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Intent myIntent = new Intent(Survey_Description.this, Dashboard.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(myIntent);
        finish();
        return;
    }

    private void getQuestions() {

        String url = Config.GET_QUESTIONNAIRE+questionnaire_id;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Survey_Description.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){

        String[] aQuestion;
        String[] aQuestionId;
        String[] aQuestionType;
        String[] aNum_option;
        String[] options;
        String[] options_q_id;
        String[] options_id;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray questions = jsonObject.getJSONArray(Config.QUESTIONS);
            JSONArray answers = jsonObject.getJSONArray(Config.ANSWERS);

            aQuestion = new String[questions.length()];  //initialize
            aQuestionId = new String[questions.length()]; //initialize
            aQuestionType = new String[questions.length()]; //initialize
            aNum_option = new String[questions.length()]; //initialize
            options = new String[answers.length()]; //initialize
            options_q_id = new String[answers.length()]; //initialize
            options_id = new String[answers.length()]; //initialize

            for(int i = 0; i < questions.length(); i++){
                JSONObject questionsAry = questions.getJSONObject(i);

                aQuestionId[i] = questionsAry.getString(Config.QUESTION_ID);
                aQuestion[i] = questionsAry.getString(Config.QUESTION_TEXT);
                aQuestionType[i] = questionsAry.getString(Config.QUESTION_TYPE);
                aNum_option[i] = questionsAry.getString(Config.NUM_OPTION);

            }

            for (int j = 0; j < answers.length(); j++){
                JSONObject optionsAry = answers.getJSONObject(j);

                options_id[j] = optionsAry.getString(Config.ANSWER_ID);
                options[j] = optionsAry.getString(Config.ANSWER_TEXT);
                options_q_id[j] = optionsAry.getString(Config.ANSWER_QUESTION_ID);
            }

            Intent intent = new Intent(Survey_Description.this, AnswerSurvey.class);
            intent.putExtra(Config.SURVEY_ID, survey_id);
            intent.putExtra(Config.STUDENT_ID, student_id);
            intent.putExtra(Config.QUESTION_TEXT, aQuestion);
            intent.putExtra(Config.QUESTION_ID, aQuestionId);
            intent.putExtra(Config.QUESTION_TYPE, aQuestionType);
            intent.putExtra(Config.NUM_OPTION, aNum_option);
            intent.putExtra(Config.ANSWER_ID, options_id);
            intent.putExtra(Config.ANSWER_TEXT, options);
            intent.putExtra(Config.ANSWER_QUESTION_ID, options_q_id);
            finish();
            startActivity(intent);

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

}
