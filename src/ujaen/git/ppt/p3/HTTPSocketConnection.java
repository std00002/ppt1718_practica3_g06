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
    private String contentType = "";
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
            
            

               
                request_line= input.readLine();
                //Esto se deberÃ­a sacar de este bucle: Do while conflictivo
                String parts[]=request_line.split(" ");                    
              if(request_line.startsWith("GET ")){
                if(parts.length==3){
                        

                    String resourceFile="";
                        


                        //Version soportada
                        String[] status_line;
                        status_line=parts[2].split("/");
                        

                        
                        if(!status_line[1].equals("1.1") && !status_line[1].equals("1.0")){
                         outmesg=Recurso_505;
                        outdata=outmesg.getBytes();
                        }else{
                        
                        
                        if(parts[1].equalsIgnoreCase("/")){
                            resourceFile="/index.html";

                        
                        }else{
                            resourceFile=parts[1];
                        }
                        
                        
                        recurso=leerRecurso(resourceFile);
                        
                        if(recurso==null){
                            //recurso no encontrado
                            outmesg=Recurso_404;
                            outdata=outmesg.getBytes();    
                        }else{
                        
                        //Si existe algún recurso, obtenemos el tipo de archivo
                        if(recurso!=null){
                        contentType=getMimeType(resourceFile);
                        }
                        outmesg="HTTP/1.1 200 OK\r\nConnection: close\r\n"
                        +"Content-type:"+contentType+"\r\n"+ //Mime del archivo
                        //DATE
                        "Date: " + new Date().toString() + "\r\n"+ //Fecha actual del servidor
                        //Server
                        "Server: Salva1.0\r\n"+ //Nombre del host opcional
                        //Cabecera content-length
                        "Content-length: "+recurso.length+"\r\n"+ //Tamaño del contenido
                        //Cabecera allow
                        "Allow: GET\r\n"+ //Metodos HTTP a permitir
                        "\r\n";
                            
                        outdata=outmesg.getBytes();
                        
                        
                        }
                        //String local_recurso="C:/Users/Salvador/Desktop"+resourceFile+"";

                        }
            }else{
                outmesg=Recurso_400;
                outdata=outmesg.getBytes();
                }
                        
                    //peticion erronea
                }else
                   outmesg=Recurso_405;//metodo no permitido
                   outdata=outmesg.getBytes();
                
               do{
               request_line= input.readLine();
                
                System.out.println(request_line);
            }while(request_line.compareTo("")!=0);
            //CABECERAS....
           output.write(outdata);            
            //Recurso
           if(recurso!=null){
           output.write(recurso);
           }
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
                    
          File f = new File ("./"+resourceFile+"");//./ root project de netbeans
          if (f.exists()){ // y si el archivo existe en el directorio devolvemos bytes, si no null. Y controlamos segun lo devuelto
          long length = f.length();
          byte[] bytes = new byte[(int) length];
          fileInputStream = new FileInputStream(f);
          bufferedInputStream = new BufferedInputStream(fileInputStream);
          bufferedInputStream.read(bytes,0,bytes.length); // copiado el archivo en el byteArray
	 	
          
          return bytes;
          }
              
          
          return null;
    }
    
        /* Aquí de forma sencilla encontramos el MIME del archivo en la petición del recurso, siempre y cuando el recurso EXISTA antes*/
    public String getMimeType(String resourceFile) {
        String type = "";
        if (resourceFile.endsWith(".txt")) {
            type = "text/txt";
        } else if (resourceFile.endsWith(".html") || resourceFile.endsWith("htm")) {
            type = "text/html; Charset=UTF-8";
        } else if (resourceFile.endsWith(".jpg")) {
            type = "image/jpg";
        } else if (resourceFile.endsWith(".png")) {
            type = "image/png";
        } else if (resourceFile.endsWith(".jpeg")) {
            type = "image/jpeg";
        }
        return type;
    }
}