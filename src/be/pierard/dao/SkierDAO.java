package be.pierard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import be.pierard.pojo.Skier;

public class SkierDAO extends DAO<Skier>{
	public SkierDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(Skier obj){		
		 String sql = "INSERT INTO Skier (Lastname, Firstname, Age, Address, Email, Insurance, Level) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
	            stmt.setString(1, obj.getLastname());
	            stmt.setString(2, obj.getFirstname());
	            stmt.setInt(3, obj.getAge());
	            stmt.setString(4, obj.getAddress());
	            stmt.setString(5, obj.getEmail());
	            stmt.setBoolean(6, obj.isInsurance());
	            stmt.setString(7, obj.getLevel());
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	}
	
	public boolean delete(Skier obj){
		return false;
	}
	
	public boolean update(Skier obj){
		String sql = "UPDATE Skier SET Lastname = ?, Firstname = ?, Age = ?, Address = ?, Email = ?, Insurance = ?, Level = ? WHERE SkierId = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, obj.getLastname());
            stmt.setString(2, obj.getFirstname());
            stmt.setInt(3, obj.getAge());
            stmt.setString(4, obj.getAddress());
            stmt.setString(5, obj.getEmail());
            stmt.setBoolean(6, obj.isInsurance());
            stmt.setString(7, obj.getLevel());
            stmt.setInt(8, obj.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public Skier find(int id){
		return null;
	}

	public ArrayList<Skier> findAll() {
		ArrayList<Skier> skiers = new ArrayList<>();
	    String sql = "SELECT * FROM Skier";

	    try (PreparedStatement stmt = connect.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Skier skier = new Skier(rs.getInt("SkierId"), rs.getString("Lastname"), rs.getString("Firstname"), rs.getInt("Age"),
	            		rs.getString("Address"), rs.getString("Email"), rs.getBoolean("Insurance"), rs.getString("Level"));
	            skiers.add(skier);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return skiers;
	}
}
