package com.example.testindrajit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.testindrajit.adapter.CardAdapter;
import com.example.testindrajit.internet.CheckConnectivity;
import com.example.testindrajit.model.MovieModel;
import com.example.testindrajit.utils.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvMovie;
    private CardAdapter cardAdapter;
    private ArrayList<MovieModel> movieModelArrayList = new ArrayList<>();
    String searchkey = "home";
    ImageView btnSearch;
    EditText etSearch;
    String moviename, year, Released, Runtime, Genre, Director, Writer, Actors, Plot, Language, Country, Awards, Poster, imdbRating, collection, vote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvMovie = findViewById(R.id.rvMovie);
        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);

        cardAdapter = new CardAdapter(this, movieModelArrayList);
        rvMovie.setAdapter(cardAdapter);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rvMovie.setLayoutManager(manager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.photos_list_spacing);
        rvMovie.addItemDecoration(itemDecoration);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etSearch.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "Enter Valid Search Keyword!", Toast.LENGTH_SHORT).show();
                } else {

                    searchkey = etSearch.getText().toString();
                    movielist();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                }
            }
        });

        movielist();
    }


    public void movielist() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, "https://www.omdbapi.com/?s=" + searchkey + "&apikey=cbddbe07", null, response -> {
                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    JSONArray response_data = result.getJSONArray("Search");
                    movieModelArrayList.clear();
                    for (int i = 0; i < response_data.length(); i++) {

                        MovieModel movieModel = new MovieModel();
                        JSONObject responseobj = response_data.getJSONObject(i);
                        movieModel.setImdbid(responseobj.getString("imdbID"));
                        movieModel.setPoster(responseobj.getString("Poster"));
                        movieModel.setTitle(responseobj.getString("Title"));
                        movieModel.setType(responseobj.getString("Type"));
                        movieModel.setYear(responseobj.getString("Year"));
                        movieModelArrayList.add(movieModel);

                    }
                    cardAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);

        } else {

            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

        }
    }


    public void ViewMovies(MovieModel movieModel) {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            showProgressDialog();
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, "http://www.omdbapi.com/?i=" + movieModel.getImdbid() + "&apikey=cbddbe07", null, response -> {
                Log.i("Response-->", String.valueOf(response));
                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    moviename = result.getString("Title");
                    year = result.getString("Year");
                    Released = result.getString("Released");
                    Runtime = result.getString("Runtime");
                    Genre = result.getString("Genre");
                    Director = result.getString("Director");
                    Writer = result.getString("Writer");
                    Actors = result.getString("Actors");
                    Plot = result.getString("Plot");
                    Language = result.getString("Language");
                    Country = result.getString("Country");
                    Awards = result.getString("Awards");
                    Poster = result.getString("Poster");
                    imdbRating = result.getString("imdbRating");
                    collection = result.getString("BoxOffice");
                    vote = result.getString("imdbVotes");

                    showDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);

        } else {

            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

        }


    }

    public void showDialog() {

        final TextView tvMoviename, tvYear, tvGenre, tvDuration, tvIMDBRating, tvPlot,
                tvDirector, tvWriters, tvActors, tvAwards, tvLang, tvCountry, tvReleased, tvAmount, tvVotes;

        ImageView btn_back, imgBanner;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.movie_details);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        tvMoviename = (TextView) dialog.findViewById(R.id.tvMoviename);
        tvYear = (TextView) dialog.findViewById(R.id.tvYear);
        tvGenre = (TextView) dialog.findViewById(R.id.tvGenre);
        tvDuration = (TextView) dialog.findViewById(R.id.tvDuration);
        tvIMDBRating = (TextView) dialog.findViewById(R.id.tvIMDBRating);
        tvPlot = (TextView) dialog.findViewById(R.id.tvPlot);
        tvDirector = (TextView) dialog.findViewById(R.id.tvDirector);
        tvWriters = (TextView) dialog.findViewById(R.id.tvWriters);
        tvActors = (TextView) dialog.findViewById(R.id.tvActors);
        tvAwards = (TextView) dialog.findViewById(R.id.tvAwards);
        tvLang = (TextView) dialog.findViewById(R.id.tvLang);
        tvCountry = (TextView) dialog.findViewById(R.id.tvCountry);
        tvReleased = (TextView) dialog.findViewById(R.id.tvReleased);
        btn_back = (ImageView) dialog.findViewById(R.id.btn_back);
        imgBanner = (ImageView) dialog.findViewById(R.id.imgBanner);
        tvAmount = (TextView) dialog.findViewById(R.id.tvAmount);
        tvVotes = (TextView) dialog.findViewById(R.id.tvVotes);

        tvMoviename.setText(moviename);
        tvYear.setText(year);
        tvGenre.setText(Genre);
        tvDuration.setText(Runtime);
        tvIMDBRating.setText(imdbRating);
        tvPlot.setText(Plot);
        tvDirector.setText(Director);
        tvWriters.setText(Writer);
        tvActors.setText(Actors);
        tvAwards.setText(Awards);
        tvLang.setText(Language);
        tvCountry.setText(Country);
        tvReleased.setText(Released);
        tvVotes.setText(vote);
        tvAmount.setText(collection);

        Glide.with(MainActivity.this)
                .load(Poster)
                .placeholder(R.drawable.pic2)
                .into(imgBanner);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

    }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}