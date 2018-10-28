package edu.lawrence.hw_survey;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class SurveyReaderActivity extends AppCompatActivity {
    private Gson gson;
    public final static String Survey_ID = "edu.lawrence.HW_Survey.Survey_ID";
    public final static String Question_ID = "edu.lawrence.HW_Survey.Question_ID";
    private int surveyid;
    private Question [] questions = null;
    private int qNo;
    private Question q = null;
    private Response [] responses = null;
    private TextView qprompt;
    private ListView anschoices;
    private EditText anstext;
    private int choice = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            surveyid = savedInstanceState.getInt(this.Survey_ID);
            qNo = savedInstanceState.getInt(this.Question_ID);
        } else {
            Intent intent = getIntent();
            surveyid = intent.getIntExtra(this.Survey_ID,-1);
            qNo = intent.getIntExtra(this.Question_ID, 0);
        }
        setContentView(R.layout.activity_survey_reader);
        gson = new Gson();
        new SurveyTask().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
        arg0.putInt(this.Survey_ID, surveyid);
        arg0.putInt(this.Question_ID, qNo);
    }

    private class SurveyTask extends AsyncTask<String, Void, String> {
        private String uri;

        SurveyTask() {
            uri = "http://" + URIHandler.hostName + "/Survey/api/question?survey="+surveyid;
        }

        @Override
        protected String doInBackground(String... urls) {
            return URIHandler.doGet(uri, "");
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            loadQuestions(result);
        }
    }

    private void loadQuestions(String json) {
        questions = gson.fromJson(json,Question[].class);
        q = questions[qNo];

        qprompt = (TextView) this.findViewById(R.id.question_prompt);
        anschoices = (ListView) this.findViewById(R.id.ans_choices);
        anstext = (EditText) this.findViewById(R.id.ans_text);
        String questiontext = q.getQuestion();
        qprompt.setText(questiontext);

        String allResponses = q.getResponses();
        String list[] = allResponses.split(", ");

        if(allResponses.equals("")){
            anschoices.setVisibility(View.GONE);
            anstext.setVisibility(View.VISIBLE);
        }else{
            anstext.setVisibility(View.GONE);
            anschoices.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list);
            anschoices.setAdapter(adapter);

            anschoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    choice = i;
                }
            });
        }
    }

    private Response setupResponse(int id, String repsonse){
        Response r = new Response();
        r.setQuestion(id);
        r.setSurvey(surveyid);
        r.setResponse(repsonse);
        return r;
    }

    public void doquiz(View view) {
        String allResponses = q.getResponses();
        String list[] = allResponses.split(", ");
        String resp;
        if (allResponses.equals("")) {
            resp = anstext.getText().toString();
        } else {
            resp = list[choice];
        }
        int id = q.getIdquestion();
        Response r = setupResponse(id, resp);
        new NewResponseTask(r).execute();
        qNo ++ ;

        if (qNo > questions.length  - 1){
            Intent intent = new Intent(this, SurveyPicker.class);
            intent.putExtra(SurveyPicker.Selected, -1);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, SurveyReaderActivity.class);
            intent.putExtra(SurveyReaderActivity.Survey_ID, surveyid);
            intent.putExtra(SurveyReaderActivity.Question_ID, qNo);
            startActivity(intent);
        }
    }

    // Task to post a new Response
    private class NewResponseTask extends AsyncTask<String, Void, String> {
        private String uri;
        private String toSend;
        NewResponseTask(Response r) {
            uri = "http://" + URIHandler.hostName + "/Survey/api/response?key=IxYdz4g3lyyns47PzPPr5g==";
            this.toSend = gson.toJson(r);
        }

        @Override
        protected String doInBackground(String... urls) {
            return URIHandler.doPost(uri, toSend);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String messageId) {

        }
    }
}
