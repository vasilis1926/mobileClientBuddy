package com.example.connectioncheck;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private SeekBar sBar;
    private TextView tView;
    private String host ="178.254.32.23";
    // own laptop "192.168.2.206";
    // home 77.181.72.44
    private int pval = 0;
    private RetrieveFeedTask rft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        final TextView data = findViewById(R.id.l_data);
        setSupportActionBar(toolbar);
        rft = new RetrieveFeedTask();
        rft.execute(host);
        sBar = (SeekBar) findViewById(R.id.seekBar1);
        tView = (TextView) findViewById(R.id.tw_bar);
        tView.setText("Drag to log walk time!");


        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                tView.setText(pval + " Minutes");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //tView.setText(pval + " Minutes");
            }
        });

        try {
            data.setText("Buddy went out at\n" + prepareOutput(rft.get()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("1-----------------rftget textview: "+e.getMessage()+"-------------------------");
        }

        final CheckBox shit = findViewById(R.id.cb_shit);
        final CheckBox pee = findViewById(R.id.cb_pee);
        final Button log = findViewById(R.id.b_send);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);
                    String str =dateNow();
                    if(shit.isChecked()) str = str.concat(";true");
                    else {str = str.concat(";false");}
                    if(pee.isChecked()) str = str.concat(";true");
                    else {str= str.concat(";false");}
                    str = str.concat(";" + pval);
                System.out.println("1-----------------string: "+str+"-------------------------");
                    rft.onPostExecute(str);
                try {
                    rft.readData();
                    data.setText("Buddy went out at\n" + prepareOutput(rft.getServerData()));
                } catch (Exception e) {
                    System.out.println("1-----------------rftget button: "+e.getMessage()+"-------------------------");
                }
                Snackbar.make(view, "data sent to server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    private String prepareOutput(String str) {
        String  output="";
        String[] data = str.trim().split("\t");

        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println(data[0]);
        System.out.println(data[1]);
        System.out.println(data[2]);
        System.out.println(data[3]);
        System.out.println("------------------------------------------------------------------------------------------------");

        output = data[0].concat("\n");
        output = output.concat( "for " +data[3]+ " minutes\n");
        if (data[1].equals("true"))
        {
            output = output.concat("and shited");
        }
        if(data[2].equals("true"))
        {
            output = output.concat(" and peed");
        }
        return output;
    }
    private String dateNow()
    {
        Date date = new Date(); // this object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);

    }
}
