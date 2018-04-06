package br.com.ufal.employee;

import java.util.ArrayList;

import br.com.ufal.business.Point;
import br.com.ufal.business.Sale;

public class Comissioned extends Employee {

	private float salarioFixo;
	private ArrayList<Sale> sales;

	public Comissioned() {
		this.sales = new ArrayList<>();
	}
	
	public ArrayList<Sale> getSales() {
		return sales;
	}
	
	public void setSales(ArrayList<Sale> sales) {
		this.sales = sales;
	}

	public void resetSales() {
		this.sales = new ArrayList<>();
	}
	
	public void addSale(Sale sale) {
		sales.add(sale);
	}
	
	public void setSalarioFixo(float salary)
	{
		this.salarioFixo = salary;
	}
	public float getSalarioFixo()
	{
		return this.salarioFixo;
	}
	
	public Employee clone() {
		
		Comissioned emp = new Comissioned();
		
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
		
		ArrayList<Sale> cloneS = new ArrayList<>();
		cloneS.addAll(sales);
		
		emp.setSales(cloneS);
		
		return emp;
		
	}
	
}
