package br.com.ufal.employee;

import java.util.ArrayList;

import br.com.ufal.business.Point;

public class Salaried extends Employee {

	private float salarioFixo;

	public void setSalarioFixo(float salary)
	{
		this.salarioFixo = salary;
	}
	public float getSalarioFixo()
	{
		return this.salarioFixo;
	}
	
	public Employee clone() {
		
		Salaried emp = new Salaried();
		
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
		
		emp.setSalarioFixo(salarioFixo);		
		
		return emp;
		
	}
	
}
