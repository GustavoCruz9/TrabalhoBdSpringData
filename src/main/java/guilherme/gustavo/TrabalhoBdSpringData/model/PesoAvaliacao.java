package guilherme.gustavo.TrabalhoBdSpringData.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "PesoAvaliacao")
public class PesoAvaliacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	
	@Column(name = "tipo", length = 30, nullable = false)
	private String tipo;
	
	@Column(name = "peso", nullable = false)
	private double peso;
	
	
	@ManyToOne(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codDisciplina", nullable = false)
	private Disciplina disciplina;
	
}
