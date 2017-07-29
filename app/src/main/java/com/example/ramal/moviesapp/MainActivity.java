package com.example.ramal.moviesapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView gridView ;
    String item="popularity" ;
    ProgressDialog progressDialog;
    MyAsyncTask myAsyncTask;
    String TAG = "position";
    JSONArray jsonArray;
    JSONObject objectJSON;
    //JSONObject objectJSON1;
    ConnectionChecker cc ;
    ArrayList<String> movieTitle = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridView);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Wait until data is loaded Please and ThankYou ");
        cc = new ConnectionChecker(this);
        myAsyncTask = new MyAsyncTask();
        if(cc.isConnected()) {
            myAsyncTask.execute("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=");
        }
        else
        {
            Toast.makeText(MainActivity.this,"No Internet Please try again",Toast.LENGTH_SHORT).show();
            alert();
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,MovieDetails.class);
                try {
                     intent.putExtra("title",jsonArray.getJSONObject(position).getString("title"));
                    intent.putExtra("posterpath",jsonArray.getJSONObject(position).getString("poster_path"));
                    intent.putExtra("year",jsonArray.getJSONObject(position).getString("release_date"));
                    intent.putExtra("rating",jsonArray.getJSONObject(position).getString("vote_average"));
                    //intent.putExtra("time",jsonArray.getJSONObject(position).getString("title"));
                    intent.putExtra("description",jsonArray.getJSONObject(position).getString("overview"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

               // intent.putExtra("title",movieTitle.get(position));
                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        myAsyncTask = new MyAsyncTask();
        if(item.getItemId()==R.id.popular)
        {
            if(cc.isConnected()) {
                myAsyncTask.execute("https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=");
            }
            else
            {
                Toast.makeText(MainActivity.this,"No Internet Please try again",Toast.LENGTH_SHORT).show();
                alert();
            }
        }
        else
        {
            //myAsyncTask.execute("https://api.themoviedb.org/3/discover/movie?sort_by=rating.desc&api_key=");
            if(cc.isConnected()) {
                myAsyncTask.execute("https://api.themoviedb.org/3/discover/movie?sort_by=rating.desc&api_key=");
            }
            else
            {
                Toast.makeText(MainActivity.this,"No Internet Please try again",Toast.LENGTH_SHORT).show();
                alert();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void alert()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Please Connect to Internet");
        dialog.setMessage("Go to Menu and press Popular or Rating to load movies" );
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //Action for "Delete".
            }
        })
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Action for "Cancel".
                    }
                });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    class MyAsyncTask extends AsyncTask<String,Void,JSONObject> {



        ArrayList<String> urlList = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String result;
                String nextline;

                while ((nextline = bufferedReader.readLine()) != null) {
                    stringBuilder.append(nextline);
                }
                result = stringBuilder.toString();
                jsonObject = new JSONObject(result);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(final JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            progressDialog.dismiss();


            try {
                jsonArray = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*try {
                jsonArray = jsonObject.getJSONArray("results");

                //Log.d("arraylength","length"+jsonArray.length());
                for(int i = 0;i<jsonArray.length();i++)
                {
                    objectJSON1 = jsonArray.getJSONObject(i);
                    String movietitle =objectJSON1.getString("title");
                    String urllist =objectJSON1.getString("poster_path");
                    String url = "http://image.tmdb.org/t/p/w185/"+urllist;
                    movieTitle.add(movietitle);
                    urlList.add(url);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
*/
            /*ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this,R.layout.frame_movie,R.id.textView2, movieTitle);
            gridView.setAdapter(arrayAdapter);*/
            final ArrayList<String> moviesList = new ArrayList<>();
            final ArrayList<String> urlList1 = new ArrayList<>();
            class MyAdapter extends BaseAdapter
            {
                @Override
                public int getCount()
                {
                    //objectJSON = jsonArray.getJSONObject();
                   // Log.d("arraylength","length"+jsonArray.length());
                    return jsonArray.length();

                }

                @Override
                public Object getItem(int position) {
                    Object a = null;
                    try {
                        a = jsonArray.getJSONObject(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return a;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }
                //String url=null;
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    convertView= getLayoutInflater().inflate(R.layout.frame_movie,null);
                    String urllist=null;
                    String title=null;
                    ImageView imageView = (ImageView) convertView.findViewById(R.id.movieImage);
                    TextView textView = (TextView) convertView.findViewById(R.id.textView2);
                    try {
                        objectJSON = jsonArray.getJSONObject(position);
                        title =objectJSON.getString("title");
                        urllist =objectJSON.getString("poster_path");
                        Log.d("poster","poster"+urllist);
                        //moviesList.add(title);
                        //urlList1.add(urllist);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://image.tmdb.org/t/p/w185/"+urllist;
                    Log.d(TAG,"position"+position);

                    Picasso.with(MainActivity.this).load(url).into(imageView);
                    textView.setText(title);
                    return convertView;
                }
            }

            MyAdapter myAdpater = new MyAdapter();
            gridView.setAdapter(myAdpater);


        }
    }
}
