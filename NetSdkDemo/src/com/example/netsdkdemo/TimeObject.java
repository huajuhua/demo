package com.example.netsdkdemo;

public class TimeObject {
	Integer Hour;
	Integer Minute;
	int Day;
	int Month;
	int Year;
	
	TimeObject(Integer Minute,Integer Hour,int Day,int Month,int Year){
		this.Hour=Hour;
		this.Minute=Minute;
		this.Day=Day;
		this.Month=Month;
		this.Year=Year;
	}
	public Integer getHour() {
		return Hour;
	}
	public void setHour(Integer hour) {
		Hour = hour;
	}
	public Integer getMinute() {
		return Minute;
	}
	public void setMinute(Integer minute) {
		Minute = minute;
	}
	public int getDay() {
		return Day;
	}
	public void setDay(int day) {
		Day = day;
	}
	public int getYear() {
		return Year;
	}
	public void setYear(int year) {
		Year = year;
	}
	public int getMonth() {
		return Month;
	}
	public void setMonth(int month) {
		Month = month;
	}
}
