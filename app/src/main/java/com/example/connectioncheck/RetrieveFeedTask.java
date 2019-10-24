package com.example.connectioncheck;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


class RetrieveFeedTask extends AsyncTask<String, Void, String> {

    private Exception exception;
    private final static int PORT = 4330;
    private  SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Date date = new Date(System.currentTimeMillis());
    private Socket socket;
    private String serverData;
    private PrintWriter out;
    private BufferedReader in;
    private final static String REQUEST="Need LastWalk";


    protected String doInBackground(String... host) {
        initialize(host[0]);
        readData();
        //sendData("13.09.1000 10:00;true;true;180");
        System.out.println("1-----------------"+serverData+"-------------------------");
        return serverData;
    }

    public void initialize(String host) {
        try {
            this.socket = new Socket(host, PORT);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (Exception e){ System.out.println("-------------------------" + e.toString() +"-----------------------"); }
    }

    public void requestData() {
        try { out.println(REQUEST); out.flush();
        }catch (Exception e){ System.out.println("-------------------------" + e.toString() +"-----------------------"); }
    }

    public void readData() {
        try {  requestData();
            this.serverData = in.readLine();
        }catch (Exception e){ System.out.println("-------------------------" + e.toString() +"-----------------------"); }
    }

    public void sendData(String data) {
        try {out.println(data); out.flush();
        }catch (Exception e){ System.out.println("-------------------------" + e.toString() +"-----------------------"); }
    }

    protected void onPostExecute(String feed) {
        // TODO: check this.exception
        // TODO: do something with the feed
        sendData(feed);
    }

    public String getServerData() {
        return serverData;
    }
}
