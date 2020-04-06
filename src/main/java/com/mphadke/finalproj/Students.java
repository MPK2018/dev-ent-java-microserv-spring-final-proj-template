package com.mphadke.finalproj;


public class Students {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;


    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getphoneNumber() { return phoneNumber;}
    public void setphoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber;}
}

class DeleteStudent {
    private int studentId;



    public int getStudentId() {
        return studentId;
    }
    public void setFirstName(int studentId) {
        this.studentId = studentId;
    }

}
class UpdateStudent {
    private int studentId;
    private String email;
    private String phoneNumber;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "UpdateStudent{" +
                "studentId=" + studentId +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}