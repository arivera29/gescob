package com.are.gescob.model;

public class Alert {
	
	public static int LEVEL_INFO = 1;
	public static int LEVEL_WARNING = 2;
	public static int LEVEL_DANGER = 3;
	
	private Integer level;
	private String message;
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Alert(Integer level, String message) {
		super();
		this.level = level;
		this.message = message;
	}
	
	public Alert() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
