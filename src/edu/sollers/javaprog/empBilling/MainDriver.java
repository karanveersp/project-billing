
/**
 *
 */
package edu.sollers.javaprog.empBilling;

import java.io.File;
import java.io.FileWriter;

import java.sql.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class parses an xml (or db) file with Manager and Subordinate elements
 * and instantiates objects of those classes.
 * @author karan
 *
 */
public class MainDriver {
    private Document doc;
    private ArrayList<Manager> managers;

    /**
     *     The constructor for this class
     */
    public MainDriver() {
        managers = new ArrayList<Manager>();
    }

    /**
     * Runner for XML file reading and writing
     */
    public void XMLrun() {
        String fileName = "data/employees.xml";

        openFile(fileName);
        xmlTraversal(doc);
        printManagers();
        System.out.println();

        // writeXML(managers); // my version
        writeXMLAlternate();
    }

    /**
     * Method to connect to a sqlite database
     * @return The instantiated Connection object
     */
    private Connection connect(String dbName) {
        String     url  = "jdbc:sqlite:" + dbName;
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

        return conn;
    }

    /**
     * Helper method that formats the date from "m/d/yyyy" to "yyyy-mm-dd"
     * @param date As string "m/d/yyyy"
     * @return formatted string date as "yyyy-mm-dd"
     */
    public String dateFormat(String date) {
        String[] datePart = date.split("/");

        // add leading "0" to day/month value if it is less than 10
        for (int i = 0; i < 2; i++) {
            int currInt = Integer.parseInt(datePart[i]);

            if (currInt < 10) {
                datePart[i] = "0" + String.valueOf(currInt);
            }
        }

        String formattedDate = datePart[2] + "-" + datePart[0] + "-" + datePart[1];

        return formattedDate;
    }

