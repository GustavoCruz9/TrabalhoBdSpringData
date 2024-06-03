package guilherme.gustavo.TrabalhoBdSpringData.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ListaChamada")
@IdClass(ListaChamadaId.class)
public class ListaChamada {

	@Id
	@Column(name = "dataChamada", nullable = false)
	private LocalDate dataChamada;

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
