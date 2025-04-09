package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import rs.etf.sab.operations.CityOperations;

public class gn210186_CityOperations implements CityOperations {
	
	Connection conn=DB.getInstance().getConnection();

	@Override
	public boolean deleteCity(int idCity) {
		try (PreparedStatement deleteSt=conn.prepareStatement("Delete from Grad where Id=?");){
			deleteSt.setInt(1, idCity);
			int row=deleteSt.executeUpdate();
			if (row>0) return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int deleteCity(String... names) {
		int rows=0;
		try (PreparedStatement deleteSt=conn.prepareStatement("Delete from Grad where Naziv=?");){
			for (String name : names) {
				deleteSt.setString(1, name);
				rows+=deleteSt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public List<Integer> getAllCities() {
		List<Integer> list=new ArrayList();
		try (Statement st=conn.createStatement();
				ResultSet rs=st.executeQuery("Select Id from Grad");){
			
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int insertCity(String name, String postalCode) {
		try (PreparedStatement check = conn.prepareStatement("SELECT Id FROM Grad WHERE Naziv = ? OR PostanskiBroj = ?");
			 PreparedStatement insertSt=conn.prepareStatement("Insert into Grad (Naziv, PostanskiBroj) values (?,?)",Statement.RETURN_GENERATED_KEYS);){
			check.setString(1, name);
			check.setString(2, postalCode);
			try(ResultSet rs=check.executeQuery()){
				if(!rs.next()) {
			
					insertSt.setString(1, name);
					insertSt.setString(2, postalCode);
					int row=insertSt.executeUpdate();
					if (row>0) {
						try(ResultSet id=insertSt.getGeneratedKeys();){
							if (id.next()) {
								return id.getInt(1);
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
