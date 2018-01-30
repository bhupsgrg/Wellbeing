package dp015507.reading.uk.ac.surveyapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by User on 14/11/16.
 */
public class AnswerSurvey extends AppCompatActivity {

    private int total_question = 0;
    private int question_answered = 0;
    private TextView completed_percent;
    private int percent;
    private ProgressBar percentage_bar;
    private TextView question_num;
    private TextView question_text;
    private String[] questions;
    private String[] questions_type;
    private String[] questions_id;
    private String[] num_options;
    private String[] option_id;
    private String[] options;
    private String[] options_q_id;
    private String survey_id;
    private static final int NEXT = 1;
    private static final int PREVIOUS = 0;
    private static final int FIRST_Q = 2;
    private int a = 0; //current question
    private RadioGroup radioGroup;
    private ArrayList<String> satisfaction_selection;
    private int count_options = 0; //keeps track of options for each question
    private boolean answered = false;
    private Map<String, ArrayList<String>> uploadAnswers;
    private int count = 0;
    private List<Integer> multiple_choice;
    private EditText openAnswer;
    private String student_id;
    private int single_qes_num_holder; //holds number of option of current single question
    private int count_single = 0;
    private int last_one_single_question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionaire);

        //capture intent data passed from Survey_description
        Intent intent = getIntent();
        questions = intent.getStringArrayExtra(Config.QUESTION_TEXT);
        questions_type = intent.getStringArrayExtra(Config.QUESTION_TYPE);
        questions_id = intent.getStringArrayExtra(Config.QUESTION_ID);
        num_options = intent.getStringArrayExtra(Config.NUM_OPTION);
        option_id = intent.getStringArrayExtra(Config.ANSWER_ID);
        options = intent.getStringArrayExtra(Config.ANSWER_TEXT);
        options_q_id = intent.getStringArrayExtra(Config.ANSWER_QUESTION_ID);
        survey_id = intent.getStringExtra(Config.SURVEY_ID);
        student_id = intent.getStringExtra(Config.STUDENT_ID);

        total_question = questions.length; //get total number of question

        question_num = (TextView) findViewById(R.id.question_num);
        question_text = (TextView) findViewById(R.id.question_text);
        completed_percent = (TextView) findViewById(R.id.completed_percent);
        percentage_bar = (ProgressBar) findViewById(R.id.progressBar);
        percentage_bar.setMax(100);
        Button next = (Button) findViewById(R.id.next_question);
        Button previous = (Button) findViewById(R.id.previous_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        uploadAnswers = new HashMap<>();
        completed_percent.setText(question_answered+"/"+total_question);
        satisfaction_selection = new ArrayList<>();
        setquestionsanswers(FIRST_Q);

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (answered) {

                    saveAnswer();
                    //Toast.makeText(AnswerSurvey.this, String.valueOf(uploadAnswers.size()), Toast.LENGTH_SHORT).show();
                    question_answered++;
                    set_completed_percentage();
                    if(question_answered >= total_question){ //if all answers are answered, upload the answers
                        /*try{
                            upload();
                            //uploadAnswers(); //uploads answers to database
                        } catch (JSONException e){
                            e.printStackTrace();
                        }*/

                        final AlertDialog.Builder submit_answer_builder = new AlertDialog.Builder(AnswerSurvey.this);
                        submit_answer_builder.setTitle(R.string.app_name);
                        submit_answer_builder.setMessage(R.string.submitAnswers);
                        submit_answer_builder.setPositiveButton(R.string.dialog_positive_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                upload();
                                //alertdialog to show message the survey is complete
                                AlertDialog.Builder builder = new AlertDialog.Builder(AnswerSurvey.this);
                                builder.setTitle(R.string.app_name);
                                builder.setMessage(R.string.survey_complete_msg);
                                builder.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent1 = new Intent(AnswerSurvey.this, Dashboard.class); //takes user back to dashboard
                                        startActivity(intent1);
                                    }
                                });
                                builder.show();
                            }
                        });
                        submit_answer_builder.setNegativeButton(R.string.dialog_negative_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        submit_answer_builder.show();


                        Iterator myVeryOwnIterator = uploadAnswers.keySet().iterator();
                        while(myVeryOwnIterator.hasNext()) {
                            String key=(String)myVeryOwnIterator.next();
                            String value= (String) uploadAnswers.get(key).toString();
                            Log.v(key, value);
                            //Toast.makeText(AnswerSurvey.this, "Key: "+key+" Value: "+value, Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        setquestionsanswers(NEXT); //gets the next question
                        //satisfaction_selection.clear();
                        answered = false;
                    }
                } else {
                    Toast.makeText(AnswerSurvey.this, "Please select a option above.", Toast.LENGTH_SHORT ).show();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a != 0){
                    deleteAnswer();
                    setquestionsanswers(PREVIOUS);
                    set_completed_percentage();
                } else {

                }

            }
        });
    }


    private int calculate_percentage(int total, int completed ){
        percent = ((completed*100)/total);
        return  percent;
    }

    private void set_completed_percentage(){
        calculate_percentage(total_question, question_answered);
        completed_percent.setText(question_answered+"/"+total_question);
        percentage_bar.setProgress(percent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Intent myIntent = new Intent(AnswerSurvey.this, Survey_Description.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
        startActivity(myIntent);
        finish();
        return;
    }

    private void setquestionsanswers(int nextorprevoius){

        if(nextorprevoius == NEXT || nextorprevoius == FIRST_Q){ //FIRST_Q = first question of survey
            question_num.setText(String.valueOf(a+1));
            question_text.setText(questions[a]);

            switch (questions_type[a]){
                case Config.SINGLE_OPTION:
                    count_single++;
                    last_one_single_question = a;
                    //single_qes_num_holder = Integer.parseInt(num_options[a]);
                    singleChoice(Integer.parseInt(num_options[a]), NEXT);
                    break;
                case Config.MULTIPLE_OPTION: multipleChoice(Integer.parseInt(num_options[a])); break;
                case Config.SATISFACTION: satisfaction(); break;
                case Config.OPEN_ANSWER: openAnswer(); break;
                case Config.YESNO: yesNo(); break;
                case Config.SCALE: linearScale(5); break;
            }
        }else if(nextorprevoius == PREVIOUS){
            ((LinearLayout) findViewById(R.id.options_container)).removeAllViews();
            question_answered = question_answered - 1;
            a = a - 1;

            last_one_single_question = last_one_single_question - 1;
            question_num.setText(String.valueOf(a+1));
            question_text.setText(questions[a]);

            switch (questions_type[a]){
                case Config.SINGLE_OPTION:
                    count_single = count_single - 1;
                    single_qes_num_holder = Integer.parseInt(num_options[last_one_single_question]);
                    singleChoice(Integer.parseInt(num_options[a]), PREVIOUS);
                    break;
                case Config.MULTIPLE_OPTION: multipleChoice(Integer.parseInt(num_options[a])); break;
                case Config.SATISFACTION: satisfaction(); break;
                case Config.OPEN_ANSWER: openAnswer(); break;
                case Config.YESNO: yesNo(); break;
                case Config.SCALE: linearScale(5); break;
            }
        }

    }

    private void saveAnswer(){

        if(((LinearLayout) findViewById(R.id.options_container)).getChildCount() > 0){

            if(questions_type[a].equals(Config.SINGLE_OPTION) || questions_type[a].equals(Config.SATISFACTION) || questions_type[a].equals(Config.YESNO) || questions_type[a].equals(Config.SCALE)){
                addValues(questions_id[a], satisfaction_selection.get(0));
            } else if (questions_type[a].equals(Config.MULTIPLE_OPTION)){
                for(int i = 0; i < multiple_choice.size(); i++){
                    addValues(questions_id[a], String.valueOf(multiple_choice.get(i)));
                }
            } else if(questions_type[a].equals(Config.OPEN_ANSWER)) {
                Log.v("open answer", openAnswer.getText().toString());
                addValues(questions_id[a], openAnswer.getText().toString());
            }
            ((LinearLayout) findViewById(R.id.options_container)).removeAllViews();
            a++;
        }
    }

    private void deleteAnswer(){
        if(((LinearLayout) findViewById(R.id.options_container)).getChildCount() > 0){
                uploadAnswers.remove(questions_id[a]);
        }
    }

    private void addValues(String key, String value) {
        ArrayList tempList = null;
        if (uploadAnswers.containsKey(key)) {
            tempList = uploadAnswers.get(key);
            if(tempList == null)
                tempList = new ArrayList();
            tempList.add(value);
        } else {
            tempList = new ArrayList();
            tempList.add(value);
        }
        uploadAnswers.put(key,tempList);
    }

    private void satisfaction(){


        radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        RadioButton completely_sat = new RadioButton(getApplicationContext()); completely_sat.setTextColor(Color.WHITE);
        RadioButton very_sat = new RadioButton(getApplicationContext()); very_sat.setTextColor(Color.WHITE);
        RadioButton mildly_sat = new RadioButton(getApplicationContext()); mildly_sat.setTextColor(Color.WHITE);
        RadioButton nutral = new RadioButton(getApplicationContext()); nutral.setTextColor(Color.WHITE);
        RadioButton mildly_unsat = new RadioButton(getApplicationContext()); mildly_unsat.setTextColor(Color.WHITE);
        RadioButton very_unsat = new RadioButton(getApplicationContext()); very_unsat.setTextColor(Color.WHITE);
        RadioButton not_sat = new RadioButton(getApplicationContext()); not_sat.setTextColor(Color.WHITE);

        completely_sat.setText(Config.DEFINITELY_AGREE);
        very_sat.setText(Config.MOSTLY_AGREE);
        mildly_sat.setText(Config.NEITHER_AGREE_NOR_DISAGREE);
        nutral.setText(Config.MOSTLY_DISAGREE);
        mildly_unsat.setText(Config.DEFINITELY_DISAGREE);
        very_unsat.setText(Config.NOT_APPLICABLE);

        completely_sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satisfaction_selection.clear();
                satisfaction_selection.add("1");
                answered = true;
            }
        });

        very_sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satisfaction_selection.clear();
                satisfaction_selection.add("2");
                answered = true;
            }
        });

        mildly_sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satisfaction_selection.clear();
                satisfaction_selection.add("3");
                answered = true;
            }
        });

        nutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satisfaction_selection.clear();
                satisfaction_selection.add("4");
                answered = true;
            }
        });

        mildly_unsat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satisfaction_selection.clear();
                satisfaction_selection.add("5");
                answered = true;
            }
        });

        very_unsat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satisfaction_selection.clear();
                satisfaction_selection.add("6");
                answered = true;
            }
        });


        radioGroup.addView(completely_sat);
        radioGroup.addView(very_sat);
        radioGroup.addView(mildly_sat);
        radioGroup.addView(nutral);
        radioGroup.addView(mildly_unsat);
        radioGroup.addView(very_unsat);

        ((ViewGroup) findViewById(R.id.options_container)).addView(radioGroup);
    }

    private void openAnswer(){

        openAnswer = new EditText(getApplicationContext());
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int charLength = charSequence.length();
                //Toast.makeText(getApplicationContext(), String.valueOf(charLength), Toast.LENGTH_SHORT).show();

                if (charLength != 0) {
                    answered = true;
                } else {
                    answered = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        openAnswer.setMinWidth(400);
        openAnswer.setMinHeight(500);
        openAnswer.setBackgroundColor(Color.WHITE);
        openAnswer.setTextColor(Color.BLACK);
        openAnswer.setGravity(Gravity.TOP);

        openAnswer.addTextChangedListener(textWatcher);

        ((ViewGroup) findViewById(R.id.options_container)).addView(openAnswer);
    }

    private void yesNo(){
        radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        RadioButton yes = new RadioButton(getApplicationContext());
        RadioButton no = new RadioButton(getApplicationContext());

        yes.setText(Config.YES); yes.setTextColor(Color.WHITE);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satisfaction_selection.clear();
                satisfaction_selection.add("1");
                answered = true;
            }
        });

        no.setText(Config.NO); no.setTextColor(Color.WHITE);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satisfaction_selection.clear();
                satisfaction_selection.add("0");
                answered = true;
            }
        });


        radioGroup.addView(yes);
        radioGroup.addView(no);

        ((ViewGroup) findViewById(R.id.options_container)).addView(radioGroup);
    }

    private void linearScale(int maxScale){
        for (int row = 0; row < 1; row++) {
            RadioGroup radioGroup = new RadioGroup(getApplicationContext());
            radioGroup.setOrientation(LinearLayout.VERTICAL);

            for (int i = 0; i < maxScale; i++) {
                final RadioButton scale = new RadioButton(getApplicationContext());
                scale.setId(i);
                scale.setText(String.valueOf(i+1));

                scale.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        satisfaction_selection.clear();
                        satisfaction_selection.add(String.valueOf(scale.getId()));
                        answered = true;
                    }
                });
                count_options++;
                radioGroup.addView(scale);
            }

            ((ViewGroup) findViewById(R.id.options_container)).addView(radioGroup);
        }
    }

    private void singleChoice(int num_option, int action){
        for (int row = 0; row < 1; row++) {
            RadioGroup radioGroup = new RadioGroup(getApplicationContext());
            radioGroup.setOrientation(LinearLayout.VERTICAL);

            if (action == NEXT){

                for (int i = 0; i < num_option; i++) {
                    final RadioButton radioButton = new RadioButton(getApplicationContext());

                    radioButton.setId(Integer.parseInt(option_id[count_options]));
                    radioButton.setText(options[count_options]);

                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            satisfaction_selection.clear();
                            satisfaction_selection.add(String.valueOf(radioButton.getId()));
                            answered = true;
                        }
                    });
                    count_options++;
                    Log.v("count_option", String.valueOf(count_options));
                    radioGroup.addView(radioButton);
                }
            } else if (action == PREVIOUS){
                if(count_single == 0){
                    count_options = 0;
                } else {
                    count_options = count_options - (num_option + single_qes_num_holder);
                }

                for (int i = 0; i < num_option; i++) {
                    final RadioButton radioButton = new RadioButton(getApplicationContext());

                    radioButton.setId(Integer.parseInt(option_id[count_options]));
                    radioButton.setText(options[count_options]);

                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            satisfaction_selection.clear();
                            satisfaction_selection.add(String.valueOf(radioButton.getId()));
                            answered = true;
                        }
                    });
                    count_options++;
                    Log.v("count_option", String.valueOf(count_options));
                    radioGroup.addView(radioButton);
                }
            }


            ((ViewGroup) findViewById(R.id.options_container)).addView(radioGroup);
        }
    }

    private void multipleChoice(final int num_options){

        multiple_choice = new ArrayList<>();
        for (int row = 0; row < 1; row++) {
            final LinearLayout checkbox_container = (LinearLayout) findViewById(R.id.options_container);

            for (int i = 0; i < num_options; i++) {
                final CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setId(Integer.parseInt(option_id[count_options]));
                checkBox.setText(options[count_options]);

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            int a = checkBox.getId();
                            if(multiple_choice.contains(a)){
                                int id = multiple_choice.indexOf(checkBox.getId());
                                multiple_choice.remove(id);
                                count--;
                                if(multiple_choice.size()>0){
                                    answered = true;
                                } else {
                                    answered = false;
                                }
                            } else {
                                multiple_choice.add(count, a);
                                //Toast.makeText(getApplicationContext(), String.valueOf(a), Toast.LENGTH_SHORT).show();
                                answered = true;
                                count++;
                            }
                        }
                });
                count_options++;
                checkbox_container.addView(checkBox);
            }
        }
    }

    /*private JSONArray makeJson() throws JSONException{

        JSONArray answersArray = new JSONArray();

        Iterator myVeryOwnIterator = uploadAnswers.keySet().iterator();
        while(myVeryOwnIterator.hasNext()) {
            String key=(String)myVeryOwnIterator.next();
            String value= (String) uploadAnswers.get(key).toString();
            JSONObject answers = new JSONObject();
            answers.put("questionId", key);
            answers.put("answerText", value);
            answersArray.put(answers);
        }

        return answersArray;
    }*/

    private void upload(){

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.POST_SURVEY,  new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("survey_id", survey_id);
                    params.put("student_id", student_id);

                    int count = 0;
                    Iterator myVeryOwnIterator = uploadAnswers.keySet().iterator();
                    while (myVeryOwnIterator.hasNext()){
                        String key = (String) myVeryOwnIterator.next();
                        ArrayList<String> ans =  uploadAnswers.get(key);

                        if(ans.size() == 1){
                            params.put("questionId"+count, key);
                            params.put("answerText"+count, ans.get(0));
                        } else if(ans.size() > 1){
                            int tmp = count;
                            for(int i = 0; i < ans.size(); i++){
                                params.put("questionId"+tmp, key);
                                params.put("answerText"+tmp, ans.get(i));
                                tmp++;
                            }

                            count = tmp-1;
                        }

                        count++;
                    }

                    params.put("totalQuestion", String.valueOf(count));

                    return params;
                }
            };

            requestQueue.add(stringRequest);
    }
}
