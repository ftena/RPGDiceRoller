package com.tarlic.RPGDiceRoller;

import java.util.Date;

public class LogItem {
    public String text;
    public Date date;
    public LogItem(){
        super();
    }
    
    public LogItem(String text, Date date) {
        super();
        this.text= text;
        this.date= date;
    }
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
