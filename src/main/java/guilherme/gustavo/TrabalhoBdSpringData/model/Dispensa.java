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
@Table(name = "Dispensa")
@IdClass(DispensaId.class)
@NamedStoredProcedureQuery(
		name = "Dispensa.sp_validaCpfDuplicado",
		procedureName = "sp_validaCpfDuplicado",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cpf", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = Integer.class)
		}
)
@NamedStoredProcedureQuery(
		name = "Dispensa.sp_consultaCpf",
		procedureName = "sp_consultaCpf",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cpf", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = Integer.class)
		}
)
@NamedStoredProcedureQuery(
		name = "Dispensa.sp_atualizaDispensa",
		procedureName = "sp_atualizaDispensa",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ra", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "codDisciplina", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "statusDispensa", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class)
		}
)
@NamedStoredProcedureQuery(
		name = "Dispensa.sp_iDispensa",
		procedureName = "sp_iDispensa",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cpf", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "codDisciplina", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "instituicao", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class),
		}
)
public class Dispensa {

	@Id
	@ManyToOne(targetEntity = Aluno.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "cpf", nullable = false)
	private Aluno aluno;
	
	@Id
	@ManyToOne(targetEntity = Disciplina.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codDisciplina", nullable = false)
	private Disciplina disciplina;
	
	@Column(name = "dataDispensa", length = 12, nullable = false)
	private String dataSolicitacao;
	
	@Column(name = "statusDispensa", length = 10, nullable = false)
	private String statusDispensa;
	
	@Column(name = "instituicao", length = 100, nullable = false)
	private String instituicao;
	
}