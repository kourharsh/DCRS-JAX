package Rmiclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import Rmiserver.CompServer;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import Rmiserver.WebInterface;



public class AdvisorClient {

	Scanner input = new Scanner(System.in);
	String userId,Semester,courseID;
	String registryURL=null;
	int RMIPort;
	String courseId = null;
	String capacity = null;
	String Option = null;
	String term = null;
	Boolean flag=false,flagS=false,flagA = false;
	int choice=0;
	String studentId,advisorId = null;
	Logger logger = Logger.getLogger(AdvisorClient.class.getName());
	static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;
	boolean checkFlag = false;
	boolean checkflag1 = false;
//	static Rmiserver.RmiInterface rmiInter;
	static Rmiserver.WebInterface objClient;
	String[] Ids;
	String[] IdS =      {"COMPS7895","COMPS2233","COMPS3344","COMPS4455",
						"SOENS1122","SOENS2233", "SOENS3344","SOENS4455",
						"INSES1122","INSES2233","INSES3344","INSES4455"};
					
	
	String[] IdA =      {"COMPA1111","COMPA2222","COMPA3333","COMPA4444",
						"SOENA1111","SOENA2222", "SOENA3333","SOENA4444",
						"INSEA1111","INSEA2222","INSEA3333","INSEA4444"};
	
	
	private boolean checkUser(String userId) {
		for(int i=0;i<Ids.length;i++) {
			if(Ids[i].equals(userId)) {
				System.out.println("Hi!" + Ids[i]);
				return true;
			}
		}
		return false;
	}
	
	public void user()throws NotBoundException,NullPointerException, SecurityException, IOException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		System.out.println("Enter your Unique LoginId:");
		userId = input.nextLine().trim().toUpperCase();
		String check = userId.substring(4,5).toUpperCase();
		if(check.equals("A")) {
			Ids= IdA;
			flagA = true;
		}else if(check.equals("S")) {
			Ids= IdS;
			flagS = true;
		}else {
			logger.info("Incorrect LoginId");
			System.out.println("Incorrect LoginId, please enter correct LoginId!");
			user();
		}
		
