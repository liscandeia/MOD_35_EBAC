/**
 * 
 */
package br.com.liscandeia.domain;

import anotacao.ColunaTabela;
import anotacao.Tabela;
import anotacao.TipoChave;
import br.com.liscandeia.dao.Persistente;

import javax.persistence.*;

/**
 * @author rodrigo.pires
 *
 */
@Entity
@Table(name = "TB_CLIENTE")

public class Cliente implements Persistente {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cliente_seq")
	@SequenceGenerator(name = "cliente_seq", sequenceName = "seq_cliente", initialValue = 1,allocationSize = 1)
	private Long id;
	
	@Column(name = "NOME", nullable = false,length = 50)
	private String nome;
	
	@Column(name = "CPF", nullable = false,unique = true)
    private Long cpf;
    
	@Column(name = "TEL", nullable = false)
    private Long tel;
    
	@Column(name = "ENDE",nullable = false,length = 150)
    private String end;
	@Column(name = "NUMERO",nullable = false,length = 10)
    private Integer numero;
    
	@Column(name = "CIDADE",nullable = false,length = 100)
    private String cidade;

	@Column(name = "ESTADO",nullable = false,length = 100)
    private String estado;

	@Column(name = "EMAIL",nullable = false,length = 100)
	private String email;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getCpf() {
		return cpf;
	}
	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}
	public Long getTel() {
		return tel;
	}
	public void setTel(Long tel) {
		this.tel = tel;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {return email; }
	public void setEmail(String email) {this.email = email; }


}
