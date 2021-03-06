package br.com.ufal.ui;

import java.util.Calendar;
import java.util.Scanner;

import br.com.ufal.business.Sale;
import br.com.ufal.database.Database;
import br.com.ufal.employee.*;
import br.com.ufal.payment.MethodPay;
import br.com.ufal.payment.PaymentSchedule;
import br.com.ufal.payment.Payroll;



public class UI {

	private Scanner input = new Scanner(System.in);
	private Database database = Database.getInstance();
	private Payroll payroll = Payroll.getInstance();
	private Calendar calendar = Calendar.getInstance();

	public void initialize() {

		PaymentSchedule pay = new PaymentSchedule();
		pay.setPaymentSchedule("mensal $");
		database.addSchedule( pay );

		pay = new PaymentSchedule();
		pay.setPaymentSchedule("semanal 1 sexta");
		database.addSchedule( pay );

		pay = new PaymentSchedule();
		pay.setPaymentSchedule("semanal 2 sexta");
		database.addSchedule( pay );

		showInitialMenu();

	}

	public void showInitialMenu()
	{
		int continuar = 1;
		int realizar, escolha;

		boolean undoing = false;
		boolean redoing = false;

		while(continuar != 0)
		{
			System.out.println("Digite 1(empregado) 2(lancar) 3(folha) 0(sair)");
			if(undoing)
				System.out.println("4 (undo)");
			else if(redoing)
				System.out.println("4 (redo)");
			realizar = Integer.valueOf( input.nextLine() );

			if(realizar == 1)
			{
				System.out.println("Digite 1(incluir) ou 2(editar) ou 3(remover)");
				escolha = Integer.valueOf( input.nextLine() );

				if(escolha == 1){
					addEmployee();
					undoing = true;
				} else if(escolha == 2) {
					editEmployee();
					undoing = true;
				} else if(escolha == 3) {
					removeEmployee();
					undoing = true;
				}else
				{
					System.out.println("Opcao invalida!!");
					continue;
				}
			}
			else if(realizar == 2)
			{
				System.out.println("Digite: 1(Cartao de Ponto) 2(Venda) 3(Taxa de servico)");
				int lancar = Integer.valueOf( input.nextLine() );

				System.out.println("Digite o id: ");
				Employee emp = database.searchEmployee( Integer.valueOf( input.nextLine() ) );
				if( emp == null ) {
					System.out.println("Usuario inexistente!");
					continue;
				}

				if(lancar == 1){
					emp.addPointCard();
					undoing = true;
				}else if(lancar == 2){
					lancarVenda( emp );
					undoing = true;
				}else if(lancar == 3)
				{
					lancarTaxaServico( emp );
					undoing = true;
				}
				else
					System.out.println("Numero digitado invalido!");
					continue;
			}
			else if(realizar == 3)
			{
				System.out.println("Digite: 1(Folha de hoje) 2(criacao de agenda)");
				int folha= Integer.valueOf( input.nextLine() );

				if(folha == 1)
				{
					System.out.println("Digite o dia: ");
					int day = Integer.valueOf( input.nextLine() );
					System.out.println("Digite o mes: ");
					int month = Integer.valueOf( input.nextLine() );
					System.out.println("Digite o ano: ");
					int year = Integer.valueOf( input.nextLine() );

					calendar.set(year, month - 1, day);

					payroll.run( calendar.getTime() );
				}
				else if(folha == 2)
				{
					criaAgenda();
				}
				else
				{
					System.out.println("Opcao invalida!!");
					continue;
				}
			}
			else if(realizar == 4){
				if(undoing) {
					database.undo();
					undoing = false;
					redoing = true;
					System.out.println("Feito");
				} else if(redoing) {
					database.redo();
					redoing = false;
					undoing = true;
					System.out.println("Feito");
				}
			}
			else
			{
				System.out.println("Opcao invalida.");
				continue;
			}

		}
		System.out.println("Programa encerrado.");
	}

	private void editEmployee() {

		System.out.println("Digite o id do empregado: ");
		int id =  Integer.valueOf( input.nextLine() );
		Employee emp = database.searchEmployee(id);

		if( emp == null ) {
			System.out.println("Empregado inexistente!");
			return;
		}

		System.out.println("Digite 1 para editar as informacoes pessoais do empregado ou 0 para continuar:");
		int option = Integer.valueOf( input.nextLine() );
		if( option == 1 )
			emp = editPersonalInfo(emp);

		System.out.println("Digite 1 para editar as informacoes financeiras do empregado ou 0 para continuar:");
		option = Integer.valueOf( input.nextLine() );
		if( option == 1 )
			emp = editFinicialInfo(emp);

		System.out.println("Digite 1 para editar as informacoes sindicais do empregado ou 0 para continuar:");
		option = Integer.valueOf( input.nextLine() );
		if( option == 1 )
			emp = editSindicateInfo(emp);

		database.editEmployee(emp);
		System.out.println("Feito!");

	}

