package com.hmi.device;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.util.BitVector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

@SpringBootApplication
public class DeviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeviceApplication.class, args);
		System.out.println("Action");

		try {
			// Define the Modbus TCP connection
			InetAddress inetAddress = InetAddress.getByName("192.168.10.10");
			TCPMasterConnection connection = new TCPMasterConnection(inetAddress);
			connection.setPort(502);  // Modbus TCP port is usually 502

			System.out.println("Connecting. .... ");

			// Establish the connection
			connection.connect();

			System.out.println("Connected.....");

			// Create a Modbus TCP transaction
			ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);

			// Prepare a Modbus request
			//ReadInputDiscretesRequest request = new ReadInputDiscretesRequest(0, 10);  // Read 10 discrete inputs starting from address 0

			readInputRegistersRequest(transaction);

			//transaction.setRequest(request);
			// Execute the transaction
			//transaction.execute();

			// Get the response
//			if (transaction.getResponse() instanceof ReadInputDiscretesResponse) {
//				ReadInputDiscretesResponse response = (ReadInputDiscretesResponse) transaction.getResponse();
//				BitVector inputs = response.getDiscretes();
//				// Process the response (print or manipulate the data as needed)
//				for (int i = 0; i < inputs.size(); i++) {
//					boolean state = inputs.getBit(i);
//					System.out.println("Discrete Input " + i + " Value: " + state);
//				}
//			}
			System.out.println("Closing connection...");
			// Close the connection
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void readInputRegistersRequest(ModbusTCPTransaction transaction) throws ModbusException {
		ReadInputRegistersRequest request = new ReadInputRegistersRequest(0,10);
		request.setDataLength(8);
		request.setUnitID(1);
		// Set the transaction request
		transaction.setRequest(request);
		// Execute the transaction
		transaction.execute();
		if (transaction.getResponse() instanceof ReadInputRegistersResponse) {
			ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();
			InputRegister[] inputs = response.getRegisters();
			// Process the response (print or manipulate the data as needed)
			for (int i = 0; i < inputs.length; i++) {
				InputRegister register = inputs[i];
				System.out.println("Register Input " + i + " Value: " + register.getValue());
			}
		}
	}

}
