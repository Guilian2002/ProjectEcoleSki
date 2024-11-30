package be.pierard.dao;

import java.sql.Connection;
import java.util.ArrayList;

import be.pierard.pojo.LessonType;

public class LessonTypeDAO extends DAO<LessonType>{
	public LessonTypeDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(LessonType obj){		
		return false;
	}
	
	public boolean delete(LessonType obj){
		return false;
	}
	
	public boolean update(LessonType obj){
		return false;
	}
	
	public LessonType find(int id){
		return null;
	}

	public ArrayList<LessonType> findAll() {
		return null;
	}
}
