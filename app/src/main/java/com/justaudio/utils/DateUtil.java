package com.justaudio.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;


import com.justaudio.activities.BaseActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by VIDYA
 */

@SuppressLint("SimpleDateFormat")
public class DateUtil {

    public static String getWeatherDate() {
        long milliseconds = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        Date resultDate = new Date(milliseconds);
        return sdf.format(resultDate);
    }

    public static String getWeatherWeek(long milliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date resultDate = new Date(milliseconds);
        return sdf.format(resultDate);
    }


    /*
    *  RETURNS THE DUE ORDER LIST DATE
    * */
    public static String getDueOrderDate(String serverDate) {
        if (serverDate != null && serverDate.contains("T")) {
            String[] str = serverDate.split("T");
            String mDate = str[0];
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = format.parse(mDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            format = new SimpleDateFormat("MM-dd-yyyy");
            return format.format(newDate);
        } else
            return serverDate;
    }


    public static List<Date> getOneWeekDates() {
       /*CURRENT DATE*/
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String dateString1 = df.format(c.getTime());

        /*WEEK DATE*/
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 6);
        String dateString2 = df.format(cal.getTime());

        ArrayList<Date> dates = new ArrayList<>();
        DateFormat df1 = new SimpleDateFormat("MM-dd-yyyy");
        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            dates.add(cal1.getTime());
            cal1.add(Calendar.DATE, 1);
        }
        return dates;
    }

    /*PARSING THE DATE TO REBURIED FORMAT*/
    public static String convertDate(String serverDate) {
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        try {
            Date date = df.parse(serverDate);
            df.applyPattern("MM-dd-yyyy");
            return df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void showDateDialog(BaseActivity parent, final EditText et) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
       /* int mhour=c.get(Calendar.HOUR);
        int mMinute=c.get(Calendar.MINUTE);
        int mSecond=c.get(Calendar.SECOND)*/
        DatePickerDialog dpd = new DatePickerDialog(parent, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                et.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);

            }
        }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis());
        dpd.show();
    }

    /*
      *  RETURNS THE CALENDAR DATE FORMAT
      * */
    public static String getCalendarDate(String serverDate) {
        if (serverDate != null && serverDate.contains("T")) {
            String[] str = serverDate.split("T");
            String mDate = str[0];
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            try {
                newDate = format.parse(mDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            format = new SimpleDateFormat("MMM-dd-yyyy");
            return format.format(newDate);
        } else
            return serverDate;
    }

}
