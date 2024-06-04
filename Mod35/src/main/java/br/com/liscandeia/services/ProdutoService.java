/**
 * 
 */
package br.com.liscandeia.services;

import br.com.liscandeia.dao.IProdutoDAO;
import br.com.liscandeia.domain.Produto;
import br.com.liscandeia.services.generic.GenericService;

/**
 * @author rodrigo.pires
 *
 */
public class ProdutoService extends GenericService<Produto, String> implements IProdutoService {

	public ProdutoService(IProdutoDAO dao) {
		super(dao);
	}

}
