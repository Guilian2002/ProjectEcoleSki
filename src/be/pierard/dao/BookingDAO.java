package be.pierard.dao;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.pierard.pojo.*;

public class BookingDAO extends DAO<Booking>{
	public BookingDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(Booking obj){		
		String sql = "INSERT INTO Booking (BookingDate, Duration, Price, GroupSize, IsSpecial, LessonId_FK, SkierId_FK, PeriodId_FK) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(obj.getDate()));
            stmt.setInt(2, obj.getDuration());
            stmt.setDouble(3, obj.getPrice());
            stmt.setInt(4, obj.getGroupSize());
            stmt.setBoolean(5, obj.isSpecial());
            stmt.setInt(6, obj.getLesson().getId());
            stmt.setInt(7, obj.getSkier().getId());
            stmt.setInt(8, obj.getPeriod().getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public boolean delete(Booking obj){
		return false;
	}
	
	public boolean update(Booking obj){
        return false;
	}
	
	public Booking find(int id){
		return null;
	}

	public ArrayList<Booking> findAll() {
	    ArrayList<Booking> bookings = new ArrayList<>();
	    String sql = "SELECT l.LessonId, l.MinBookings, l.MaxBookings, l.Schedule, " +
	                 "lt.LessonTypeId, lt.Level, lt.Price, " +
	                 "a.AccreditationId, a.AccreditationName, " +
	                 "i.InstructorId, i.Lastname AS InstructorLastname, i.Firstname AS InstructorFirstname, i.Age AS InstructorAge, " +
	                 "i.Address AS InstructorAddress, i.Email AS InstructorEmail, i.HourlyRate, " +
	                 "b.BookingId, b.BookingDate, b.Duration, b.Price AS BookingPrice, b.GroupSize, b.IsSpecial, " +
	                 "s.SkierId, s.Lastname AS SkierLastname, s.Firstname AS SkierFirstname, s.Age AS SkierAge, " +
	                 "s.Address AS SkierAddress, s.Email AS SkierEmail, s.Insurance, s.Level AS SkierLevel, " +
	                 "p.PeriodId, p.StartDate, p.EndDate, p.IsVacation " +
	                 "FROM Booking b " +
	                 "INNER JOIN Lesson l ON l.LessonId = b.LessonId_FK " +
	                 "INNER JOIN Instructor i ON i.InstructorId = l.InstructorId_FK " +
	                 "INNER JOIN LessonType lt ON lt.LessonTypeId = l.LessonTypeId_FK " +
	                 "INNER JOIN Accreditation a ON a.AccreditationId = lt.AccreditationId_FK " +
	                 "INNER JOIN Skier s ON s.SkierId = b.SkierId_FK " +
	                 "INNER JOIN Period p ON p.PeriodId = b.PeriodId_FK ";

	    try (PreparedStatement stmt = connect.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        Map<Integer, Instructor> instructorMap = new HashMap<>();
	        Map<Integer, Skier> skierMap = new HashMap<>();
	        Map<Integer, Period> periodMap = new HashMap<>();
	        Map<Integer, Lesson> lessonMap = new HashMap<>();
	        Map<Integer, LessonType> lessonTypeMap = new HashMap<>();
	        Map<Integer, Accreditation> accreditationMap = new HashMap<>();

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

	            LessonType lessonType = lessonTypeMap.computeIfAbsent(lessonTypeId, ltId -> {
	                LessonType lt = new LessonType();
	                lt.setId(lessonTypeId);
	                lt.setLevel(lessonTypeLevel);
	                lt.setPrice(lessonTypePrice);
	                return lt;
	            });

	            Accreditation accreditation = accreditationMap.computeIfAbsent(accreditationId, accId -> {
	                Accreditation acc = new Accreditation();
	                acc.setId(accreditationId);
	                acc.setName(accreditationName);
	                return acc;
	            });

	            Lesson lesson = lessonMap.computeIfAbsent(lessonId, id -> {
	                Lesson newLesson = new Lesson();
	                newLesson.setId(lessonId);
	                newLesson.setMinBookings(minBookings);
	                newLesson.setMaxBookings(maxBookings);
	                newLesson.setSchedule(schedule);
	                newLesson.setLessonType(lessonType);
	                newLesson.setInstructor(instructor);
	                lessonType.setAccreditation(accreditation);
	                return newLesson;
	            });

	            int periodId = rs.getInt("PeriodId");
	            Period period = periodMap.computeIfAbsent(periodId, id -> {
	                Period newPeriod = new Period();
	                newPeriod.setId(periodId);
	                try {
						newPeriod.setStartDate(rs.getDate("StartDate") != null ? rs.getDate("StartDate").toLocalDate() : null);
		                newPeriod.setEndDate(rs.getDate("EndDate") != null ? rs.getDate("EndDate").toLocalDate() : null);
		                newPeriod.setVacation(rs.getBoolean("IsVacation"));
					} catch (SQLException e) {
						e.printStackTrace();
					}
	                return newPeriod;
	            });

	            int skierId = rs.getInt("SkierId");
	            Skier skier = skierMap.computeIfAbsent(skierId, id -> {
	                Skier newSkier = new Skier();
	                newSkier.setId(skierId);
	                try {
						newSkier.setLastname(rs.getString("SkierLastname"));
		                newSkier.setFirstname(rs.getString("SkierFirstname"));
		                newSkier.setAge(rs.getInt("SkierAge"));
		                newSkier.setAddress(rs.getString("SkierAddress"));
		                newSkier.setEmail(rs.getString("SkierEmail"));
		                newSkier.setInsurance(rs.getBoolean("Insurance"));
		                newSkier.setLevel(rs.getString("SkierLevel"));
					} catch (SQLException e) {
						e.printStackTrace();
					}
	                return newSkier;
	            });

	            Booking booking = new Booking(
	                rs.getInt("BookingId"),
	                rs.getDate("BookingDate") != null ? rs.getDate("BookingDate").toLocalDate() : null,
	                rs.getInt("Duration"),
	                rs.getDouble("BookingPrice"),
	                rs.getInt("GroupSize"),
	                rs.getBoolean("IsSpecial"),
	                instructor,
	                lesson,
	                period,
	                skier
	            );

	            lesson.addBooking(booking);
	            bookings.add(booking);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return bookings;
	}

}