		checkFlag = checkUser(userId);
		if(checkFlag&&flagS) {
			studentId = userId;
			callServer(studentId);
			callSMethods(studentId);		
		}else if(checkFlag&&flagA) {
			advisorId = userId;
			callServer(advisorId);
			callAMethods(advisorId);
		}else {
			System.out.println("Incorrect LoginId entered");
			user();
		}
	}

	public void callServer(String Id) throws MalformedURLException, RemoteException, NotBoundException,SecurityException,IOException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName{
		 fileTxt = new FileHandler("C:\\Users\\WEB\\eclipse-workspace\\CORBA\\Log files\\"+Id+".log");
		 formatterTxt = new SimpleFormatter();
	     fileTxt.setFormatter(formatterTxt);
	     logger.addHandler(fileTxt);
		String userDept = Id.substring(0,4).toUpperCase().trim();
		System.out.println("User dept is:"+ userDept);
	
		if(userDept.equals("COMP")) {	
			URL compURL = new URL("http://localhost:8080/comp?wsdl");
			QName compQName = new QName("http://Rmiserver/", "FunctionsService");
			Service compService = Service.create(compURL, compQName);
			objClient = compService.getPort(WebInterface.class);
		}
		else if(userDept.equals("SOEN")) {
			URL soenURL = new URL("http://localhost:8080/soen?wsdl");
			QName soenQName = new QName("http://Rmiserver/", "FunctionsService");
			Service soenService = Service.create(soenURL, soenQName);
			objClient = soenService.getPort(WebInterface.class);
		}
		else if(userDept.equals("INSE")) {
			URL inseURL = new URL("http://localhost:8080/inse?wsdl");
			QName inseQName = new QName("http://Rmiserver/", "FunctionsService");
			Service inseService = Service.create(inseURL, inseQName);
			objClient = inseService.getPort(WebInterface.class);
		}
	}

	public void callAMethods(String Id) throws RemoteException {	
				System.out.println("Please select the options(1/2/3/4) below: ");
				System.out.println("1. Add Course");
				System.out.println("2. Remove Course");
				System.out.println("3. List Course Availability");
				System.out.println("4. Other student related Operations");
				System.out.println("Enter your choice: ");
				choice = input.nextInt();
				logger.info("Calling Advisor Methods");
				logger.info("Choice is:"+choice);
				String AdvDept = Id.substring(0,4).toUpperCase().trim(); 
			if(choice<1 ||choice>4) {
				logger.info("Error!Incorrect choice!");
				System.out.println("Incorrect choice!");
				callAMethods(advisorId);
			}
			else if (choice == 1) {
				System.out.println("Please enter the course number you want to add");
				courseID = input.next().trim().toUpperCase();
				logger.info("courseID is:"+courseID);
				System.out.println("Please enter the semester for which you want to add" + " " + courseID );
				Semester = input.next().trim().toUpperCase();
				logger.info("Semester is:"+Semester);
				System.out.println("Please enter the capacity for" + " " + courseID );
				capacity = input.next().trim().toUpperCase();
				logger.info("Capacity is:"+capacity);
				if((Semester.equals("FALL")) || (Semester.equals("WINTER")) || (Semester.equals("SUMMER"))){
				String CorseDept = courseID.substring(0,4);
				if(CorseDept.equals(AdvDept)) {
					logger.info("Calling Add Course");
					int cap =Integer.parseInt(capacity);
					String s = objClient.addCourse(courseID, Semester,cap);
					if(s.equals("pass")) {
						logger.info(courseID + " "+ "is successfully added for"+ " "+Semester);
						
					}else {
						logger.info(courseID + " "+ "is not added for"+ " "+Semester);
					}
				}else {
					logger.info("Error!The Advisor can only add a course in his/her own department");
					System.out.println("You can only add a course in your department, please try again!");
				}
				}else {
					logger.info("Error!Invalid Semester entered");
					System.out.println("Invalid Semester");
				}
			}else if(choice == 2){
				System.out.println("Please enter the course number you want to remove");
				courseID = input.next().trim().toUpperCase();
				logger.info("courseID is:"+courseID);
				System.out.println("Please enter the semester(FALL/WINTER/SUMMER) for which you want to remove" + " " + courseID );
				Semester = input.next().trim().toUpperCase();
				logger.info("Semester is:"+Semester);
				if((Semester.equals("FALL")) || (Semester.equals("WINTER")) || (Semester.equals("SUMMER"))){
				String CorseDept = courseID.substring(0,4);
				if(CorseDept.equals(AdvDept)) {
					String s = objClient.removeCourse(courseID, Semester);
					if(s.equals("pass")) {
						logger.info(courseID + " "+ "is successfully removed for"+ " "+Semester);
					}else {
						logger.info(courseID + " "+ "is not enrolled for"+ " "+Semester);
					}
				}else {
					logger.info("Error!The Advisor can only drop a course in his/her own department");
					System.out.println("You can only drop a course in your department, please try again!");
				}}else {
					logger.info("Error!Invalid Semester entered");
					System.out.println("Invalid Semester");
				}
			}else if (choice == 3) {
				String s=null;
				System.out.println("Please enter the semester(FALL/WINTER/SUMMER)");
				Semester = input.next().trim().toUpperCase();
				logger.info("Semester is:"+Semester);
				if((Semester.equals("FALL")) || (Semester.equals("WINTER")) || (Semester.equals("SUMMER"))){
				if (AdvDept.equals("COMP")) {
					Semester += "C";
				//	System.out.println(Semester);
					s = objClient.listCourseAvailability(Semester);
				}else if(AdvDept.equals("SOEN")) {
					Semester += "S";
				//	System.out.println(Semester);
					s = objClient.listCourseAvailability(Semester);
				}else if(AdvDept.equals("INSE")) {
					Semester += "I";
				//	System.out.println(Semester);
					s = objClient.listCourseAvailability(Semester);
				}if(s.equals("fail")) {
					logger.info("Courses are not listed for semester:"+Semester);
				}else {
					logger.info("Courses available are:"+s);
					System.out.println(s);
				}
				}
			}
			else if (choice == 4) {	
				while(checkflag1 == false) {
					System.out.println("Please enter student Id:");
					Scanner input1 = new Scanner(System.in);
					studentId = input1.nextLine().trim().toUpperCase();
					System.out.println("P");
					Ids= IdS;
					System.out.println(studentId);
					checkflag1 = checkUser(studentId);
					System.out.println(checkflag1);
					if (checkflag1) {
							callSMethods(studentId);
						}else {
							System.out.println("Incorrect student Id!");
						}
					break;
					}
		}
			logger.info("Do you wish to continue? (YES/NO)");
			Option = input.next().trim().toUpperCase();
			 if (Option.equals("YES")){
				 logger.info("YES");
				 callAMethods(Id);}else {
			System.exit(1);}
	}
	
	
	
	public void callSMethods(String Id) throws RemoteException {
				logger.info("Calling Student Methods");
				System.out.println("Please select the options(1/2/3) below: ");
				System.out.println("1. Enroll Course");
				System.out.println("2. Drop Course");
				System.out.println("3. Get Class Schedule");
				System.out.println("4. Swap Course");
				System.out.println("Enter your choice: ");
				int choice = input.nextInt();
				logger.info("choice is:"+choice);
				if(choice==1) {
					System.out.println("Enter the course to be enrolled:");
					courseID = input.next().trim().toUpperCase();
					logger.info("courseID is:"+courseID);
					System.out.println("Enter the semester(FALL/WINTER/SUMMER) in which you want to enroll" + " " + courseID);
					Semester = input.next().trim().toUpperCase();
					logger.info("Semester is:"+Semester);
					if((Semester.equals("FALL")) || (Semester.equals("WINTER")) || (Semester.equals("SUMMER"))){
					//	System.out.println("Valid Semester!");
					//	System.out.println(Id + courseID + Semester);
						String s = objClient.enrolCourse(Id, courseID, Semester);
						if (s.equals("pass")) {
							logger.info(Id + " "+ "is successfully enrolled for"+ " "+courseID);
						}else {
							logger.info(Id + " "+ "is not enrolled for"+ " "+courseID);
						}
					//	System.out.println(s);
						System.out.println("Do you wish to continue? (YES/NO)");
						logger.info("Do you wish to continue? (YES/NO)");
						Option = input.next().trim().toUpperCase();
						 if (Option.equals("YES")){
							 logger.info("YES");
							 callSMethods(Id);}else {
						System.exit(1);}
					}else {
						logger.info("Error! Invalid Semester entered!");
						System.out.println("Invalid Semester!");
					}

				}else if(choice == 2) {
					System.out.println("Enter the course to be dropped:");
					courseID = input.next().trim().toUpperCase();
					logger.info("courseID is:"+courseID);
					System.out.println("Id is"+Id);
					logger.info("studentID is:"+Id);
					System.out.println("courseID is"+courseID);
					String s = objClient.dropCourse(Id, courseID);
					if (s.equals("pass")) {
						logger.info(Id + " "+ "successfully dropped"+ " "+courseID);
					}else {
						logger.info(Id + " "+ "has not dropped"+ " "+courseID);
					}
				//	System.out.println(s);
					System.out.println("Do you wish to continue? (YES/NO)");
					logger.info("Do you wish to continue? (YES/NO)");
					Option = input.next().trim().toUpperCase();
					 if (Option.equals("YES")){
						 logger.info("YES");
						 callSMethods(Id);}else {
					System.exit(1);}
				}
				else if(choice == 3) {
					String s = objClient.getClassSchedule(Id);
					if (s.equals("fail")) {
						logger.info(Id + " "+ "class schedule could not be displayed");
					}else {
						logger.info(Id + " "+s);
						System.out.println(Id + " "+s);
					}
					System.out.println("Do you wish to continue? (YES/NO)");
					logger.info("Do you wish to continue? (YES/NO)");
					Option = input.next().trim().toUpperCase();
					 if (Option.equals("YES")){
						 logger.info("YES");
						 callSMethods(Id);}else {
					System.exit(1);}
				}else if(choice == 4) {
					System.out.println("Enter the old course to be swapped:");
					String oldcourseID = input.next().trim().toUpperCase();
					logger.info("old courseID is:"+oldcourseID);
					System.out.println("Enter the new course to be swapped:");
					String newcourseID = input.next().trim().toUpperCase();
					logger.info("new courseID is:"+newcourseID);
					String s = objClient.swapCourse(Id, oldcourseID,newcourseID);
					if (s.equals("fail")) {
						logger.info(Id + "Swapping failed!");
					}else {
						logger.info(Id + " "+ "successfully swapped courses"+ " "+oldcourseID+" "+"and"+" "+newcourseID);
					}
					System.out.println(s);
					System.out.println("Do you wish to continue? (YES/NO)");
					logger.info("Do you wish to continue? (YES/NO)");
					Option = input.next().trim().toUpperCase();
					 if (Option.equals("YES")){
						 logger.info("YES");
						 callSMethods(Id);}else {
					System.exit(1);}
				}
				else if(choice<1 ||choice>4) {
				System.out.println("Incorrect Option!");
				callSMethods(studentId);
			}else {
				System.out.println("Correct choice");
				}
		}
	public static void main(String args[]) throws NotBoundException, NullPointerException, SecurityException, IOException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		AdvisorClient c = new AdvisorClient();
		c.user();
	}
	
	}
	
	
	
	
	
	
