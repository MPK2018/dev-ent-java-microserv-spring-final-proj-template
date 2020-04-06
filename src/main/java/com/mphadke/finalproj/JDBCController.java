package com.mphadke.finalproj;

import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;


@RestController
public class JDBCController {
    private final static String KEYFILEPATH = "./keyFile.key";

   //Print all the Students $$$
    @CrossOrigin
    @RequestMapping(value = "/printAllStudents", method = RequestMethod.GET)
    public ResponseEntity<String> printAllStudents() {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();

        String queryStr = "SELECT * from Students;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(queryStr);

        return new ResponseEntity<String> ("List of all Students:\n"+printStudent(sqlRowSet), HttpStatus.OK);
    }


    //Get a Student By Name
    @CrossOrigin
    @RequestMapping(value = "/getStudentsByLastname", method = RequestMethod.GET)
    public ResponseEntity<String> getStudentsByLastname(@RequestParam(value="lastName", defaultValue="") String lastName) {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();

        String resultStr ="";

        if (lastName.equals(""))
            return new ResponseEntity<String>("Please Enter the Last Name", HttpStatus.BAD_REQUEST);

        String queryStr = "SELECT * from Students where lastName = \"" +lastName +"\";";

        try {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(queryStr);
            resultStr = printStudent(sqlRowSet);
        } catch (Exception e){
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(resultStr, HttpStatus.OK);
    }

    //Add new Student  $$$
    @CrossOrigin
    @RequestMapping(value = "/registerStudent", method = RequestMethod.POST)
    public ResponseEntity<String> registerStudent(@RequestBody Students registerStudent) {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();

        String queryStr = "INSERT INTO Students(firstName, lastName, email, phoneNumber) " +
                "VALUES (" +
                "'" + registerStudent.getFirstName() + "'," +
                "'" + registerStudent.getLastName() + "'," +
                "'" + registerStudent.getEmail() + "'" +"," +
                "'" + registerStudent.getphoneNumber() + "'" +
                ");";

        int rowsUpdated = 0;
        try {
             rowsUpdated = jdbcTemplate.update(queryStr);

        }catch (DuplicateKeyException dae) {
            return new ResponseEntity<String>("You are already registered, Please enroll for the class", HttpStatus.CONFLICT);
        }catch (DataAccessException dae) {
            return new ResponseEntity<String>("Error "+dae, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("Rows Inserted: \n" + getNewStudent(registerStudent.getLastName() ), HttpStatus.OK);
    }

    //Delete Student  $$$
    @CrossOrigin
    @RequestMapping(value = "/deleteStudent", method = RequestMethod.POST)
    public ResponseEntity<String> deleteStudent(@RequestBody DeleteStudent deleteStudent) {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();

        String data = getStudentById(deleteStudent.getStudentId());

        String queryStr = "Delete from Students where studentId = "+deleteStudent.getStudentId()+";";

        int rowsUpdated = 0;
        try {
            rowsUpdated = jdbcTemplate.update(queryStr);
            if(rowsUpdated==0)
                throw new NoSuchMethodException("No record found");
        }catch (NoSuchMethodException e) {
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);
        }catch (DataAccessException dae) {
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);
        }catch (Exception  dae) {
            return new ResponseEntity<String>("Can not delete Student \n" + dae, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("Rows updated: Deleted student \n"+ data, HttpStatus.OK);
    }

    //Update Student  details $$$
    @CrossOrigin
    @RequestMapping(value = "/updateStudent", method = RequestMethod.POST)
    public ResponseEntity<String> updateStudent(@RequestBody UpdateStudent updateStudent) {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();
        String queryStr1 = createQueryString(updateStudent);
        if (queryStr1.length() <=0)
            return new ResponseEntity<String>("Nothing to update ", HttpStatus.OK);
        String queryStr = "update Students set " +  queryStr1 +
                " where studentId = "+updateStudent.getStudentId()+";";

        int rowsUpdated = 0;
        try {
            rowsUpdated = jdbcTemplate.update(queryStr);
            if(rowsUpdated==0)
                throw new NoSuchMethodException("No record found");
        }catch (NoSuchMethodException e) {
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);
        }catch (DataAccessException dae) {
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);
        }catch (Exception  dae) {
            return new ResponseEntity<String>("You did not update Student details" + dae, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("Rows updated: updated student details \n" + getStudentById(updateStudent.getStudentId()), HttpStatus.OK);
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

        try {
             rowsUpdated = jdbcTemplate.update(queryStr);
        } catch (DuplicateKeyException dae) {
             return new ResponseEntity<String>("You have enrolled for this class", HttpStatus.CONFLICT);
        }catch (DataIntegrityViolationException dae){

            if( dae.getLocalizedMessage().contains("F-Key2")) { //Subject does not exist
                String subjectError = "Subject is not offered  \n" + getallSubject();
                return new ResponseEntity<String>(subjectError, HttpStatus.FAILED_DEPENDENCY);
            }
            if( dae.getLocalizedMessage().contains("F-Key1")) {  //Student does not exist
                String subjectError ="Student is not registered Please Register";
                return new ResponseEntity<String>(subjectError, HttpStatus.FAILED_DEPENDENCY);
            }
        }catch (DataAccessException dae) {
            return new ResponseEntity<String>("Error :"+dae, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("You are succesfully enrolled in the class ", HttpStatus.OK);
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
       String[] message={""};
       int validDate = dV.formatDate(scheduleMeetingData.getDate(), message );
       if(validDate !=0)
           return new ResponseEntity<String>(message[0], HttpStatus.BAD_REQUEST);

       //If you are here the date and time are valid
       String queryStr = "INSERT INTO MeetingSchedule(studentId,subjectId,meetingDate,meetingTime) " +
               "VALUES (" +
               "'" + scheduleMeetingData.getStudentId() + "'," +
               "'" + scheduleMeetingData.getSubjectId() + "'," +
               "'" + scheduleMeetingData.getDate() + "'" +"," +
               "'" + scheduleMeetingData.getTime() + "'" +
               ");";

       try {
           int rowsUpdated = jdbcTemplate.update(queryStr);
       } catch (DuplicateKeyException dae) {
               return new ResponseEntity<String>("You have scheduled this Meeting choose different time", HttpStatus.CONFLICT);
       } catch (DataIntegrityViolationException dae){

           //Class  does not exist
              String classtError ="Class dose not exist";
               return new ResponseEntity<String>("You have not enrolled forthis class", HttpStatus.FAILED_DEPENDENCY);

       }catch (DataAccessException dae) {

           return new ResponseEntity<String>(dae.getMessage(), HttpStatus.FAILED_DEPENDENCY);
       }

           return new ResponseEntity<String>("Meeting scheduled : "
                   + scheduleMeetingData.getDate() + " at  "
                    + scheduleMeetingData.getTime(), HttpStatus.OK );

    }


    //Get a meeting by StudentId
    @CrossOrigin
    @RequestMapping(value = "/getMeetingScheduleForStudent", method = RequestMethod.GET)
    public ResponseEntity<String> getMeetingScheduleForStudent(@RequestParam(value="studentId", defaultValue="") int id) {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();

        String resultStr = "";
        String queryStr = "SELECT * from MeetingSchedule where studentId = \"" +id +"\";";

        try {
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(queryStr);
            if (!sqlRowSet.isBeforeFirst() ) {
                throw new EmptyResultDataAccessException(0);
            }
            resultStr = printMeeting(sqlRowSet);
        }catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<String>(String.valueOf(resultStr), HttpStatus.OK);

    }

    @RequestMapping(value = "/deleteMeeing", method = RequestMethod.POST)
    public ResponseEntity<String> deleteMeeting(@RequestBody DeleteMeeting deleteMeeting) {
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();
        StringBuilder resultStr = new StringBuilder("Scheduleid StudentId SubjectId Date       Time \n");
       int id = deleteMeeting.getscheduleId();
        ResponseEntity<String> result = getMeetingScheduleForStudent(id);
        String queryStr = "DELETE from MeetingSchedule where scheduleId = \"" +id +"\";";

        int rowsUpdated=0;
        try {
            rowsUpdated = jdbcTemplate.update(queryStr);
            if(rowsUpdated==0)
                throw new NoSuchMethodException("No rcord found");
        }catch (NoSuchMethodException e) {
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<String>("Record Not Found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>("Meeting deleted Successfully", HttpStatus.OK);

    }

    //get the new student details
    public String getNewStudent(String lastName){
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();

        String queryStr = "SELECT * from Students where lastName = \"" +lastName +"\";";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(queryStr);

        String resultStr = printStudent(sqlRowSet);
        if (resultStr.length()<=0)
             return "Record Not Found";
        return resultStr;
    }

    //get the student details by id
    public String getStudentById(int id){
        JdbcTemplate jdbcTemplate = JDBCConnector.getJdbcTemplate();
        StringBuilder resultStr =
                new StringBuilder("StudentId , Name                                              , Email                     , Phone Number");

         String queryStr = "SELECT * from Students where studentId = \"" + id +"\";";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(queryStr);


        if (resultStr.length()<=0)
            return "Record Not Found";
        return String.valueOf(printStudent(sqlRowSet));
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

   public String createQueryString(UpdateStudent updStd){
        String queryStr = "";
        if(!updStd.getEmail().isEmpty() || updStd.getEmail().length()>0) {
        queryStr += " email = \"" + updStd.getEmail() +"\"";
        }
        if (queryStr.length()>0)
               queryStr+=", ";
       if(!updStd.getPhoneNumber().isEmpty() || updStd.getPhoneNumber().length()>0) {
           queryStr += "phoneNumber = \""+updStd.getPhoneNumber()+"\"";
       }

        return queryStr;
   }

   public String printStudent(SqlRowSet sqlRowSet){
       StringBuilder resultStr =
               new StringBuilder("StudentId   Name                                                Email                       Phone Number\n");
       while (sqlRowSet.next()) {
              resultStr.append(String.format("%-10s", sqlRowSet.getString("studentId"))).append("  ")
                   .append(String.format("%-50s", ((sqlRowSet.getString("firstName") + " " + sqlRowSet.getString("lastName"))))).append("  ")
                   .append(String.format("%-25s", sqlRowSet.getString("email"))).append(" ").append("  ")
                   .append(String.format("%10s", sqlRowSet.getString("phoneNumber")))
                   .append("\n");
       }

       return String.valueOf(resultStr);
   }
    public String printMeeting(SqlRowSet sqlRowSet){
        StringBuilder resultStr =
                new StringBuilder("Meeting id   Student Id     Subject Id   Date      Time\n");
        while (sqlRowSet.next()) {
            resultStr.append(String.format("%-11s", sqlRowSet.getString("scheduleId"))).append("  ")
                     .append(String.format("%-11s", sqlRowSet.getString("studentId"))).append("  ")
                    .append(String.format("%-11s", sqlRowSet.getString("subjectId"))).append( "  ")
                    .append(String.format("%10s", sqlRowSet.getString("meetingDate"))).append("  ")
                    .append(String.format("%5s", sqlRowSet.getString("meetingTime")))
                    .append("\n");
        }

        return String.valueOf(resultStr);
    }

}
