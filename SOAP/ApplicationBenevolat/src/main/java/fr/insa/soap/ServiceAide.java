package fr.insa.soap;




import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName="help_service")
public class ServiceAide {
  
  public List<Aide> demandeAide = new ArrayList<>();
  
  @WebMethod(operationName = "add_help")
  public void AjouterUtilisateurAide(@WebParam(name="chain") String chaine) {
    demandeAide.add(new Aide(chaine));
  }
  
  @WebMethod(operationName = "validate_help")
  public void ValiderUtilisateurAide(@WebParam(name="integer") int index) {
    demandeAide.get(index).validate();
  }
  
  @WebMethod(operationName = "refuse_help")
  public void RefuserUtilisateurAide(@WebParam(name="integer") int index,@WebParam(name="chain") String motif) {
    demandeAide.get(index).unvalidate(motif);
  }
}