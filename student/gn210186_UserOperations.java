package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.UserOperations;

public class gn210186_UserOperations implements UserOperations {

	Connection conn=DB.getInstance().getConnection();
	
	@Override
	public int declareAdmin(String username) {
		String checkSql = "SELECT admin FROM Korisnici WHERE korisnicko_ime = ?";
        String updateSql = "UPDATE Korisnici SET admin = 1 WHERE korisnicko_ime = ?";
        try (PreparedStatement checkK = conn.prepareStatement("Select KorIme from Korisnik where KorIme=?");
        	 PreparedStatement checkA = conn.prepareStatement("Select KorIme from Administratori where KorIme=?");
             PreparedStatement insertSt = conn.prepareStatement("Insert into Administratori (KorIme) values(?)")) {
            checkK.setString(1, username);
            checkA.setString(1, username);
            try (ResultSet rsK = checkK.executeQuery();
            		ResultSet rsA = checkA.executeQuery();) {
                if (rsK.next()) {
                    if (rsA.next()) {
                        return 1;
                    }
                } 
                else {
                    return 2;
                }
            }
            insertSt.setString(1, username);
            int res=insertSt.executeUpdate();
            if(res>0)
            	return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 2;
	}

	@Override
	public int deleteUsers(String... usernames) {
		int rows=0;
		try (PreparedStatement deleteSt=conn.prepareStatement("Delete from Korisnik where KorIme=?");){
			for (String user : usernames) {
				deleteSt.setString(1, user);
				rows+=deleteSt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public List<String> getAllUsers() {
		List<String> list=new ArrayList();
		try (Statement st=conn.createStatement();
				ResultSet rs=st.executeQuery("Select KorIme from Korisnik");){
			
			while(rs.next()) {
				list.add(rs.getString(1));
			}
			return list;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Integer getSentPackages(String... userNames) {
		String check = "Select count(*) from Korisnik where KorIme IN (";
	    for (int i = 0; i < userNames.length; i++) {
	        check += "?";
	        if (i < userNames.length - 1) {
	            check += ", ";
	        }
	    }
	    check += ")";
	    
	    try (PreparedStatement checkUsersStatement = conn.prepareStatement(check)) {
	        for (int i = 0; i < userNames.length; i++) {
	            checkUsersStatement.setString(i + 1, userNames[i]);
	        }
	        
	        try (ResultSet checkUsersResult = checkUsersStatement.executeQuery()) {
	            if (checkUsersResult.next()) {
	                int count = checkUsersResult.getInt(1);
	                if (count != userNames.length) {
	                    return null;
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }

	    
	    String query = "Select SUM(BrojPoslatih) From Korisnik WHERE KorIme IN (";
        for (int i = 0; i < userNames.length; i++) {
            query+="?";
            if (i < userNames.length - 1) {
                query+=", ";
            }
        }
        query+=(")");
        try(PreparedStatement st = conn.prepareStatement(query);) {
	       for (int i = 0; i < userNames.length; i++) {
	           st.setString(i + 1, userNames[i]);
	       }
	       try (ResultSet rs = st.executeQuery()) {
	           if (rs.next()) {
	               return rs.getInt(1);
	           }
	       }
	   } catch (SQLException e) {
	       e.printStackTrace();
	   }
        return null;
	}

	
	private boolean hasNum(String str) {
		for (char c : str.toCharArray()) { 
	        if (Character.isDigit(c)) { 
	            return true; 
	        }
	    }
	    return false;
	}
	
	private boolean hasLetter(String str) {
		for (char c : str.toCharArray()) { 
	        if (Character.isAlphabetic(c)) { 
	            return true; 
	        }
	    }
	    return false;
	}
	
	@Override
	public boolean insertUser(String userName, String firstName, String lastName, String password) {
		if(!Character.isUpperCase(firstName.charAt(0)) || !Character.isUpperCase(lastName.charAt(0)) || password.length()<8 || !hasNum(password) || !hasLetter(password))
			return false;
		try(PreparedStatement check=conn.prepareStatement("Select KorIme from Korisnik where KorIme=?")){
			check.setString(1, userName);
			try(ResultSet rs=check.executeQuery()){
				if(!rs.next()) {
					try (PreparedStatement insertSt=conn.prepareStatement("Insert into Korisnik (KorIme, Ime, Prezime, Sifra) values (?,?,?,?)");){
						insertSt.setString(1, userName);
						insertSt.setString(2, firstName);
						insertSt.setString(3, lastName);
						insertSt.setString(4, password);
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
