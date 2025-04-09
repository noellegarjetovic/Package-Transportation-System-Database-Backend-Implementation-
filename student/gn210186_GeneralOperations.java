package student;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import rs.etf.sab.operations.GeneralOperations;

public class gn210186_GeneralOperations implements GeneralOperations {

	Connection conn=DB.getInstance().getConnection();
	
	@Override
	public void eraseAll() {
		String[] querys = {
				"DELETE FROM Paket",
	            "DELETE FROM Opstina",
	            "DELETE FROM ZahteviKuriri",
	            "DELETE FROM Ponuda",
	            "DELETE FROM Grad",
	            "DELETE FROM Kuriri",
	            "DELETE FROM Administratori",
	            "DELETE FROM Korisnik",
	            "DELETE FROM Vozilo"
	        };
	        
	    try (Statement st = conn.createStatement()) {
	        	
	        //st.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
	            
	        for (String query : querys) {
	            st.executeUpdate(query);
	        }
	            
	        //st.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
	            
	    } 
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}

