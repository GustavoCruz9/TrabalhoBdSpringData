package guilherme.gustavo.TrabalhoBdSpringData.model;

import java.io.Serializable;

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
public class DispensaId implements Serializable {
	
	private static final long serialVersionUID = 1l;
	private Aluno aluno;
	private Disciplina disciplina;
	
}