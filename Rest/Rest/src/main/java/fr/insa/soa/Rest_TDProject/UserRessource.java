package fr.insa.soa.Rest_TDProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("user")
public class UserRessource {
    private String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/projet_gei_050";
    private String username = "projet_gei_050";
    private String password = "eil1Etha";

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Utilisateur getUser(@PathParam("id") int id) throws ClassNotFoundException {
        Connection conn = null;
        Statement stm = null;
        ResultSet result = null;
        Utilisateur user = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "SELECT * FROM utilisateur WHERE id = " + id;

        try {
          
          conn = DriverManager.getConnection(url, username, password);
          stm = conn.createStatement();
          result = stm.executeQuery(sql);
            if (result.next()) {
                String userName = result.getString("nom_utilisateur");
                String userPassword = result.getString("mot_de_passe");

                user = new Utilisateur(userName, userPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stm != null) {
                    stm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return user;
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(Utilisateur utilisateur) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet result = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "SELECT * FROM utilisateur WHERE nom_utilisateur = ? AND mot_de_passe = ?";
        try {
            conn = DriverManager.getConnection(url, username, password);
            stm = conn.prepareStatement(sql);
            stm.setString(1, utilisateur.getName());
            stm.setString(2, utilisateur.getMdp());

            result = stm.executeQuery();
            if (result.next()) {
                return "Login réussi pour l'utilisateur : " + utilisateur.getName();
            } else {
                return "Login échoué : Nom d'utilisateur ou mot de passe incorrect." + utilisateur.getName() + " "+utilisateur.getMdp();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur lors du login : " + e.getMessage();
        } finally {
            try {
                if (result != null) result.close();
                if (stm != null) stm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUser(Utilisateur utilisateur) throws ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stm = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "INSERT INTO utilisateur (nom_utilisateur, mot_de_passe) VALUES (?, ?)";
        try {
            conn = DriverManager.getConnection(url, username, password);
            stm = conn.prepareStatement(sql);
            stm.setString(1, utilisateur.getName());
            stm.setString(2, utilisateur.getMdp());

            int rowsInserted = stm.executeUpdate();
            if (rowsInserted > 0) {
                return "Utilisateur ajouté avec succès : " + utilisateur.getName();
            } else {
                return "Échec de l'ajout de l'utilisateur.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage();
        } finally {
            try {
                if (stm != null) stm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
