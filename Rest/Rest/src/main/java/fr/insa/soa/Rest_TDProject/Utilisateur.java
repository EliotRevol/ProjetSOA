package fr.insa.soa.Rest_TDProject;

import jakarta.json.bind.annotation.JsonbProperty;

public class Utilisateur {
  @JsonbProperty("name")
  private String name;

  @JsonbProperty("mdp")
  private String mdp;
  
 public Utilisateur() {
    super();
  }
  
  public String getName() {
    return name;
  }
  
  public String getMdp() {
    return mdp;
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
  
  public Utilisateur(String name, String mdp) {
    this.name = name;
    this.mdp = mdp;
  }
}
