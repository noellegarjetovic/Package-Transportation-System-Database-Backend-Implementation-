package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.CourierOperations;

public class gn210186_CourierOperations implements CourierOperations {
	Connection conn=DB.getInstance().getConnection();

	@Override
	public boolean deleteCourier(String courierUserName) {
		try (PreparedStatement deleteSt=conn.prepareStatement("Delete from Kuriri where KorIme=?");){
			deleteSt.setString(1, courierUserName);
			int row=deleteSt.executeUpdate();
			if (row>0) return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<String> getAllCouriers() {
		List<String> list=new ArrayList();
		try (Statement st=conn.createStatement();
				ResultSet rs=st.executeQuery("Select KorIme from Kuriri order by OstvareniProfit DESC");){
			
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
	public BigDecimal getAverageCourierProfit(int numberOfDeliveries) {
		try (PreparedStatement st=conn.prepareStatement("Select avg(OstvareniProfit) from Kuriri where BrojIsporucenih>=?");){
			st.setInt(1, numberOfDeliveries);
			try(ResultSet rs=st.executeQuery();){
				if(rs.next()) {
					BigDecimal res=rs.getBigDecimal(1);
					return res;
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return BigDecimal.ZERO;
	}

	@Override
	public List<String> getCouriersWithStatus(int statusOfCourier) {
		List<String> list=new ArrayList();
		try (PreparedStatement st=conn.prepareStatement("Select KorIme from Kuriri where Status=?");){
			st.setInt(1, statusOfCourier);
			try(ResultSet rs=st.executeQuery();){
				while(rs.next()) {
					list.add(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean insertCourier(String courierUserName, String licencePlateNumber) {
		try (PreparedStatement insertSt=conn.prepareStatement("Insert into Kuriri (KorIme, RegistarskiBroj) values (?,?)");){
			insertSt.setString(1, courierUserName);
			insertSt.setString(2, licencePlateNumber);
			int row=insertSt.executeUpdate();
			if (row>0) {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


}
