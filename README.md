## Tutoring Service

Spring Final Project Template
#Tutiring Service Schedulling Spring Final Project Template

This API faciliates the process for students to enroll in a clas and schedule meetings for the class. They cab enroll in as many classes they want and manage the meeting schedule.
This API also facilitates the instructors to manage the Student information including registration.

## Setup

I have built this project with a MySQL sever, Jetty, Aws instance. All you need is my weblik to the project, which is: 
http://ec2-52-42-165-6.us-west-2.compute.amazonaws.com:8080/My-spring-proj-template/


## How to proceed

Before you can proceed with scheduling instructor have to register a student. 
## printAllStudents
  This is more useful for the instructucter/administration than the students. This does not need any user input. It prints all the students as name suggest. 
  
## printStudentByLastname
  This prints the deatils of a student and requires the last name as an input. If a students forgets their student id this will help them find it. It will also allow them to see their account details.
  
## RegisterStudent 
  Here administrator/instructor will register as a student their first name, lat name, email, and phone number for now. 
  If student is already registered, then proceed to enroll in classes of your choosing. If the students information needs to be updated then updateStudent will allow you to udate the information. If student no longer wished to use the services #deleteStudent will delete studnts enrollment and remove student from all enrolled classs and delete all meeting schedules.
   
## Enroll for the classes
  Here student/instrucor/administrator can register for one or more classes. To enroll for a class they will need  student id and subject id. 
  If the subject id is not known, enter 99 in the subject id field, and list of all the subject offered will be displayed.
  if the student is not registered for the class, the student will be directed to register for the class. If the subject is not offered then the list of all the subject offered will be displayed. If a student have already enrolled in a class then a student will be directed to schedule a meeting.

## scheduleMeeting
  Student/administrator/instructot can schedule meeting for a class using this API. Enter student id, subject id, date, and time. if you have not enrolled in the class, you will be directed to do so. The date should be a valid date in the form mm/dd/yyy for succesful scheduliing of a class. Similarly, the time is 24-hour (military) time in hh:mm format. Time should be a valid time in order to register for the class.
## getMeetingscheduleByStudentId
You can print the meeting schedule for a student and it willshow all the scheduled meetings fir that student. You will need to give your student id. if the student is not regitered for this service you will get record not found message.
   
## Improvements
   There are a couple enhancements that I wanted to do, but due to lack of time I was not able to implement them.
   If I had more time I would have implemented methods  such as
   - Delete registration
   - Change meeting schedule
   - Delete meeting. 
   ecetera. 
  ## Expectation
  I hope to complete this implementation add above mention functionality and some more and make it fully functioning service for my students.
  
   
