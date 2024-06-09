package guilherme.gustavo.TrabalhoBdSpringData.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
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
@Table(name = "Matricula")
@IdClass(MatriculaId.class)
@NamedStoredProcedureQuery(
		name = "Matricula.sp_validaRa",
		procedureName = "sp_validaRa",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = Integer.class)
		}
)
@NamedStoredProcedureQuery(
		name = "Matricula.sp_cadastrarMatricula",
		procedureName = "sp_cadastrarMatricula",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "codDisciplina", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class)
		}
)

@NamedStoredProcedureQuery(
		name = "Matricula.sp_finalizaSemestre",
		procedureName = "sp_finalizaSemestre",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "codDisciplina", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "anoSemestre", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class)
		}
)

public class Matricula {

	@Id
	@Column(name = "anoSemestre", nullable = false)
	private int anoSemestre;
	
	@Id
	@ManyToOne(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codDisciplina", nullable = false)
	private Disciplina disciplina;
	
	@Id
	@ManyToOne(targetEntity = Aluno.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "cpf", nullable = false)
	private Aluno aluno;
	
	@Column(name = "statusMatricula", length = 10, nullable = false)
	private String status = "pendente";
	
	@Column(name = "nota", nullable = true)
	private double nota;
	
	@Column(name = "dataMatricula", length = 10, nullable = false)
	private String dataMatricula;
	
	@Column(name = "qtdFaltas", nullable = true)
	private int qtdFaltas;
	
}