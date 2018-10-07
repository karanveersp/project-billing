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
public class Employee {
	private String fName;
	private String lName;
	private int empID;
	private Date joinDate;
	private double billableHours;
	
	/**
	 * @param fName
	 * @param lName
	 * @param empID
	 * @param joinDate
	 * @param billableHours
	 */
	protected Employee(String fName, String lName, int empID, Date joinDate, double billableHours) {
		super();
		this.fName = fName;
		this.lName = lName;
		this.empID = empID;
		this.joinDate = joinDate;
		this.billableHours = billableHours;
	}
	
	/**
	 * 
	 */
	protected Employee() {
	}
	
	
	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}
	/**
	 * @param fName the fName to set
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}
	/**
	 * @return the lName
	 */
	public String getlName() {
		return lName;
	}
	/**
	 * @param lName the lName to set
	 */
	public void setlName(String lName) {
		this.lName = lName;
	}
	/**
	 * @return the empID
	 */
	public int getEmpID() {
		return empID;
	}
	/**
	 * @param empID the empID to set
	 */
	public void setEmpID(int empID) {
		this.empID = empID;
	}
	
	
	/**
	 * @return the joinDate
	 */
	public Date getJoinDate() {
		return joinDate;
	}

	/**
	 * @param joinDate the joinDate to set
	 */
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	/**
	 * @return the billableHours
	 */
	public double getBillableHours() {
		return billableHours;
	}
	/**
	 * @param billableHours the billableHours to set
	 */
	public void setBillableHours(double billableHours) {
		this.billableHours = billableHours;
	}
	
	
	
	public String display() {
		String outStr = lName + ", " + fName + ", " + empID + ", " + joinDate + ", " + billableHours;
		return outStr;
	}
	
	/**
	 * Write opening tag
	 * @param fw
	 * @throws IOException
	 */
	public void writeXMLOpenTag(FileWriter fw) throws IOException {
		fw.write("<Employee ");
	}
	
	/**
	 * Write attribute sting
	 * @param fw
	 * @throws IOException
	 */
	public void writeXMLOpenAttr(FileWriter fw) throws IOException {
		fw.write("fName=\"" + fName + "\" lName=\"" + lName +
						"\" empID=\"" + empID + "\" start=\"" + joinDate.toString() + "\" billableHours=\"" +
						billableHours + "\">\n");
	}
	
	/**
	 * Write closing tag
	 * @param fw
	 * @throws IOException
	 */
	public void writeXMLCloseTag(FileWriter fw) throws IOException {
		fw.write("</Employee>\n");
	}
	
	
	
	

}
