/**
 *
 */
package br.com.liscandeia.dao;
import br.com.liscandeia.dao.generic.GenericDAO;
import br.com.liscandeia.domain.Cliente;

/**
 * @author rodrigo.pires
 *
 */
public class ClienteDAO extends GenericDAO<Cliente, Long> implements IClienteDAO<Cliente> {

	public ClienteDAO() {
		super(Cliente.class);
	}

}
