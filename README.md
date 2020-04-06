## Tutoring Service

 ## Tutoring Service Scheduling Spring Final Project Template

This API facilitates the process for students to enroll in a class and schedule meetings for the class. They can enroll in as many classes as they want and manage the meeting schedule.
This API also facilitates the instructors to manage the Student information including registration.

## Setup

I have built this project with a MySQL server, Jetty, Aws instance. All you need is my weblink to the project, which is:
http://ec2-52-42-165-6.us-west-2.compute.amazonaws.com:8080/My-spring-proj-template/


## How to proceed

Before you can proceed with scheduling instructor have to register a student.

## printAllStudents
  This is more useful for the instructor /administration than the students. This does not need any user input. It prints all the students as the name suggests.
 
## printStudentByLastname
  This prints the details of a student and requires the last name as an input. If a student forgets their student id this will help them find it. It will also allow them to see their account details.
 
## RegisterStudent
  Here administrator/instructor will register as a student their first name, last name, email, and phone number for now.
  If a student is already registered, then proceed to enroll in classes of your choosing. If the student's information needs to be updated then updateStudent will allow you to update the information. If the student no longer wishes to use the services deleteStudent will delete student's enrollment and remove the student from all enrolled classes and delete all meeting schedules. All appropriate error messages will help you navigate through Student API
   
## Enroll for the classes
  Here student/instructor/administrator can register for one or more classes. To enroll for a class they will need student id and subject id.
  If the subject id is not known, enter 99 in the subject id field, and a list of all the subjects offered will be displayed.
  if the student is not registered for the class, the student will be directed to register for the class. If the subject is not offered then the list of all the subjects offered will be displayed. If a student has already enrolled in a class then a student will be directed to schedule a meeting. All appropriate error messages will help you navigate through Enroll Student API

## scheduleMeeting/deleteMeeting
  Students/administrators/instructors can schedule meeting for a class using this API. Enter student id, subject id, date, and time. if a student is not enrolled in the class or registered, then the student will be directed to do so. The date should be a valid date in the form mm/dd/yyyy for the successful scheduling of a class. Similarly, the time is 24-hour (military) time in hh:mm format. Time should be a valid time in order to register for the class. If the student/instructor can no longer attend the meeting, then the meeting can be canceled by deleteMeeting API. All appropriate error messages will help you navigate through the Schedule meeting API.
## getMeetingscheduleByStudentId
The student/Instructor can print the meeting schedule for a student and it will show all the scheduled meetings for that student. provide the student id to print the schedule. if the student is not registered for this service you will get a record not found message. If the meeting is already scheduled then an error message will be displayed. If a meeting needs to be rescheduled it can be achieved by deleting a meeting and then adding it again.
   
## Improvements
   There are a couple of enhancements that I wanted to do, but due to lack of time, I was not able to implement them.
   If I had more time I would have implemented methods  such as
   - updateSchedule
   I will have an available meeting schedule for a class and have students choose from that data.
   I did not add am/pm because I wanted to add the above-mentioned option which would have made this option redundant.
   etcetera.
   
  ## Expectation
  I hope to complete this implementation add above mention functionality and some more and make it a fully functioning service for my students.
  
   
