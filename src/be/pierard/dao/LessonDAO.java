package be.pierard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.pierard.pojo.*;

public class LessonDAO extends DAO<Lesson>{
	public LessonDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(Lesson obj){		
		String sql = "INSERT INTO Lesson (MinBookings, MaxBookings, Schedule, InstructorId_FK, LessonTypeId_FK) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, obj.getMinBookings());
            stmt.setInt(2, obj.getMaxBookings());
            stmt.setString(3, obj.getSchedule());
            stmt.setInt(4, obj.getInstructor().getId());
            stmt.setInt(5, obj.getLessonType().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public boolean delete(Lesson obj){
		return false;
	}
	
	public boolean update(Lesson obj){
		String sql = "UPDATE Lesson SET MinBookings = ?, MaxBookings = ?, Schedule = ?, InstructorId_FK = ?, LessonTypeId_FK = ? WHERE LessonId = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setInt(1, obj.getMinBookings());
            stmt.setInt(2, obj.getMaxBookings());
            stmt.setString(3, obj.getSchedule());
            stmt.setInt(4, obj.getInstructor().getId());
            stmt.setInt(5, obj.getLessonType().getId());
            stmt.setInt(6, obj.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public Lesson find(int id){
		return null;
	}

	public ArrayList<Lesson> findAll() {
	    ArrayList<Lesson> lessonList = new ArrayList<>();

	    String sql = "SELECT l.*, lt.*, i.*, a.*, asub.*, ltsub.* " +
	                 "FROM Lesson l " +
	                 "INNER JOIN Instructor i ON l.InstructorId_FK = i.InstructorId " +
	                 "INNER JOIN LessonType lt ON l.LessonTypeId_FK = lt.LessonTypeId " +
	                 "INNER JOIN Accreditation a ON lt.AccreditationId_FK = a.AccreditationId" +
	                 "INNER JOIN InstructorAccreditation ia ON i.InstructorId = ia.InstructorId_FK " +
                     "INNER JOIN Accreditation asub ON ia.AccreditationId_FK = asub.AccreditationId " +
                     "INNER JOIN LessonType ltsub ON ltsub.AccreditationId_FK = a.AccreditationId and ltsub.level = ia.level";
	    
	    try (PreparedStatement stmt = connect.prepareStatement(sql);
	            ResultSet rs = stmt.executeQuery()) {
	        Map<Integer, Accreditation> accreditationMap = new HashMap<>();
	           while (rs.next()) {
	               int lessonId = rs.getInt("l.LessonId");
	               int minBookings = rs.getInt("l.MinBookings");
	               int maxBookings = rs.getInt("l.MaxBookings");
	               String schedule = rs.getString("l.Schedule");

	               LessonType lessonType = new LessonType();
	               lessonType.setId(rs.getInt("lt.LessonTypeId"));
	               lessonType.setLevel(rs.getString("lt.Level"));
	               lessonType.setPrice(rs.getDouble("lt.Price"));
	               
	               int accreditationId = rs.getInt("a.AccreditationId");
	               String accreditationName = rs.getString("a.Name");
                   ArrayList<LessonType> lessonTypeList = new ArrayList<>();
                   lessonTypeList.add(lessonType);
                   Accreditation accreditation = new Accreditation(accreditationId, accreditationName, lessonTypeList);
                   lessonType.setAccreditation(accreditation);
                   
                   LessonType lessonType2 = new LessonType();
                   lessonType2.setId(rs.getInt("ltsub.LessonTypeId"));
                   lessonType2.setLevel(rs.getString("ltsub.Level"));
                   lessonType2.setPrice(rs.getDouble("ltsub.Price"));
                   
                   Accreditation accreditation2 = Accreditation.createIfAbsent(
                       accreditationMap, 
                       rs.getInt("asub.AccreditationId"), 
                       rs.getString("asub.AccreditationName"), 
                       lessonType
                   );
                   
                   ArrayList<Accreditation> accreditationList = new ArrayList<>();
                   accreditationList.add(accreditation2);

	               int instructorId = rs.getInt("i.InstructorId");
                   String lastname = rs.getString("i.Lastname");
                   String firstname = rs.getString("i.Firstname");
                   int age = rs.getInt("i.Age");
                   String address = rs.getString("i.Address");
                   String email = rs.getString("i.Email");
                   double hourlyRate = rs.getDouble("i.HourlyRate");
	               Instructor instructor = new Instructor(instructorId, lastname, firstname, age, address, email, hourlyRate, accreditationList);
	               Lesson lesson = new Lesson(lessonId, minBookings, maxBookings, schedule, lessonType, instructor);

	               lessonList.add(lesson);
	           }
	       } catch (SQLException e) {
	           e.printStackTrace();
	       }

	       return lessonList;
	   }
}
