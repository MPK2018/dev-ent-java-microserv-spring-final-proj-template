# dev-ent-java-microserv-spring-final-proj-template
Spring Final Project Template
#Tutiring Service Schedulling Spring Final Project Template

This project faciliates the process for students to register for a tutoring service. They can register for as many classes a they need and can schedule the meetings.
## Setup

I have built this project with a MySQL sever, Jetty, Aws instance. All you need is my weblik to the project, which is: 
http://ec2-52-42-165-6.us-west-2.compute.amazonaws.com:8080/My-spring-proj-template/


## How to proceed

Before you can proceed with scheduling you have to register as a student.
## printAllStudents
  This is more useful for the instructucter than the students. This does not need any user input. It prints all the students as name suggest. 
## printStudentByLastname
  This prints the deatils of a student and requires the last name as an input. If you forget ypur student id this will help you find it.
## RegisterStudent 
  Here you will register as a student with your name, email, and phone number for now. 
  If you have already registered, you will be directed to enroll in classes of your choosing. 
## Enroll for the classes
  Here ypu will register for one or more classes. To enroll for a class ypu will need ypur student id and subject id. 
  if ypu do not know the subject id enter 99 in subject id and ypu will get the list of all the subject offered.
  You can register as a student with ypur name, email, and phone number for now. 
  If ypu have already register ypu will be directed to enroll in the classes

## scheduleMeeting
   you cna schedule meeting for a class using this method. You will need to give ypu student id, subject id date and time. if you have nore enrolled in the class ypu will be directed to do so. The date should be valid date in the form mm/dd/yyy for susscelful scheduliing of a class. Similarly the time is 24 hour time in hh:mm format. Time should be a valid time in order to register for the class.
  
## Improvements
   there are couple enhancements that I wanted to do but due to lack of time I was notable to implement them.
   If I had more time I would have impemelted methods  such as
   Delete registration
   Change meeting schedule
   Ddelete meeting. 
   and so on...
  ## Expectation
  I hope to complete this implementation add above mention functionality and some more and make it fully functioning service for my students.
  
   
