package pi.pi.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String nome;
	@NotBlank
	private String rg;
	@ManyToOne
	private Sorvete sorvete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nome=" + nome + ", rg=" + rg + ", sorvete=" + sorvete + "]";
	}

	public Sorvete getSorvete() {
		return sorvete;
	}

	public void setSorvete(Sorvete sorvete) {
		this.sorvete = sorvete;
	}
}
