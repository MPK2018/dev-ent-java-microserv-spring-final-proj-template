package com.mphadke.finalproj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateValidator {
    public boolean formatTime(String time){

        String[] arrOfStr = time.split(":");
        if((Integer.parseInt(arrOfStr[0])>24) || (Integer.parseInt(arrOfStr[1])>59))
            return false;
        return true;
    }

    public int formatDate(String date, String[] message){
        String[] errStr = {"Valid date"," Past year cannot schedule Meeting","Invalid Month",
                "Invalid Date","Past Month cannot schedule Meeting","Invalid leap year Date"};
        DateValidator Dv = new DateValidator();
        Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016
        Calendar cal = Calendar.getInstance();
        //cal.setTime(today); // don't forget this if date is arbitrary e.g. 01-01-2014


        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH); // 17
        int month = cal.get(Calendar.MONTH)+1; // 5
        int year = cal.get(Calendar.YEAR); // 2016

        int err= 0;
        String[] arrOfStr = date.split("/");
        int dd = Integer.parseInt(arrOfStr[1]);
        int mm = Integer.parseInt(arrOfStr[0]);
        int yy = Integer.parseInt(arrOfStr[2]);
        if(yy < year) {
            message[0] = errStr[1];
            return 1;
        }
        if( mm < 1 || mm > 12 ) {
            message[0] = errStr[2];
            return 2;
        }
        if (dd <1 || dd>31) {
            message[0] = errStr[3];
            return 3;
        }
        if(yy == year && mm < month) {
            message[0] = errStr[4];
            return 4;
        }
        if((mm ==2 && yy%4!=0 && dd>28) || (mm==2 && yy%4==0 && dd>29)) {
            message[0] = errStr[5];
            return 5;
        }
        return err;
    }

    public boolean isThisDateValid(String dateToValidate, String dateFromat) {

        if (dateToValidate == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);

            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }
}