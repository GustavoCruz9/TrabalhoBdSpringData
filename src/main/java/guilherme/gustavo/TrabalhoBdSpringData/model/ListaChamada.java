package guilherme.gustavo.TrabalhoBdSpringData.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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
@Table(name = "ListaChamada")
@IdClass(ListaChamadaId.class)

@NamedStoredProcedureQuery(
		name = "ListaChamada.sp_validaProfessor",
		procedureName = "sp_validaProfessor",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "codProfessor", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = Integer.class)
		}
)

@NamedStoredProcedureQuery(
		name = "ListaChamada.sp_cadastraChamada",
		procedureName = "sp_cadastraChamada",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "dataChamada", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "anoSemestre", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cpf", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "codDisciplina", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "presenca", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "ausencia", type = Integer.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "aula1", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "aula2", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "aula3", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "aula4", type = String.class)
		}
)
public class ListaChamada {

	@Id
	@Column(name = "dataChamada", nullable = false)
	private String dataChamada;

	@Id
	@ManyToOne
    @JoinColumns({
            @JoinColumn(name="anoSemestre", referencedColumnName="anoSemestre", insertable = false, updatable = false),
            @JoinColumn(name="cpf", referencedColumnName="cpf", insertable = false, updatable = false),
            @JoinColumn(name="codDisciplina", referencedColumnName="codDisciplina", insertable = false, updatable = false)
    })
    private Matricula matricula;
	
	@Column(name = "presenca", nullable = false)
	private int presenca;

	@Column(name = "ausencia", nullable = false)
	private int ausencia;
	
	@Column(name = "aula1", length =1, nullable = false)
	private String aula1;

	@Column(name = "aula2", length =1, nullable = false)
	private String aula2;

	@Column(name = "aula3", length =1, nullable = false)
	private String aula3;

	@Column(name = "aula4", length =1, nullable = false)
	private String aula4;
	
}
