package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import guilherme.gustavo.TrabalhoBdSpringData.model.Matricula;
import guilherme.gustavo.TrabalhoBdSpringData.model.MatriculaId;

public interface IMatriculaRepository extends JpaRepository<Matricula, MatriculaId>{
	
	@Procedure(name = "Matricula.sp_validaRa")
	int sp_validaRa(@Param("ra") String ra);
	
	@Query(value = """
			 	select diaSemana, codDisciplina, disciplina, horasSemanais,
				horaInicio, horaFinal, statusMatricula
				from fn_popularMatricula(:ra)
			""", nativeQuery = true)
	List<Object[]> listarDisciplinas(@Param("ra") String ra);
	
	@Procedure(name = "Matricula.sp_cadastrarMatricula")
	String cadastrarMatricula(@Param("ra") String ra, @Param("codDisciplina") int codDisciplina);
	
	@Query(value = """
		 	select d.codDisciplina, d.nome, d.horasSemanais, d.horaInicio, d.horaFinal, m.statusMatricula
			from Disciplina d, Matricula m, Aluno a
			where a.ra = :ra and d.codDisciplina = m.codDisciplina and m.cpf = a.cpf
		""", nativeQuery = true)
	List<Object[]> listarMatriculas(@Param("ra") String ra);
	
	@Query(value = """
		 	select ra, nome, nomeCurso, dataPrimeiraMatricula, pontuacaoVestibular, posicaoVestibular
			from dbo.fn_historico(:ra)
		""", nativeQuery = true)
	List<Object[]> populaInfosMatricula(@Param("ra") String ra);
	
	@Query(value = """
				select codDisciplina, nomeDisciplina, nomeProfessor, notaFinal, qtdFaltas
				from fn_matriculaAprovada(:ra)
		""", nativeQuery = true)
	List<Object[]> populaHistorico(@Param("ra") String ra);
	
 }