package com.listview;

public class Data {
	private char kind;
	private String title;
	private int image;
	
	public Data(char kind, String title, int image) {
		super();
		this.kind = kind;
		this.title = title;
		this.image = image;
	}
	public char getKind() {
		return kind;
	}
	public void setKind(char kind) {
		this.kind = kind;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	
}
