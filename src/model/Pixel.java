package model;

public class Pixel {
	private int row;
	private int col;
	private int clusterNo;	
	private float colorValue;

	public Pixel(int row, int col, float colorValue) {
		super();
		this.row = row;
		this.col = col;
		this.colorValue = colorValue;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getClusterNo() {
		return clusterNo;
	}
	public void setClusterNo(int clusterNo) {
		this.clusterNo = clusterNo;
	}
	public float getColorValue() {
		return colorValue;
	}
	public void setColorValue(float colorValue) {
		this.colorValue = colorValue;
	}

}
