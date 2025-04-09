package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.VehicleOperations;

public class gn210186_VehicleOperations implements VehicleOperations {
	
	Connection conn=DB.getInstance().getConnection();

	@Override
	public boolean changeConsumption(String licencePlateNumber, BigDecimal fuelConsumption) {
		try(PreparedStatement check=conn.prepareStatement("Select RegistarskiBroj from Vozilo where RegistarskiBroj=?");){
			check.setString(1, licencePlateNumber);
			try(ResultSet rs=check.executeQuery();){
				if(rs.next()) {
					try(PreparedStatement updateSt=conn.prepareStatement("Update Vozilo set Potrosnja=? where RegistarskiBroj=?");){
						updateSt.setBigDecimal(1, fuelConsumption);
						updateSt.setString(2, licencePlateNumber);
						int res=updateSt.executeUpdate();
						if(res>0) {
							return true;
						}
				}
			}
		}
		}
		catch(SQLException e) {}
		return false;
	}

	@Override
	public List<String> getAllVehichles() {
		List<String> list=new LinkedList<>();
		try (Statement st=conn.createStatement();
				ResultSet rs=st.executeQuery("Select RegistarskiBroj from Vozilo");){
			
			while(rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean changeFuelType(String licencePlateNumber, int fuelType) {
		try(PreparedStatement check=conn.prepareStatement("Select RegistarskiBroj from Vozilo where RegistarskiBroj=?");){
			check.setString(1, licencePlateNumber);
			try(ResultSet rs=check.executeQuery();){
				if(rs.next()){
					try(PreparedStatement updateSt=conn.prepareStatement("Update Vozilo set TipGoriva=? where RegistarskiBroj=?");){
						updateSt.setInt(1, fuelType);
						updateSt.setString(2, licencePlateNumber);
						int res=updateSt.executeUpdate();
						if(res>0) {
							return true;
						}
				}
			}
		}
		}
		catch(SQLException e) {}
		return false;
	}

	@Override
	public int deleteVehicles(String... licencePlateNumbers) {
		int rows=0;
		try (PreparedStatement deleteSt=conn.prepareStatement("Delete from Vozilo where RegistarskiBroj=?");){
			for (String licence : licencePlateNumbers) {
				deleteSt.setString(1, licence);
				rows+=deleteSt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public boolean insertVehicle(String licencePlateNumber, int fuelType, BigDecimal fuelConsumption) {
		try(PreparedStatement check=conn.prepareStatement("Select RegistarskiBroj from Vozilo where RegistarskiBroj=?")){
			check.setString(1, licencePlateNumber);
			try(ResultSet rs=check.executeQuery()){
				if(!rs.next()) {
					try (PreparedStatement insertSt=conn.prepareStatement("Insert into Vozilo (RegistarskiBroj, TipGoriva, Potrosnja) values (?,?,?)");){
						insertSt.setString(1, licencePlateNumber);
						insertSt.setInt(2, fuelType);
						insertSt.setBigDecimal(3, fuelConsumption);
						int row=insertSt.executeUpdate();
						if (row>0) {
							return true;
					}
				}
			}
		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


}
