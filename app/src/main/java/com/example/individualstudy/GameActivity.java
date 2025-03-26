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

public class GameActivity extends AppCompatActivity {
    private static final String API_URL_GAME = "https://api.rawg.io/api/games?key=e018c14a780f4c7b9030273b7221254b";
    private static final String API_URL_GAME_SEARCH = "https://api.rawg.io/api/games?key=e018c14a780f4c7b9030273b7221254b&search=";

    GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gridLayout = findViewById(R.id.gameItem);

        updateGridLayoutColumns();

        Button btnAnime = findViewById(R.id.btn_anime);
        btnAnime.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, AnimeActivity.class);
            startActivity(intent);
        } );
        Button btnBook = findViewById(R.id.btn_books);
        btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, BookActivity.class);
            startActivity(intent);
        } );
        Button btnShow = findViewById(R.id.btn_shows);
        btnShow.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, ShowActivity.class);
            startActivity(intent);
        } );
        Button btnMovie = findViewById(R.id.btn_movies);
        btnMovie.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MovieActivity.class);
            startActivity(intent);
        } );
        Button btnGame = findViewById(R.id.btn_games);
        btnGame.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
        } );

        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setClickable(true);
        backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
        });

        APICall apiCall = new APICall(this);
        apiCall.fetchGameList(API_URL_GAME);

        EditText txtSearch = findViewById(R.id.txt_search);
        Button btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(v -> {
            String search = txtSearch.getText().toString();

            if(search.equals("Search...")) return;
            apiCall.fetchGameList(API_URL_GAME_SEARCH + search);
        });
        Button btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(v -> {
            txtSearch.setText("Search...");

            apiCall.fetchGameList(API_URL_GAME);
        });
    }

    public void updateUIWithGames(List<Game> gameList) {
        runOnUiThread(() -> createItems(R.drawable.item_layout5, gameList));
    }

    void createItems(int drawableID, List<Game> gameList){
        gridLayout.removeAllViews();

        for (Game game : gameList) {
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
            loadImageFromUrl(game.getImageUrl(), imageView);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(game.getTitle());
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
        Toast.makeText(GameActivity.this, text, Toast.LENGTH_SHORT).show();
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
