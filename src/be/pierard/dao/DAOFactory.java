package be.pierard.dao;

import java.sql.Connection;

import be.pierard.pojo.*;

public class DAOFactory extends AbstractDAOFactory{
	protected static final Connection connect = EcoleSkiConnection.getInstance();
	public DAO<Accreditation> getAccreditationDAO(){
		return new AccreditationDAO(connect);
	}
	public DAO<LessonType> getLessonTypeDAO(){
		return new LessonTypeDAO(connect);
	}
	public DAO<Skier> getSkierDAO(){
		return new SkierDAO(connect);
	}
	public DAO<Instructor> getInstructorDAO(){
		return new InstructorDAO(connect);
	}
	public DAO<Booking> getBookingDAO(){
		return new BookingDAO(connect);
	}
	public DAO<Period> getPeriodDAO(){
		return new PeriodDAO(connect);
	}
	public DAO<Lesson> getLessonDAO(){
		return new LessonDAO(connect);
	}
	
}
