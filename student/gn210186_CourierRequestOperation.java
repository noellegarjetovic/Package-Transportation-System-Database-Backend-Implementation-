package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.CourierRequestOperation;

public class gn210186_CourierRequestOperation implements CourierRequestOperation {

	Connection conn = DB.getInstance().getConnection();
	
	@Override
	public boolean changeVehicleInCourierRequest(String username, String licencePlateNumber) {
	    try (PreparedStatement check = conn.prepareStatement("Select RegistarskiBroj from Vozilo where RegistarskiBroj = ?");
	         PreparedStatement updateSt = conn.prepareStatement("Update ZahteviKuriri set RegistarskiBroj = ? where KorIme = ?")) {
	         
	       
	        check.setString(1, licencePlateNumber);
	        try (ResultSet rs = check.executeQuery()) {
	            if (rs.next()) {
	            	updateSt.setString(1, licencePlateNumber);
	    	        updateSt.setString(2, username);
	    	        int row = updateSt.executeUpdate();
	    	        if(row>0)
	    	        	System.out.println("prosao promenu vozila za kurira");
	    	        	return true;
	            }
	        }

	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        
	    }
	    return false;
	}

	@Override
	public boolean deleteCourierRequest(String userName) {
		try (PreparedStatement deleteSt=conn.prepareStatement("Delete from ZahteviKuriri where KorIme=?");){
			deleteSt.setString(1, userName);
			int row=deleteSt.executeUpdate();
			if (row>0) {
				System.out.println("prosao brisanje kurira");
				return true;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<String> getAllCourierRequests() {
        List<String> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("Select KorIme from ZahteviKuriri")) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("liste svih zahteva kurira");
        System.out.println(list);
        return list;
    }
	

	@Override
	public boolean grantRequest(String userName) {
        try (PreparedStatement st = conn.prepareStatement("Select Id from ZahteviKuriri where KorIme = ?")) {
            st.setString(1, userName);
            try(ResultSet rs = st.executeQuery();){

	            if (rs.next()) {
	                int id = rs.getInt(1);
	                try (CallableStatement stmt = conn.prepareCall("{call SPgrantRequest(?)}")) {
	                    stmt.setInt(1, id);
	                    stmt.execute();
	                    System.out.println("prosao prihvatanje zahteva");
	                    return true;
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                    return false;
	                }
	            } else {
	                return false;
	            }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
	}

	@Override
	public boolean insertCourierRequest(String userName, String licencePlateNumber) {
	    try (PreparedStatement checkK = conn.prepareStatement("Select KorIme from Korisnik where KorIme = ?");
	         PreparedStatement checkZ = conn.prepareStatement("Select KorIme from ZahteviKuriri where KorIme = ?");
	         PreparedStatement checkV = conn.prepareStatement("Select RegistarskiBroj from Vozilo where RegistarskiBroj = ?");
	         PreparedStatement insertSt = conn.prepareStatement("Insert into ZahteviKuriri (KorIme, RegistarskiBroj) values (?, ?)")) {
	         
	        
	        checkK.setString(1, userName);
	        try (ResultSet rsK = checkK.executeQuery()) {
	            if (!rsK.next()) {
	                return false;
	            }
	        }
	        checkZ.setString(1, userName);
	        try (ResultSet rsZ = checkZ.executeQuery()) {
	            if (rsZ.next()) {
	                return false;
	            }
	        }
	        checkV.setString(1, licencePlateNumber);
	        try (ResultSet rsV = checkV.executeQuery()) {
	            if (!rsV.next()) {
	                return false;
	            }
	        }
	        insertSt.setString(1, userName);
	        insertSt.setString(2, licencePlateNumber);
	        int row = insertSt.executeUpdate();
	        if(row>0)
	        	System.out.println("prosao dodavanje kurira");
	        	return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
