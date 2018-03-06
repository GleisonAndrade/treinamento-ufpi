package br.ufpi.carrinhoCompras.listeners;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;

import br.ufpi.carrinhoCompras.model.Produto;
import br.ufpi.carrinhoCompras.util.MensagensManager;

public class ProdutoListener {
	
	@PrePersist
	public void antesAdicionarProduto(Produto produto) {
		if(produto.getPreco() > 20){
			System.out.println(MensagensManager.readMessage("warning.preco"));
		}
	}
	
	@PostPersist
	public void depoisAdicionarProduto(Produto produto) {
			System.out.println("O produto foi salvo com sucesso!!");
			FacesContext
			.getCurrentInstance()
			.addMessage(null, 
				new FacesMessage("Sucesso!", 
						"Produto salvo com sucesso!"));
		
	}
	
	@PostUpdate
	public void depoisAtualizarProduto(Produto produto) {
			System.out.println("O produto foi salvo com sucesso!!");
			FacesContext
			.getCurrentInstance()
			.addMessage(null, 
				new FacesMessage("Sucesso!", 
						"Produto salvo com sucesso!"));
	}
}
