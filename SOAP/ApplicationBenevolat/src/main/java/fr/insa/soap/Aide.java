package fr.insa.soap;

public class Aide {
  private final String name;
  private Statut valid = Statut.PENDING;
  private String motif = "";
  
  public Aide(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  
  public void validate() {
    valid = Statut.VALID;
  }
  
  public void unvalidate(String motif) {
    valid = Statut.NOT_VALID;
    this.motif = motif;
  }
  
  
  public boolean isValid() {
    return valid!=Statut.NOT_VALID;
  }
  
  public String getMotif() {
    return motif;
  }
}
