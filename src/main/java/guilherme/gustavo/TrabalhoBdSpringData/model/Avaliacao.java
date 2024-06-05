package guilherme.gustavo.TrabalhoBdSpringData.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Avaliacao")
public class Avaliacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int codigo;
	
	@ManyToOne
    @JoinColumns({
            @JoinColumn(name="anoSemestre", referencedColumnName="anoSemestre", insertable = false, updatable = false),
            @JoinColumn(name="cpf", referencedColumnName="cpf", insertable = false, updatable = false),
            @JoinColumn(name="codDisciplina", referencedColumnName="codDisciplina", insertable = false, updatable = false)
    })
    private Matricula matricula;
	
	@ManyToOne(targetEntity = PesoAvaliacao.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoPesoAvaliacao", nullable = false)
	private PesoAvaliacao pesoAvaliacao;
	
	@Column(name = "nota", nullable = false)
	private float nota;
	
}
