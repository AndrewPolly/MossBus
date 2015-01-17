package com.mossbuss.webapp.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.MaintenanceRecordDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	//General
		void resetDatabase(String string, AsyncCallback<Void> asyncCallback);
		void doLogin(DriverDTO driverDTO, AsyncCallback<DriverDTO> asyncCallback);
	//Customers
		void saveStudent(ClientDTO clientDetails,AsyncCallback<ClientDTO> asyncCallback);
		/**
		 * don't know what this does or what this means. 
		 */
		void updateContactNameOracle(String sql,AsyncCallback<ArrayList<String>> asyncCallback);
		void getStudent(int selectedID, AsyncCallback<ClientDTO> asyncCallback);
		

		
	//Admin
		void saveDriver(DriverDTO userdetails, AsyncCallback<Void> asyncCallback);
		void listDrivers(String string,AsyncCallback<ArrayList<DriverDTO>> asyncCallback);
	//Bus
		void saveBus(BusDTO bus, AsyncCallback<Void> asyncCallback);
		void getBus(int selectedID, AsyncCallback<BusDTO> asyncCallback);
		/**
		 * Again dont know what code oracle is.
		 */
		void updateBusCodeOracle(String sql,AsyncCallback<ArrayList<String>> asyncCallback);
		
	//TripSheet
		void saveTripSheet(TripSheetDTO tripSheet,AsyncCallback<TripSheetDTO> asyncCallback);
		
		
		
	

}
