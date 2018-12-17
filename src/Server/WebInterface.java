package Rmiserver;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WebInterface {

	
	public String addCourse(String courseID,String semester,int capacity);
	public String removeCourse(String courseID,String semester);
	public String listCourseAvailability(String semester);
	public String enrolCourse(String studentID, String courseID,String semester);
	public String getClassSchedule(String studentID);
	public String dropCourse(String studentID,String courseID);
	public String swapCourse(String studentID, String oldcourseID, String newcourseID);

}
