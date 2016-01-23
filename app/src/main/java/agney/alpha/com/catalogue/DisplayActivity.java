package agney.alpha.com.catalogue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayActivity extends AppCompatActivity implements OneFragment.OnFragmentInteractionListener,TwoFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    CustomAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);





        Intent intent = getIntent();
        String message = intent.getStringExtra("item");
        String category = message.split("/")[0];
        final String item = message.split("/")[2];
        final String url = category + "/" + item;

        generateJSON(url);
        generateFlipOutput(item.replace(" ", "+"));

    }





    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new TwoFragment(), "INFORMATION");
        adapter.addFragment(new OneFragment(), "WEBSITES");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void generateJSON(String input) {

        String tag_json_obj = "json_obj_req";
        String url_first = "http://api.pricecheckindia.com/feed/product/";
        String url_last = ".json?user=agneymen&key=MXLRSOICZLBIAXZV";
        String url = url_first+input+url_last;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            populateList(response);


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

    public void populateList(JSONObject response) {

        final List<Attributes> list;
        list = Attributes.fromJson(response);
        if(list.isEmpty()) {
            Toast.makeText(this,"Sorry! No products are found.",Toast.LENGTH_SHORT).show();
        }

        int position = SortList.findMin(list);
        rvAdapter = new CustomAdapter(list);
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.setAdapter(rvAdapter);
        final Button button = (Button) findViewById(R.id.sortButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Collections.sort(list);
                rvAdapter.notifyDataSetChanged();
            }
        });
                setLrp(list.get(position).getSellingPrice(), list.get(position).getWebsite(), list.get(position).getProductUrl());
                rv.addOnItemTouchListener(
                        new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                // TODO Handle item click
                                String item = list.get(position).getProductUrl();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item));
                                startActivity(browserIntent);
                            }
                        })
                );


            }

            public void generateFlipOutput(String message) {
                String tag_json_obj = "json_obj_req";
                String url_first = "https://affiliate-api.flipkart.net/affiliate/search/json?query=";
                String url_last = "&resultCount=1";
                String flip_url = url_first + message + url_last;

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, flip_url, (String) null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    List<Attributes> fliplist = Attributes.forInfo(response);
                                    Attributes products = fliplist.get(0);
                                    printResult(products);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                                VolleyLog.d("My Activity", "Error:" + error.getMessage());

                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Fk-Affiliate-Id", "agneymeno");
                        headers.put("Fk-Affiliate-Token", "106fd66634b24d41bb0f82507b169580");
                        return headers;
                    }
                };
                AppController.getInstance().addToRequestQueue(jsObjRequest, tag_json_obj);
            }

            public void printResult(Attributes product) {

                ImageView iV = (ImageView) findViewById(R.id.image);
                Picasso.with(this)
                        .load(product.imageUrl)
                        .resize(400, 400)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .centerInside()
                        .into(iV);
                TextView title = (TextView) findViewById(R.id.title);
                TextView desc = (TextView) findViewById(R.id.description);
                TextView max = (TextView) findViewById(R.id.mrp);
                title.setText(product.title);
                desc.setText(product.productDescription);
                max.setText("Maximum Retail Price:  " + product.maximumRetailPrice);

            }

            private void setLrp(int sellingPrice, String website, final String url) {
                TextView sP = (TextView) findViewById(R.id.lrp);
                sP.setText(String.valueOf(sellingPrice));
                String webImage = "drawable/" + website;
                int imageResource = getResources().getIdentifier(webImage, null, getPackageName());
                ImageView websiteImage = (ImageView) findViewById(R.id.websiteImage);
                Picasso.with(this)
                        .load(imageResource)
                        .into(websiteImage);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });
            }

        }
