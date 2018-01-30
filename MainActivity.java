package dp015507.reading.uk.ac.surveyapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static android.R.attr.type;

public class MainActivity extends Activity {

    private EditText email, password;
    public static final String FIRST_NAME = "FNAME";
    public static final String LAST_NAME = "LASTNAME";
    public static final String ID = "ID";
    public static final String AGE = "AGE";
    public static final String COURSE = "COURSE";
    public static final String GENDER = "GENDER";

    private String student_id;
    private String student_fname;
    private String student_sname;
    private String student_age;
    private String student_course;
    private String student_gender;


    public void setStudent_age(String student_age) {
        this.student_age = student_age;
    }

    public void setStudent_course(String student_course) {
        this.student_course = student_course;
    }

    public void setStudent_fname(String student_fname) {
        this.student_fname = student_fname;
    }

    public void setStudent_gender(String student_gender) {
        this.student_gender = student_gender;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public void setStudent_sname(String student_sname) {
        this.student_sname = student_sname;
    }

    public String getStudent_sname() {
        return student_sname;
    }

    public String getStudent_age() {
        return student_age;
    }

    public String getStudent_course() {
        return student_course;
    }

    public String getStudent_fname() {
        return student_fname;
    }

    public String getStudent_gender() {
        return student_gender;
    }

    public String getStudent_id() {
        return student_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView signup =(TextView) findViewById(R.id.btn_signup);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dp015507.webs.sse.reading.ac.uk/student_signUp1.php"));
                startActivity(browserIntent);
            }
        });

    }

    public void onLogin(View view) {

        final String emailString = email.getText().toString();
        String passwordString = password.getText().toString();
        String type = "login";

        class Login extends AsyncTask<String, Void, String> {

            Dialog loadingDailog;

            @Override
            protected String doInBackground(String... params) {

                String type = params[0];
                String logInURL = "http://dp015507.webs.sse.reading.ac.uk/app_student_login.php";


                if (type.equals("login")) {
                    try {
                        String email = params[1];
                        String password = params[2];
                        URL url = new URL(logInURL);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                        bufferedWriter.write(post_data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        outputStream.close();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                        String result = "";
                        String line = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            result += line;
                        }
                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                        return result;

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                loadingDailog = ProgressDialog.show(MainActivity.this, "Please Wait", "Loading.....");
            }

            @Override
            protected void onPostExecute(String result) {
                String s = result.trim();
                loadingDailog.dismiss();
                if(s.equalsIgnoreCase("true")){
                    getData();
                    }else if(s.equalsIgnoreCase("notVerified")){
                    Toast.makeText(getApplicationContext(), R.string.userNotVerified, Toast.LENGTH_LONG).show();
                    } else if(s.equalsIgnoreCase("false")){
                    Toast.makeText(getApplicationContext(), R.string.incorrect_user_name_password, Toast.LENGTH_LONG).show();
                    }
                }

            }

        Login login = new Login();
        login.execute(type, emailString, passwordString);

    }

    private void getData() {

        final String emailString = email.getText().toString();
        String url = Config.DATA_URL+emailString;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
                Log.v("response", response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject studentData = result.getJSONObject(0);
            setStudent_id(studentData.getString(Config.STUDENT_ID));
            setStudent_fname(studentData.getString(Config.FIRST_NAME));
            setStudent_sname(studentData.getString(Config.LAST_NAME));
            setStudent_age(studentData.getString(Config.AGE));
            setStudent_course(studentData.getString(Config.COURSE));
            setStudent_gender(studentData.getString(Config.GENDER));

            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            intent.putExtra(ID, getStudent_id());
            intent.putExtra(FIRST_NAME, getStudent_fname());
            intent.putExtra(LAST_NAME, getStudent_sname());
            intent.putExtra(AGE, getStudent_age());
            intent.putExtra(COURSE, getStudent_course());
            intent.putExtra(GENDER, getStudent_gender());
            finish();
            startActivity(intent);

        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
