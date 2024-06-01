package guilherme.gustavo.TrabalhoBdSpringData.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Aluno")
@NamedStoredProcedureQuery(
		name = "Aluno.sp_iuAluno",
		procedureName = "sp_iuAluno",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "op", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cpf", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "codCurso", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "nome", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "nomeSocial", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "dataNascimento", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "dataConclusao2Grau", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "instituicao2Grau", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "pontuacaoVestibular", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "posicaoVestibular", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "anoIngresso", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "semestreIngresso", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "semestreLimite", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class)
		}
)
@NamedStoredProcedureQuery(
		name = "Aluno.sp_cunsultaCpf",
		procedureName = "sp_consultaCpf",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cpf", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = Integer.class)
		}
)
public class Aluno {
	
	@Id
	@Column(name = "cpf", length = 11, nullable = false)
	private String cpf;
	
	@ManyToOne(targetEntity = Curso.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codCurso", nullable = false)
	private Curso curso;
	
	@Column(name = "ra", length = 9, nullable = false)
	private String ra; 
	
	@Column(name = "nome", length = 150, nullable = false)
	private String nome;

	@Column(name = "nomeSocial", length = 150, nullable = true)
	private String nomeSocial;
	
	@Column(name = "dataNascimento", nullable = false)
	private LocalDate dataNascimento;
	
	@Column(name = "email", length = 100, nullable = false)
	private String email;
	
	@Column(name = "emailCorporativo", length = 100, nullable = false)
	private String emailCorporativo;
	
	@Column(name = "dataConclusao2Grau", nullable = false)
	private LocalDate dataConclusao2Grau;
	
	@Column(name = "instituicao2Grau", length = 100, nullable = false)
	private String instituicao2Grau;
	
	@Column(name = "pontuacaoVestibular", nullable = false)
	private int pontuacaoVestibular;
	
	@Column(name = "posicaoVestibular", nullable = false)
	private int posicaoVestibular;
	
	@Column(name = "anoIngresso", nullable = false)
	private int anoIngresso;
	
	@Column(name = "semestreIngresso", nullable = false)
	private int semestreIngresso;
	
	@Column(name = "anoLimite", nullable = false)
	private int anoLimite;
	
	@Column(name = "semestreLimite", nullable = false)
	private int semestreLimite;
	
	@Column(name = "turno", length = 10, nullable = false)
	private String turno;
	
	@ElementCollection
	List<Telefone> telefones = new ArrayList<>();
}
