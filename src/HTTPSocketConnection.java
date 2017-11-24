
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author usuario
 */
public class HTTPSocketConnection implements Runnable {
    
    private Socket mSocket=null;
    
    public HTTPSocketConnection(Socket s){
        mSocket = s;
    }
    public void run() {
        String request_line="";
        BufferedReader input;
        DataOutputStream output;
        FileInputStream input_file;
        try {
            input = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            output = new DataOutputStream(mSocket.getOutputStream());
            do{
                request_line= input.readLine();
                output.write(("OK "+request_line+"\r\n").getBytes());
            }while(request_line.compareTo("quit")!=0);

            input.close();
            output.close();
            mSocket.close();
        } catch (IOException e) {
            System.err.println("Exception" + e.getMessage());
        }
        }
}
