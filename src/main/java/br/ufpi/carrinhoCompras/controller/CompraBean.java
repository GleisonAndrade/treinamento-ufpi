package br.ufpi.carrinhoCompras.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.DragDropEvent;

import br.ufpi.carrinhoCompras.model.Compra;
import br.ufpi.carrinhoCompras.model.ItemCompra;
import br.ufpi.carrinhoCompras.model.Produto;
import br.ufpi.carrinhoCompras.repository.CompraDao;

@Named
@ViewScoped
public class CompraBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private CompraDao compraDao;
	
	private Compra compra;
	
	private List<ItemCompra> itens;
	
	@Inject
	private ClienteBean clienteBean;

	private Produto produtoSelecionado;
	
	public CompraBean() {
	}
	
	@PostConstruct
	private void init(){
		itens = new ArrayList<ItemCompra>();
		this.produtoSelecionado = new Produto();
	}
	
	public void realizarCompra(){
		Compra compra = new Compra();
		compra.setItensCompra(itens);
		compra.setCliente(clienteBean.getUsuarioLogado());
		double valor = 0;
		for (ItemCompra itemCompra : itens) {
			valor += itemCompra.getQuantidade() * itemCompra.getProduto().getPreco();
		}
		compra.setValorTotal(valor);
		
		compraDao.salvar(compra);
		itens = new ArrayList<ItemCompra>();
		produtoSelecionado = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Compra Finalizada!", "Parabéns, sua compra foi realizada com sucesso!"));
	}
	
	public void adicionarNoCarrinho(Produto produto){
		inserirNoCarrinho(produto);
	}
	
	private void inserirNoCarrinho(Produto produto){
		boolean produtoExiste = false;
		for (ItemCompra itemCompra : itens) {
			if(itemCompra.getProduto().getId().equals(produto.getId())){
				itemCompra.setQuantidade(itemCompra.getQuantidade()+1);
				produtoExiste = true;
				break;
			}
		}
		if(!produtoExiste){
			ItemCompra item = new ItemCompra();
			item.setProduto(produto);
			item.setQuantidade(1);
			itens.add(item);
		}
	}
	
	public void selecionarProduto(DragDropEvent dragDropEvent){
		Produto produto = (Produto) dragDropEvent.getData();
		inserirNoCarrinho(produto);
	}
	
	public void removeItem(ItemCompra itemCompra){
		itens.remove(itemCompra);
	}
	
	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public List<ItemCompra> getItens() {
		return itens;
	}

	public void setItens(List<ItemCompra> itens) {
		this.itens = itens;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}
	
	
}
