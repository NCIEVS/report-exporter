package gov.nih.nci.evs.report.exporter.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class CSVUtility {
	
	public <T> String produceCSVOutputFromList(List<T> list) {
		BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(null));
		StringBuffer oneLine = new StringBuffer();
		 oneLine.append("name: value");
		 try {
			writer.write(oneLine.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String ...args) {
		new CSVUtility().produceCSVOutputFromList(null);
	}

}
