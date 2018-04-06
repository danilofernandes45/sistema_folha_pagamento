package br.com.ufal.employee;

import java.util.ArrayList;
import java.util.Date;

import br.com.ufal.business.Point;
import br.com.ufal.payment.MethodPay;
import br.com.ufal.payment.PaymentSchedule;

public class Employee 
{
	protected int id;
	protected String nome;
	protected String endereco;
	
	protected MethodPay metodoPagam;
	protected Date ultimoPagamento; 
	
	protected PaymentSchedule paymentSchedule;

	protected boolean sindicalizado;
	protected int codigoSindicato;
	protected float taxaSind;
	protected float taxaServicos;

	protected ArrayList<Point> cartao;
	
	protected boolean deleted;
	
	public Employee() {
		this.setTaxaServicos(0);
		this.ultimoPagamento = new Date();
		this.deleted = false;
		this.sindicalizado = false;
		this.cartao = new ArrayList<>();
		this.paymentSchedule = new PaymentSchedule();
	
		
	}	
	
	public Employee clone(){
		return null;
	}
	
	public PaymentSchedule getPaymentSchedule() {
		return paymentSchedule;
	}

	public void setPaymentSchedule(PaymentSchedule pay) {
		this.paymentSchedule = pay;
	}
	
	public void setPaymentSchedule(String pay) {
		this.paymentSchedule.setPaymentSchedule(pay);
	}
	
	public ArrayList<Point> getCartao() {
		return cartao;
	}

	public void setCartao(ArrayList<Point> cartao) {
		this.cartao = cartao;
	}
	
	public void resetPointsCard() {
		this.cartao = new ArrayList<>();
	}

	public void addPointCard() {
		
		Point point = new Point( false );		
		this.cartao.add(point);
	}
	
	public boolean isDayToPay(Date today) {
		return paymentSchedule.isDayToPay(today, ultimoPagamento);
	}
	
	public void addServiceRate(Float rate) {
		this.setTaxaServicos(this.getTaxaServicos() + rate);
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	public String getNome()
	{
		return this.nome;
	}
	
	public void setEndereco(String endereco)
	{
		this.endereco = endereco;
	}
	public String getEndereco()
	{
		return this.endereco;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setIdSindicado(int id)
	{
		this.codigoSindicato = id;
	}
	public int getIdSindicato()
	{
		return this.codigoSindicato;
	}

	public void setTaxaSind(float rate)
	{
		this.taxaSind = rate;
	}
	public float getTaxa()
	{
		return this.taxaSind;
	}

	public void setSindicalizado(boolean sind)
	{
		this.sindicalizado = sind;	
	}
	public boolean getSindicalizado()
	{
		return this.sindicalizado;
	}

	public void setMetodo(MethodPay method)
	{
		
		this.metodoPagam = method;
		
	}
	public MethodPay getMetodo()
	{
		return this.metodoPagam;
	}

	public void setUltimoPagamento(Date ultimo)
	{
		this.ultimoPagamento = ultimo;
	}
	public Date getUltimoPagamento()
	{
		return this.ultimoPagamento;
	}
	
	public float getTaxaServicos() {
		return taxaServicos;
	}

	public void setTaxaServicos(float taxaServicos) {
		this.taxaServicos = taxaServicos;
	}
}