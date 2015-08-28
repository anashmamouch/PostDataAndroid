package com.example.anas.postdata;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private Button button;

    /*url to get the json data*/
    private static String url = "http://wakeuppilot.herokuapp.com/players.json";

    /*Logs JSON Array*/
    private JSONArray games = null;

    /*JSON Node names*/
    private static String TAG_ID = "id";
    private static String TAG_BALL_TOUCHED = "ball_touched";
    private static String TAG_TOTAL_TOUCHES = "total_touches";
    private static String TAG_FIRST_TIME = "first_time";
    private static String TAG_PLAYER_ID = "player_id";
    private static String TAG_DATE = "created_at";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendGame().execute(url);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class SendGame extends AsyncTask<String, Void, String>{

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {

            //process the Search parameter string
            for(String sendUrl:params) {
                //try to fetch the data
                    try {
                        URL requestUrl = new URL(sendUrl);
                        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setRequestMethod("POST");

                        JSONObject data = new JSONObject();

                        data.put("username", "Anas");
                        data.put("age", "- 40 ans");
                        data.put("genre", "Homme");

                        //sending data & specifying the encoding utf-8
                        OutputStream os = connection.getOutputStream();
                        os.write(data.toString().getBytes("UTF-8"));
                        os.close();
                        //connection.connect();


                        //display what returns the POST request

                        StringBuilder sb = new StringBuilder();

                        int responseCode = connection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                sb.append(line + "\n");
                            }

                            br.close();

                            Log.d("BENZINO", "HTTP POST Response : " + sb.toString());

                        } else {

                            Log.d("BENZINO", "HTTP POST Response Message : " + connection.getResponseMessage());
                        }

                        Log.d("BENZINO", "HTTP Response Code: " + responseCode);

                    } catch (MalformedURLException e) {
                        Log.d("BENZINO", "Error processing URL", e);
                    } catch (IOException e) {
                        Log.d("BENZINO", "Error connecting to Host", e);
                    } catch (JSONException e) {
                        Log.d("BENZINO", "Error handling JSON Object", e);
                    }
                }

            return null;
        }
    }

}
