/**
 * 
 */
package edu.sollers.javaprog.empBilling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * @author karan
 *
 */
public class Subordinate extends Employee {
	private Manager m;

	/**
	 * @return the m
	 */
	public Manager getM() {
		return m;
	}

	/**
	 * @param m the m to set
	 */
	public void setM(Manager m) {
		this.m = m;
	}

	/**
	 * @param fName
	 * @param lName
	 * @param empID
	 * @param joinDate
	 * @param billableHours
	 * @param m
	 */
	public Subordinate(String fName, String lName, int empID, Date joinDate, double billableHours, Manager m) {
		super(fName, lName, empID, joinDate, billableHours);
		this.m = m;
	}
	
	
	/**
	 * 
	 * @param fw
	 * @throws IOException
	 */
	public void writeXMLOpenTag(FileWriter fw) throws IOException {
		fw.write("<Subordinate ");
	}

	/**
	 * 
	 * @param fw
	 * @throws IOException
	 */
	public void writeXMLCloseTag(FileWriter fw) throws IOException {
		fw.write("</Subordinate>\n");
	}
	
	

}
