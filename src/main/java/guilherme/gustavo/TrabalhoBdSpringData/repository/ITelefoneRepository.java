package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import guilherme.gustavo.TrabalhoBdSpringData.model.Telefone;

public interface ITelefoneRepository extends JpaRepository<Telefone, String>{
	@Procedure(name = "Telefone.sp_iudTelefone")
	String sp_iudTelefone(@Param("op") String op, @Param("cpf") String cpf, 
			@Param("telefoneAntigo") String telefoneAntigo, @Param("telefoneNovo") String telefoneNovo);
	
	@Query(value = """
			 SELECT a.cpf, a.nome, t.numero
			 FROM Telefone t
			 JOIN Aluno a ON a.cpf = t.cpf
			""", nativeQuery = true)
	List<Object[]> fn_listarTelefones();
	
	@Query(value = """
			 SELECT a.cpf, a.nome, t.numero
			 FROM Telefone t
			 JOIN Aluno a ON a.cpf = t.cpf and t.cpf = :cpf
			""", nativeQuery = true)
	List<Object[]> listarTelefonesComParam(@Param("cpf") String cpf);
}