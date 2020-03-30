package com.mphadke.finalproj;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;



@RestController
public class JDBCController {
    private final static String KEYFILEPATH = "./keyFile.key";

   //Print all the Students $$$
    @CrossOrigin
    @RequestMapping(value = "/printAllStudents", method = RequestMethod.GET)
    public ResponseEntity<String> printAllStudents() {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();
        StringBuilder resultStr = new StringBuilder("List of all Students:\n");

        String queryStr = "SELECT * from Students;";
        System.out.println(queryStr);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(queryStr);
        while (sqlRowSet.next()) {
            resultStr.append(sqlRowSet.getString("studentId")).append(", ")
                    .append(sqlRowSet.getString("firstName")).append(", ")
                    .append(sqlRowSet.getString("lastName")).append(", ")
                    .append(sqlRowSet.getString("email")).append(", ")
                    .append(sqlRowSet.getString("phoneNumber"))
                    .append("\n");
        }
        return new ResponseEntity<String> (String.valueOf(resultStr), HttpStatus.OK);
    }


    //Get a Student By Name
    @CrossOrigin
    @RequestMapping(value = "/getStudentsByLastname", method = RequestMethod.GET)
    public ResponseEntity<String> getStudentsByLastname(@RequestParam(value="lastName", defaultValue="") String lastName) {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();
        StringBuilder resultStr = new StringBuilder();
        System.out.println("lastName is "+lastName);
        if (lastName.equals(""))
            return new ResponseEntity<String>("Please Enter the Last Name", HttpStatus.BAD_REQUEST);

        String queryStr = "SELECT * from Students where lastName = \"" +lastName +"\";";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(queryStr);
        while (sqlRowSet.next()) {
            resultStr.append(sqlRowSet.getString("studentId")).append(", ")
                    .append(sqlRowSet.getString("firstName")).append(", ")
                    .append(sqlRowSet.getString("lastName")).append(", ")
                    .append(sqlRowSet.getString("phoneNumber")).append(", ")
                    .append(sqlRowSet.getString("email")).append(", ")
                    .append("\n");
        }
        if (resultStr.length()>0)
            return new ResponseEntity<String>(String.valueOf(resultStr), HttpStatus.OK);
        else
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);

    }

    //Add new Student  $$$
    @CrossOrigin
    @RequestMapping(value = "/registerStudent", method = RequestMethod.POST)
    public ResponseEntity<String> registerStudent(@RequestBody Students registerStudent) {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();
        System.out.println("I am here");
    /*    if (!checkForNull(registerStudent.getEmail())){
            return new ResponseEntity<String>("Email cannot be empty, Please enter it again", HttpStatus.BAD_REQUEST);
        }
        if (!checkForNull(registerStudent.getphoneNumber())){
            return new ResponseEntity<String>("Phone number cannot be empty, Please enter it again", HttpStatus.BAD_REQUEST);
        }*/
        String queryStr = "INSERT INTO Students(firstName, lastName, email, phoneNumber) " +
                "VALUES (" +
                "'" + registerStudent.getFirstName() + "'," +
                "'" + registerStudent.getLastName() + "'," +
                "'" + registerStudent.getEmail() + "'" +"," +
                "'" + registerStudent.getphoneNumber() + "'" +
                ");";
        System.out.println(queryStr);
        int rowsUpdated = 0;
        try {
             rowsUpdated = jdbcTemplate.update(queryStr);
        }catch (DuplicateKeyException dae) {
            System.out.println("Record Already Exist");
            return new ResponseEntity<String>("You are already registered, Please enroll for the class", HttpStatus.CONFLICT);
        }catch (DataAccessException dae) {
            System.err.println("Error is :"+ dae);
        }
        return new ResponseEntity<String>("Rows updated: " + rowsUpdated, HttpStatus.OK);
    }

    //Enroll in class
    @CrossOrigin
    @RequestMapping(value = "/enrollInClass", method = RequestMethod.POST)
    public ResponseEntity<String> enrollInClass(@RequestBody EnrollInClass enrollClass) {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();

        int student_id = enrollClass.getStudentId();
        int subject_id = enrollClass.getSubjectId();
        String queryStr = "INSERT INTO EnrolledClasses(studentId, subjectId) " +
                "VALUES (" +
                "'" + student_id + "'," +
                "'" + subject_id + "'" +
                ");";
        int rowsUpdated = 0;
        System.out.println(queryStr);
        try {

             rowsUpdated = jdbcTemplate.update(queryStr);

        } catch (DuplicateKeyException dae) {
            System.out.println("Record Already Exist");
            return new ResponseEntity<String>("You have enrolled for this class", HttpStatus.CONFLICT);
        }catch (DataIntegrityViolationException dae){
            System.out.println("I am here "+ dae.getMessage());
            //Subject does not exist
            if( dae.getLocalizedMessage().contains("F-Key2")) {
                String subjectError = "Subject is not offered  \n" + getallSubject();
                System.out.println(subjectError);
                return new ResponseEntity<String>(subjectError, HttpStatus.FAILED_DEPENDENCY);
            }
            //Student does not exist
            if( dae.getLocalizedMessage().contains("F-Key1")) {
                String subjectError ="Student is not registered Please Register";
                return new ResponseEntity<String>(subjectError, HttpStatus.FAILED_DEPENDENCY);
            }
        }catch (DataAccessException dae) {
            System.err.println("Error is :"+ dae);
        }
        return new ResponseEntity<String>("Rows updated: " + rowsUpdated, HttpStatus.OK);
    }



   // Schedule A meeting
   @CrossOrigin
   @RequestMapping(value = "/scheduleMeeting", method = RequestMethod.POST)
   public ResponseEntity<String> scheduleMeeting(@RequestBody  ScheduleMeeting scheduleMeetingData) {

       String[] errStr = {"Valid date"," for past year","Invalid Month",
               "Invalid Date","for past month","Invalid leap year Date"};
       DateValidator dV = new DateValidator();
       JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();

       // Check for valid date
       if(!dV.isThisDateValid(scheduleMeetingData.getDate(), "mm/dd/yyyy"))
           return new ResponseEntity<String>("Please enter date as mm/dd/yyyy", HttpStatus.BAD_REQUEST);
       if(!dV.isThisDateValid(scheduleMeetingData.getTime(), "HH:mm"))
           return new ResponseEntity<String>("Please enter Time in 24 hour Format HH:MM", HttpStatus.BAD_REQUEST);
       if(!dV.formatTime(scheduleMeetingData.getTime()))
           return new ResponseEntity<String>("Please enter Time in 24 hour Format HH:MM", HttpStatus.BAD_REQUEST);
       int validDate = dV.formatDate(scheduleMeetingData.getDate());
       if(validDate !=0)
           return new ResponseEntity<String>("cannot schedule Meeting "+ errStr[validDate], HttpStatus.BAD_REQUEST);

       //If you are here the date and time are valid
       String queryStr = "INSERT INTO MeetingSchedule(studentId,subjectId,meetingDate,meetingTime) " +
               "VALUES (" +
               "'" + scheduleMeetingData.getStudentId() + "'," +
               "'" + scheduleMeetingData.getSubjectId() + "'," +
               "'" + scheduleMeetingData.getDate() + "'" +"," +
               "'" + scheduleMeetingData.getTime() + "'" +
               ");";

       System.out.println(queryStr);
       try {
           int rowsUpdated = jdbcTemplate.update(queryStr);
       } catch (DuplicateKeyException dae) {
               System.out.println("Record Already Exist");
               return new ResponseEntity<String>("You have scheduled this Meeting choose different time", HttpStatus.CONFLICT);
       } catch (DataIntegrityViolationException dae){
           System.out.println("I am here "+ dae.getMessage());
           //Class  does not exist
              String classtError ="Class dose not exist";
               return new ResponseEntity<String>("You have not enrolled forthis class", HttpStatus.FAILED_DEPENDENCY);

       }catch (DataAccessException dae) {
           System.err.println("Error is :"+ dae);
           return new ResponseEntity<String>(dae.getMessage(), HttpStatus.FAILED_DEPENDENCY);
       }

           return new ResponseEntity<String>("Meeting scheduled : "
                   + scheduleMeetingData.getDate() + " at  "
                    + scheduleMeetingData.getTime(), HttpStatus.OK );

    }

    /*// Delete a Meeting not tested
    @CrossOrigin
    @RequestMapping(value = "/deleteMeeting", method = RequestMethod.POST)
    public ResponseEntity<String> deleteMeeting(@RequestBody DeleteMeeting deleteMeeting) {

        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();
        System.out.println("id is :"+ deleteMeeting.getMeetingId());
       try {
           String queryStr = "DELETE FROM MeetingSchedule where scheduleId = "+ deleteMeeting.getMeetingId() +";";
           System.out.println(queryStr);
           int rowsUpdated=jdbcTemplate.update(queryStr);

        }catch (DataAccessException e) {
           return new ResponseEntity<String>("Cannot Delete Record does not exist"+ e, HttpStatus.NOT_FOUND);
       }catch (Exception e){
            return new ResponseEntity<String>("Cannot Delete "+ e, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("Meeting  cancelled: ", HttpStatus.OK );
    }
*/


// General Methods

    public boolean checkForNull(String str){
        if(str.isEmpty())
        //if (str.length()==0))
            return false;
        return true;
    }

    //get all subjecs from database
    public String getallSubject(){
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();
        StringBuilder resultStr = new StringBuilder();

        String queryStr = "SELECT * from Subjects;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(queryStr);
        while (sqlRowSet.next()) {
            resultStr.append(sqlRowSet.getString("subjectId")).append(", ")
                    .append(sqlRowSet.getString("subjectDesc")).append(", ")
                    .append("\n");
        }
        return String.valueOf(resultStr);
    }
}
