package com.example.individualstudy;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieActivity extends AppCompatActivity {
    private static String searchWord[] = {"avengers", "batman", "sonic", "pokemon", "dragon"};
    private static String API_URL_MOVIE = "https://www.omdbapi.com/?apikey=80e4c721&s=";

    GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.movie), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridLayout = findViewById(R.id.movieItem);

        updateGridLayoutColumns();

        Button btnAnime = findViewById(R.id.btn_anime);
        btnAnime.setOnClickListener(v -> {
            Intent intent = new Intent(MovieActivity.this, AnimeActivity.class);
            startActivity(intent);
        } );
        Button btnBook = findViewById(R.id.btn_books);
        btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(MovieActivity.this, BookActivity.class);
            startActivity(intent);
        } );
        Button btnShow = findViewById(R.id.btn_shows);
        btnShow.setOnClickListener(v -> {
            Intent intent = new Intent(MovieActivity.this, ShowActivity.class);
            startActivity(intent);
        } );
        Button btnMovie = findViewById(R.id.btn_movies);
        btnMovie.setOnClickListener(v -> {
            Intent intent = new Intent(MovieActivity.this, MainActivity.class);
            startActivity(intent);
        } );
        Button btnGame = findViewById(R.id.btn_games);
        btnGame.setOnClickListener(v -> {
            Intent intent = new Intent(MovieActivity.this, GameActivity.class);
            startActivity(intent);
        } );

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setClickable(true);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(MovieActivity.this, MainActivity.class);
            startActivity(intent);
        });

        APICall apiCall = new APICall(this);
        for(String search : searchWord) {
            apiCall.fetchMovieList(API_URL_MOVIE + search);
        }

        EditText txtSearch = findViewById(R.id.txt_search);
        Button btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(v -> {
            String search = txtSearch.getText().toString();

            if(search.equals("Search...")) return;
            gridLayout.removeAllViews();
            apiCall.fetchMovieList(API_URL_MOVIE + search);
        });
        Button btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(v -> {
            gridLayout.removeAllViews();
            txtSearch.setText("Search...");

            for(String search : searchWord) {
                apiCall.fetchMovieList(API_URL_MOVIE + search);
            }
        });
    }

    public void updateUIWithMovies(List<Movie> movieList) {
        runOnUiThread(() -> createItems(R.drawable.item_layout4, movieList));
    }
    void createItems(int drawableID, List<Movie> movieList){

        for (Movie movie : movieList) {
            LinearLayout customLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(230, 300);
            layoutParams.setMargins(0, 0, 20, 20);
            customLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            customLayout.setLayoutParams(layoutParams);
            customLayout.setOrientation(LinearLayout.VERTICAL);
            customLayout.setGravity(Gravity.CENTER);
            customLayout.setBackgroundResource(drawableID);

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(200, 200);
            imageParams.setMargins(0, 10, 0, 10);
            imageView.setLayoutParams(imageParams);
            loadImageFromUrl(movie.getImageUrl(), imageView);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(movie.getTitle());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);

            customLayout.addView(imageView);
            customLayout.addView(textView);

            customLayout.setClickable(true);
            customLayout.setOnClickListener(v -> itemClick(textView.getText().toString()));

            gridLayout.addView(customLayout);
        }
    }

    public void loadImageFromUrl(String imageUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView);
    }
    void itemClick(String text){
        Toast.makeText(MovieActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        updateGridLayoutColumns();
    }

    private void updateGridLayoutColumns() {
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayout.setColumnCount(4);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            int count = width/230;
            gridLayout.setColumnCount(count);
        }

        gridLayout.requestLayout();
    }
}
