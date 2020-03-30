package com.mphadke.finalproj;

public class ScheduleMeeting {
    int studentId;
    int subjectId;
    String date;
    String time;
    String MorningEvening;

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

    public String getDate() {

        return date.toString();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getMorningEvening() {
        return MorningEvening;
    }

    public void setMorningEvening(String amPm) {
        System.out.println("In setampm");
        this.MorningEvening = amPm;
        System.out.println("In setampm" + this.MorningEvening);
    }

    @Override
    public String toString() {
        return "ScheduleMeeting{" +

                ", studentId=" + studentId +
                ", subjectId=" + subjectId +
                ", date=" + date +
                ", time=" + time +
                ", AmPm=" + MorningEvening +
                '}';
    }
}
