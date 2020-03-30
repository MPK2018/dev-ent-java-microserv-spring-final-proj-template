package com.mphadke.finalproj;

public class EnrollInClass {
    //int enrolledId;
    int studentId;
    int subjectId;


   /* public int getEnrolledId() {
        return enrolledId;
    }

    public void setEnrolledId(int enrolledId) {
        this.enrolledId = enrolledId;
    }*/

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public String toString() {
        return "EnrollInClass{" +
               // "enrolledId=" + enrolledId +
                ", studentId=" + studentId +
                ", subjectId=" + subjectId +
                '}';
    }
}
