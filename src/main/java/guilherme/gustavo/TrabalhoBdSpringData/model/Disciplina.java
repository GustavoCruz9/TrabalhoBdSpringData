package guilherme.gustavo.TrabalhoBdSpringData.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Disciplina")
public class Disciplina {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codDisciplina;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String disciplina;
	
	@Column(name = "horasSemanais", nullable = false)
	private String horasSemanais;
	
	@Column(name = "horaInicio", nullable = false)
	private String horaInicio; 
	
	@Column(name = "horaFinal", nullable = false)
    private String horaFinal;
	
	@Column(name = "diaSemana", length = 15, nullable = false)
	private String diaSemana;
	
	@Column(name = "semestre", nullable = false)
	private int semestre;
	
	@ManyToOne(targetEntity = Curso.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codCurso", nullable = false)
	private Curso curso;
	
	@ManyToOne(targetEntity = Professor.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codProfessor", nullable = false)
	private Professor professor;
	
}