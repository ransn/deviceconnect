package com.hmi.device;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
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

			readMultipleRegistersRequest(transaction);

			System.out.println("Closing connection...");
			// Close the connection
			connection.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void readMultipleRegistersRequest(ModbusTCPTransaction transaction) throws ModbusException {

		ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(0, 10);
		request.setUnitID(1);
		transaction.setRequest(request);
		transaction.execute();
		if(transaction.getResponse() instanceof ReadMultipleRegistersResponse){
			ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
			Register[] registers = response.getRegisters();
			int index = 0;
			for (Register register: registers) {
				String binaryString = Integer.toBinaryString(register.getValue() & 0xFFFF);
				binaryString = String.format("%16s",binaryString).replace(' ', '0');
				System.out.println("index "+index+"  "+binaryString);
				index ++;
			}
		}
	}
}
