package be.pierard.pojo;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import be.pierard.dao.LessonTypeDAO;

public class LessonType {
	private int id;
	private String level;
	private double price;
	private Accreditation accreditation;
	private ArrayList<Lesson> lessonList;
	
	//CTOR
	public LessonType() {}
	public LessonType(int id, String level, double price, Accreditation accreditation,
			ArrayList<Lesson> lessonList) {
		this.id = id;
		this.level = level;
		this.price = price;
		if(accreditation.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "l'accreditation ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.accreditation = accreditation;
		if(lessonList.isEmpty())
			this.lessonList = new ArrayList<Lesson>();
		else
			this.lessonList = lessonList;
	}

	public LessonType(int id, String level, double price, Accreditation accreditation) {
		this.id = id;
		this.level = level;
		this.price = price;
		if(accreditation.equals(null)) {
			JOptionPane.showMessageDialog(
	                null,
	                "l'accreditation ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
		else
			this.accreditation = accreditation;
		this.lessonList = new ArrayList<Lesson>();
	}

	//Getters/setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Accreditation getAccreditation() {
		return accreditation;
	}

	public void setAccreditation(Accreditation accreditation) {
		this.accreditation = accreditation;
	}

	public ArrayList<Lesson> getLessonList() {
		return lessonList;
	}

	public void setLessonList(ArrayList<Lesson> lessonList) {
		this.lessonList = lessonList;
	}
	
	//Business methods
	public String getFullLevel() {
		return "Level: " + accreditation.getName()+ " " + level + ", Price: " + price + "€/week";
	}
	
	//DAO methods
	public boolean createLessonType(LessonTypeDAO lessonTypeDAO) {
		return lessonTypeDAO.create(this);
	}
	
	public boolean updateLessonType(LessonTypeDAO lessonTypeDAO) {
		return lessonTypeDAO.update(this);
	}
	
	public static ArrayList<LessonType> findAllLessonType(LessonTypeDAO lessonTypeDAO){
		return lessonTypeDAO.findAll();
	}

	//Usual methods
	@Override
	public int hashCode() {
		return Objects.hash(accreditation, id, lessonList, level, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LessonType other = (LessonType) obj;
		return Objects.equals(accreditation, other.accreditation) && id == other.id
				&& Objects.equals(lessonList, other.lessonList) && Objects.equals(level, other.level)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price);
	}

	@Override
	public String toString() {
		return "LessonType {" +
				"id=" + id + 
				", level=" + level + 
				", price=" + price + 
				", accreditation=" + accreditation + 
				", lessonList=" + (lessonList != null ?
						lessonList.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")) : "[]") + 
				"}";
	}
}
