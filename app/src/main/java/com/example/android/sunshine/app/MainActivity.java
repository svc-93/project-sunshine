package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ArrayAdapter<String> adapter;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Dummy data for ListView - "Day, Weather, high/low"
            String[] weatherForecastArray = {
                    "Today - Rainy - 37/11",
                    "Tomorrow - Foggy - 33/16",
                    "Sat - Sunny - 34/23",
                    "Sun - Sunny - 40/27",
                    "Mon - Cloudy - 34/21",
                    "Tue - Sunny - 42/24",
                    "Wed - Sunny - 38/29"
            };

            // Creating an ArrayList
            List<String> weeklyForecast = new ArrayList<String>(
                    Arrays.asList(weatherForecastArray)
            );

            /**
             *  Create a new ArrayAdapter -
             *  @params:
             *  Current context (fragment's parent activity),
             *  ID of list item layout,
             *  ID of textview to populate,
             *  String Data
             */
            adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item_forecast,R.id.list_item_forecast_textview,weeklyForecast);

            // Attach adapter to ListView
            ListView listView = (ListView) rootView.findViewById(R.id.list_view_forecast);
            listView.setAdapter(adapter);

            // Add networking code snippet here
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a String
            String forecastJson = null;

            try{
                // Construct the URL for query
                String baseUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?q=560024,india&mode=json&units=metric&cnt=7";
                String apikey = "&APPID=" + BuildConfig.OPEN_WEATHER_MAP_API_KEY;
                URL url = new URL(baseUrl.concat(apikey));

                // Create request to url and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read input stream
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null){
                    // nothing to do
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line=reader.readLine())!=null){
                    // pretty print json for debug logs
                    buffer.append(line + "\n");
                }

                if(buffer.length()==0){
                    // stream is empty, no use parsing
                    return null;
                }
                forecastJson = buffer.toString();
            } catch(IOException e) {
                Log.e("PlaceholderFragment", "Error", e);
                // if code didn't retrieve the weather data, no use parsing
                return null;
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    } catch (final IOException e){
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return rootView;
        }
    }
}
