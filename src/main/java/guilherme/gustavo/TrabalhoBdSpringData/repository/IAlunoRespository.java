package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
