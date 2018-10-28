package edu.lawrence.hw_survey;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

public class SurveyPicker extends AppCompatActivity {
    public final static String Selected = "edu.lawrence.HW_Survey.Selected";
    private Survey[] Surveys = null;
    private Gson gson;
    private int selected_survey = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            selected_survey = savedInstanceState.getInt(this.Selected);
        } else {
            Intent intent = getIntent();
            selected_survey = -1;
        }
        setContentView(R.layout.activity_survey_picker);
        gson = new Gson();
        new HandlesTask().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
        arg0.putInt(this.Selected, selected_survey);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new HandlesTask().execute();
    }

    private class HandlesTask extends AsyncTask<String, Void, String> {
        private String uri;

        HandlesTask() {
            uri = "http://" + URIHandler.hostName + "/Survey/api/survey?key=IxYdz4g3lyyns47PzPPr5g==";
        }

        @Override
        protected String doInBackground(String... urls) {
            return URIHandler.doGet(uri, "");
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            loadSurveys(result);
        }
    }

    public void readSurvey(View view) {
        if (selected_survey != -1) {
            Survey s = Surveys[selected_survey];
            Intent intent = new Intent(this, SurveyReaderActivity.class);
            intent.putExtra(SurveyReaderActivity.Survey_ID, s.getIdsurvey());
            startActivity(intent);
        }
    }

    private void loadSurveys(String json) {
        Surveys = null;
        String[] surveyStrs = null;

        ListView surveyssList = (ListView) findViewById(R.id.survey_list);

        Surveys = gson.fromJson(json,Survey[].class);
        surveyStrs = new String[Surveys.length];
        for(int n = 0;n < surveyStrs.length;n++) {
            Survey s = Surveys[n];
            surveyStrs[n] = s.getTitle() + ":" + s.getPrompt();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, surveyStrs);
        surveyssList.setAdapter(adapter);

        surveyssList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int i, long l) {
                // remember the selection
                selected_survey = i;
            }
        });
    }
}
