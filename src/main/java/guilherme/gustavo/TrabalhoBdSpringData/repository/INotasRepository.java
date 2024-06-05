package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import guilherme.gustavo.TrabalhoBdSpringData.model.Avaliacao;
import guilherme.gustavo.TrabalhoBdSpringData.model.PesoAvaliacao;

public interface INotasRepository extends JpaRepository<Avaliacao, Integer> {
	
	@Query(value = """
			select a.nome, a.cpf, pav.tipo ,av.nota 
			from Aluno a, Avaliacao av, PesoAvaliacao pav 
			where av.cpf = a.cpf 
				  and av.codigoPesoAvaliacao = pav.codigo
				  and a.cpf = :cpf
				  and av.codDisciplina = :codDisciplina
			""", nativeQuery = true)
	List<Object[]> buscaNotas(@Param("cpf") String cpf, @Param("codDisciplina") int codDisciplina);

	
	@Query(value = """
			select * from PesoAvaliacao where codDisciplina = :codDisciplina
			""", nativeQuery = true)
	List<PesoAvaliacao> buscaPesoAvaliacao(@Param("codDisciplina") int codDisciplina);
}
