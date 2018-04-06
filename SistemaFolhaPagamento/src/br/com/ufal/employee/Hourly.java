package br.com.ufal.employee;

import java.util.ArrayList;

import br.com.ufal.business.Point;

public class Hourly extends Employee{

	private float salarioHora;

	public void setSalarioHora(float salario)
	{
		this.salarioHora = salario;
	}
	public float getSalarioHora()
	{
		return this.salarioHora;
	}
	
	public void addPointCard() {
		
		Point point = new Point( true );
		
		int size = cartao.size();
		if(size >= 8) {
			//Converte o intervalo de tempo de milisegundos para horas
			long time = (point.getDate().getTime() - cartao.get( size - 8 ).getDate().getTime()) / 3600000; 
			
			if(time < 24)
				point.setOvertime(true);
		}
		
		this.cartao.add(point);
	}
	
	public Employee clone() {
		
		Hourly emp = new Hourly();
		
		emp.setId(id);
		emp.setNome(nome);
		emp.setEndereco(endereco);
		emp.setMetodo(metodoPagam);
		emp.setUltimoPagamento(ultimoPagamento);
		emp.setPaymentSchedule(paymentSchedule);
		emp.setSindicalizado(sindicalizado);
		emp.setTaxaSind(taxaSind);
		emp.setTaxaServicos(taxaServicos);
		
		ArrayList<Point> clone = new ArrayList<>();
		clone.addAll(cartao);
		
		emp.setCartao(clone);
		emp.setDeleted(deleted);
		
		emp.setSalarioHora(salarioHora);
		
		return emp;
		
	}
	
}
