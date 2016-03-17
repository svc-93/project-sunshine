package com.example.android.sunshine.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

            return rootView;
        }
    }
}
