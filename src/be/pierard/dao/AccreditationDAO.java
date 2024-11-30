package be.pierard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.pierard.pojo.*;

public class AccreditationDAO extends DAO<Accreditation>{
	public AccreditationDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(Accreditation obj){		
		return false;
	}
	
	public boolean delete(Accreditation obj){
		return false;
	}
	
	public boolean update(Accreditation obj){
		return false;
	}
	
	public Accreditation find(int id){
		return null;
	}

	public ArrayList<Accreditation> findAll() {
        ArrayList<Accreditation> accreditations = new ArrayList<Accreditation>();
        String sql = "SELECT a.*, lt.* " +
                     "FROM Accreditation a " +
                     "INNER JOIN LessonType lt ON lt.AccreditationId_FK = a.AccreditationId ";

        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Accreditation> accreditationMap = new HashMap<>();

                while (rs.next()) {
                    LessonType lessonType = new LessonType();
                    lessonType.setId(rs.getInt("LessonTypeId"));
                    lessonType.setLevel(rs.getString("Level"));
                    lessonType.setPrice(rs.getDouble("Price"));
                    Accreditation.createIfAbsent(accreditationMap, rs.getInt("AccreditationId"), 
                    		rs.getString("AccreditationName"), lessonType);
                }
                accreditations.addAll(accreditationMap.values());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accreditations;
    }

}
