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
		String sql = "UPDATE Lesson SET MinBookings = ?, MaxBookings = ?, Schedule = ?, InstructorId_FK = ?, LessonTypeId_FK = ?"
				+ " WHERE LessonId = ?";
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
	    Map<Integer, Lesson> lessonMap = new HashMap<>();
	    Map<Integer, Instructor> instructorMap = new HashMap<>();
	    Map<Integer, LessonType> lessonTypeMap = new HashMap<>();

	    String sql = "SELECT l.LessonId, l.MinBookings, l.MaxBookings, l.Schedule, " +
	                 "lt.LessonTypeId, lt.Level, lt.Price, " +
	                 "a.AccreditationId, a.AccreditationName, " +
	                 "i.InstructorId, i.Lastname AS InstructorLastname, i.Firstname AS InstructorFirstname, i.Age AS InstructorAge, " +
	                 "i.Address AS InstructorAddress, i.Email AS InstructorEmail, i.HourlyRate, " +
	                 "b.BookingId, b.BookingDate, b.Duration, b.Price AS BookingPrice, b.GroupSize, b.IsSpecial, " +
	                 "s.SkierId, s.Lastname AS SkierLastname, s.Firstname AS SkierFirstname, s.Age AS SkierAge, " +
	                 "s.Address AS SkierAddress, s.Email AS SkierEmail, s.Insurance, s.Level AS SkierLevel, " +
	                 "p.PeriodId, p.StartDate, p.EndDate, p.IsVacation " +
	                 "FROM Lesson l " +
	                 "INNER JOIN Instructor i ON l.InstructorId_FK = i.InstructorId " +
	                 "INNER JOIN LessonType lt ON l.LessonTypeId_FK = lt.LessonTypeId " +
	                 "INNER JOIN Accreditation a ON lt.AccreditationId_FK = a.AccreditationId " +
	                 "LEFT JOIN Booking b ON b.LessonId_FK = l.LessonId " +
	                 "LEFT JOIN Skier s ON s.SkierId = b.SkierId_FK " +
	                 "LEFT JOIN Period p ON p.PeriodId = b.PeriodId_FK";

	    try (PreparedStatement stmt = connect.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            int lessonId = rs.getInt("LessonId");
                int minBookings = rs.getInt("MinBookings");
                int maxBookings = rs.getInt("MaxBookings");
                String schedule = rs.getString("Schedule");
                
                int lessonTypeId = rs.getInt("LessonTypeId");
                String lessonTypeLevel = rs.getString("Level");
                double lessonTypePrice = rs.getDouble("Price");
                
                int accreditationId = rs.getInt("AccreditationId");
                String accreditationName = rs.getString("AccreditationName");
                
                int instructorId = rs.getInt("InstructorId");
                String instructorLastname = rs.getString("InstructorLastname");
                String instructorFirstname = rs.getString("InstructorFirstname");
                int instructorAge = rs.getInt("InstructorAge");
                String instructorAddress = rs.getString("InstructorAddress");
                String instructorEmail = rs.getString("InstructorEmail");
                double instructorHourlyRate = rs.getDouble("HourlyRate");
                
	            Lesson lesson = lessonMap.computeIfAbsent(lessonId, id -> {
	                LessonType lessonType = lessonTypeMap.computeIfAbsent(lessonTypeId, ltId -> {
	                    LessonType lt = new LessonType();
	                    lt.setId(lessonTypeId);
	                    lt.setLevel(lessonTypeLevel);
	                    lt.setPrice(lessonTypePrice);
	                    ArrayList<LessonType> lessonTypeList = new ArrayList<LessonType>();
	                    lessonTypeList.add(lt);
	                    Accreditation accreditation = new Accreditation(accreditationId, accreditationName, lessonTypeList);
	                    lt.setAccreditation(accreditation);
	                    return lt;
	                });

	                Instructor instructor = instructorMap.computeIfAbsent(instructorId, instId -> {
	                    Instructor inst = new Instructor();
	                    inst.setId(instructorId);
	                    inst.setLastname(instructorLastname);
	                    inst.setFirstname(instructorFirstname);
	                    inst.setAge(instructorAge);
	                    inst.setAddress(instructorAddress);
	                    inst.setEmail(instructorEmail);
	                    inst.setHourlyRate(instructorHourlyRate);
	                    return inst;
	                });

	                Lesson newLesson = new Lesson(lessonId, minBookings, maxBookings, schedule, lessonType, instructor);
	                newLesson.setBookingList(new ArrayList<>());
	                return newLesson;
	            });
	            
	            int skierId = rs.getInt("SkierId");
	            String skierLastname = rs.getString("SkierLastname");
	            String skierFirstname = rs.getString("SkierFirstname");
	            int skierAge = rs.getInt("SkierAge");
	            String skierAddress = rs.getString("SkierAddress");
	            String skierEmail = rs.getString("SkierEmail");
	            boolean skierInsurance = rs.getBoolean("Insurance");
	            String skierLevel = rs.getString("SkierLevel");

	            Skier skier = new Skier();
	            skier.setId(skierId);
	            skier.setLastname(skierLastname);
	            skier.setFirstname(skierFirstname);
	            skier.setAge(skierAge);
	            skier.setAddress(skierAddress);
	            skier.setEmail(skierEmail);
	            skier.setInsurance(skierInsurance);
	            skier.setLevel(skierLevel);

	            Period period = new Period(
	                rs.getInt("PeriodId"),
	                rs.getDate("StartDate") != null ? rs.getDate("StartDate").toLocalDate() : null,
	                rs.getDate("EndDate") != null ? rs.getDate("EndDate").toLocalDate() : null,
	                rs.getBoolean("IsVacation")
	            );

	            Booking booking = new Booking(
	                rs.getInt("BookingId"),
	                rs.getDate("BookingDate")!= null ? rs.getDate("BookingDate").toLocalDate() : null,
	                rs.getInt("Duration"),
	                rs.getDouble("BookingPrice"),
	                rs.getInt("GroupSize"),
	                rs.getBoolean("IsSpecial"),
	                lesson.getInstructor(),
	                lesson,
	                period,
	                skier
	            );

	            lesson.addBooking(booking);
	        }

	        lessonList.addAll(lessonMap.values());

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lessonList;
	}

}
