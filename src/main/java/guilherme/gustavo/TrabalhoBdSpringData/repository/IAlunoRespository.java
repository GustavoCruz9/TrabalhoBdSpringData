package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import guilherme.gustavo.TrabalhoBdSpringData.model.Aluno;

public interface IAlunoRespository extends JpaRepository<Aluno, String>{
	
	@Procedure(name = "Aluno.sp_iuAluno")
	String sp_iuAluno(
			 	@Param("op") String op,
		        @Param("cpf") String cpf,
		        @Param("codCurso") int codCurso,
		        @Param("nome") String nome,
		        @Param("nomeSocial") String nomeSocial,
		        @Param("dataNascimento") String dataNascimento,
		        @Param("email") String email,
		        @Param("dataConclusao2Grau") String dataConclusao2Grau,
		        @Param("instituicao2Grau") String instituicao2Grau,
		        @Param("pontuacaoVestibular") int pontuacaoVestibular,
		        @Param("posicaoVestibular") int posicaoVestibular,
		        @Param("anoIngresso") int anoIngresso,
		        @Param("semestreIngresso") int semestreIngresso,
		        @Param("semestreLimite") int semestreLimite
	);
	
	@Procedure(name = "Aluno.sp_consultaCpf")
	Integer sp_consultaCpf(@Param("cpf") String cpf);
	
	@Query(value = """
			 	SELECT a.cpf, a.codCurso, a.ra, a.nome, a.nomeSocial, a.dataNascimento, a.email, a.emailCorporativo,
				a.dataConclusao2Grau, a.instituicao2Grau, a.pontuacaoVestibular, a.posicaoVestibular, a.anoIngresso,
				a.semestreIngresso, a.anoIngresso, a.anoLimite, a.semestreLimite,
				(SELECT t1.numero FROM Telefone t1 WHERE t1.cpf = a.cpf AND t1.numero IS NOT NULL ORDER BY t1.numero OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY) AS telefone1,
				(SELECT t2.numero FROM Telefone t2 WHERE t2.cpf = a.cpf AND t2.numero IS NOT NULL ORDER BY t2.numero OFFSET 1 ROWS FETCH NEXT 1 ROW ONLY) AS telefone2
				FROM Aluno a
			""", nativeQuery = true)
	List<Object[]> listarAlunos();
	
	@Query(value = """
            select d.nome, pav.tipo ,av.nota 
           from Aluno a, Avaliacao av, PesoAvaliacao pav, Matricula m, Disciplina d
           where av.cpf = a.cpf
                 and a.cpf = m.cpf
                 and av.codigoPesoAvaliacao = pav.codigo
                 and av.anoSemestre = m.anoSemestre
                 and m.codDisciplina = d.codDisciplina
                 and av.cpf = :cpf
                 and m.statusMatricula = 'pendente'
                 and av.codDisciplina = m.codDisciplina
                 and av.anoSemestre = 20241
                 order by pav.tipo asc
       """, nativeQuery = true)
   List<Object[]> buscaAvaliacoes(@Param("cpf") String cpf);
}
