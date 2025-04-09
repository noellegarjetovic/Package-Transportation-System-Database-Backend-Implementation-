package main;


import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.operations.DistrictOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.operations.VehicleOperations;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;
import student.gn210186_CityOperations;
import student.gn210186_CourierOperations;
import student.gn210186_CourierRequestOperation;
import student.gn210186_DistrictOperations;
import student.gn210186_GeneralOperations;
import student.gn210186_PackageOperations;
import student.gn210186_UserOperations;
import student.gn210186_VehicleOperations;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;




public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new gn210186_CityOperations(); 
        DistrictOperations districtOperations = new gn210186_DistrictOperations(); 
        CourierOperations courierOperations = new gn210186_CourierOperations();
        CourierRequestOperation courierRequestOperation = new gn210186_CourierRequestOperation();
        GeneralOperations generalOperations = new gn210186_GeneralOperations();
        UserOperations userOperations = new gn210186_UserOperations();
        VehicleOperations vehicleOperations = new gn210186_VehicleOperations();
        PackageOperations packageOperations = new gn210186_PackageOperations();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);
        TestRunner.runTests();
    	
       
        
    }
}