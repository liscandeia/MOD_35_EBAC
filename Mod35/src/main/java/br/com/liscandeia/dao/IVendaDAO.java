/**
 * 
 */
package br.com.liscandeia.dao;

import br.com.liscandeia.dao.generic.IGenericDAO;
import br.com.liscandeia.domain.Venda;
import br.com.liscandeia.exceptions.DAOException;
import br.com.liscandeia.exceptions.TipoChaveNaoEncontradaException;

/**
 * @author rodrigo.pires
 *
 */
public interface IVendaDAO<T extends Persistente> extends IGenericDAO<T, Long>{

	public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
	
	public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
	public Venda consultarComCollection(Long id);

}