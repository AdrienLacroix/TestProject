package com.src.adrien.testproject;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;

    MyListAdapter adapter;

    ProgressDialog progressDialog;

    private static final String loadUrl = "http://infolist.netne.net/testM.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        listView = (ListView) findViewById(R.id.list_view);
        adapter = new MyListAdapter(this);
        listView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(true);
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        loadData(false);
    }

    private void loadData(final boolean bRefresh) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this, loadUrl, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                if (!bRefresh) {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {

                    ArrayList<ListObject> arrayList = new ArrayList<ListObject>();
                    try {
                        JSONArray jsonArray = response.getJSONArray("rows");
                        for (int i=0; i<jsonArray.length(); i++) {
                            ListObject object = new ListObject();

                            object.title = jsonArray.getJSONObject(i).getString("title");
                            object.content = jsonArray.getJSONObject(i).getString("description");
                            object.imageUrl = jsonArray.getJSONObject(i).getString("imageHref");

                            arrayList.add(object);
                        }

                        adapter.setData(arrayList);
                    } catch (JSONException e) {}
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

                swipeRefreshLayout.setRefreshing(false);
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
    }
}
