/**
 * 
 */
package br.com.liscandeia.dao;

import br.com.liscandeia.dao.generic.IGenericDAO;
import br.com.liscandeia.domain.Cliente;

/**
 * @author rodrigo.pires
 *
 */
public interface IClienteDAO<T extends Persistente> extends IGenericDAO<T, Long> {

}
