package Rmiclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;


import Rmiserver.WebInterface;

public class TestFileSwap{
	
	static Rmiserver.WebInterface objTest;
	
	public static void main(String args[]) throws NotBoundException, NullPointerException, SecurityException, IOException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		AdvisorClient c = new AdvisorClient();
		callServer("COMPS4455");
		int cap = 2;
		String z = objTest.addCourse("COMP6542","FALL",cap);
		if(z.equals("pass")) {
			System.out.println("COMP6542 is successfully added for FALL with capacity:"+ " "+cap);
			
		}else {
			System.out.println("COMP6542 is not added for FALL");
		}
		String m = objTest.enrolCourse("COMPS4455", "COMP6431", "FALL");
		if (m.equals("pass")) {
			System.out.println("COMPS4455 is successfully enrolled for COMP6431");
			String a = objTest.getClassSchedule("COMPS4455");
			if (a.equals("fail")) {
				System.out.println("COMPS4455 class schedule could not be displayed");
			}else {
				System.out.println("COMPS4455 :"+a);
			}
		}else {
			System.out.println("COMPS4455 is not enrolled for COMP6431");
		}
		
		String n = objTest.enrolCourse("COMPS2233", "COMP6431", "FALL");
		if (n.equals("pass")) {
			System.out.println("COMPS2233 is successfully enrolled for COMP6431");
			String b = objTest.getClassSchedule("COMPS2233");
			if (b.equals("fail")) {
				System.out.println("COMPS2233 class schedule could not be displayed");
			}else {
				System.out.println("COMPS2233 :"+b);
			}
		}else {
			System.out.println("COMPS2233 is not enrolled for COMP6431");
		}
		
		String o = objTest.enrolCourse("COMPS3344", "COMP6431", "FALL");
		if (o.equals("pass")) {
			System.out.println("COMPS3344 is successfully enrolled for COMP6431");
			String l = objTest.getClassSchedule("COMPS3344");
			if (l.equals("fail")) {
				System.out.println("COMPS3344 class schedule could not be displayed");
			}else {
				System.out.println("COMPS3344 :"+l);
			}
		}else {
			System.out.println("COMPS3344 is not enrolled for COMP6431");
		}
		
		Runnable task = () -> {
			try {
				run("COMPS4455","COMP6431","COMP6542");
			} catch (SecurityException | InvalidName | NotFound | CannotProceed
					| org.omg.CosNaming.NamingContextPackage.InvalidName | NotBoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		Runnable task1 = () -> {
			try {
				run("COMPS2233","COMP6431","COMP6542");
			} catch (SecurityException | InvalidName | NotFound | CannotProceed
					| org.omg.CosNaming.NamingContextPackage.InvalidName | NotBoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		Runnable task2 = () -> {
			try {
				run("COMPS3344","COMP6431","COMP6542");
			} catch (SecurityException | InvalidName | NotFound | CannotProceed
					| org.omg.CosNaming.NamingContextPackage.InvalidName | NotBoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		Thread t1 = new Thread(task);
		Thread t2 = new Thread(task1);
		Thread t3 = new Thread(task2);
		
		t1.start();
		t2.start();
		t3.start();
	}
	
	public static void run(String Id,String oldcourseID, String newcourseID) throws MalformedURLException, RemoteException, SecurityException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName, NotBoundException, IOException {
	//	callServer(Id);
		System.out.println(Id + " " +"is swapping course"+" "+ oldcourseID +" "+ "with course"+" "+newcourseID);
		String s = objTest.swapCourse(Id, oldcourseID,newcourseID);
		System.out.println(Id + "-Swapping result is:" +s);
	//	}
	}
	
	
	public static void callServer(String Id) throws MalformedURLException, RemoteException, NotBoundException,SecurityException,IOException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName{
		String userDept = Id.substring(0,4).toUpperCase().trim();
		System.out.println("User dept is:"+ userDept);
	
		if(userDept.equals("COMP")) {
			URL compURL = new URL("http://localhost:8080/comp?wsdl");
			QName compQName = new QName("http://Rmiserver/", "FunctionsService");
			Service compService = Service.create(compURL, compQName);
			objTest = compService.getPort(WebInterface.class);
		}
		else if(userDept.equals("SOEN")) {
			URL soenURL = new URL("http://localhost:8080/soen?wsdl");
			QName soenQName = new QName("http://Rmiserver/", "FunctionsService");
			Service soenService = Service.create(soenURL, soenQName);
			objTest = soenService.getPort(WebInterface.class);
		}
		else if(userDept.equals("INSE")) {
			URL inseURL = new URL("http://localhost:8080/inse?wsdl");
			QName inseQName = new QName("http://Rmiserver/", "FunctionsService");
			Service inseService = Service.create(inseURL, inseQName);
			objTest = inseService.getPort(WebInterface.class);
		}
	}

}
