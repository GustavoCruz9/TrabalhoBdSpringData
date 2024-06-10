package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import guilherme.gustavo.TrabalhoBdSpringData.model.ListaChamada;
import guilherme.gustavo.TrabalhoBdSpringData.model.ListaChamadaId;

public interface IListaChamadaRepository extends JpaRepository<ListaChamada, ListaChamadaId> {
	
	@Procedure(name = "ListaChamada.sp_validaProfessor")
	int validaProfessor(@Param("codProfessor") int codProfessor);
	
	@Query(value = """
			select codDisciplina, nome + ' - ' + turno as nome 
			from Disciplina 
			where codProfessor = :codProfessor
			""", nativeQuery = true)
	List<Object[]> buscaDisciplina(@Param("codProfessor") int codProfessor);
	
	@Query(value = """
			select dataChamada, nome, anoSemestre, cpf, codDisciplina
			from fn_obterChamadasUnicas(:codDisciplina)
			""", nativeQuery = true)
	List<Object[]> buscaChamada(@Param("codDisciplina") int CodDisciplina); 
	
	@Query(value = """
			select 
			    cpf,
			    nome,
			    string_agg(convert(varchar, dataChamada, 103), '; ') as datas,
			    string_agg(cast(presencaSemana as varchar), '; ') as presencas,
			    string_agg(cast(faltasSemana as varchar), '; ') as ausencias,
			    max(totalFaltas) as totalFaltas,
			    max(statusAluno) as statusAluno
			from fn_calculaFaltasEFrequencia(:codDisciplina)
			group by cpf, nome
			order by cpf;

			""", nativeQuery = true)
	List<Object> buscaFaltaPorSemanas(@Param("codDisciplina") int CodDisciplina); 
	
	@Procedure(name = "ListaChamada.sp_cadastraChamada")
	void cadastrarChamada (@Param("dataChamada") String dataChamada, @Param("anoSemestre") int anoSemestre,
			@Param("cpf") String cpf, @Param("codDisciplina") int codDisciplina, @Param("presenca") int presenca,
			 @Param("ausencia") int ausencia, @Param("aula1") String aula1, @Param("aula2") String aula2,
			 @Param("aula3") String aula3, @Param("aula4") String aula4);
	
	@Procedure(name = "ListaChamada.sp_atualizaChamada")
	void atualizaChamada (@Param("presenca") int presenca, @Param("ausencia") int ausencia, 
			@Param("aula1") String aula1, @Param("aula2") String aula2, @Param("aula3") String aula3, 
			@Param("aula4") String aula4, @Param("codDisciplina") int codDisciplina, @Param("cpf") String cpf,
			@Param("dataChamada") String dataChamada);
	
	@Query(value = """
				select a.nome as nomeAluno, a.ra, a.cpf, d.nome, d.codDisciplina, d.horasSemanais, d.diaSemana, m.anoSemestre
				from Matricula m, Disciplina d, Aluno a
				where a.cpf = m.cpf
				      and m.codDisciplina = d.codDisciplina
					  and d.codDisciplina = :codDisciplina
					  and m.anoSemestre = (dbo.fn_obterAnoSemestre())
					  and m.statusMatricula <> 'Dispensado'
			""", nativeQuery = true)
	List<Object[]> buscarAlunos(@Param("codDisciplina") int codDisciplina);
	
	@Query(value = """
				select lc.dataChamada, lc.anoSemestre, a.cpf, a.nome, lc.codDisciplina,  d.horasSemanais, 
				d.nome as nomeDisciplina, d.diaSemana, lc.presenca, lc.ausencia, lc.aula1, lc.aula2, lc.aula3,
				lc.aula4, a.ra 
				from ListaChamada lc, Aluno a, Disciplina d
				where lc.codDisciplina = :codDisciplina
					and lc.anoSemestre = (dbo.fn_obterAnoSemestre())
					and lc.dataChamada = :dataChamada
					and lc.cpf = a.cpf
					and d.codDisciplina = lc.codDisciplina
			""", nativeQuery = true)
	List<Object[]> buscarAlunosEditarChamada(@Param("codDisciplina") int codDisciplina, @Param("dataChamada") String dataChamada);
}
