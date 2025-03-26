package com.example.individualstudy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.view.*;
import android.widget.*;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private static final String API_URL_ANIME = "https://api.jikan.moe/v4/top/anime";
    private static final String API_URL_BOOK = "https://openlibrary.org/search.json?q=best+books";
    private static final String API_URL_GAME = "https://api.rawg.io/api/games?key=e018c14a780f4c7b9030273b7221254b";
    private static String API_URL_MOVIE  = "https://www.omdbapi.com/?s=pokemon&apikey=80e4c721";
    private static final String API_URL_SHOW = "https://api.tvmaze.com/shows";

    private static final String API_URL_ANIME_SEARCH = "https://api.jikan.moe/v4/anime?q=";
    private static final String API_URL_BOOK_SEARCH = "https://openlibrary.org/search.json?q=";
    private static final String API_URL_GAME_SEARCH = "https://api.rawg.io/api/games?key=e018c14a780f4c7b9030273b7221254b&search=";
    private static String API_URL_MOVIE_SEARCH  = "https://www.omdbapi.com/?apikey=80e4c721&s=";
    private static final String API_URL_SHOW_SEARCH = "https://api.tvmaze.com/search/shows?q=";

    private APICall apiCall;
    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnAnime = findViewById(R.id.btn_anime);
        btnAnime.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AnimeActivity.class);
            startActivity(intent);
        } );
        Button btnBook = findViewById(R.id.btn_books);
        btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookActivity.class);
            startActivity(intent);
        } );
        Button btnShow = findViewById(R.id.btn_shows);
        btnShow.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShowActivity.class);
            startActivity(intent);
        } );
        Button btnMovie = findViewById(R.id.btn_movies);
        btnMovie.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            startActivity(intent);
        } );
        Button btnGame = findViewById(R.id.btn_games);
        btnGame.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        } );

        apiCall = new APICall(this);
        executorService = Executors.newFixedThreadPool(5);

        fetchData();

        EditText txtSearch = findViewById(R.id.txt_search);
        Button btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(v -> {
            String search = txtSearch.getText().toString();
            if (search.equals("Search...")) return;

            fetchSearchData(search);
        });

        Button btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(v -> {
            txtSearch.setText("Search...");
            fetchData();
        });
    }

    private void fetchData() {
        executorService.execute(() -> apiCall.fetchAnimeList(API_URL_ANIME));
        executorService.execute(() -> apiCall.fetchBookList(API_URL_BOOK));
        executorService.execute(() -> apiCall.fetchGameList(API_URL_GAME));
        executorService.execute(() -> apiCall.fetchMovieList(API_URL_MOVIE));
        executorService.execute(() -> apiCall.fetchShowList(API_URL_SHOW));
    }

    private void fetchSearchData(String search) {
        executorService.execute(() -> apiCall.fetchAnimeList(API_URL_ANIME_SEARCH + search));
        executorService.execute(() -> apiCall.fetchBookList(API_URL_BOOK_SEARCH + search));
        executorService.execute(() -> apiCall.fetchGameList(API_URL_GAME_SEARCH + search));
        executorService.execute(() -> apiCall.fetchMovieList(API_URL_MOVIE_SEARCH + search));
        executorService.execute(() -> apiCall.fetchShowList(API_URL_SHOW_SEARCH + search));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    public void updateUIWithAnime(List<Anime> animeList) {
        runOnUiThread(() -> createItems(R.id.animeItem, R.drawable.item_layout1, animeList));
    }

    public void updateUIWithBooks(List<Book> bookList) {
        runOnUiThread(() -> createItems(R.id.bookItem, R.drawable.item_layout2, bookList));
    }

    public void updateUIWithGames(List<Game> gameList) {
        runOnUiThread(() -> createItems(R.id.gameItem, R.drawable.item_layout5, gameList));
    }

    public void updateUIWithMovies(List<Movie> movieList) {
        runOnUiThread(() -> createItems(R.id.movieItem, R.drawable.item_layout4, movieList));
    }

    public void updateUIWithShows(List<Show> showList) {
        runOnUiThread(() -> createItems(R.id.showItem, R.drawable.item_layout3, showList));
    }

    void createItems(int layoutID, int drawableID, List<? extends MediaItem> itemList) {
        LinearLayout linearLayout = findViewById(layoutID);
        linearLayout.removeAllViews();

        int i = 0;
        for (MediaItem item : itemList) {
            LinearLayout customLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(210, 280);
            layoutParams.setMargins(0, 0, 16, 0);
            customLayout.setLayoutParams(layoutParams);
            customLayout.setOrientation(LinearLayout.VERTICAL);
            customLayout.setGravity(Gravity.CENTER);
            customLayout.setBackgroundResource(drawableID);

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(180, 180);
            imageParams.setMargins(0, 10, 0, 10);
            imageView.setLayoutParams(imageParams);

            loadImageFromUrl(item.getImageUrl(), imageView);

            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(item.getTitle());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);

            customLayout.addView(imageView);
            customLayout.addView(textView);

            customLayout.setClickable(true);
            customLayout.setOnClickListener(v -> itemClick(item.getTitle()));

            linearLayout.addView(customLayout);
            i++;
            if(i == 20) break;
        }
    }

    public void loadImageFromUrl(String imageUrl, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView);
    }

    void itemClick(String text){
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}