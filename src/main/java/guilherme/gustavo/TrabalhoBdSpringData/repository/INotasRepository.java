package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import guilherme.gustavo.TrabalhoBdSpringData.model.Avaliacao;
import jakarta.transaction.Transactional;

public interface INotasRepository extends JpaRepository<Avaliacao, Integer> {
	
	@Query(value = """
			select a.nome, a.cpf, pav.tipo ,av.nota 
			from Aluno a, Avaliacao av, PesoAvaliacao pav 
			where av.cpf = a.cpf 
				  and av.codigoPesoAvaliacao = pav.codigo
				  and a.cpf = :cpf
				  and av.codDisciplina = :codDisciplina
			""", nativeQuery = true)
	List<Object[]> buscaNotasComParam(@Param("cpf") String cpf, @Param("codDisciplina") int codDisciplina);
	
	@Query(value = """
			select a.nome, a.cpf, pav.tipo ,av.nota 
			from Aluno a, Avaliacao av, PesoAvaliacao pav 
			where av.cpf = a.cpf 
				  and av.codigoPesoAvaliacao = pav.codigo
				  and av.codDisciplina = :codDisciplina
				  order by a.cpf, pav.tipo asc
			""", nativeQuery = true)
	List<Object[]> buscaNotas(@Param("codDisciplina") int codDisciplina);

	
	@Query(value = """
			select * from PesoAvaliacao where codDisciplina = :codDisciplina order by tipo asc
			""", nativeQuery = true)
	List<Object[]> buscaPesoAvaliacao(@Param("codDisciplina") int codDisciplina);
	
	@Query(value = """
			select a.codigo from Avaliacao a, PesoAvaliacao pav 
			where pav.codigo = a.codigoPesoAvaliacao and a.codDisciplina = :codDisciplina and a.cpf = :cpf
			order by pav.tipo
			""", nativeQuery = true)
	List<Object[]> buscaCodigosAvaliacoes(@Param("codDisciplina") int codDisciplina, @Param("cpf") String cpf);
	
	@Modifying
    @Transactional
	@Query(value = """
			INSERT INTO Avaliacao
			(nota, cpf, anoSemestre, codDisciplina, codigoPesoAvaliacao) 
			VALUES (?1, ?2, ?3, ?4, ?5)
			""", nativeQuery = true)
	void cadastraNotasDefault(Float nota, String cpf, int anoSemestre, 
							  int codDisciplina, int codigoPesoAvaliacao);
	
	@Modifying
    @Transactional
	@Query(value = """
			update Avaliacao set nota = ?1 where codigo = ?2
			""", nativeQuery = true)
	void cadastraNotas(Float nota, int codigo);
	
	
}
