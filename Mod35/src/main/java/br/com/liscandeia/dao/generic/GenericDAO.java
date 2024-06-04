package br.com.liscandeia.dao.generic;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import anotacao.ColunaTabela;
import anotacao.Tabela;
import anotacao.TipoChave;
import br.com.liscandeia.dao.Persistente;
import br.com.liscandeia.dao.generic.jdbc.ConnectionFactory;
import br.com.liscandeia.exceptions.DAOException;
import br.com.liscandeia.exceptions.MaisDeUmRegistroException;
import br.com.liscandeia.exceptions.TableException;
import br.com.liscandeia.exceptions.TipoChaveNaoEncontradaException;
import br.com.liscandeia.exceptions.TipoElementoNaoConhecidoException;
import org.hibernate.internal.EntityManagerMessageLogger;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author rodrigo.pires
 *
 * Classe genérica que implementa interface genérica com os métodos de CRUD
 */
public abstract class GenericDAO<T extends Persistente, E extends Serializable> implements IGenericDAO<T,E> {
	protected EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	private Class<T> persistenteClass;
	public GenericDAO(Class<T> persistenteClass){
		this.persistenteClass = persistenteClass;
	}
	@Override
	public T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException{
		openConnection();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
		closeConnection();
		return entity;
	}

	@Override
	public void excluir(T entity) throws DAOException{
		openConnection();
		entity = entityManager.merge(entity);
		entityManager.remove(entity);
		entityManager.getTransaction().commit();
		closeConnection();
	}
	@Override
	public T alterar(T entity) throws TipoChaveNaoEncontradaException{
		openConnection();
		entity = entityManager.merge(entity);
		entityManager.getTransaction().commit();
		closeConnection();
		return entity;
	}
	@Override
	public T consultar(E valor) throws MaisDeUmRegistroException, TableException, DAOException{
		openConnection();
		T entity = entityManager.find(this.persistenteClass, valor);
		entityManager.getTransaction().commit();
		closeConnection();
		return entity;
	}
	@Override
	public Collection<T> buscarTodos() throws DAOException{
		openConnection();
		List<T> list =  entityManager.createQuery(getSelectSql(), this.persistenteClass).getResultList();
		closeConnection();
		return list;
	}

	protected String getSelectSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT obj FROM ");
		sb.append(this.persistenteClass.getSimpleName());
		sb.append(" obj");
		return sb.toString();
	}

	protected void closeConnection() {
		entityManager.close();
		entityManagerFactory.close();
	}

	protected void openConnection() {
		entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
	}
}
