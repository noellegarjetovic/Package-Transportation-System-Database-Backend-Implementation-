package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.DistrictOperations;

public class gn210186_DistrictOperations implements DistrictOperations{

	Connection conn=DB.getInstance().getConnection();
	
	@Override
	public int deleteAllDistrictsFromCity(String nameOfTheCity) {
		try (PreparedStatement st=conn.prepareStatement("Delete from Opstina where GradId in (Select Id from Grad where Naziv = ?)");){
			st.setString(1, nameOfTheCity);
			
			int row=st.executeUpdate();
			return row;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean deleteDistrict(int idDistrict) {
		try (PreparedStatement deleteSt=conn.prepareStatement("Delete from Opstina where Id=?");){
			deleteSt.setInt(1, idDistrict);
			int row=deleteSt.executeUpdate();
			if (row>0) return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int deleteDistricts(String... names) {
		int rows=0;
		try (PreparedStatement deleteSt=conn.prepareStatement("Delete from Opstina where Naziv=?");){
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
	public List<Integer> getAllDistricts() {
		List<Integer> list=new ArrayList();
		try (Statement st=conn.createStatement();
				ResultSet rs=st.executeQuery("Select Id from Opstina");){
			
			while(rs.next()) {
				list.add(rs.getInt(1));
			}
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Integer> getAllDistrictsFromCity(int idCity) {
		List<Integer> list=new ArrayList();
		try (PreparedStatement st=conn.prepareStatement("Select Id from Opstina where GradId=?");){
			st.setInt(1, idCity);
			
			try(ResultSet rs=st.executeQuery();){
				while(rs.next()) {
					list.add(rs.getInt(1));
				}
				return list;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int insertDistrict(String name, int cityId, int xCord, int yCord) {
        try (PreparedStatement check = conn.prepareStatement("Select Id From Grad Where Id = ?")) {
            check.setInt(1, cityId);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
					try (PreparedStatement insertSt=conn.prepareStatement("Insert into Opstina (Naziv, GradId, X, Y) values (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);){
						insertSt.setString(1, name);
						insertSt.setInt(2, cityId);
						insertSt.setInt(3, xCord);
						insertSt.setInt(4, yCord);
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
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}

