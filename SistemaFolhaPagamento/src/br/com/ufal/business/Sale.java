package br.com.ufal.business;
import java.util.Date;

public class Sale
{
	private Date data;
	private float valor;
	private float comissao;
	
	public Sale() {
		this.data = new Date();
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public float getComissao() {
		return comissao;
	}

	public void setComissao(float comissao) {
		this.comissao = comissao;
	}
	
	public float getValueCommission() {
		return valor*comissao;		
	}
}