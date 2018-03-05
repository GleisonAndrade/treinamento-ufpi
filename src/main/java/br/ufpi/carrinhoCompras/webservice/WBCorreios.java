/**
 * 
 */
package br.ufpi.carrinhoCompras.webservice;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import br.com.techzee.correios.ws.ConsultaCorreios;
import br.com.techzee.correios.ws.dto.CorreiosPrecoPrazo;
import br.com.techzee.correios.ws.enumeration.CorreiosTipoServico;
import br.com.techzee.correios.ws.enumeration.IndicadorSN;

/**
 * @author gleison
 *
 */

@Named
@RequestScoped
public class WBCorreios implements Serializable {

	private static final long serialVersionUID = 1L;
	private CorreiosPrecoPrazo result;
	private String cepOrigem = "64004325";
	private String cepDestino;
	private String prazo;
	private String preco;
	private boolean renderized= false;
	
	public WBCorreios() {
	}
	
	public void mostrarFrete() {
		System.out.println("mostrarFrete()");
		this.renderized = !renderized;
	}
	
	public void calculaPrazoEntrega() {
		result = calculaPrazoEntrega2(CorreiosTipoServico.PAC_VAREJO, cepOrigem, cepDestino);
		this.prazo = result.getPrazoEntrega().toString();
		this.preco = result.getPrecoEntregaEmMaos().toString();
		
		System.out.println("Preço: " + preco + ", Prazo: " + prazo);
	}
	
	public CorreiosPrecoPrazo calculaPrazoEntrega2(CorreiosTipoServico tipoServico, String cepOrigem, String cepDestino) {
		try {
			result = new ConsultaCorreios()
					.servicos(tipoServico) //tipo de serviço 'PAC'
					.entregarEmMaos(IndicadorSN.SIM) //indicador que define se a entrega deve ser em mãos
					.calcularPrecoPrazo(cepOrigem, cepDestino)[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * @return the cepOrigem
	 */
	public String getCepOrigem() {
		return cepOrigem;
	}

	/**
	 * @param cepOrigem the cepOrigem to set
	 */
	public void setCepOrigem(String cepOrigem) {
		this.cepOrigem = cepOrigem;
	}

	/**
	 * @return the cepDestino
	 */
	public String getCepDestino() {
		return cepDestino;
	}

	/**
	 * @param cepDestino the cepDestino to set
	 */
	public void setCepDestino(String cepDestino) {
		this.cepDestino = cepDestino;
	}

	/**
	 * @return the prazo
	 */
	public String getPrazo() {
		return prazo;
	}

	/**
	 * @param prazo the prazo to set
	 */
	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}

	/**
	 * @return the preco
	 */
	public String getPreco() {
		return preco;
	}

	/**
	 * @param preco the preco to set
	 */
	public void setPreco(String preco) {
		this.preco = preco;
	}

	/**
	 * @return the renderized
	 */
	public boolean isRenderized() {
		return renderized;
	}

	/**
	 * @param renderized the renderized to set
	 */
	public void setRenderized(boolean renderized) {
		this.renderized = renderized;
	}
	
	public void cancelaFrete() {
		this.renderized = false;
	}
}
