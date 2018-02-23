import java.util.Date;
import java.util.ArrayList;

public class Employee {

	private int id;
	private String name;
	private String address;
	private TypeEmp type;
	private MethodPag method;
	
	private ArrayList<Point> points;
	private int indexSchedule;
	private Date lastPayment;
	
	private float hourly;
	private float salary;
	private float commission;
	
	private boolean assocSynd; 
	private int idSynd;
	private float rateSynd;
	
	private float salesValue;	
	
	public Employee(int id, String name, String address, TypeEmp type, MethodPag method, int indexSchedule,
			Date lastPayment) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.type = type;
		this.method = method;
		this.indexSchedule = indexSchedule;
		this.lastPayment = lastPayment;
		this.points = new ArrayList<>();
		this.salesValue = 0;
		this.commission = 0;
	}

	public Employee() {
		this.points = new ArrayList<>();
		this.salesValue = 0;
		this.commission = 0;
		this.lastPayment = new Date();
	}
	
	public Date getLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(Date lastPayment) {
		this.lastPayment = lastPayment;
	}
	
	public int getIndexSchedule() {
		return indexSchedule;
	}

	public void setIndexSchedule(int indexSchedule) {
		this.indexSchedule = indexSchedule;
	}
	
	public void addPointCard() {
		
		Point point = new Point();
		
		int size = points.size();
		if(size >= 8) {
			long time = (point.getDate().getTime() - points.get( size - 8 ).getDate().getTime()) / 3600000;
			
			if(time < 24)
				point.setOvertime(true);
		}
		
		this.points.add(point);
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	public boolean hasPoints() {
		return !this.points.isEmpty();
	}
	
	public float getSalesValue() {
		return salesValue;
	}
	public void setSalesValue(float salesValue) {
		this.salesValue = salesValue;
	}
	public void addSalesValue(float increment) {
		this.salesValue = this.salesValue + increment;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public TypeEmp getType() {
		return type;
	}
	public void setType(TypeEmp type) {
		this.type = type;
	}
	public float getHourly() {
		return hourly;
	}
	public void setHourly(float hourly) {
		this.hourly = hourly;
	}
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	public float getCommission() {
		return commission;
	}
	public void setCommission(float comission) {
		this.commission = comission;
	}
	public MethodPag getMethod() {
		return method;
	}
	public void setMethod(MethodPag method) {
		this.method = method;
	}
	public boolean isAssocSynd() {
		return assocSynd;
	}
	public void setAssocSynd(boolean assocSynd) {
		this.assocSynd = assocSynd;
	}
	public int getIdSynd() {
		return idSynd;
	}
	public void setIdSynd(int idSynd) {
		this.idSynd = idSynd;
	}
	public float getRateSynd() {
		return rateSynd;
	}
	public void setRateSynd(float rateSynd) {
		this.rateSynd = rateSynd;
	}
	
	
	
	

}
