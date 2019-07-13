package quiz.android.bits.com.movieassignment.activities;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;

import quiz.android.bits.com.movieassignment.R;
import quiz.android.bits.com.movieassignment.adapters.MovieListAdapter;
import quiz.android.bits.com.movieassignment.services.MyService;

public class MovieListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ServiceConnection {

    private String TAG = MovieListActivity.class.getSimpleName();
    private String[] movieNamesList;
    private String[] moviePlotsList;
    private ListView movieList;

    public static String LIST_POSITION = "quiz.android.bits.com.movieassignment.list_position";
    public static String MOVIE_PLOT = "quiz.android.bits.com.movieassignment.movie_plot";
    public static String MOVIE_TITLE = "quiz.android.bits.com.movieassignment.movie_title";
    public static String MOVIE_IMAGE_URI = "quiz.android.bits.com.movieassignment.movie_poster_uri";

    private int screenOrientation;
    private boolean mBind;

    private AppBarLayout appBarLayout;
    private TextView titleView;
    private TextView plotView;
    private Bundle listItemBundle;

    private final String LAST_SELECTED_LIST_INDEX = "LAST_SELECTED_LIST_INDEX";
    private int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_activity);

        movieNamesList = getResources().getStringArray(R.array.movie_list_data_feed);
        moviePlotsList = this.getResources().getStringArray(R.array.movie_plots_data_feed);

        movieList = findViewById(R.id.list_view);
        MovieListAdapter listAdapter = new MovieListAdapter(this, movieNamesList);
        movieList.setAdapter(listAdapter);
        movieList.setOnItemClickListener(this);

        screenOrientation = this.getResources().getConfiguration().orientation;

        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt(LAST_SELECTED_LIST_INDEX);
        }
        updateOrientionViews(selectedIndex);


        //TODO: Test service codes from class
        //startMyService();

        //TODO: Pending Intent Example:
        /*Intent intent = new Intent(this, MovieInformationActivity.class);
        // Creating a pending intent and wrapping our intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            // Perform the operation associated with our pendingIntent
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }*/


    }

    private void updateOrientionViews(int lastSelectedIndex) {
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            appBarLayout = (AppBarLayout) findViewById(R.id.info_screen_appbar);
            titleView = (TextView) findViewById(R.id.movie_info_title_textview);
            plotView = (TextView) findViewById(R.id.movie_plot_textview);

            updateMovieDetails(lastSelectedIndex);
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick in t" + position);

        if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            this.startActivity(new Intent(getApplicationContext(), MovieInformationActivity.class)
                    .putExtras(getMovieItemDetails(position)));

        } else if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateMovieDetails(position);
        }
        selectedIndex = position;

        //TODO: Test service codes from class
        //stopMyService();

    }

    private Bundle getMovieItemDetails(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(LIST_POSITION, position);
        bundle.putString(MOVIE_TITLE, movieNamesList[position]);
        bundle.putString(MOVIE_PLOT, moviePlotsList[position]);
        bundle.putString(MOVIE_IMAGE_URI, getMovieIdFromName(movieNamesList[position]));
        return bundle;
    }

    private String getMovieIdFromName(String movieName) {
        movieName = movieName.replaceAll("[$&+,:;=?@#!<>.^*()%]", "");
        movieName = (movieName.replaceAll(" ", "_") + ".jpeg");
        return movieName.toLowerCase();
    }

    private void updateMovieDetails(int position) {
        try {
            appBarLayout.setBackground(Drawable.createFromStream(getAssets().open(getMovieIdFromName(movieNamesList[position])),
                    null));
        } catch (IOException e) {
            Log.e("MovieInfo", "IOException: Failed to load the movie image from asset");
            e.printStackTrace();
        }
        titleView.setText(movieNamesList[position]);
        plotView.setText(moviePlotsList[position]);
    }

    /* Test service codes from class */
    private void stopMyService() {
        this.stopService(new Intent(this, MyService.class));

        //this.unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        MyService serviceRef = ((MyService.LocalBinder) binder).getService();

        mBind = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LAST_SELECTED_LIST_INDEX, selectedIndex);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
