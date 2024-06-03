package guilherme.gustavo.TrabalhoBdSpringData.model;

import java.io.Serializable;
import java.time.LocalDate;

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
public class ListaChamadaId implements Serializable{

	private static final long serialVersionUID = 1l;
	private LocalDate dataChamada; 
	private Matricula matricula;
}