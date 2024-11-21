package be.pierard.pojo;

import java.time.LocalDate;
import java.util.Objects;

import javax.swing.JOptionPane;

public class Booking {
	private int id;
	private LocalDate date;
	private int duration;
	private double price;
	private int groupSize;
	private boolean isSpecial;
	private Instructor instructor;
	private Lesson lesson;
	private Period period;
	private Skier skier;
	
	//CTOR
	public Booking(int id, LocalDate date, int duration, double price, int groupSize, boolean isSpecial,Instructor instructor,
			Lesson lesson, Period period, Skier skier) {
		this.id = id;
		this.date = date;
		this.duration = duration;
		this.price = price;
		this.isSpecial = isSpecial;
		this.groupSize = groupSize;
		if(instructor.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "l'instructeur ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.instructor = instructor;
		if(lesson.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "la leçon ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.lesson = lesson;
		if(period.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "la période ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.period = period;
		if(skier.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "le skieur ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.skier = skier;
	}

	//Getters/setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public void setGroupSize(int groupSize) {
		this.groupSize = groupSize;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Skier getSkier() {
		return skier;
	}

	public void setSkier(Skier skier) {
		this.skier = skier;
	}
	
	//Business methods
	public double calculatePrice() {
	    double totalPrice = 0.0;
	    double basePrice = lesson.getLessonPrice();
	    int totalWeeks = (int) Math.ceil((double) duration / 7);

	    if (isSpecial) {
	        if (duration == 1) {
	            basePrice = 60.0;
	        } else if (duration == 2) {
	            basePrice = 90.0;
	        } else {
	            throw new IllegalArgumentException("Durée de cours particulier invalide.");
	        }
	        if (period.isDuringVacation(date)) {
	            if (date.isBefore(LocalDate.now().plusWeeks(1))) {
	                throw new IllegalArgumentException("Cours particulier : réservation 1 semaine à l'avance requise en période scolaire.");
	            }
	        } else {
	            if (date.isBefore(LocalDate.now().plusMonths(1))) {
	                throw new IllegalArgumentException("Cours particulier : réservation 1 mois à l'avance requise hors période scolaire.");
	            }
	        }
	        totalPrice = basePrice * groupSize;

	    } else {
	        if (!lesson.isBookingValidForLesson(this)) {
	            throw new IllegalArgumentException("Nombre d'élèves non conforme pour un cours collectif.");
	        }

	        totalPrice = basePrice;

	        if (lesson.getSchedule().equalsIgnoreCase("matin+après-midi")) {
	            totalPrice *= 0.85;
	        }
	    }
        if (skier.isInsurance()) {
            totalPrice += 20.0 * totalWeeks;
        }

	    return totalPrice;
	}
	
	//Usual methods
	@Override
	public int hashCode() {
		return Objects.hash(date, duration, groupSize, id, instructor, lesson, period, price, skier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;
		return Objects.equals(date, other.date) && duration == other.duration && groupSize == other.groupSize
				&& id == other.id && Objects.equals(instructor, other.instructor)
				&& Objects.equals(lesson, other.lesson) && Objects.equals(period, other.period)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(skier, other.skier);
	}

	@Override
	public String toString() {
		return "Booking {" + 
				"id=" + id + 
				", date=" + date + 
				", duration=" + duration + 
				", price=" + price + 
				", groupSize=" + groupSize + 
				", instructor=" + instructor + 
				", lesson=" + lesson + 
				", period=" + period + 
				", skier=" + skier + 
				"}";
	}

}
