/**
 * 
 */
package br.com.liscandeia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.liscandeia.dao.factory.ProdutoQuantidadeFactory;
import br.com.liscandeia.dao.factory.VendaFactory;
import br.com.liscandeia.dao.generic.GenericDAO;
import br.com.liscandeia.domain.Cliente;
import br.com.liscandeia.domain.Produto;
import br.com.liscandeia.domain.ProdutoQuantidade;
import br.com.liscandeia.domain.Venda;
import br.com.liscandeia.domain.Venda.Status;
import br.com.liscandeia.exceptions.DAOException;
import br.com.liscandeia.exceptions.MaisDeUmRegistroException;
import br.com.liscandeia.exceptions.TableException;
import br.com.liscandeia.exceptions.TipoChaveNaoEncontradaException;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author rodrigo.pires
 *
 */
public class VendaDAO extends GenericDAO<Venda, Long> implements IVendaDAO<Venda>  {

	public VendaDAO() {
		super(Venda.class);
	}

	@Override
	public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
		super.alterar(venda);
	}

	@Override
	public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
		super.alterar(venda);
	}


	@Override
	public void excluir(Venda entity) throws DAOException{
		throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA!");
	}
	@Override
	public Venda cadastrar(Venda entity) throws TipoChaveNaoEncontradaException,DAOException{
		try {
			openConnection();
			entity.getProdutos().forEach(prod -> {
				Produto prodJpa = entityManager.merge(prod.getProduto());
				prod.setProduto(prodJpa);
			});
			Cliente cliente = entityManager.merge(entity.getCliente());
			entity.setCliente(cliente);
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
			closeConnection();
			return entity;
		} catch (Exception e) {
			throw new DAOException("ERRO SALVANDO VENDA ", e);
		}

	}

	@Override
	public Venda consultarComCollection(Long id) {
		openConnection();

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Venda> query = builder.createQuery(Venda.class);
		Root<Venda> root = query.from(Venda.class);
		root.fetch("cliente");
		root.fetch("produtos");
		query.select(root).where(builder.equal(root.get("id"), id));
		TypedQuery<Venda> tpQuery =
				entityManager.createQuery(query);
		Venda venda = tpQuery.getSingleResult();
		closeConnection();
		return venda;
	}
}
