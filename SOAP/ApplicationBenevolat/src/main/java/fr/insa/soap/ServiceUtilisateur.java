package fr.insa.soap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import fr.insa.project.serviceUtilisateur.Demandeur;

@WebService(serviceName="user_service")
public class ServiceUtilisateur {
  List<Benevole> benevoles = new ArrayList<>();
  List<Demandeur> demandeurs = new ArrayList<>();
  List<Valideur> valideurs = new ArrayList<>();
  List<Utilisateur> totalUsers = new ArrayList<>();
  boolean loaded =false;
  
 
  @WebMethod(operationName = "add_user")
  public void AjouterUtilisateur(@WebParam(name="user") String chaine, @WebParam(name="password")  String mdp,  @WebParam(name="type")  String userType) {
    if(UserExist(chaine)) {
      System.out.println("Adding an already existing user");
      return;
    }
    if(Utilisateur.checkType(userType)) {
      Connection connection = null;
      String sql = "INSERT INTO utilisateur (type, nom_utilisateur, mot_de_passe) VALUES (?, ?, ?)";
      try (Connection conn = DriverManager.getConnection(Main.url, Main.username, Main.password);
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
          String type="";
          
          if(userType.equals("user")) {
            type = "user";
            Demandeur demandeur = new Demandeur(chaine,mdp);
            demandeurs.add(demandeur);
            totalUsers.add(demandeur);
          }
          else if(userType.equals("valideur")) {
            type = "valideur";
            Valideur valideur = new Valideur(chaine,mdp);
            valideurs.add(valideur);
            totalUsers.add(valideur);
          }
          else {
            type = "benevole";
            Benevole benevole = new Benevole(chaine,mdp);
            benevoles.add(benevole);
            totalUsers.add(benevole);
          }

           pstmt.setString(1, type); 
           pstmt.setString(2, chaine);
           pstmt.setString(3, mdp);

           int rowsAffected = pstmt.executeUpdate();
           System.out.println(rowsAffected + " ligne(s) insérée(s).");
    } catch (SQLException e) {
      System.out.println("Erreur de connexion : " + e.getMessage());
    } finally {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture : " + e.getMessage());
            }
        }
      }
    }
    else {
      System.out.println("Wrong type given.");
    }
  }
  
  @WebMethod(operationName = "identify")
  public boolean Identify(@WebParam(name="chain") String chaine, @WebParam(name="chain2") String mdp) {
    if(UserExist(chaine)) {
      Utilisateur user = GetId(chaine);
      return user.identify(mdp);
    }
    return false;
  }
  
  private boolean UserExist(String chaine) {
    for(Utilisateur user: totalUsers) {
      if(user.getName().equals(chaine)) {
        return true;
      }
    }
    
    return false;
  }
  
  public void loadUsers() {
    if(loaded == true) {
      System.out.println("Already loaded, aborting.");
      return;
    }
    loaded = true;
    Connection connection = null;
    String sql = "SELECT type,nom_utilisateur,mot_de_passe FROM utilisateur";
    try (Connection conn = DriverManager.getConnection(Main.url, Main.username, Main.password);
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)) {
      
        while (resultSet.next()) {
            String type = resultSet.getString("type");
            String nomUtilisateur = resultSet.getString("nom_utilisateur");
            String motDePasse = resultSet.getString("mot_de_passe");
            
            if(!UserExist(nomUtilisateur)) {
              if(type.equals("user")) {
                Demandeur demandeur = new Demandeur(nomUtilisateur,motDePasse);
                demandeurs.add(demandeur);
                totalUsers.add(demandeur);
              }
              else if(type.equals("valideur")) {
                Valideur valideur = new Valideur(nomUtilisateur,motDePasse);
                valideurs.add(valideur);
                totalUsers.add(valideur);
              }
              else if(type.equals("benevole")){
                Benevole benevole = new Benevole(nomUtilisateur,motDePasse);
                benevoles.add(benevole);
                totalUsers.add(benevole);
              }
            }
            else {
              System.out.println("Reading an already existing user");
            }
        }
  } catch (SQLException e) {
    System.out.println("Erreur de connexion : " + e.getMessage());
  } finally {
      if (connection != null) {
          try {
              connection.close();
              System.out.println("Connexion fermée.");
          } catch (SQLException e) {
              System.out.println("Erreur lors de la fermeture : " + e.getMessage());
          }
      }
    }
  }
  
  private Utilisateur GetId(String chaine) {
    for(Utilisateur user: totalUsers) {
      if(user.getName().equals(chaine)) {
        return user;
      }
    }
    
    return null;
  }
  
}
