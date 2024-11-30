package be.pierard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import be.pierard.pojo.LessonType;

public class LessonTypeDAO extends DAO<LessonType>{
	public LessonTypeDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(LessonType obj){		
		String sql = "INSERT INTO LessonType (Level, Price, AccreditationId_FK) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, obj.getLevel());
            stmt.setDouble(2, obj.getPrice());
            stmt.setInt(3, obj.getAccreditation().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public boolean delete(LessonType obj){
		return false;
	}
	
	public boolean update(LessonType obj){
		String sql = "UPDATE LessonType SET Level = ?, Price = ?, AccreditationId_FK = ? WHERE LessonTypeId = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, obj.getLevel());
            stmt.setDouble(2, obj.getPrice());
            stmt.setInt(3, obj.getAccreditation().getId());
            stmt.setInt(4, obj.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public LessonType find(int id){
		return null;
	}

	public ArrayList<LessonType> findAll() {
		return null;
	}
}
