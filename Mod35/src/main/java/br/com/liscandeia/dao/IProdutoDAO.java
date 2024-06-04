/**
 * 
 */
package br.com.liscandeia.dao;

import br.com.liscandeia.dao.generic.IGenericDAO;
import br.com.liscandeia.domain.Produto;

/**
 * @author rodrigo.pires
 *
 */
public interface IProdutoDAO<T extends Persistente> extends IGenericDAO<T, Long>{

}
