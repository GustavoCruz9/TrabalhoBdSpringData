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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Professor")	
public class Professor {
		
	@Id
	@Column(name = "codProfessor", nullable = false)
	private int codProfessor ;
	
	@Column(name = "nome", length = 100, nullable = false)
	private String nome;
}