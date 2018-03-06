/**
 * 
 */
package br.ufpi.carrinhoCompras.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.ufpi.carrinhoCompras.model.Produto;
import javax.enterprise.context.SessionScoped;
import br.ufpi.carrinhoCompras.repository.ProdutoDao;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * @author Vanderson Moura
 *
 */
@Named
@SessionScoped
public class ProdutoBean  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoDao produtoDao;

	private Produto produto;

	public ProdutoBean() {
	}

	@PostConstruct
	private void init() {
		produto = new Produto();
	}

	public void salvaProduto() {
		produtoDao.salvar(produto);
		produto = new Produto();
		
	}

	public List<Produto> listarProdutos() {
		return produtoDao.listar();
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public byte[] gerarRelatorio() {
		List<Produto> produtos = produtoDao.listar();
		JRDataSource datasource = new JRBeanCollectionDataSource(produtos);

		try {
			Map<String, Object> params = new HashMap<String, Object>();
			
			FileInputStream fileInputStream = (FileInputStream) Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("RelatorioProdutos.jrxml");
			
			JasperReport jasperReport = JasperCompileManager.compileReport(fileInputStream);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, datasource);
			
			return JasperExportManager.exportReportToPdf(jasperPrint);

			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return null;
		}
	}
	
	public StreamedContent downloadRelatorio(){
		try {
			byte[] contentReport =  gerarRelatorio();
			
			InputStream is = new ByteArrayInputStream(contentReport);
			
						
			return new DefaultStreamedContent(is, "application/pdf", "RelatorioProdutos.pdf");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void editar(Produto produto) {
		this.produto = new Produto();
		this.produto = produto;
	}

	
}
