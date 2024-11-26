package be.pierard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.pierard.pojo.Accreditation;
import be.pierard.pojo.Instructor;
import be.pierard.pojo.LessonType;

public class InstructorDAO extends DAO<Instructor>{
	public InstructorDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(Instructor obj){		
		String sql = "INSERT INTO Instructor (Lastname, Firstname, Age, Address, Email, IsAvailable, HourlyRate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, obj.getLastname());
            stmt.setString(2, obj.getFirstname());
            stmt.setInt(3, obj.getAge());
            stmt.setString(4, obj.getAddress());
            stmt.setString(5, obj.getEmail());
            stmt.setBoolean(6, obj.isAvailable());
            stmt.setDouble(7, obj.getHourlyRate());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public boolean delete(Instructor obj){
		return false;
	}
	
	public boolean update(Instructor obj){
		String sql = "UPDATE Instructor SET Lastname = ?, Firstname = ?, Age = ?, Address = ?, Email = ?, IsAvailable = ?, HourlyRate = ? WHERE InstructorId = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, obj.getLastname());
            stmt.setString(2, obj.getFirstname());
            stmt.setInt(3, obj.getAge());
            stmt.setString(4, obj.getAddress());
            stmt.setString(5, obj.getEmail());
            stmt.setBoolean(6, obj.isAvailable());
            stmt.setDouble(7, obj.getHourlyRate());
            stmt.setInt(8, obj.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public Instructor find(int id){
		return null;
	}

	public ArrayList<Instructor> findAll() {
		ArrayList<Instructor> instructors = new ArrayList<Instructor>();
        String sql = "SELECT i.*, a.*, lt.* " +
                     "FROM Instructor i	 " +
                     "INNER JOIN InstructorAccreditation ia ON i.InstructorId = ia.InstructorId_FK " +
                     "INNER JOIN Accreditation a ON ia.AccreditationId_FK = a.AccreditationId " +
                     "INNER JOIN LessonType lt ON lt.AccreditationId_FK = a.AccreditationId and lt.level = ia.level";

        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                Map<Integer, Accreditation> accreditationMap = new HashMap<>();
                Map<Integer, Instructor> instructorMap = new HashMap<>();

                while (rs.next()) {
                    LessonType lessonType = new LessonType();
                    lessonType.setId(rs.getInt("LessonTypeId"));
                    lessonType.setLevel(rs.getString("Level"));
                    lessonType.setPrice(rs.getDouble("Price"));
                    
                    Accreditation accreditation = Accreditation.createIfAbsent(
                        accreditationMap, 
                        rs.getInt("AccreditationId"), 
                        rs.getString("AccreditationName"), 
                        lessonType
                    );

                    int instructorId = rs.getInt("InstructorId");
                    String lastname = rs.getString("Lastname");
                    String firstname = rs.getString("Firstname");
                    int age = rs.getInt("Age");
                    String address = rs.getString("Address");
                    String email = rs.getString("Email");
                    boolean isAvailable = rs.getBoolean("IsAvailable");
                    double hourlyRate = rs.getDouble("HourlyRate");
                    
                    Instructor instructor = instructorMap.computeIfAbsent(instructorId, id -> {
                        return new Instructor(
                            id, 
                            lastname, 
                            firstname, 
                            age, 
                            address, 
                            email, 
                            isAvailable, 
                            hourlyRate, 
                            new ArrayList<Accreditation>()
                        );
                    });

                    instructor.addAccreditation(accreditation);
                }

                instructors.addAll(instructorMap.values());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return instructors;
	}
	
	public boolean createInstructorAccreditation(int instructorId, int accreditationId, String level) {
		String sql = "INSERT INTO InstructorAccreditation (InstructorId_FK, AccreditationId_FK, Level) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, instructorId);
            stmt.setInt(2, accreditationId);
            stmt.setString(3, level);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public boolean updateInstructorAccreditation(int instructorId, int accreditationId, String level) {
		String sql = "UPDATE InstructorAccreditation SET Level = ? WHERE InstructorId_FK = ? and AccreditationId_FK = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, level);
        	stmt.setInt(2, instructorId);
            stmt.setInt(3, accreditationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
}
