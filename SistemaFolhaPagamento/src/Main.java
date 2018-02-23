import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Main {
	
	private static int count = 0;
	private static final Scanner input = new Scanner(System.in);
	private static final ArrayList<Employee> employees = new ArrayList<>();
	private static final ArrayList<String> schedule = new ArrayList<>();
	
	public static void main(String[] args) {
		
		schedule.add("mensal $");
		schedule.add("semanal 1 sexta");
		schedule.add("semanal 2 sexta");
		
		Employee emp = new Employee(count++, "Maria", "UFAL", TypeEmp.SALARIED, MethodPag.DEPOSIT, 0, new Date());
		emp.setSalary(1000);
		employees.add(emp);
		emp = new Employee(count++, "João", "UFAL", TypeEmp.COMMISIONED, MethodPag.MAIL, 2, new Date());
		emp.setSalary(600);
		emp.setCommission(15);
		employees.add(emp);
		emp = new Employee(count++, "Joana", "UFAL", TypeEmp.HOURLY, MethodPag.DIRECTLY, 1, new Date());
		emp.setHourly(10);
		employees.add(emp);
			
		
		/*MENU*/
		
		int op = -1;
		while(op != 0) {
			System.out.println("Digite a opção desejada:\n\n"
							 + "1 - Adicionar empregado\n"
							 + "2 - Remover empregado\n"
							 + "3 - Lançar um cartão de ponto\n"
							 + "4 - Lançar um resultado de venda\n"
							 + "5 - Lançar uma taxa de serviço\n"
							 + "6 - Alterar detalhes de um empregado\n"
							 + "7 - Rodar a folha de pagamento para hoje\n"
							 + "8 - Undo/redo\n"
							 + "9 - Alterar agenda de pagamento de um empregado\n"
							 + "10 - Criar nova agenda de pagamento\n"
							 + "0 - Parar");
			op = Integer.valueOf(input.nextLine());
			
			switch (op){
			
				case 1:
					addEmployee();
					break;
				case 2:
					deleteEmployee();
					break;
				case 3:
					addPointCard();
					break;
				case 4:
					addSalesValue();
					break;
				case 5:
					addServiceRate();
					break;
				case 6:
					editEmployee();
					break;
				case 7:
					runPayroll();
					break;
				case 8:
					undo_redo();
					break;
				case 9:
					editPaymentSchedule();
					break;
				case 10:
					addPaymentSchedule();
					break;
			
			}
		}
		

	}
	
	private static void undo_redo() {}
	
	private static void runPayroll() {
		
		System.out.println("Digite a data: [dd/mm/aaaa]");
		String dateS = input.nextLine();
		
		System.out.println("	FOLHA DE PAGAMENTO");
		System.out.println("-------------------------------------------------");
		System.out.println("ID | NOME | VALOR BRUTO | TRIBUTO");
		System.out.println("-------------------------------------------------");
		
		int day = Integer.valueOf( dateS.substring(0, 2) );
		int month = Integer.valueOf( dateS.substring(3, 5) );
		int year = Integer.valueOf( dateS.substring(6) );
		
		Calendar calendarActual = Calendar.getInstance();
		calendarActual.set(year, month -1 , day);
		int dayOfWeek = calendarActual.get(Calendar.DAY_OF_WEEK);
		int maxDaysMonth = calendarActual.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int size = employees.size();
		Employee emp;
		for(int i=0; i<size; i++) {
			
			emp = employees.get(i);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(emp.getLastPayment());
				
			String empSched = schedule.get(emp.getIndexSchedule());
						
			if(day == calendar.get(Calendar.DAY_OF_MONTH) && ( month - 1 ) == calendar.get(Calendar.MONTH))
				continue;
			if(empSched.charAt(0) == 'm' ) {
					
				if(empSched.charAt(7) != '$') {
						
					int dayToPay = 0;
					if(empSched.charAt(8) != ' ')
						dayToPay = Integer.valueOf( empSched.substring(7, 9) );
					else
						dayToPay = empSched.charAt(7) - 48;
					if(dayToPay == day ) {
						
						if(emp.getType() == TypeEmp.HOURLY)
							employeePaymentHourly(emp);
						
						else if(dayToPay == calendar.get(Calendar.DAY_OF_MONTH) && (month - 1) != calendar.get(Calendar.MONTH)) 
							employeePayment(emp, calendar, calendarActual);
						
						else if(dayToPay != calendar.get(Calendar.DAY_OF_MONTH)){
							employeePaymentVariable(emp, calendar, calendarActual, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));	
						}
							
					}
						
				} else if(day == maxDaysMonth){
					
					if(emp.getType() == TypeEmp.HOURLY)
						employeePaymentHourly(emp);
					
					else if( calendar.get(Calendar.DAY_OF_MONTH) == calendar.getActualMaximum(Calendar.DAY_OF_MONTH) ) 
						employeePayment(emp, calendar, calendarActual);
						
					else 
						employeePaymentVariable(emp, calendar, calendarActual, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
						
				}
			} else if( getConstantCalendar(empSched.substring(10) ) == dayOfWeek ){
					
				if( empSched.charAt(8) == '1') {
					
					if(emp.getType() == TypeEmp.HOURLY)
						employeePaymentHourly(emp);
					else	
						employeePaymentVariable(emp, calendar, calendarActual, 7);					
						
				} else if( empSched.charAt(8) == '2'){
					
					if(countDays(calendarActual, calendar) >= 14) {
						
						if(emp.getType() == TypeEmp.HOURLY)
							employeePaymentHourly(emp);
						else
							employeePaymentVariable(emp, calendar, calendarActual, 14);
					}
				}
				
			}
	
				
		}
		
		System.out.println("-------------------------------------------------");
		
	}
	
	private static int countDays(Calendar calendarActual, Calendar calendar) {
		
		long timeActual = calendarActual.getTime().getTime();
		long time = calendar.getTime().getTime();
		
		return (int)(timeActual - time) / 86400000 ;
	}
	
	private static void employeePaymentHourly(Employee emp) {
		
		float count = emp.getPoints().size();
		for(Point p : emp.getPoints()) {
			if(p.isOvertime())
				count += 0.5;
		}
		
		System.out.printf(emp.getId()+" - "+emp.getName()+" - R$ %.2f - R$ %.2f\n",count * emp.getHourly(), emp.getRateSynd());
		emp.setPoints(new ArrayList<>());
		emp.setLastPayment(new Date());
		
	}
	
	private static void employeePayment(Employee emp, Calendar calendar, Calendar calendarActual) {
		
		float commission = emp.getSalesValue() * emp.getCommission() / 100;
		System.out.printf(emp.getId()+" - "+emp.getName()+" - R$ %.2f - R$ %.2f\n",emp.getSalary()+commission, emp.getRateSynd());
		emp.setLastPayment(calendarActual.getTime());
		emp.setPoints(new ArrayList<>());
		emp.setSalesValue(0);
		
	}
	
	private static void employeePaymentVariable(Employee emp, Calendar calendar, Calendar calendarActual, int factor) {
		
		float daily = emp.getSalary() / factor;
		int workedDays = countDays(calendarActual, calendar);		
		float commission = emp.getSalesValue() * emp.getCommission() / 100;
		
		System.out.printf(emp.getId()+" - "+emp.getName()+" - R$ %.2f - R$ %.2f\n",( daily * workedDays )+commission, emp.getRateSynd());
		emp.setLastPayment(calendarActual.getTime());
		emp.setPoints(new ArrayList<>());
		emp.setSalesValue(0);
		
	}
	
	private static int getConstantCalendar(String dayOfWeek) {
		
		if(dayOfWeek.equals("segunda"))
			return Calendar.MONDAY;
		else if(dayOfWeek.equals("terça"))
			return Calendar.TUESDAY;
		else if(dayOfWeek.equals("quarta"))
			return Calendar.WEDNESDAY;
		else if(dayOfWeek.equals("quinta"))
			return Calendar.THURSDAY;
		else if(dayOfWeek.equals("sexta"))
			return Calendar.FRIDAY;
		else if(dayOfWeek.equals("sabádo"))
			return Calendar.SATURDAY;
		
		return Calendar.SUNDAY;
		
	}
	
	private static void editPaymentSchedule() {
		
		System.out.println("Digite o id do empregado:");
		int index = searchEmployee( Integer.valueOf( input.nextLine() ));
		if(index == -1){
			System.out.println("Usuário não encontrado");
			return;
		}
		
		Employee emp = employees.get(index);
		
		int size = schedule.size();
		System.out.println("Digite o id da agenda de pagamento desejada: ");
		for(int i=0; i<size; i++) {
			System.out.println(i+" - "+schedule.get(i));
		}
		int op = Integer.valueOf( input.nextLine() );
		if(op < 0 || op >=size) {
			System.out.println("Id inválido");
			return;
		}
		
		int lastSched = emp.getIndexSchedule();
		emp.setIndexSchedule(op);
		
		if(schedule.get(lastSched).charAt(0) == 'm' && schedule.get(op).charAt(0) == 's') {
			
			if(schedule.get(op).charAt(8) == '1')
				emp.setSalary( emp.getSalary() / 4);
			else
				emp.setSalary( emp.getSalary() / 2);
		}
		else if(schedule.get(lastSched).charAt(0) == 's' && schedule.get(op).charAt(0) == 'm') {
			
			if(schedule.get(lastSched).charAt(8) == '1')
				emp.setSalary( emp.getSalary() * 4);
			else
				emp.setSalary( emp.getSalary() * 2);
		}
		
		System.out.println("Feito!");
		
	}
	
	private static void addPaymentSchedule() {
		System.out.println("Digite a nova agenda de pagamento:");
		schedule.add( input.nextLine() );
		System.out.println("Feito!");
	}
	
	private static void addSalesValue() {
		
		System.out.println("Digite o id do empregado");
		int index = searchEmployee( Integer.valueOf( input.nextLine() ) );
		if(index < 0) {
			System.out.println("Empregado não encontrado!");
			return;
		}
		
		Employee emp = employees.get(index);
		
		System.out.println("Digite o valor da venda: ");
		emp.addSalesValue( Float.valueOf( input.nextLine() ) );
				
		System.out.println("Feito!");
		
	}
	
	private static void addPointCard() {
		
		System.out.println("Digite o id do empregado");
		int index = searchEmployee( Integer.valueOf( input.nextLine() ) );
		if(index < 0) {
			System.out.println("Empregado não encontrado!");
			return;
		}
		
		Employee emp = employees.get(index);
		emp.addPointCard();
		System.out.println("---------------------------------");
		System.out.println(emp.getId()+" - "+emp.getName());
		System.out.println("---------------------------------");
		
	}
	
	private static void addServiceRate() {
		
		System.out.println("Digite o id do empregado");
		int index = searchEmployee( Integer.valueOf( input.nextLine() ) );
		if(index < 0) {
			System.out.println("Empregado não encontrado!");
			return;
		}
		
		Employee emp = employees.get(index);
		System.out.println("Digite a taxa de serviço");
		float rate = Float.valueOf( input.nextLine() );
		
		emp.setRateSynd( emp.getRateSynd() + rate );

		System.out.println("Feito!");
		
	}
	
	private static void addEmployee() {
		
		Employee emp = new Employee();
		
		emp = editPersonalEmp(emp);
		
		emp = editFinancialEmp(emp);
		
		emp = editSyndicateEmp(emp);
		
		emp.setId( count++ );
		
		employees.add(emp);
		
		System.out.println("ID: "+(count-1)+"\nFeito!\n");

		
	}
	
	private static void editEmployee() {
		
		System.out.println("Digite o id do empregado: ");
		int id = Integer.valueOf( input.nextLine() );
		
		int index = searchEmployee(id);
		
		if(index < 0) {
			System.out.println("Usuário não encontrado!");
			return;
		}
		
		Employee emp = employees.get(index);
		
		int ans = -1;
		while(ans != 0) {
			System.out.println("Digite que tipo dados deseja alterar: \n" +
								"1 - Dados pessoais\n" +
								"2 - Dados financeiros\n" +
								"3 - Dados sindicais\n" +
								"Digite 0 para concluir");
			ans = Integer.valueOf( input.nextLine( ) );
			if(ans == 1) 
				emp = editPersonalEmp(emp);
			else if(ans == 2) {
				checkPending(emp);
				emp = editFinancialEmp(emp);
			}
			else if(ans == 3)
				emp = editSyndicateEmp(emp);	
			
		}
		
		System.out.println("Feito!\n");
		
	}
	
	private static void checkPending(Employee emp) {
		
		System.out.println("Pendência a ser paga\n");
		if(emp.getType() == TypeEmp.HOURLY) 
			employeePaymentHourly(emp);
		else {
			Calendar calendarActual = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(emp.getLastPayment());
				
			String empSched = schedule.get(emp.getIndexSchedule());
				
			if(empSched.charAt(0) == 'm')
				employeePaymentVariable(emp, calendar, calendarActual, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			else if( empSched.charAt(8) == '1') 	
					employeePaymentVariable(emp, calendar, calendarActual, 7);					
					
			else if( empSched.charAt(8) == '2')				
				employeePaymentVariable(emp, calendar, calendarActual, 14);
			
		}
		emp.setSalary(0);
		emp.setCommission(0);
	}
	
	private static int searchEmployee(int id) {
		
		int size = employees.size();
		for(int i = 0; i<size; i++) {
			if(id == employees.get(i).getId())
				return i;
		}
		return -1;
	}

	private static Employee editSyndicateEmp(Employee emp) {
		
		System.out.println("Associação com sidicato:\n" +
				   "1 - Sim\n" +
				   "2 - Não\n");
		int ans = Integer.valueOf( input.nextLine() );
		emp.setAssocSynd( ans == 1 ? true : false );
		
		if( emp.isAssocSynd() ) {
			System.out.println("Digite a identificação no sindicato: ");
			emp.setIdSynd( Integer.valueOf( input.nextLine() ) );
			System.out.println("Digite a taxa sindical: ");
			emp.setRateSynd( Integer.valueOf( input.nextLine() ) );
		}
		
		return emp;
	}

	private static Employee editFinancialEmp(Employee emp) {
		
		System.out.println("Defina o tipo:\n" +
				   "1 - Horista\n" +
				   "2 - Assalariado\n" +
				   "3 - Comissionado");
		int type = Integer.valueOf( input.nextLine() );		
		if(type == 1) {
			emp.setType(TypeEmp.HOURLY);
			System.out.println("Digite quanto recebe por hora trabalhada: ");
			emp.setHourly( Float.valueOf( input.nextLine() ));
			emp.setIndexSchedule(1);
		} else {
			System.out.println("Digite o salário: ");
			emp.setSalary( Float.valueOf( input.nextLine() ));
			
			if(type == 2) {
				emp.setType(TypeEmp.SALARIED);
				emp.setIndexSchedule(0);
			}else {
				System.out.println("Digite de quanto é a comissão: ");
				emp.setCommission( Float.valueOf( input.nextLine() ));
				emp.setType(TypeEmp.COMMISIONED);
				emp.setIndexSchedule(2);
			}
		}
		
		System.out.println("Defina o método de pagamento:\n" +
				   "1 - Cheque pelos Correios\n" +
				   "2 - Cheque em mãos\n" +
				   "3 - Depósito em conta bancária");
		int method = Integer.valueOf( input.nextLine() );
		if(method == 1) 
			emp.setMethod(MethodPag.MAIL);
		else if(method == 2)
			emp.setMethod(MethodPag.DIRECTLY);
		else
			emp.setMethod(MethodPag.DEPOSIT);
		
		return emp;
	}

	private static Employee editPersonalEmp(Employee emp) {
		
		System.out.println("Digite o nome: ");
		emp.setName( input.nextLine() );
		System.out.println("Digite o endereço: ");
		emp.setAddress( input.nextLine() );
		
		return emp;
		
	}
	
	private static void deleteEmployee() {
		
		System.out.println("Digite o id do empregado: ");
		int id = Integer.valueOf( input.nextLine() );
		
		int index = searchEmployee(id);
		
		if(index < 0) {
			System.out.println("Usuário não encontrado!");
			return;
		}
		
		employees.remove(index);
		System.out.println("Feito!");
	}

}
