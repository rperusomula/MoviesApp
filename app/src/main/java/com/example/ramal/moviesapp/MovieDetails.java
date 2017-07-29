package com.example.ramal.moviesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {

    /*@BindView(R.id.movieTitle) TextView movieTitle;
    @BindView(R.id.year) TextView year;
    @BindView(R.id.time) TextView time;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.movieDescription) TextView movieDescription;
    @BindView(R.id.movieImage)*/
    ImageView movieImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        TextView movieTitle = (TextView) findViewById(R.id.movieTitle);
        TextView year = (TextView) findViewById(R.id.year);
        TextView time = (TextView) findViewById(R.id.time);
        TextView movieDescription = (TextView) findViewById(R.id.movieDescription);
        TextView rating = (TextView) findViewById(R.id.rating);
        ImageView movieImage = (ImageView) findViewById(R.id.movieImage);
        String title = getIntent().getStringExtra("title");
        String yearString = getIntent().getStringExtra("year");
        String ratingString = getIntent().getStringExtra("rating");
        String movieDescriptionString = getIntent().getStringExtra("description");
        String posterpath = getIntent().getStringExtra("posterpath");
        String url = "http://image.tmdb.org/t/p/w185/"+posterpath;

        movieTitle.setText(title);
        year.setText(yearString);
        rating.setText(ratingString);
        movieDescription.setText(movieDescriptionString);
        time.setText("120min");
        Picasso.with(MovieDetails.this).load(url).into(movieImage);

    }
}
