package com.example.individualstudy;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APICall {
    private MainActivity activity;
    private AnimeActivity activity1;
    private BookActivity activity2;
    private GameActivity activity3;
    private MovieActivity activity4;
    private ShowActivity activity5;
    String title, imageUrl;
    BufferedReader reader;
    StringBuilder result;
    URL url;
    HttpURLConnection connection;
    JSONObject jsonResponse;

    public APICall(MainActivity activity) {
        this.activity = activity;
    }
    public APICall(AnimeActivity activity) {
        this.activity1 = activity;
    }
    public APICall(BookActivity activity) {
        this.activity2 = activity;
    }
    public APICall(GameActivity activity) {
        this.activity3 = activity;
    }
    public APICall(MovieActivity activity) {
        this.activity4 = activity;
    }
    public APICall(ShowActivity activity) {
        this.activity5 = activity;
    }

    public void fetchAnimeList(String apiUrl) {
        new FetchAnimeTask().execute(apiUrl);
    }

    public void fetchBookList(String apiUrl) {
        new FetchBookTask().execute(apiUrl);
    }

    public void fetchGameList(String apiUrl) {
        new FetchGameTask().execute(apiUrl);
    }

    public void fetchMovieList(String apiUrl) {
        new FetchMovieTask().execute(apiUrl);
    }

    public void fetchShowList(String apiUrl) {
        new FetchShowTask().execute(apiUrl);
    }

    private class FetchAnimeTask extends AsyncTask<String, Void, List<Anime>> {
        @Override
        protected List<Anime> doInBackground(String... urls) {
            List<Anime> animeList = new ArrayList<>();
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                    jsonResponse = new JSONObject(result.toString());
                    JSONArray animeArray = jsonResponse.getJSONArray("data");

                    for (int i = 0; i < animeArray.length(); i++) {
                        JSONObject animeObject = animeArray.getJSONObject(i);
                        title = animeObject.getString("title");
                        imageUrl = animeObject.getJSONObject("images").getJSONObject("jpg").getString("image_url");

                        animeList.add(new Anime(title, imageUrl));
                    }
                }
            } catch (Exception e) {
                Log.e("APICall", "Error fetching anime data", e);
            }
            return animeList;
        }

        @Override
        protected void onPostExecute(List<Anime> animeList) {
            if (animeList.isEmpty()) {
                Log.e("APICall", "No anime data received.");
                return;
            }
            if (activity != null) {
                activity.updateUIWithAnime(animeList);
            }
            if (activity1 != null) {
                activity1.updateUIWithAnime(animeList);
            }
        }
    }

    private class FetchBookTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... urls) {
            List<Book> bookList = new ArrayList<>();
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                    // Parse JSON
                    jsonResponse = new JSONObject(result.toString());
                    JSONArray bookArray = jsonResponse.getJSONArray("docs");

                    for (int i = 0; i < bookArray.length(); i++) {
                        JSONObject bookObject = bookArray.getJSONObject(i);
                        title = bookObject.getString("title");
                        imageUrl = bookObject.has("cover_i")
                                ? "https://covers.openlibrary.org/b/id/" + bookObject.getInt("cover_i") + "-L.jpg"
                                : "https://openlibrary.org/images/icons/avatar_book-sm.png";

                        bookList.add(new Book(title, imageUrl));
                    }
                }
            } catch (Exception e) {
                Log.e("APICall", "Error fetching book data", e);
            }
            return bookList;
        }

        @Override
        protected void onPostExecute(List<Book> bookList) {
            if (bookList.isEmpty()) {
                Log.e("APICall", "No book data received.");
                return;
            }
            if (activity != null) {
                activity.updateUIWithBooks(bookList);
            }
            else if(activity2 != null){
                activity2.updateUIWithBooks(bookList);
            }
        }
    }

    private class FetchGameTask extends AsyncTask<String, Void, List<Game>> {
        @Override
        protected List<Game> doInBackground(String... urls) {
            List<Game> gameList = new ArrayList<>();
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                    // Parse JSON
                    jsonResponse = new JSONObject(result.toString());
                    JSONArray gameArray = jsonResponse.getJSONArray("results");

                    for (int i = 0; i < gameArray.length(); i++) {
                        JSONObject gameObject = gameArray.getJSONObject(i);
                        title = gameObject.getString("name");
                        imageUrl = gameObject.getString("background_image");

                        gameList.add(new Game(title, imageUrl));
                    }
                }
            } catch (Exception e) {
                Log.e("APICall", "Error fetching game data", e);
            }
            return gameList;
        }

        @Override
        protected void onPostExecute(List<Game> gameList) {
            if (gameList.isEmpty()) {
                Log.e("APICall", "No game data received.");
                return;
            }
            if (activity != null) {
                activity.updateUIWithGames(gameList);
            }
            if(activity3 != null) {
                activity3.updateUIWithGames(gameList);
            }
        }
    }

    private class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... urls) {
            List<Movie> movieList = new ArrayList<>();
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                    jsonResponse = new JSONObject(result.toString());

                    if (jsonResponse.getString("Response").equals("True")) {
                        JSONArray moviesArray = jsonResponse.getJSONArray("Search");
                        for (int i = 0; i < moviesArray.length(); i++) {
                            JSONObject movieObject = moviesArray.getJSONObject(i);
                            title = movieObject.getString("Title");
                            imageUrl = movieObject.getString("Poster");

                            movieList.add(new Movie(title, imageUrl));
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("APICall", "Error fetching movie data", e);
            }
            return movieList;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            if (movieList.isEmpty()) {
                Log.e("APICall", "No movie data received.");
                return;
            }
            if (activity != null) {
                activity.updateUIWithMovies(movieList);
            }
            if(activity4 != null) {
                activity4.updateUIWithMovies(movieList);
            }
        }
    }

    private class FetchShowTask extends AsyncTask<String, Void, List<Show>> {
        @Override
        protected List<Show> doInBackground(String... urls) {
            List<Show> showList = new ArrayList<>();
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();

                    JSONArray jsonResponse = new JSONArray(result.toString());

                    for (int i = 0; i < jsonResponse.length(); i++) {
                        JSONObject showDataWrapper = jsonResponse.getJSONObject(i);
                        if (showDataWrapper.has("show")) {
                            JSONObject showData = showDataWrapper.getJSONObject("show");

                            title = showData.optString("name");
                            imageUrl = "";

                            if (showData.has("image") && !showData.isNull("image")) {
                                JSONObject imageData = showData.getJSONObject("image");
                                imageUrl = imageData.optString("medium", "");
                            }
                        }
                        else {
                            title = showDataWrapper.getString("name");
                            imageUrl = "";
                            if (showDataWrapper.has("image") && !showDataWrapper.isNull("image")) {
                                JSONObject imageData = showDataWrapper.getJSONObject("image");
                                imageUrl = imageData.getString("medium");
                            }
                        }
                        showList.add(new Show(title, imageUrl));
                    }
                }
            } catch (Exception e) {
                Log.e("APICall", "Error fetching show data", e);
            }
            return showList;
        }

        @Override
        protected void onPostExecute(List<Show> showList) {
            if (showList.isEmpty()) {
                Log.e("APICall", "No show data received.");
                return;
            }
            if (activity != null) {
                activity.updateUIWithShows(showList);
            }
            if(activity5 != null) {
                activity5.updateUIWithShows(showList);
            }
        }
    }
}
