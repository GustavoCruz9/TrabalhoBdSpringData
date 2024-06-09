package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import guilherme.gustavo.TrabalhoBdSpringData.model.Avaliacao;
import jakarta.transaction.Transactional;

public interface INotasRepository extends JpaRepository<Avaliacao, Integer> {
	
	
	@Query(value = """
			select a.nome, a.cpf, pav.tipo ,av.nota 
			from Aluno a, Avaliacao av, PesoAvaliacao pav, Matricula m
			where av.cpf = a.cpf
				  and a.cpf = m.cpf
				  and av.codigoPesoAvaliacao = pav.codigo
				  and av.anoSemestre = m.anoSemestre
				  and av.cpf = :cpf
				  and ( m.statusMatricula = 'pendente' 
				  or m.statusMatricula = 'Reprovado' 
				  or m.statusMatricula = 'Aprovado' 
				  or m.statusMatricula = 'Exame')
				  and av.codDisciplina = m.codDisciplina
				  and av.anoSemestre = :anoSemestre
				  and av.codDisciplina = :codDisciplina
				  order by pav.tipo asc
			""", nativeQuery = true)
	List<Object[]> buscaNotasComParam(@Param("cpf") String cpf, @Param("codDisciplina") int codDisciplina,
									 @Param("anoSemestre") int anoSemestre);
	
	@Query(value = """
			select a.nome, a.cpf, pav.tipo ,av.nota, m.nota, m.statusMatricula
			from Aluno a, Avaliacao av, PesoAvaliacao pav, Matricula m
			where av.cpf = a.cpf 
				  and a.cpf = m.cpf
				  and av.codigoPesoAvaliacao = pav.codigo
				  and av.anoSemestre = m.anoSemestre
				  and ( m.statusMatricula = 'pendente' 
				  or m.statusMatricula = 'Reprovado' 
				  or m.statusMatricula = 'Aprovado' 
				  or m.statusMatricula = 'Exame')
				  and av.codDisciplina = m.codDisciplina
				  and av.anoSemestre = :anoSemestre
				  and av.codDisciplina = :codDisciplina
				  order by a.cpf, pav.tipo asc
			""", nativeQuery = true)
	List<Object[]> buscaNotas(@Param("codDisciplina") int codDisciplina, @Param("anoSemestre") int anoSemestre);

	
	@Query(value = """
			select * from PesoAvaliacao where codDisciplina = :codDisciplina order by tipo asc
			""", nativeQuery = true)
	List<Object[]> buscaPesoAvaliacao(@Param("codDisciplina") int codDisciplina);
	
	@Query(value = """
			select count(codigo) from PesoAvaliacao where codDisciplina = :codDisciplina
			""", nativeQuery = true)
	int buscaQtdPesos(@Param("codDisciplina") int codDisciplina);
	
	@Query(value = """
			select a.codigo from Avaliacao a, PesoAvaliacao pav 
			where pav.codigo = a.codigoPesoAvaliacao and a.codDisciplina = :codDisciplina and a.cpf = :cpf
			and a.anoSemestre = :anoSemestre
			order by pav.tipo
			""", nativeQuery = true)
	List<Object[]> buscaCodigosAvaliacoes(@Param("codDisciplina") int codDisciplina, @Param("cpf") String cpf,
										  @Param("anoSemestre") int anoSemestre);
	
	@Query(value = """
            select d.nome, 
			    string_agg(
			        concat(pav.tipo, ' ', NULL), 
			        '; '
			    ) within group (order by pav.tipo) as tipoavaliacoes,
				string_agg(
			        concat(av.nota, ' ', NULL), 
			        '; '
			    ) within group (order by pav.tipo) as avaliacoes, 
			    isnull(max(m.nota), -1) as nota,
				m.statusMatricula
			from Aluno a, Avaliacao av, PesoAvaliacao pav, Matricula m, Disciplina d
			where av.cpf = a.cpf
					and a.cpf = m.cpf
					and av.codigoPesoAvaliacao = pav.codigo
					and av.anoSemestre = m.anoSemestre
					and m.codDisciplina = d.codDisciplina
					and av.cpf = :cpf
					and( m.statusMatricula = 'pendente' 
					or m.statusMatricula = 'Reprovado'
					or m.statusMatricula = 'Aprovado' 
					or m.statusMatricula = 'Exame')
					and av.codDisciplina = m.codDisciplina
					and av.anoSemestre = :anoSemestre
			group by d.nome, m.statusMatricula
			order by d.nome
       """, nativeQuery = true)
   List<Object> buscaAvaliacoes(@Param("cpf") String cpf, @Param("anoSemestre") int anoSemestre);
	
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
