package guilherme.gustavo.TrabalhoBdSpringData.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Curso")	
public class Curso {

	@Id
	@Column(name = "codCurso", nullable = false)
	private int codigo;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
	
	@Column(name = "cargaHoraria", nullable = false)
	private int cargaHoraria;
	
	@Column(name = "sigla", length = 3, nullable = false)
	private String sigla;
	
	@Column(name = "notaEnade", nullable = false)
	private int notaEnade;
	
}
