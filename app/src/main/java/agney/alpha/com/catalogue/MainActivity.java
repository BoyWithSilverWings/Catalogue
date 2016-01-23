package agney.alpha.com.catalogue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private boolean flag = true;
    public String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        final Button button = (Button) findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText searchInput = (EditText) findViewById(R.id.editText);
                if(searchInput.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(),"Please Enter Search Term",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ScrollingActivity.class);
                String message = searchInput.getText().toString();
                message = item + "/" + message;
                intent.putExtra("message", message);
                if(flag) {
                    startActivity(intent);
                }


            }
        });

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
                if(isFirstStart) {
                    Intent i = new Intent(MainActivity.this,AppIntro.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getPrefs.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });
        t.start();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString().toLowerCase().replace(" ", "_");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
        flag = false;

    }



}

