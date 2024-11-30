package be.pierard.pojo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JOptionPane;

import be.pierard.dao.PeriodDAO;

public class Period {
	private int id;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean isVacation;
	private ArrayList<Booking> bookingList;
	
	//CTOR
	public Period() {}
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

	private boolean isDuringVacation(LocalDate date) {
		ArrayList<Period> vacationPeriods = new ArrayList<>();
        vacationPeriods.add(new Period(1, LocalDate.of(2024, 12, 21), LocalDate.of(2025, 1, 4), true));
        vacationPeriods.add(new Period(2, LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 8), true));
        vacationPeriods.add(new Period(3, LocalDate.of(2025, 4, 12), LocalDate.of(2025, 4, 26), true));
        return vacationPeriods.stream()
                .anyMatch(period -> !date.isBefore(period.getStartDate()) && !date.isAfter(period.getEndDate()));
    }
	
	public void addBooking(Booking booking) {
        if (!bookingList.contains(booking)) {
        	bookingList.add(booking);
        }
    }
	
	public boolean makePeriod(PeriodDAO periodDAO) {
		LocalDate stationOpenDate = LocalDate.of(2024, 12, 6);
        LocalDate stationCloseDate = LocalDate.of(2025, 5, 3);
        
        if (!isDateWithinStationPeriod(stationOpenDate, stationCloseDate)) {
        	JOptionPane.showMessageDialog(null, "Error: Period startDate or endDate is not within station open/close date.",
		               "Date Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        this.isVacation = isDuringVacation(startDate) && isDuringVacation(endDate);
    	if (!createPeriod(periodDAO)) {
 	       JOptionPane.showMessageDialog(null, "Error: Failed to save period data to the database.",
 	               "Database Error", JOptionPane.ERROR_MESSAGE);
 	       return false;
 		}
 		JOptionPane.showMessageDialog(
 			null,
 			"Success: The period has been successfully created.",
 			"Operation Successful",
 			JOptionPane.INFORMATION_MESSAGE
 		);
        return true;
    }
	
	public int getNumberOfDays() {
	    return (int) ChronoUnit.DAYS.between(startDate, endDate);
	}
	
	public int getNumberOfWeeks() {
	    return (int) Math.ceil(getNumberOfDays() / 7.0);
	}
	
	private boolean isDateWithinStationPeriod(LocalDate stationOpenDate, LocalDate stationCloseDate) {
        return !(startDate.isBefore(stationOpenDate) || endDate.isAfter(stationCloseDate));
    }
	
	//DAO methods
	public boolean createPeriod(PeriodDAO periodDAO) {
		return periodDAO.create(this);
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
				"startDate =" + startDate + 
				", endDate =" + endDate + 
				", isVacation =" + (isVacation ? "yes" : "no") +
				"}";
	}

	public boolean isWithinOneWeek() {
	    LocalDate currentDate = LocalDate.now();
	    LocalDate oneWeekLater = currentDate.plusWeeks(1);
	    return !this.getStartDate().isAfter(oneWeekLater);
	}

	public boolean isWithinOneMonth() {
	    LocalDate currentDate = LocalDate.now();
	    LocalDate oneMonthLater = currentDate.plusMonths(1);
	    return !this.getStartDate().isAfter(oneMonthLater);
	}
}
