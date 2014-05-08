package be.ap.parentcontrollauncher;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Wouter on 25/04/2014.
 */
public class NetClient {
    private String host;
    private Integer port;
    private Socket socket = null;
    private BufferedWriter output = null;
    private BufferedReader input = null;
    final int BUFFER_SIZE = 2048;

    public NetClient (String _host, Integer _port)
    {
        host = _host;
        port = _port;
    }

    public void ConnectWithServer()
    {
        try {
            if (socket == null) {
                socket = new Socket(host, port);
                socket.setReceiveBufferSize(16384);
                socket.setSendBufferSize(16384);
                //output = new PrintWriter(socket.getOutputStream());
                output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.i("NetClient", "Connected to server");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DisConnectWithServer() {
        if (socket != null) {
            if (socket.isConnected()) {
                try {
                    input.close();
                    output.close();
                    socket.close();
                    Log.i("NetClient", "Disconnected from server");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void SendDataToServer(String message) {
        try {
            if (message != null) {
                output.write(message);
                output.flush();
                Log.i("NetClient", "Data send to server");
            }
        }
        catch (Exception e) {
            Log.i("NetClient", "Error while sending data:  " + e.getMessage());
        }
    }

    public String ReceiveDataFromServer() {
        try {
            String message = "";
            int charsRead = 0;
            char[] buffer = new char[BUFFER_SIZE];

            while ((charsRead = input.read(buffer)) != -1) {
                message += new String(buffer).substring(0, charsRead);
            }

            return message;
        } catch (Exception e) {
            return "Error receiving response:  " + e.getMessage();
        }
    }
}
