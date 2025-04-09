package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.PackageOperations;

public class gn210186_PackageOperations implements PackageOperations {
	
	Connection conn=DB.getInstance().getConnection();
	
	
	private BigDecimal getEuklidskaDistanca(int opstinaP, int opstinaD) {
        try(PreparedStatement st = conn.prepareStatement("Select X, Y from Opstina where Id = ?");){
	        st.setInt(1, opstinaP);
	        try(ResultSet rsP = st.executeQuery();
	        	PreparedStatement stD = conn.prepareStatement("Select X, Y from Opstina where Id = ?");){
	        	
		        stD.setInt(1, opstinaD);
		        try(ResultSet rsD = stD.executeQuery();){
		
			        if (rsP.next() && rsD.next()) {
			            int x1 = rsP.getInt(1);
			            int y1 = rsP.getInt(2);
			            int x2 = rsD.getInt(1);
			            int y2 = rsD.getInt(2);
			
			            double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
			            return BigDecimal.valueOf(dist);
			        }
		        }
	        }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
	}

	@Override
	public boolean acceptAnOffer(int offerId) {
		try (PreparedStatement checkO = conn.prepareStatement("Select PaketId, KurirKorIme, Procenat from Ponuda where Id = ?");){
            checkO.setInt(1, offerId);
            try(ResultSet rs = checkO.executeQuery();){

	            if (!rs.next()) {
	                return false;
	            }

	            int paketId = rs.getInt(1);
	            String kurirUserName = rs.getString(2);
	            BigDecimal procenat = rs.getBigDecimal(3);
	
	            try(PreparedStatement paket = conn.prepareStatement("Select Tezina, Tip, OpstinaP, OpstinaD from Paket WHERE Id = ?");){
		            paket.setInt(1, paketId);
		            try(ResultSet rsP = paket.executeQuery();){
		
			            if (!rsP.next()) {
			                return false; 
			            }
		
			            BigDecimal tezina = rsP.getBigDecimal(1);
			            int tip = rsP.getInt(2);
			            int opstinaP = rsP.getInt(3);
			            int opstinaD = rsP.getInt(4);
			
			            
			            BigDecimal osnovnaCena;
			            BigDecimal tezinskiFaktor;
			            BigDecimal cenaKG;

			            switch (tip) {
			                case 0: 
			                    osnovnaCena = new BigDecimal("10");
			                    tezinskiFaktor = new BigDecimal("0");
			                    cenaKG = BigDecimal.ZERO;
			                    break;
			                case 1:
			                    osnovnaCena = new BigDecimal("25");
			                    tezinskiFaktor = new BigDecimal("1");
			                    cenaKG = new BigDecimal("100");
			                    break;
			                case 2: 
			                    osnovnaCena = new BigDecimal("75");
			                    tezinskiFaktor = new BigDecimal("2");
			                    cenaKG = new BigDecimal("300");
			                    break;
			                default:
			                    return false;
			            }

			            BigDecimal cena = osnovnaCena.add(tezinskiFaktor.multiply(tezina).multiply(cenaKG));
			            BigDecimal euklidskaDistanca = getEuklidskaDistanca(opstinaP, opstinaD);
			            BigDecimal finalnaCena = cena.multiply(euklidskaDistanca).multiply(procenat.divide(BigDecimal.valueOf(100)).add(BigDecimal.ONE));
			
			            try(PreparedStatement updateSt = conn.prepareStatement("Update Paket set StatusIsporuke = 1, KurirKorIme = ?, VremeP = ?, Cena = ? WHERE Id = ?");){
				            updateSt.setString(1, kurirUserName);
				            updateSt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
				            updateSt.setBigDecimal(3, finalnaCena);
				            updateSt.setInt(4, paketId);
				
				            int rows = updateSt.executeUpdate();
				
				            if (rows>0) {
				                return true;
				            } else {
				                return false; 
				            }
			            }
		            }
	            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return false;
	}

	@Override
	public boolean changeType(int packageId, int newType) {
		try(PreparedStatement check=conn.prepareStatement("Select Id from Paket where Id=?");){
			check.setInt(1, packageId);
			try(ResultSet rs=check.executeQuery();){
				if(rs.next()) {
					try(PreparedStatement updateSt=conn.prepareStatement("Update Paket set Tip=? where Id=?");){
						updateSt.setInt(1, newType);
						updateSt.setInt(2, packageId);
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
	public boolean changeWeight(int packageId, BigDecimal newWeight) {
		try(PreparedStatement check=conn.prepareStatement("Select Id from Paket where Id=?");){
			check.setInt(1, packageId);
			try(ResultSet rs=check.executeQuery();){
				if(rs.next()) {
					try(PreparedStatement updateSt=conn.prepareStatement("Update Paket set Tezina=? where Id=?");){
						updateSt.setBigDecimal(1, newWeight);
						updateSt.setInt(2, packageId);
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
	public boolean deletePackage(int packageId) {
		try (PreparedStatement deleteSt=conn.prepareStatement("Delete from Paket where Id=?");){
			deleteSt.setInt(1, packageId);
			int row=deleteSt.executeUpdate();
			if (row>0) return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int driveNextPackage(String courierUserName) {
	    // Query to select the courier's vehicle
	    String selectVehicleQuery = "SELECT TipGoriva, Potrosnja FROM Vozilo WHERE RegistarskiBroj = (SELECT RegistarskiBroj FROM Kuriri WHERE KorIme = ?)";
	    
	    // Query to select the next package to deliver
	    String selectPackageQuery = "SELECT Id, Cena, OpstinaP, OpstinaD FROM Paket WHERE KurirKorIme = ? AND StatusIsporuke IN (1,2) ORDER BY VremeP ASC";
	    
	    // Query to update the package status to "pokupljen" (2) for all packages assigned to the courier
	    String updateAllPackagesStatusQuery = "UPDATE Paket SET StatusIsporuke = 2 WHERE KurirKorIme = ? AND StatusIsporuke = 1";
	    
	    // Query to update the package status to "isporučen" (3) for the specific package
	    String updatePackageStatusQuery = "UPDATE Paket SET StatusIsporuke = 3 WHERE Id = ?";
	    
	    // Query to update the courier's profit
	    String updateCourierProfitQuery = "UPDATE Kuriri SET OstvareniProfit = OstvareniProfit + ? WHERE KorIme = ?";
	    
	    try {
	        conn.setAutoCommit(false);
	        
	        // Check if the courier's vehicle exists and get fuel details
	        int fuelType;
	        double fuelConsumption;
	        try (PreparedStatement selectVehicleStmt = conn.prepareStatement(selectVehicleQuery)) {
	            selectVehicleStmt.setString(1, courierUserName);
	            try (ResultSet rs = selectVehicleStmt.executeQuery()) {
	                if (!rs.next()) {
	                    conn.rollback();
	                    return -1; // Vehicle does not exist for this courier
	                }
	                fuelType = rs.getInt("TipGoriva");
	                fuelConsumption = rs.getDouble("Potrosnja");
	            }
	        }
	        
	        // Select the next package to deliver
	        int packageId = -1;
	        BigDecimal packagePrice = BigDecimal.ZERO;
	        int OpstinaP = 0;
	        int OpstinaD = 0;
	        int OpstinaC=0;
	        try (PreparedStatement selectPackageStmt = conn.prepareStatement(selectPackageQuery)) {
	            selectPackageStmt.setString(1, courierUserName);
	            try (ResultSet rs = selectPackageStmt.executeQuery()) {
	                if (rs.next()) {
	                    packageId = rs.getInt("Id");
	                    packagePrice = rs.getBigDecimal("Cena");
	                    OpstinaP = rs.getInt("OpstinaP");
	                    OpstinaD = rs.getInt("OpstinaD");
	                    if(rs.next()) {
	                    	OpstinaC=rs.getInt("OpstinaP");
	                    }
	                    else {
	                    	OpstinaC=OpstinaD;
	                    }
	                } else {
	                    conn.rollback();
	                    return -1; // No packages to deliver
	                }
	            }
	        }
	        
	        
	        // Update the status of all packages assigned to the courier to "pokupljen" (2)
	        try (PreparedStatement updateAllPackagesStatusStmt = conn.prepareStatement(updateAllPackagesStatusQuery)) {
	            updateAllPackagesStatusStmt.setString(1, courierUserName);
	            updateAllPackagesStatusStmt.executeUpdate();
	        }
	        
	        // Update the status of the specific package to "isporučen" (3)
	        try (PreparedStatement updatePackageStatusStmt = conn.prepareStatement(updatePackageStatusQuery)) {
	            updatePackageStatusStmt.setInt(1, packageId);
	            updatePackageStatusStmt.executeUpdate();
	        }
	        
	        // Calculate the fuel cost
	        BigDecimal distance = calculateDistance(OpstinaP, OpstinaD);
	        distance=distance.add(calculateDistance(OpstinaD, OpstinaC));
	        
	        
	        BigDecimal fuelCostPerLitre;
	        
	        switch (fuelType) {
	            case 0:
	                fuelCostPerLitre = new BigDecimal(15);
	                break;
	            case 1:
	                fuelCostPerLitre = new BigDecimal(36);
	                break;
	            case 2:
	                fuelCostPerLitre = new BigDecimal(32);
	                break;
	            default:
	                fuelCostPerLitre = BigDecimal.ZERO;
	                break;
	        }
	        System.out.println("po litru "+fuelCostPerLitre);
	        System.out.println("distanca "+distance);
	        System.out.println("potrosnja "+fuelConsumption);
	        
	        BigDecimal fuelCost = fuelCostPerLitre.multiply(distance).multiply(new BigDecimal(fuelConsumption));
	        
	        
	        // Update the courier's profit
	        try (PreparedStatement updateCourierProfitStmt = conn.prepareStatement(updateCourierProfitQuery)) {
	            BigDecimal profit = packagePrice.subtract(fuelCost);
	            System.out.println("dobio"+packagePrice);
	            System.out.println("izgubio"+fuelCost);
	            System.out.println("profit"+profit);
	            updateCourierProfitStmt.setBigDecimal(1, profit);
	            updateCourierProfitStmt.setString(2, courierUserName);
	            updateCourierProfitStmt.executeUpdate();
	        }
	        
	        conn.commit();
	        return packageId; // Return the ID of the delivered package
	        
	    } catch (SQLException e) {
	        try {
	            conn.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        e.printStackTrace();
	        return -2; // Error occurred
	    } finally {
	        try {
	            conn.setAutoCommit(true);
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }
	}

	// Placeholder method to calculate distance between municipalities
	private BigDecimal calculateDistance(int OpstinaP, int OpstinaD) {
	    BigDecimal res = getEuklidskaDistanca(OpstinaP, OpstinaD);
	    if (res != null) {
	        return res;
	    } else {
	        // Vraćamo neku grešku ili default vrednost u slučaju da je res null
	        return BigDecimal.ZERO;
	    }
	}







	@Override
	public Date getAcceptanceTime(int packageId) {
		try (PreparedStatement st=conn.prepareStatement("Select VremeP from Paket where Id=?");){
			st.setInt(1, packageId);
			try(ResultSet rs=st.executeQuery()){
				if(rs.next()) {
					return rs.getDate(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Integer> getAllOffers() {
		List<Integer> list=new ArrayList();
		try (Statement st=conn.createStatement();
				ResultSet rs=st.executeQuery("Select Id from Ponuda");){
			
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
	public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int packageId) {
		List<Pair<Integer, BigDecimal>> list = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement("Select Id, Procenat from Ponuda where PaketId = ?")) {
            st.setInt(1, packageId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    BigDecimal procenat = rs.getBigDecimal(2);
                    PackageOperations.Pair<Integer, BigDecimal> pair = new gn210186_Pair<>(id,procenat) ;
                    list.add(pair);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
	}

	@Override
	public List<Integer> getAllPackages() {
		List<Integer> list=new ArrayList();
		try (Statement st=conn.createStatement();
				ResultSet rs=st.executeQuery("Select Id from Paket");){
			
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
	public List<Integer> getAllPackagesWithSpecificType(int type) {
		List<Integer> list=new ArrayList();
		try (PreparedStatement st=conn.prepareStatement("Select Id from Paket where Tip=?");){
			st.setInt(1, type);
			try(ResultSet rs=st.executeQuery()){
				while(rs.next()) {
					list.add(rs.getInt(1));
				}
				return list;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Integer getDeliveryStatus(int packageId) {
		try (PreparedStatement st=conn.prepareStatement("Select StatusIsporuke from Paket where Id=?");){
			st.setInt(1, packageId);
			try(ResultSet rs=st.executeQuery()){
				if(rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<Integer> getDrive(String courierUsername) {
		 List<Integer> list = new ArrayList<>();
		
		 try (PreparedStatement st = conn.prepareStatement("Select Id from Paket where KurirKorIme=? and Status in (0,1,2)")) {
		     st.setString(1, courierUsername);
		
		     try (ResultSet rs = st.executeQuery()) {
		         while (rs.next()) {
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
	public BigDecimal getPriceOfDelivery(int packageId) {
		try (PreparedStatement st=conn.prepareStatement("Select Cena from Paket where Id=?");){
			st.setInt(1, packageId);
			try(ResultSet rs=st.executeQuery()){
				if(rs.next()) {
					return rs.getBigDecimal(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int insertPackage(int districtFrom, int districtTo, String userName, int packageType, BigDecimal weight) {
		try (PreparedStatement checkK = conn.prepareStatement("Select KorIme from Korisnik where KorIme=?")) {
	        checkK.setString(1, userName);
	        try (ResultSet rs = checkK.executeQuery()) {
	            if(!rs.next()) {
	            	return -1;
	            }
	        }
	        System.out.println(111111);
	        try (PreparedStatement checkDF = conn.prepareStatement("Select Id from Opstina where Id=?");
	        	 PreparedStatement checkDT = conn.prepareStatement("Select Id from Opstina where Id=?");) {
		        checkDF.setInt(1, districtFrom);
		        checkDT.setInt(1, districtTo);
		        try (ResultSet rsDF = checkDF.executeQuery(); ResultSet rsDT = checkDT.executeQuery();) {
		            if(!rsDF.next() || !rsDT.next()) {
		            	return -1;
		            }
		        }
		        System.out.println(22222);
		        
		        try (PreparedStatement insertSt = conn.prepareStatement("Insert into Paket (KorIme, OpstinaP, OpstinaD, Tip, Tezina, StatusIsporuke) values (?, ?, ?, ?, ?, 0)",Statement.RETURN_GENERATED_KEYS)) {
                    insertSt.setString(1, userName);
                    insertSt.setInt(2, districtFrom);
                    insertSt.setInt(3, districtTo);
                    insertSt.setInt(4, packageType);
                    insertSt.setBigDecimal(5, weight);

                    int rows = insertSt.executeUpdate();
                    if (rows > 0) {
                        try (ResultSet generatedKeys = insertSt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                return generatedKeys.getInt(1);
                            }
                        }
                    }
            	}
	        }
		}
	     catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	     }
		return -1;
	}

	@Override
	public int insertTransportOffer(String courierUserName, int packageId, BigDecimal pricePercentage) {
		try (PreparedStatement check = conn.prepareStatement("Select Id from Paket where Id=?")) {
	        check.setInt(1, packageId);
	        try (ResultSet rs = check.executeQuery()) {
	            if(rs.next()) {
	            	try (PreparedStatement insertSt = conn.prepareStatement("Insert into Ponuda (KurirKorIme, PaketId, Procenat) VALUES (?, ?, ?)",Statement.RETURN_GENERATED_KEYS)) {
	                    insertSt.setString(1, courierUserName);
	                    insertSt.setInt(2, packageId);
	                    insertSt.setBigDecimal(3, pricePercentage);

	                    int rows = insertSt.executeUpdate();
	                    if (rows > 0) {
	                        try (ResultSet generatedKeys = insertSt.getGeneratedKeys()) {
	                            if (generatedKeys.next()) {
	                                return generatedKeys.getInt(1);
	                            }
	                        }
	                    }
	            	}
	            }
	        }
		}
	     catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	     }
		return -1;
	}
}
