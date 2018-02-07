/**
 * 
 */
package br.ufpi.carrinhoCompras.webservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.ufpi.carrinhoCompras.model.Produto;
import br.ufpi.carrinhoCompras.model.to.ProdutoTO;
import br.ufpi.carrinhoCompras.repository.ProdutoDao;

/**
 * @author gleison
 *
 */

@Named
@RequestScoped
public class WBProduto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoDao produtoDao;
	
	public List<Produto> importarObras(String url, boolean salvar){
		List<Produto> produtos = importarObras(url);
		
		if (salvar) {
			produtos = salvarProdutos(produtos);
		}
		
		return produtos;
	}
	
	/**
	 * http://www.json-generator.com/api/json/get/cqjiDOWlsO?indent=0
	 * 
	 * @param url
	 * @return
	 */
	public List<Produto> importarObras(String url){
		List<Produto> produtos = new ArrayList<>();
		List<ProdutoTO> produtosTO = new ArrayList<>();
		
//		String servicoURL = "https://obrasweb.herokuapp.com/rest/service/obras/";
		Client cliente = ClientBuilder.newClient();
		
		WebTarget target = cliente.target(url);
		String resposta = target.request(MediaType.APPLICATION_JSON).get(String.class);
		Gson gson = new Gson();
		TypeToken<List<ProdutoTO>> token = new TypeToken<List<ProdutoTO>>(){};
		
		produtosTO = gson.fromJson(resposta, token.getType());
		
		produtos = converte(produtosTO);
//		produtos = eliminaDuplicados(produtos);
		
		return produtos;
	}

	private List<Produto> salvarProdutos(List<Produto> produtos) {
		for (Produto produto: produtos) {
			produtoDao.salvar(produto);
		}
		
		return produtoDao.listar();
	}

	private List<Produto> converte(List<ProdutoTO> produtosTO) {
		List<Produto> produtos = new ArrayList<>();
		
		for (ProdutoTO produtoTO : produtosTO) {
			Produto produto = new Produto();
			
			produto.setDescricao(produtoTO.getDescricao());
			produto.setPreco(produtoTO.getPreco());
			produto.setTipo(produtoTO.getTipo());
			
			produtos.add(produto);
		}
		
		return produtos;
	}

}
