package br.com.liscandeia.dao;

import br.com.liscandeia.domain.Cliente;
import br.com.liscandeia.domain.Produto;
import br.com.liscandeia.domain.Venda;
import br.com.liscandeia.exceptions.DAOException;
import br.com.liscandeia.exceptions.MaisDeUmRegistroException;
import br.com.liscandeia.exceptions.TableException;
import br.com.liscandeia.exceptions.TipoChaveNaoEncontradaException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collection;
import java.util.Random;

import static br.com.liscandeia.domain.Venda.Status.*;
import static org.junit.Assert.*;


    public class VendaTest {
        private IVendaDAO vendaDao;

        private IVendaDAO vendaExclusaoDao;

        private IClienteDAO clienteDao;

        private IProdutoDAO produtoDao;

        private Random rd;

        private Cliente cliente;

        private Produto produto;

        public VendaTest() {
            this.vendaDao = new VendaDAO();
            vendaExclusaoDao = new VendaExclusaoDAO();
            this.clienteDao = new ClienteDAO();
            this.produtoDao = new ProdutoDAO();
            rd = new Random();
        }

        @Before
        public void init() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            this.cliente = cadastrarCliente();
            this.produto = cadastrarProduto("A1", BigDecimal.TEN);
        }

        @After
        public void end() throws DAOException {
            excluirVendas();
            excluirProdutos();
            clienteDao.excluir(this.cliente);
        }

        @Test
        public void pesquisar() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            Venda venda = criarVenda("A1");
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);
            Venda vendaConsultada = (Venda) vendaDao.consultar(venda.getId());
            assertNotNull(vendaConsultada);
            assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        }

        @Test
        public void salvar() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
            Venda venda = criarVenda("A2");
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);

            assertTrue(venda.getValorTotal().equals(BigDecimal.valueOf(20)));
            assertTrue(venda.getStatus().equals(INICIADA));

            Venda vendaConsultada = (Venda) vendaDao.consultar(venda.getId());
            assertTrue(vendaConsultada.getId() != null);
            assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        }

        @Test
        public void cancelarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            String codigoVenda = "A3";
            Venda venda = criarVenda(codigoVenda);
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);
            assertNotNull(venda);
            assertEquals(codigoVenda, venda.getCodigo());

            retorno.setStatus(CANCELADA);
            vendaDao.cancelarVenda(venda);

            Venda vendaConsultada = (Venda) vendaDao.consultar(venda.getId());
            assertEquals(codigoVenda, vendaConsultada.getCodigo());
            assertEquals(CANCELADA, vendaConsultada.getStatus());
        }

        @Test
        public void adicionarMaisProdutosDoMesmo() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            String codigoVenda = "A4";
            Venda venda = criarVenda(codigoVenda);
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);
            assertNotNull(venda);
            assertEquals(codigoVenda, venda.getCodigo());

            Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
            vendaConsultada.adicionarProduto(produto, 1);

            assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
            BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
            assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
            assertTrue(vendaConsultada.getStatus().equals(INICIADA));
        }

        @Test
        public void adicionarMaisProdutosDiferentes() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            String codigoVenda = "A5";
            Venda venda = criarVenda(codigoVenda);
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);
            assertNotNull(venda);
            assertEquals(codigoVenda, venda.getCodigo());

            Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
            assertNotNull(prod);
            assertEquals(codigoVenda, prod.getCodigo());

            //TODO Usando este método apra evitar a exception org.hibernate.LazyInitializationException
            // Ele busca todos os dados da lista pois a mesma por default é lazy
            Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
            vendaConsultada.adicionarProduto(prod, 1);

            assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
            BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
            assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
            assertTrue(vendaConsultada.getStatus().equals(INICIADA));
        }

        @Test(expected = DAOException.class)
        public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontradaException, DAOException {
            Venda venda = criarVenda("A6");
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);

            Venda venda1 = criarVenda("A6");
            Venda retorno1 = (Venda) vendaDao.cadastrar(venda1);
            assertNull(retorno1);
            assertTrue(venda.getStatus().equals(INICIADA));
        }

        @Test
        public void removerProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            String codigoVenda = "A7";
            Venda venda = criarVenda(codigoVenda);
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);
            assertNotNull(venda);
            assertEquals(codigoVenda, venda.getCodigo());

            Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
            assertNotNull(prod);
            assertEquals(codigoVenda, prod.getCodigo());

            Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
            vendaConsultada.adicionarProduto(prod, 1);
            assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
            BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
            assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


            vendaConsultada.removerProduto(prod, 1);
            assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
            valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
            assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
            assertTrue(vendaConsultada.getStatus().equals(INICIADA));
        }

        @Test
        public void removerApenasUmProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            String codigoVenda = "A8";
            Venda venda = criarVenda(codigoVenda);
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);
            assertNotNull(venda);
            assertEquals(codigoVenda, venda.getCodigo());

            Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
            assertNotNull(prod);
            assertEquals(codigoVenda, prod.getCodigo());

            Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
            vendaConsultada.adicionarProduto(prod, 1);
            assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
            BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
            assertEquals(vendaConsultada.getValorTotal(), valorTotal);


            vendaConsultada.removerProduto(prod, 1);
            assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
            valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
            assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
            assertTrue(vendaConsultada.getStatus().equals(INICIADA));
        }

        @Test
        public void removerTodosProdutos() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            String codigoVenda = "A9";
            Venda venda = criarVenda(codigoVenda);
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);
            assertNotNull(venda);
            assertEquals(codigoVenda, venda.getCodigo());

            Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
            assertNotNull(prod);
            assertEquals(codigoVenda, prod.getCodigo());

            Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
            vendaConsultada.adicionarProduto(prod, 1);
            assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
            BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
            assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


            vendaConsultada.removerTodosProdutos();
            assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 0);
            assertTrue(vendaConsultada.getValorTotal().equals(BigDecimal.valueOf(0)));
            assertTrue(vendaConsultada.getStatus().equals(INICIADA));
        }

        @Test
        public void finalizarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            String codigoVenda = "A10";
            Venda venda = criarVenda(codigoVenda);
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);
            assertNotNull(venda);
            assertEquals(codigoVenda, venda.getCodigo());

            venda.setStatus(CONCLUIDA);
            vendaDao.finalizarVenda(venda);

            Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
            assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
            assertEquals(CONCLUIDA, vendaConsultada.getStatus());
        }

        @Test(expected = UnsupportedOperationException.class)
        public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            String codigoVenda = "A11";
            Venda venda = criarVenda(codigoVenda);
            Venda retorno = (Venda) vendaDao.cadastrar(venda);
            assertNotNull(retorno);
            assertNotNull(venda);
            assertEquals(codigoVenda, venda.getCodigo());

            venda.setStatus(CONCLUIDA);
            vendaDao.finalizarVenda(venda);

            Venda vendaConsultada = vendaDao.consultarComCollection(venda.getId());
            assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
            assertEquals(CONCLUIDA, vendaConsultada.getStatus());

            vendaConsultada.adicionarProduto(this.produto, 1);

        }


        private void excluirProdutos() throws DAOException {
            Collection<Produto> list = this.produtoDao.buscarTodos();
            list.forEach(prod -> {
                try {
                    this.produtoDao.excluir(prod);
                } catch (DAOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }

        private void excluirVendas() throws DAOException {
            Collection<Venda> list = this.vendaExclusaoDao.buscarTodos();
            list.forEach(prod -> {
                try {
                    this.vendaExclusaoDao.excluir(prod);
                } catch (DAOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }

        private Produto cadastrarProduto(String codigo, BigDecimal valor) throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
            Produto produto = new Produto();
            produto.setCodigo(codigo);
            produto.setDescricao("Produto desc");
            produto.setNome("Produto teste");
            produto.setValor(BigDecimal.TEN);
            produtoDao.cadastrar(produto);
            return produto;
        }

        private Cliente cadastrarCliente() throws TipoChaveNaoEncontradaException, DAOException {
            Cliente cliente = new Cliente();
            cliente.setCpf(rd.nextLong());
            cliente.setNome("Lis");
            cliente.setCidade("tagua");
            cliente.setEnd("End");
            cliente.setEstado("DF");
            cliente.setNumero(10);
            cliente.setTel(6199999999L);
            cliente.setEmail("ex@gmail.com");
            clienteDao.cadastrar(cliente);
            return cliente;
        }

        private Venda criarVenda(String codigo) {
            Venda venda = new Venda();
            venda.setCodigo(codigo);
            venda.setDataVenda(Instant.now());
            venda.setCliente(this.cliente);
            venda.setStatus(INICIADA);
            venda.adicionarProduto(this.produto, 2);
            return venda;
        }
    }



