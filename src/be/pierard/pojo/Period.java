package be.pierard.pojo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import be.pierard.dao.PeriodDAO;

public class Period {
	private int id;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean isVacation;
	private ArrayList<Booking> bookingList;
	
	//CTOR
	public Period(int id, LocalDate startDate, LocalDate endDate, boolean isVacation, ArrayList<Booking> bookingList) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isVacation = isVacation;
		if(bookingList.isEmpty())
			this.bookingList = new ArrayList<Booking>();
		else
			this.bookingList = bookingList;
	}
	
	public Period(int id, LocalDate startDate, LocalDate endDate, boolean isVacation) {
		this.id = id;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isVacation = isVacation;
		this.bookingList = new ArrayList<Booking>();
	}

	//Getters/setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isVacation() {
		return isVacation;
	}

	public void setVacation(boolean isVacation) {
		this.isVacation = isVacation;
	}

	public ArrayList<Booking> getBookingList() {
		return bookingList;
	}

	public void setBookingList(ArrayList<Booking> bookingList) {
		this.bookingList = bookingList;
	}
	
	//Business methods
	public boolean isDateWithinPeriod(LocalDate date) {
	    return (date.isEqual(startDate) || date.isAfter(startDate)) && 
	           (date.isEqual(endDate) || date.isBefore(endDate));
	}

	public boolean isDuringVacation(LocalDate date) {
	    return isVacation && isDateWithinPeriod(date);
	}
	
	//DAO methods
	public boolean createPeriod(PeriodDAO periodDAO) {
		return periodDAO.create(this);
	}
	
	public boolean updatePeriod(PeriodDAO periodDAO) {
		return periodDAO.update(this);
	}
	
	public static ArrayList<Period> findAllPeriod(PeriodDAO periodDAO){
		return periodDAO.findAll();
	}

	//Usual methods
	@Override
	public int hashCode() {
		return Objects.hash(bookingList, endDate, id, isVacation, startDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Period other = (Period) obj;
		return Objects.equals(bookingList, other.bookingList) && Objects.equals(endDate, other.endDate)
				&& id == other.id && isVacation == other.isVacation && Objects.equals(startDate, other.startDate);
	}

	@Override
	public String toString() {
		return "Period {" + 
				"id=" + id + 
				", startDate=" + startDate + 
				", endDate=" + endDate + 
				", isVacation=" + (isVacation ? "yes" : "no") +
				", bookingList=" + (bookingList != null ?
						bookingList.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")) : "[]") + 
				"}";
	}
}
