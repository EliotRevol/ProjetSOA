package fr.insa.project.serviceUtilisateur;

public class Utilisateur {
 private String name ="";
 private String mdp = "";
  
  public Utilisateur(String name, String mdp) {
    this.name = name;
    this.mdp = mdp;
  }
  
  public Utilisateur() {
    
  }
  
  public String getName() {
    return name;
  }
  
  public String getMdp() {
    return mdp;
  }
  
  public String type() {
    if(this instanceof Benevole) {
      return "benevole";
    }
    else if (this instanceof Valideur) {
      return "valideur";
    }
    else {
      return "user";
    }
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
