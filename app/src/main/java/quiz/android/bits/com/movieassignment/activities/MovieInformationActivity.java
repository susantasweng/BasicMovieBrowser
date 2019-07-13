package quiz.android.bits.com.movieassignment.activities;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import quiz.android.bits.com.movieassignment.R;

public class MovieInformationActivity extends AppCompatActivity {

    String[] movies = new String[] {"sholey", "hera_pheri", "avengers", "3_diots", "dangal",
            "deewaar", "mother_india","lagaan", "black", "barfi",
            "forrest_gump", "harry_potter", "titanic", "star_trek_beyond", "frozen", "man_on_fire", "matrix"};

    private AppBarLayout appBarLayout;
    private ImageView movieBackground;
    private TextView titleView;
    private TextView plotView;
    private Bundle listItemBundle;
    private int screenOrientation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_info_activity);
        screenOrientation = this.getResources().getConfiguration().orientation;

        titleView = (TextView)findViewById(R.id.movie_info_title_textview);
        plotView = (TextView)findViewById(R.id.movie_plot_textview);

        updateGeneralViews();

        if(screenOrientation == Configuration.ORIENTATION_PORTRAIT) {
            appBarLayout = (AppBarLayout)findViewById(R.id.info_screen_appbar);
            updatePortraitViews();
        } else {
            movieBackground = (ImageView) findViewById(R.id.movie_image);
            updateLandscapeView();
        }
    }

    private void updateGeneralViews() {
        titleView.setText(listItemBundle.getString(MovieListActivity.MOVIE_TITLE));
        plotView.setText(listItemBundle.getString(MovieListActivity.MOVIE_PLOT));
    }

    private void updatePortraitViews() {
        listItemBundle = getIntent().getExtras();
        try {
            appBarLayout.setBackground(Drawable.createFromStream(getAssets().open(listItemBundle.getString(
                    MovieListActivity.MOVIE_IMAGE_URI)), null));
        } catch (IOException e) {
            Log.e("MovieInfo", "IOException: Failed to load the movie image from asset");
            e.printStackTrace();
        }

        CoordinatorLayout.LayoutParams appBarLayoutParams = (CoordinatorLayout.LayoutParams)appBarLayout.getLayoutParams();
        AppBarLayout.Behavior layoutBehaviour = new AppBarLayout.Behavior();
        appBarLayoutParams.setBehavior(layoutBehaviour);
        layoutBehaviour.setDragCallback(new AppBarLayout.Behavior.BaseDragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return false;
            }
        });
    }

    private void updateLandscapeView() {
        try {
            movieBackground.setImageDrawable(Drawable.createFromStream(getAssets().open(listItemBundle.getString(
                    MovieListActivity.MOVIE_IMAGE_URI)), null));
        } catch (IOException e) {
            Log.e("MovieInfo", "IOException: Failed to load the movie image from asset");
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
