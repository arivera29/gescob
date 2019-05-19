package com.are.gescob.entity;

public class Alert {
	public static final int INFO = 1;
	public static final int WARNING = 2;
	public static final int DANGER = 3;

	private int level;
	private String message;
	
	public Alert(int level, String message) {
		super();
		this.level = level;
		this.message = message;
	}
	public Alert() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
