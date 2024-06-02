package guilherme.gustavo.TrabalhoBdSpringData.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
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
@Table(name = "Telefone")
@IdClass(TelefoneId.class)
@NamedStoredProcedureQuery(
		name = "Telefone.sp_iudTelefone",
		procedureName = "sp_iudTelefone",
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "op", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "cpf", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "telefoneAntigo", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "telefoneNovo", type = String.class),
				@StoredProcedureParameter(mode = ParameterMode.OUT, name = "saida", type = String.class)
		}
)
@NamedNativeQuery(
		name = "Telefone.fn_listarTelefones",
		query = "SELECT nome, cpf, numero FROM fn_listarTelefones()",
		resultClass = Aluno.class
	)
public class Telefone {
	
	@Id
	@Column(name = "numero", length = 11, nullable = false)
	private String numero;
	
	@Id
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Aluno.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "cpf", nullable = false)
    private Aluno aluno;
	
}