    public void loadFromDB() throws SQLException {
        try {
            Connection c = connect("testDB.db");

            c.setAutoCommit(false);

            Statement stmt = c.createStatement();
            ResultSet rs   = stmt.executeQuery("SELECT * FROM employees;");

            while (rs.next()) {

                // Assign attributes to variables
                int        id            = rs.getInt("id");
                String     fName         = rs.getString("firstName");
                String     lName         = rs.getString("lastName");
                int        manID         = rs.getInt("manID");
                DateFormat sourceFormat  = new SimpleDateFormat("MM/dd/yyyy");
                Date       joinDate      = sourceFormat.parse(rs.getString("joinDate"));
                double     billableHours = rs.getDouble("billableHours");

//              LocalDate joinDate = LocalDate.parse(start);
                if (manID == 0) {    // we have encountered a manager
                    Manager emp = new Manager(fName, lName, id, joinDate, billableHours);

                    managers.add(emp);
                } else {

                    // find manager object for this subordinate
                    Manager currManager = new Manager();

                    for (Manager m : managers) {
                        if (m.getEmpID() == manID) {    // where manID from db row matches empID for manager
                            currManager = m;
                        }
                    }

                    // create subordinate object and add to currManager
                    Subordinate emp = new Subordinate(fName, lName, id, joinDate, billableHours, currManager);

                    currManager.addSubordinate(emp);
                }

                // System.out.println("ID = " + id);
                // System.out.println("FNAME = " + fName);
                // System.out.println("LNAME = " + lName);
                // System.out.println("MANID = " + manID);
                // System.out.println("START = " + start.toString());
                // System.out.println("BillableHours = " + billableHours);
                // System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        System.out.println("Loaded objects from db successfully");
    }

    /**
     * @param args
     *        System arguments
     */
    public static void main(String[] args) {
        MainDriver driver = new MainDriver();

        try {
            driver.loadFromDB();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        driver.writeXMLAlternate();
    }

    /**
     * Method to parse and traverse the given xml file
     *
     * @param fileName
     *        The path of the xml file to parse
     */
    public void openFile(String fileName) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        docBuilder        = docBuilderFactory.newDocumentBuilder();

            doc = docBuilder.parse(new File(fileName));
            doc.getDocumentElement().normalize();

//          System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void printManagers() {
        System.out.println("Printing created objects");

        for (Manager m : managers) {
            System.out.println(m.display());
        }
    }

    /*
     * try {
     *       Document...
     *       till prse file
     *
     *       Nodelist nodes = doc.getChildNodes();
     *       // children of top level are managers
     *       for (int i = 0; i < nodes.getLength(); i++){
     *               Node child = (Node)(nodes.item(i);
     *               if (child.getNodeType() == Node.ELEMENT_NODE) {
     *                       Element e = (Element)child;
     *                       String fName = e.getAttribute("fName");
     *                       String lName = e.getAttribute("lName");
     *                       ...
     *                       DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
     *                       Date date = sourceFormat.parse(e.getAttribute("start"));
     *                       Manager m = new Manager(fName, lName, empID, date, 0);
     *                       if (child.hasChildNodes()) { // child nodes of manager level are subordinates
     *                               NodeList subs = child.getChildNodes();
     *                               for (int j = 0; j < subs.getLength(); j++) {
     *                                       Node subChild = (Node)(subs.item(j));
     *                                       if (subChild.getNoteType() == Node.ELEMENT_NODE) {
     *                                               Element ec = (Element)child;
     *                                               String cfName = ec.getAttribute("fFname");
     *                                               String clName = ec.getAttribute("lName");
     *                                               int cempID = Integer.parseInt(ec.getAttribute("empID"));
     *                                               DateFormat sourceFormat2 = new SimpleDateFormat("dd/MM/yyyy");
     *                                               Date date2 = sourceFormate.parse(e.getAttribute("start"));
     *                                               Subordinate s = new Subordinate(cfName, clName, cempID, date2, 0.0, m);
     *                                               m.addSubordinate(s);
     *                                       }
     *                               }
     *                       }
     *                       managers.add(m);
     *               }
     *       }
     *       for (Manager m: managers) {
     *               System.out.print(m.display());
     *       }
     * } catch (Exception e) {
     *       e.printstackTrace(System.out);
     * }
     *
     *
     *
     *
     *
     *
     */
    public void writeXML() {
        try {

            // open file for write
            File file = new File("data/report.xml");

            file.delete();

            if (file.createNewFile()) {
                System.out.println("Success");
            }

            FileWriter report = new FileWriter(file);

            // write xml header
            report.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" + "<Employees>" + "\n");

            // for each manager
            for (Manager m : managers) {
                report.write(m.toXml());
            }

            report.write("</Employees>");
            report.flush();
            report.close();
            System.out.println("WROTE THE FILE!!");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        // The Algorithm :-
        // open the file
        // write xml header
        // iterate through managers list
        // for each manager
        // write manager info
        // iterate through subodinates
        // for each subordinate
        // write subordinate info
        // close manager info
        // write xml closing
    }

    public void writeXMLAlternate() {
        try {

            // open file for write
            FileWriter fw = new FileWriter("data/dbReport.xml");

            // write xml header
            fw.write("<Employees>\n");

            // iterate through managers list
            for (Manager manager : managers) {

                // write manager info
                manager.writeXMLOpenTag(fw);
                manager.writeXMLOpenAttr(fw);

                // iterate through subordinates
                ArrayList<Employee> subs = manager.getSubordinates();

                // for each subordinate
                for (Employee employee : subs) {

                    // write subordinate info
                    employee.writeXMLOpenTag(fw);
                    employee.writeXMLOpenAttr(fw);
                    employee.writeXMLCloseTag(fw);
                }

                // close manager info
                manager.writeXMLCloseTag(fw);
            }

            // write xml closing
            fw.write("</Employees>\n");
            fw.flush();
            fw.close();
            System.out.println("Wrote to XML file successfully");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * This recursive method traverses nodes in an XML file.
     *
     * @input node
     *                The Document object root node, or any other node as input
     */
    public void xmlTraversal(Node node) {

        // navigate through the xml file
        // Store child nodes in list
        NodeList nodes = node.getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node child = nodes.item(i);

            // Skip over text nodes
            if (child.getNodeName().equals("#text")) {
                continue;
            }

//          System.out.println("Child: " + child.getNodeName());
            try {
                if ((child.getNodeType() == Node.ELEMENT_NODE) && child.hasAttributes()) {
                    Element e = (Element) child;

                    if (e.getNodeName().equals("Manager")) {

                        // create and store a manager object
                        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Date       date         = sourceFormat.parse(e.getAttribute("start"));

//                      LocalDate date = LocalDate.parse(dateFormat(e.getAttribute("start")));
                        Manager manager = new Manager(e.getAttribute("fName"),
                                                      e.getAttribute("lName"),
                                                      Integer.parseInt(e.getAttribute("empID")),
                                                      date,
                                                      Double.parseDouble(e.getAttribute("billableHours")));

                        managers.add(manager);
                    } else {

                        // get manager for this subordinate (i.e most recent manager added to managers array)
                        Manager    currManager  = managers.get(managers.size() - 1);
                        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy");
                        Date       date         = sourceFormat.parse(e.getAttribute("start"));

//                      LocalDate date = LocalDate.parse(dateFormat(e.getAttribute("start")));
                        // create a subordinate object
                        Subordinate sub = new Subordinate(e.getAttribute("fName"),
                                                          e.getAttribute("lName"),
                                                          Integer.parseInt(e.getAttribute("empID")),
                                                          date,
                                                          Double.parseDouble(e.getAttribute("billableHours")),
                                                          currManager);

                        // assign to manager
                        currManager.addSubordinate(sub);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }

            // If this node has child nodes, then recurse through them
            if (child.hasChildNodes()) {
                xmlTraversal(child);
            }
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
