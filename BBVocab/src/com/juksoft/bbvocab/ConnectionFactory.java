package com.juksoft.bbvocab;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.WLANInfo;

public class ConnectionFactory {
	
	public SocketConnection getSocketConnection(String host, int port, int timeoutInSeconds) throws IOException  {
		String connectionString = "socket://" + host + ":" + port + getConnectionParameters() + 
										";ConnectionTimeout=" + (timeoutInSeconds * 1000);
		SocketConnection socket = (SocketConnection)Connector.open(connectionString, Connector.READ_WRITE, true);
		return socket;
	}	
	
	public String getConnectionParameters()
	{
		String connectionParameters = "";
		if (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED) {
			//Connected to Wifi access point
			connectionParameters = ";interface=wifi";			
		}
		else  {
			int coverageStatus = CoverageInfo.getCoverageStatus(); //bitmask of coverage status info
			ServiceRecord record = getWAP2ServiceRecord();
			if (record != null && 
					(coverageStatus & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT) {
				//Network coverage and WAP 2.0 service book record
				connectionParameters = ";deviceside=true;ConnectionUID=" + record.getUid();
			}
			else if ((coverageStatus & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS)  {
				//Network coverage and MDS 
				connectionParameters = ";deviceside=false";
			}
			else if ((coverageStatus & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT) {
				//Network coverage but no WAP 2.0
				connectionParameters =";deviceside=true";
			}						
		}
		return connectionParameters;
	}

	private ServiceRecord getWAP2ServiceRecord() {
		ServiceBook sb = ServiceBook.getSB();
		ServiceRecord[] records = sb.getRecords();
		
		for(int i = 0; i < records.length; i++) {
			String cid = records[i].getCid().toLowerCase();
			String uid = records[i].getUid().toLowerCase();
			
			if(cid.indexOf("wptcp") != -1 && uid.indexOf("wifi") == -1 && uid.indexOf("mms") == -1){
				return records[i];
			}
		}
		return null;
	}

}
