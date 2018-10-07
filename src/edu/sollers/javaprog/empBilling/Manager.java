/**
 * 
 */
package edu.sollers.javaprog.empBilling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author karan
 *
 */
public class Manager extends Employee {
	ArrayList<Employee> subordinates;

	/**
	 * @return the subordinates
	 */
	public ArrayList<Employee> getSubordinates() {
		return subordinates;
	}
	
	/**
	 * Add subordinate
	 * @param e
	 */
	public void addSubordinate(Employee e) {
		subordinates.add(e);
	}
	
	/**
	 * Constructor
	 * 
	 * @param fName
	 * @param lName
	 * @param empID
	 * @param joinDate
	 * @param billableHours
	 */
	public Manager(String fName, String lName, int empID, Date joinDate, double billableHours) {
		super(fName, lName, empID, joinDate, billableHours);
		subordinates = new ArrayList<Employee>();
	}
	
	/**
	 * 
	 */
	public Manager() {
		subordinates = new ArrayList<Employee>();
	}
	
	public String display() {
		String outStr = "";
		outStr = super.display();
		if (!subordinates.isEmpty()) {
			for (Employee e: subordinates) {
				outStr = String.format("%s\n%s",  outStr, e.display());
				// outStr += "\n" + e.display();
				// concatenating two strings %s with a newline \n
			}
		}
		return outStr;
	}
	
	/**
	 * 
	 * @param fw
	 * @throws IOException
	 */
	public void writeXMLOpenTag(FileWriter fw) throws IOException {
		fw.write("<Manager ");
	}
	
	/**
	 * 
	 * @param fw
	 * @throws IOException
	 */
	public void writeXMLCloseTag(FileWriter fw) throws IOException {
		fw.write("</Manager>\n");
	}
	
	/**
	 * Method to create an xml string of the state of the object
	 * My version of how to solve the problem of outputting the xml
	 * 
	 * Run the function writeXML(managers) in MainDriver run() method to get that version
	 * which prints report.xml
	 * 
	 * @return String containing xml formatted state of the object
	 */
	public String toXml() {
		String outStr = this.display();
		
		String[] lineArray = outStr.split("\n");
		String xmlOutput = "";
		
		for (int i = 0; i < lineArray.length; i++) {
			String[] wordArray = lineArray[i].split(", ");
			if (i == 0) { // if i is 0, we are processing the manager
				xmlOutput += "<Manager fName=\"" + wordArray[0] + "\" lName=\"" + wordArray[1] +
						"\" empID=\"" + wordArray[2] + "\" start=\"" + wordArray[3] + "\" billableHours=\"" +
						wordArray[4] + "\">";
			}
			else {
				xmlOutput += "\n" + "<Sub fName=\"" + wordArray[0] + "\" lName=\"" + wordArray[1] +
						"\" empID=\"" + wordArray[2] + "\" start=\"" + wordArray[3] + "\" billableHours=\"" +
						wordArray[4] + "\"></Sub>";
			}
		}
		
		xmlOutput += "\n" + "</Manager>" + "\n";
		
		return xmlOutput;
	}
	

}
