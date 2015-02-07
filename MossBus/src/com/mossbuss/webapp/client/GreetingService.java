package com.mossbuss.webapp.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;



/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	//General
	void resetDatabase(String string) throws Exception;
	DriverDTO doLogin(DriverDTO driverDTO) throws Exception;
//Customers
	
	StudentDTO saveStudent(StudentDTO studentDetails) throws Exception;
	/**
	 * don't know what this does or what this means. 
	 */
	ClientDTO saveClient(ClientDTO clientDetails) throws Exception;
	ArrayList<String> updateContactNameOracle(String sql) throws Exception;
	
	StudentDTO getStudent(int selectedID) throws Exception;
	ClientDTO getClient(int selectedID) throws Exception;
	TripSheetDTO getTripSheet(int selectedID) throws Exception;

	
	//Admin
		void saveDriver(DriverDTO userdetails) throws Exception;
		ArrayList<DriverDTO> listDrivers(String string) throws Exception;
		//Stock
		void saveBus(BusDTO item) throws Exception;
		BusDTO getBus(int selectedID) throws Exception;
		ArrayList<String> updateBusCodeOracle(String sql) throws Exception;
		
		//Sales
		TripSheetDTO saveTripSheet(TripSheetDTO orderDetails) throws Exception;
		ArrayList<StudentDTO> getStudentsFromParent(int selectedParentID)
				throws Exception;
		ArrayList<StudentDTO> getStudentsFromTripSheet(int selectedTripSheetID)
				throws Exception;
		ArrayList<DriverDTO> getAllDrivers() throws Exception;
		ArrayList<BusDTO> getAllBusses() throws Exception;
		void updateDBtripSheetSelected(int tripSheetID) throws Exception;
		ArrayList<String> updateTripSheetNameOracle(String sql)
				throws Exception;
}
