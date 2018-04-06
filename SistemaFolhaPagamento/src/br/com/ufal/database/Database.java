package br.com.ufal.database;
import java.util.ArrayList;
import java.util.Date;

import br.com.ufal.employee.Employee;
import br.com.ufal.payment.PaymentSchedule;


public class Database {
	
	private ArrayList<Employee> principal = new ArrayList<Employee>();
	private ArrayList<Employee> secondary = new ArrayList<Employee>();
	private ArrayList<Employee> actual;
	
	private ArrayList<PaymentSchedule> schedules = new ArrayList<>();
	
	private DB_Used actual_DB;
	private int idLastDeleted;
	
	private static Database database = new Database();
	
	private Database() {
		actual = principal;
		actual_DB = DB_Used.PRINCIPAL;
		idLastDeleted = -1;
	}
	
	public static Database getInstance() {	
		return database;	
	}
	
	public Employee searchEmployee(int id) {
		
		for(Employee emp : actual) {
			if(emp.getId() == id) {
				
				if(emp.isDeleted())
					return null;
				
				return emp;
			}
		}
		
		return null;
		
	}
	
	public void addSchedule(PaymentSchedule sched) {
		schedules.add(sched);
	}
	
	public ArrayList<PaymentSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(ArrayList<PaymentSchedule> schedules) {
		this.schedules = schedules;
	}

	public int addEmployee(Employee emp) {
		
		update();
		
		int id = actual.size();
		emp.setId( id );
		actual.add(emp);
		return id;
	}
	
	public void editEmployee(Employee emp) {
		
		update();
		
		actual.set(emp.getId(), emp);
	}
	
	public boolean removeEmployee(int id) {
		
		if( id < actual.size() && actual.get( id ).isDeleted() != true ) {
			actual.get( id ).setDeleted( true );
			idLastDeleted = id;
			return true;
		}
		
		return false;
		
	}
	
	public ArrayList<Employee> getEmployees() {
		return actual;
	}
	
	public void setEmployees( ArrayList<Employee> emps) {
		
		update();
		
		if(actual_DB == DB_Used.SECONDARY) {
			secondary = new ArrayList<>();
			actual = secondary;
		
		} else {
			principal = new ArrayList<>();
			actual = principal;
		}
		
		actual.addAll( emps );
		
	}
		
	public void undo() {
		
		if( idLastDeleted != -1 ) {
			actual.get(idLastDeleted).setDeleted( false );
		} else {
			actual = secondary;
			actual_DB = DB_Used.SECONDARY;
		}
	}
	
	public void redo() {
		
		if( idLastDeleted != -1 ) {
			actual.get(idLastDeleted).setDeleted( true );
		} else {
			actual = principal;
			actual_DB = DB_Used.PRINCIPAL;
		}
	}
	
	private ArrayList<Employee> cloneArray(ArrayList<Employee> array) {
		
		ArrayList<Employee> newArray = new ArrayList<>();
		
		for(Employee emp : array) {
			newArray.add( emp.clone() );
		}
		
		return newArray;
		
	}
	
	private void update() {
		
		idLastDeleted = -1;
		
		if(actual_DB == DB_Used.PRINCIPAL) {
			secondary = cloneArray(principal);
		} else {
			principal = cloneArray(secondary);
		}
		
	}
	
}
