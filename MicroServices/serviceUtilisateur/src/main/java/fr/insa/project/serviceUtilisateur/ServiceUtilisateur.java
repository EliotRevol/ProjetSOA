package fr.insa.project.serviceUtilisateur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/utilisateur")
public class ServiceUtilisateur {
    @Value("${server.port}")
    private String serverPort;
  
    @Value("${db.uri}")
    private String url;
    
    @Value("${db.login}")
    private String username;
    
    @Value("${db.pwd}")
    private String password;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/get/{idUtilisateur}")
    public Utilisateur getUtilisateur(@PathVariable("idUtilisateur") int id) {
      Connection connection = null;
      String sql = "SELECT type,nom_utilisateur,mot_de_passe FROM utilisateur WHERE id = "+String.valueOf(id);
      try (Connection conn = DriverManager.getConnection(url, username, password);
          Statement statement = conn.createStatement();
          ResultSet resultSet = statement.executeQuery(sql)) {
          
          while (resultSet.next()) {
              String type = resultSet.getString("type");
              String nomUtilisateur = resultSet.getString("nom_utilisateur");
              String motDePasse = resultSet.getString("mot_de_passe");
              System.out.println("Found a query");
              switch(type) {
                case "user" :return new Demandeur(nomUtilisateur,motDePasse); 
                case "valideur" :return new Valideur(nomUtilisateur,motDePasse); 
                case "benevole" :return new Benevole(nomUtilisateur,motDePasse); 
                default : return new Utilisateur("pasmarché","mdp");
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
      return new Utilisateur("pasmarché","mdp");
    }
    
    @PostMapping("/add")
    public String addUtilisateur(@RequestBody Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (type, nom_utilisateur, mot_de_passe) VALUES ('" 
                     + "user" + "','" 
                     + utilisateur.getName() + "','" 
                     + utilisateur.getMdp() + "')";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
            return "Utilisateur ajouté avec succès";
        } catch (SQLException e) {
            return "Erreur lors de l'ajout : " + e.getMessage();
        }
    }
    
    @PostMapping("/login")
    public String login(@RequestBody Utilisateur utilisateur) {
        String sql = "SELECT COUNT(*) AS count FROM utilisateur WHERE nom_utilisateur = '"
                     + utilisateur.getName() + "' AND mot_de_passe = '" 
                     + utilisateur.getMdp() + "'";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next() && resultSet.getInt("count") > 0) {
                return "Connexion réussie";
            } else {
                return "Nom d'utilisateur ou mot de passe incorrect";
            }
        } catch (SQLException e) {
            return "Erreur lors de la connexion : " + e.getMessage();
        }
    }


}
