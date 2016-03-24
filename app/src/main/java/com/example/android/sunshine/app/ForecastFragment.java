package com.example.android.sunshine.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

/**
 * A simple view for forecasting a week's weather
 * Created by chockali on 3/21/2016.
 */
public class ForecastFragment extends Fragment {
    private ArrayAdapter<String> adapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Fragment method to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            new FetchWeatherTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<Void, Void, Void>{

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params){
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
                Log.v(LOG_TAG, forecastJson);
            } catch(IOException e) {
                Log.e(LOG_TAG, "Error", e);
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
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}
