package ssp.javasp.sp.fundamental.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class ExternalProcess {
	
	public static void main(String[] args) throws Exception {
		String[] commandArray = {"tasklist", "/?"};
		List<String> lineDatas = ExternalProcess.readFromExternalProcess(commandArray);
		
		//TODO : 데이터 처리
		
		System.out.println("======================================");
		String[] writeCommandArray = {"tasklist"};
		ExternalProcess.writeToExternalProcess(writeCommandArray, lineDatas);
	}
	
	
	public static List<String> readFromExternalProcess(String[] commandArray) throws Exception {
		Process theProcess = Runtime.getRuntime().exec(commandArray);
	    
		List<String> readData = new ArrayList<String>();
		try(BufferedReader inStream = new BufferedReader(new InputStreamReader( theProcess.getInputStream(),"euc-kr"))) {
		    String line = null;
		    while ( ( line = inStream.readLine( ) ) != null ) {
		    	readData.add(line);
		    	System.out.println(line);
		    }
		}
	    
	    return readData;		
	}
	
	public static void writeToExternalProcess(String[] commandArray, List<String> writeData) throws Exception {
		Process theProcess = Runtime.getRuntime().exec(commandArray);
		
		try(PrintWriter printWriter = new PrintWriter(theProcess.getOutputStream(), true)) {
			for(String oneData : writeData) {
				printWriter.println(oneData);
			}
		}
	}
}
