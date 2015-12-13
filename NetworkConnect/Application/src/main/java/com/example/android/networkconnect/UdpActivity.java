package com.example.android.networkconnect;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpActivity extends Activity {

    private EditText ip;
    private EditText port;
    private EditText message;
    private TextView inMsg;
    private Button button;
    private String received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);
        ip = (EditText) findViewById(R.id.ip);
        port = (EditText) findViewById(R.id.port);
        message = (EditText) findViewById(R.id.message);
        inMsg = (TextView) findViewById(R.id.inMsg);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(runnableNet).start();
                inMsg.setText(received);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_udp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.main_action:
                finish();
                return true;

        }
        return false;
    }

    Runnable runnableNet = new Runnable()
    {

        @Override
        public void run()
        {
            String ipString = ip.getText().toString();
            int portNr = Integer.parseInt(port.getText().toString());
            String msgString = message.getText().toString();

            try
            {
                DatagramSocket outSocket = new DatagramSocket();
                DatagramSocket inSocket = new DatagramSocket(portNr);

                InetAddress addr = InetAddress.getByName(ipString);

                byte[] msg = msgString.getBytes();
                DatagramPacket packet = new DatagramPacket(msg, msg.length,addr,portNr);

                DatagramPacket inPacket = new DatagramPacket(new byte[1500], 1500);
                outSocket.send(packet);

                while(true) {
                    inSocket.receive(inPacket);
                    String receivedString = new String(inPacket.getData());
                    received = receivedString;
                    outSocket.close();
                    inSocket.close();
                    return;
                }

            }
            catch (SocketException e) {
                e.printStackTrace();
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
