package fr.insa.soap;

import java.net.MalformedURLException;

import javax.xml.ws.Endpoint;

public class Main {
  
  public static String host = "localhost";
  public static int port =8091;
  
  public static String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_050";
  public static String username = "projet_gei_050";
  public static String password = "eil1Etha";
  
  public void demarrerService() {
    String url = "http://"+host+":"+port+"/";
    Endpoint.publish(url, new ServiceUtilisateur());
    
  }
  
  public static void main(String [] args) throws MalformedURLException {
    new Main().demarrerService();
    System.out.println("Service a démarré");
  }
}
