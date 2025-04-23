package me.cbhud.model;

public class TimeResponse {
    private float year;
    private float month;
    private float day;
    private float hour;
    private float minute;
    private float seconds;
    private float milliSeconds;
    private String dateTime;
    private String date;
    private String time;
    private String timeZone;
    private String dayOfWeek;
    private boolean dstActive;


    public float getYear() {
        return year;
    }

    public float getMonth() {
        return month;
    }

    public float getDay() {
        return day;
    }

    public float getHour() {
        return hour;
    }

    public float getMinute() {
        return minute;
    }

    public float getSeconds() {
        return seconds;
    }

    public float getMilliSeconds() {
        return milliSeconds;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public boolean getDstActive() {
        return dstActive;
    }

    // Setter Methods

    public void setYear( float year ) {
        this.year = year;
    }

    public void setMonth( float month ) {
        this.month = month;
    }

    public void setDay( float day ) {
        this.day = day;
    }

    public void setHour( float hour ) {
        this.hour = hour;
    }

    public void setMinute( float minute ) {
        this.minute = minute;
    }

    public void setSeconds( float seconds ) {
        this.seconds = seconds;
    }

    public void setMilliSeconds( float milliSeconds ) {
        this.milliSeconds = milliSeconds;
    }

    public void setDateTime( String dateTime ) {
        this.dateTime = dateTime;
    }

    public void setDate( String date ) {
        this.date = date;
    }

    public void setTime( String time ) {
        this.time = time;
    }

    public void setTimeZone( String timeZone ) {
        this.timeZone = timeZone;
    }

    public void setDayOfWeek( String dayOfWeek ) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDstActive( boolean dstActive ) {
        this.dstActive = dstActive;
    }
}
