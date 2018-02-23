import java.util.Date;

public class Point {
	
	private Date date;
	private boolean overtime;
	
	public Point() {
		this.date = new Date();
		this.overtime = false;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isOvertime() {
		return overtime;
	}
	public void setOvertime(boolean overtime) {
		this.overtime = overtime;
	}
	
}
