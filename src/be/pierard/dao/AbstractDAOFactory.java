package be.pierard.dao;

import be.pierard.pojo.*;

public abstract class AbstractDAOFactory {
	public static final int DAO_FACTORY = 0;
	public abstract DAO<Accreditation> getAccreditationDAO();
	public abstract DAO<LessonType> getLessonTypeDAO();
	public abstract DAO<Skier> getSkierDAO();
	public abstract DAO<Instructor> getInstructorDAO();
	public abstract DAO<Booking> getBookingDAO();
	public abstract DAO<Period> getPeriodDAO();
	public abstract DAO<Lesson> getLessonDAO();
	public static AbstractDAOFactory getFactory(int type){
		switch(type){
			case DAO_FACTORY:
				return new DAOFactory();
			default:
			return null;
		}
	}
}
