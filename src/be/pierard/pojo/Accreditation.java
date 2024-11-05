package be.pierard.pojo;

import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JOptionPane;

public class Accreditation {
    private int id;
    private String name;
    private ArrayList<Instructor> instructorList;
    private ArrayList<LessonType> lessonTypeList;
    
    //CTOR
    public Accreditation(int id, String name, ArrayList<LessonType> lessonTypeList) {
    	this.id = id;
		this.name = name;
		this.instructorList = new ArrayList<Instructor>();
		if(!lessonTypeList.isEmpty())
			this.lessonTypeList = lessonTypeList;
		else {
			JOptionPane.showMessageDialog(
	                null,
	                "La liste de type de leçons pour les accreditations ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
    }
	public Accreditation(int id, String name, ArrayList<Instructor> instructorList,
			ArrayList<LessonType> lessonTypeList) {
		this.id = id;
		this.name = name;
		this.instructorList = instructorList;
		if(!lessonTypeList.isEmpty())
			this.lessonTypeList = lessonTypeList;
		else {
			JOptionPane.showMessageDialog(
	                null,
	                "La liste de type de leçons pour les accreditations ne doit pas être vide.",
	                "Erreur",
	                JOptionPane.ERROR_MESSAGE
	            );
			throw new IllegalArgumentException();
		}
	}
	
	//Getters/Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Instructor> getInstructorList() {
		return instructorList;
	}
	public void setInstructorList(ArrayList<Instructor> instructorList) {
		this.instructorList = instructorList;
	}
	public ArrayList<LessonType> getLessonTypeList() {
		return lessonTypeList;
	}
	public void setLessonTypeList(ArrayList<LessonType> lessonTypeList) {
		this.lessonTypeList = lessonTypeList;
	}
	
	//Usual methods
	@Override
	public int hashCode() {
		return Objects.hash(id, instructorList, lessonTypeList, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Accreditation other = (Accreditation) obj;
		return id == other.id && Objects.equals(instructorList, other.instructorList)
				&& Objects.equals(lessonTypeList, other.lessonTypeList) && Objects.equals(name, other.name);
	}
	@Override
	public String toString() {
		return "Accreditation {" +
	            "id=" + id +
	            ", name='" + name + '\'' +
	            ", instructorList=" + (instructorList != null ? instructorList.toString() : "[]") +
	            ", lessonTypeList=" + (lessonTypeList != null ? lessonTypeList.toString() : "[]") +
	            '}';
	}
}
