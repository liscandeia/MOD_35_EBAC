/**
 * 
 */
package br.com.liscandeia.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.liscandeia.dao.generic.GenericDAO;
import br.com.liscandeia.domain.Cliente;
import br.com.liscandeia.domain.Produto;

/**
 * @author rodrigo.pires
 *
 */
public class ProdutoDAO extends GenericDAO<Produto, Long> implements IProdutoDAO<Produto>  {
	public ProdutoDAO() {
		super(Produto.class);
	}
}
