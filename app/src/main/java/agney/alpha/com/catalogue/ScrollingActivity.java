package agney.alpha.com.catalogue;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONObject;

import java.util.List;

public class ScrollingActivity extends AppCompatActivity {

    static String message;
    RotateLoading rotateLoading;
    List<Attributes> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.scrolling_activity);
        final SwipeRefreshLayout swipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        Intent intent = getIntent();
        if(intent!=null) {
            message = intent.getStringExtra("message");
        }

        Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internet Connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY",new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                       generateJSON(makeurl(message));
                    }
                });
        if (!checkConnection(this)) {

            snackbar.show();
        }


        generateJSON(makeurl(message));
        rotateLoading = (RotateLoading) findViewById(R.id.rotateLoading);
        rotateLoading.start();

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                generateJSON(message);
                swipeToRefresh.setRefreshing(false);

            }
        });

    }
    @Override
    public void onBackPressed(){
        list.clear();
        finish();
        super.onBackPressed();
    }


    public String makeurl(String input) {

        String url_first = "http://api.pricecheckindia.com/feed/product/";
        String url_last = ".json?user=agneymen&key=MXLRSOICZLBIAXZV";
        String url = url_first+input+url_last;
        return url;

    }
    public void generateJSON(String url) {

        String tag_json_obj = "json_obj_req";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            populateList(response);
                            VolleyLog.d("My Activity","Response:"+response.toString() );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        VolleyLog.v("My Activity", "Error" + error.getMessage());

                    }
                });
        AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
    }
    void populateList(JSONObject response) {

        list = Attributes.forList(response);
        if(list.isEmpty()) {
            Toast.makeText(this,"Sorry! No Products found in this category.",Toast.LENGTH_SHORT).show();
        }
        rotateLoading.stop();
        ItemAdapter adapter = new ItemAdapter(list);
        RecyclerView rv = (RecyclerView) findViewById(R.id.listView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Intent intent = new Intent(ScrollingActivity.this, DisplayActivity.class);
                        String item = list.get(position).getTitle();
                        intent.putExtra("item", message + "/" + item);
                        startActivity(intent);
                    }
                })
        );
    }
    public boolean checkConnection(Context context) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return ((activeNetwork!=null)&&(activeNetwork.isConnectedOrConnecting()));
    }


}