	private void addEmployee() {

		Employee emp;
		
		System.out.println("Digite 1(por hora) 2(assalariado) 3 (comissionado)");
		int tipo = Integer.valueOf( input.nextLine() );

		if(tipo == 1) {

			emp = new Hourly();
			System.out.println("Valor da hora trabalhada:");
			((Hourly)emp).setSalarioHora( Float.valueOf( input.nextLine() ) );
			emp.setPaymentSchedule("semanal 1 sexta");

		} else {
			
			System.out.println("Digite o salario fixo: ");
			if( tipo == 2 ) {
				emp = new Salaried();
				((Salaried)emp).setSalarioFixo( Float.valueOf( input.nextLine() ) );
				emp.setPaymentSchedule("semanal 2 sexta");
			}else{
				emp = new Comissioned();
				((Comissioned)emp).setPaymentSchedule("mensal $");
			}

		}		
		
		emp = editPersonalInfo(emp);
		emp = editFinicialInfo(emp);
		emp = editSindicateInfo(emp);

		System.out.println("Empregado cadastrado! ID do novo empregado:"+ database.addEmployee(emp));

	}

	private Employee editPersonalInfo(Employee emp) {

		System.out.println("Digite o nome do empregado:");
		emp.setNome( input.nextLine() );

		System.out.println("Digite o endereco:");
		emp.setEndereco( input.nextLine() );

		return emp;

	}

	private Employee editFinicialInfo(Employee emp) {

		System.out.println("Digite 1(cheque) 2(conta bancaria): ");
		int metodo = Integer.valueOf( input.nextLine() );

		if(metodo == 1)
		{
			System.out.println("Receber por 1(correios) ou 2(em maos): ");
			int receber = Integer.valueOf( input.nextLine() );
			if(receber == 1)
				emp.setMetodo(MethodPay.MAIL);
			else if(receber == 2)
				emp.setMetodo(MethodPay.DIRECT);
			else
			{
				System.out.println("Metodo invalido!");
			}
		}
		else if(metodo == 2)
			emp.setMetodo(MethodPay.ACCOUNT);
		else
		{
			System.out.println("Metodo invalido!");
		}

		return emp;

	}

	private Employee editSindicateInfo(Employee emp) {

		System.out.println("Pertence ao sindicato?");
		System.out.println("Digite 1(sim) ou 0(nao)");
		int sindicatoInt = Integer.valueOf( input.nextLine() );

		if(sindicatoInt == 1) {

			emp.setSindicalizado( true );

			System.out.println("Digite o id do empregado no sindicato:");
			emp.setIdSindicado( Integer.valueOf( input.nextLine() ) );

			System.out.println("Digite a taxa mensal do sindicato: ");
			 emp.setTaxaSind( Float.valueOf( input.nextLine() ) );

		}

		return emp;

	}

	private void removeEmployee() {

		System.out.println("Digite o id do empregado:");
		int id = input.nextInt();
		input.nextLine();
		if (database.removeEmployee(id) )
			System.out.println("Feito!");
		else
			System.out.println("Empregado inexistente!");

	}

	private void criaAgenda()
	{
		PaymentSchedule sched = new PaymentSchedule();
		System.out.println("Digite a nova agenda: ");
		boolean returned = sched.setPaymentSchedule( input.nextLine() );
		if(returned) {
			database.addSchedule(sched);
			System.out.println("Feito");
		} else {
			System.out.println("Entrada invalida");
		}

	}

	private void lancarTaxaServico(Employee emp)
	{
		System.out.println("Digite a taxa de servico: ");
		emp.addServiceRate( Float.valueOf( input.nextLine() ) );

	}

	private void lancarVenda(Employee emp)
	{
		Sale sale = new Sale();

		System.out.println("Digite o valor da venda:");
		sale.setValor(Float.valueOf( input.nextLine() ) );
		System.out.println("Digite o valor da comissao [Ex.: Se for 10%, digite 0.1]: ");
		sale.setComissao(Float.valueOf( input.nextLine() ) );

		((Comissioned)emp).addSale(sale);
		database.editEmployee(emp);
	}

}
