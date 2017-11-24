
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author usuario
 */
public class MainServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
    String host="simu11";
    try{    
InetAddress[] addressList = InetAddress.getAllByName(host);
for (InetAddress address : addressList) {
System.out.println("\t" + address.getHostName() + "/" +
address.getHostAddress());

InetAddress destination= InetAddress.getByName("www10.ujaen.es");

    System.out.println("Conectando con socket"+destination.toString());
    
    Socket socket= new Socket(destination,80);

}
}catch (UnknownHostException e) {
System.out.println("\tUnable to find address for " + host);
}
    
  }
     
}
