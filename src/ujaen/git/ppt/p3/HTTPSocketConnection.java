package ujaen.git.ppt.p3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.Random;

/**
 * Clase de atención de un servidor TCP sencillo
 * Prácticas de Protocolos de Transporte
 * Grado en Ingeniería Telemática
 * Universidad de Jaén
 * 
 * @author Juan Carlos Cuevas Martínez
 */
public class HTTPSocketConnection implements Runnable {
    
    public static final String Recurso_405="HTTP/1.1 405\r\nContent-type:text/html\r\n\r\n<html><body><h1>Metodo no permitido</h1></body></html>";
    public static final String Recurso_404="HTTP/1.1 404\r\nContent-type:text/html\r\n\r\n<html><body><h1>No encontrado</h1></body></html>";
    public static final String Recurso_400="HTTP/1.1 400\r\nContent-type:text/html\r\n\r\n<html><body><h1>Error de sintaxis/cliente</h1></body></html>";
    public static final String Recurso_505="HTTP/1.1 505\r\nContent-type:text/html\r\n\r\n<html><body><h1>HTTP Version Not Supported</h1></body></html>";
    
    private Socket mSocket=null;
    public final static String FILE_TO_SEND = "index.html";
    private String fileMimeType = "";
    /**
     * Se recibe el socket conectado con el cliente
     * @param s Socket conectado con el cliente
     */
    public HTTPSocketConnection(Socket s){
        mSocket = s;
    }
    
    public void run() {
        //Variables
        Random r = new Random(System.currentTimeMillis());
        int n=r.nextInt();
        String request_line="";
        BufferedReader input;
        DataOutputStream output;
        
        
        try {
            byte[] outdata=null;
            byte[] recurso=null;
            String outmesg="";
            input = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            output = new DataOutputStream(mSocket.getOutputStream());

            
            do{
               
                request_line= input.readLine();
                //Esto se deberÃ­a sacar de este bucle
                String parts[]=request_line.split(" ");
                if(request_line.startsWith("GET ")){
                    String resourceFile="";
               
                    
                    if(parts.length==3){
                        //Version soportada
                        String[] status_line;
                        status_line=parts[2].split("/");
                        
                        if(status_line[1].equals("1.1") || status_line[1].equals("1.0")){
                            
                        if(!parts[0].equals("GET ")){
                            outmesg=Recurso_405;
                       outdata=outmesg.getBytes();    
                        }
                        if(parts[1].equalsIgnoreCase("/")){
                            resourceFile="index.html";
                        }else{
                            resourceFile=parts[1];
                        }
                        String local_recurso="C:\\Users\\LENOVO\\Desktop\\UNIVERSIDAD\\3º Curso\\Protocolos de Transporte\\ppt1718_practica3_g06/"+resourceFile+"";
                        
                        recurso=leerRecurso(resourceFile);
                        //Content-type
                        outmesg="HTTP/1.1 200 OK\r\nContent-type:text/html\r\n"+
                        //DATE
                        "Date: " + new Date().toString() + "\r\n"+
                        //Server
                        "Server: Salva1.0\r\n"+
                        //Cabecera content-length111
                        "Content-length: "+local_recurso.length()+"\r\n"+
                        //Cabecera allow
                        "Allow: GET\r\n"+
                        "\r\n";
                        outmesg.length();
                        outdata=outmesg.getBytes();
                        
                        if(outdata==null)    {
                            outmesg=Recurso_404;
                            outdata=outmesg.getBytes();
                            
                        }
                   
                    
                    /*else{
                        outmesg=Recurso_400;
                        outdata=outmesg.getBytes();
                    }*/
                    }else{
                        outmesg=Recurso_505;
                        outdata=outmesg.getBytes();
                        }  
            } 
                    
                }
                
                
                System.out.println(request_line);
            }while(request_line.compareTo("")!=0);
            //CABECERAS....
            
            //Recurso
           output.write(outdata);
           output.write(recurso);
           
           output.flush();
            input.close();
            output.close();
            mSocket.close();
    
        } catch (IOException e) {
            System.err.println("Exception" + e.getMessage());
        }
        }

    /**
     * MÃ©todo para leer un recurso del disco
     * @param resourceFile
     * @return los bytes del archio o null si Ã©ste no existe
     */
    private byte[] leerRecurso(String resourceFile) throws FileNotFoundException, IOException {
        //Se debe comprobar que existe
            FileInputStream fileInputStream = null;
	    BufferedInputStream bufferedInputStream = null;
                    //OutputStream outputStream = null;
          File f = new File ("C:/Users/Salvador/Desktop"+resourceFile+"");
          if (f.exists()){
          long length = f.length();
          byte[] bytes = new byte[(int) length];
          fileInputStream = new FileInputStream(f);
          bufferedInputStream = new BufferedInputStream(fileInputStream);
          bufferedInputStream.read(bytes,0,bytes.length); // copied file into byteArray
	 
	//sending file through socket
	//output = mSocket.getOutputStream();
        //System.out.println("Sending " + resourceFile + "( size: " + bytes.length + " bytes)");
	//output.write(bytes,0,bytes.length);			//copying byteArray to socket
        //output.flush();										//flushing socke
        //System.out.println("Done.");		
          
          return bytes;
          }
              
          
          return null;
    }
}