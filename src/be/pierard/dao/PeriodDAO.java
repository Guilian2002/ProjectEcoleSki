package be.pierard.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import be.pierard.pojo.Period;

public class PeriodDAO extends DAO<Period>{
	public PeriodDAO(Connection conn){
		super(conn);
	}
	
	public boolean create(Period obj){		
		String sql = "INSERT INTO Period (StartDate, EndDate, IsVacation) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(obj.getStartDate()));
            stmt.setDate(2, Date.valueOf(obj.getEndDate()));
            stmt.setBoolean(3, obj.isVacation());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public boolean delete(Period obj){
		return false;
	}
	
	public boolean update(Period obj){
		String sql = "UPDATE Period SET StartDate = ?, EndDate = ?, IsVacation = ? WHERE PeriodId = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(obj.getStartDate()));
            stmt.setDate(2, Date.valueOf(obj.getEndDate()));
            stmt.setBoolean(3, obj.isVacation());
            stmt.setInt(4, obj.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	public Period find(int id){
		return null;
	}

	public ArrayList<Period> findAll() {
		ArrayList<Period> periods = new ArrayList<>();
        String sql = "SELECT * FROM Period";

        try (PreparedStatement stmt = connect.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Period period = new Period(rs.getInt("PeriodId"), rs.getDate("StartDate").toLocalDate(),
                		rs.getDate("EndDate").toLocalDate(), rs.getBoolean("IsVacation"));
                periods.add(period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return periods;
	}
}
