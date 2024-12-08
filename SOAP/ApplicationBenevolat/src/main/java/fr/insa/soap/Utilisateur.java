package fr.insa.soap;

public class Utilisateur {
 private String name ="";
 private String mdp = "";
  
  public Utilisateur(String name, String mdp) {
    this.name = name;
    this.mdp = mdp;
  }
  
  public String getName() {
    return name;
  }
  
  public boolean identify(String mdp) {
    if(!this.mdp.equals(mdp)) {
      return false;
    }
    return true;
  }
  
  public static boolean checkType(String type) {
    if(!type.equals("user") && !type.equals("benevole") && !type.equals("valideur")) {
      return false;
    }
    
    return true;
  }
}
