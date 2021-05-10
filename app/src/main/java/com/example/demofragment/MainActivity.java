package com.example.demofragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.demofragment.storage.Repo;
import com.example.demofragment.storage.RepoStorage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements UpdateFragment {

    private Context mContext;
    public static FragmentManager mFragmentManager;
    List<Repo> repoList;
    public static BottomNavigationView mBottomNavigationView;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        repoList = new ArrayList<>();

        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        new GetAllocationAsync().execute();

        mFragmentManager = getSupportFragmentManager();
        setupNavigationView(mBottomNavigationView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setupNavigationView(final BottomNavigationView navigationView) {

        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_repo:
                    getSupportActionBar().setTitle(
                            Html.fromHtml("<font color='#515151'>Repo</font>"));
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_container, new RepoFragment()).commit();
                    break;
                case R.id.action_description:
                    getSupportActionBar().setTitle(
                            Html.fromHtml("<font color='#515151'>Description</font>"));
                    FragmentTransaction fragmentTransaction_one = mFragmentManager.beginTransaction();
                    fragmentTransaction_one.replace(R.id.frame_container, new DescriptionFragment(0)).commit();
                    break;

                case R.id.action_details:
                    getSupportActionBar().setTitle(
                            Html.fromHtml("<font color='#515151'>Details</font>"));
                    FragmentTransaction fragmentTransaction_two = mFragmentManager.beginTransaction();
                    fragmentTransaction_two.replace(R.id.frame_container, new DetailsFragment(0)).commit();
                    break;

            }
            return true;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void update(int currentTab, int repoId) {
        setCurrentTab(currentTab, repoId);
    }

    private void setCurrentTab(int currentTab, int repoId) {
        try {
            if (currentTab == 1) {
                FragmentTransaction fragmentTransaction_one = mFragmentManager.beginTransaction();
                fragmentTransaction_one.replace(R.id.frame_container, new DescriptionFragment(repoId)).commit();
            } else {
                FragmentTransaction fragmentTransaction_one = mFragmentManager.beginTransaction();
                fragmentTransaction_one.replace(R.id.frame_container, new DetailsFragment(repoId)).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetAllocationAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog = null;
        int mLoginError = 1;
        private int mAllocationError = 1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Downloading Allocations...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @SuppressLint("MissingPermission")
        @Override
        protected Void doInBackground(Void... params) {

            // Get Method
            try {
                DefaultHttpClient httpclientMaster = new DefaultHttpClient();
                HttpGet httpgetMaster = new HttpGet(getResources().getString(
                        R.string.url));

                httpgetMaster.addHeader("Accept", "application/json");
               /* httpgetMaster.addHeader("accept-encoding", "gzip, deflate");
                httpgetMaster.addHeader("accept-language", "en-US,en;q=0.8");
                httpgetMaster.addHeader("user-agent", "application/json");
                httpgetMaster.addHeader("Accept", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
*/
                HttpResponse httpresponseMaster = httpclientMaster
                        .execute(httpgetMaster);

                if (httpresponseMaster.getStatusLine().getStatusCode() == 401) {
                    mLoginError = -2;
                } else {
                    String responseMaster = EntityUtils
                            .toString(httpresponseMaster.getEntity());

                    JSONArray jsonArray = new JSONArray(responseMaster);

                    for (int count = 0; count < jsonArray.length(); count++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(count);
                        JSONObject jsonObject1 = jsonArray.getJSONObject(count).getJSONObject("owner");
                        Repo repo = new Repo();
                        repo.RepoId = jsonObject.getInt("id");
                        repo.NodeId = jsonObject.getString("node_id");
                        repo.Name = jsonObject.getString("name");
                        repo.FullName = jsonObject.getString("full_name");

                        repo.Type = jsonObject1.getString("type");
                        repo.AvatarURL = jsonObject1.getString("avatar_url");
                        repo.Description = "";
                        repoList.add(repo);
                    }
                }
            } catch (Exception e) {
                boolean mConnect = checkConnection();
                if (mConnect) {
                    mLoginError = -1;
                } else {
                    mLoginError = 0;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mProgressDialog.dismiss();

            if (mLoginError == 0) {
                Toast.makeText(mContext,
                        "Internet unavailable. Please try again later.",
                        Toast.LENGTH_LONG).show();
            } else if (mLoginError == -1) {
                Toast.makeText(mContext,
                        "Server unreachable. Please try again later.",
                        Toast.LENGTH_LONG).show();
            } else {
                RepoStorage repoStorage = RepoStorage.getInstance(mContext);
                repoStorage.insertRepo(repoList);
            }
        }

        public boolean checkConnection() {
            boolean success = false;
            try {
                URL url = new URL("https://google.com");
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setConnectTimeout(10000);
                connection.connect();
                success = connection.getResponseCode() == 200;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return success;
        }
    }
}