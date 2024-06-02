package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import guilherme.gustavo.TrabalhoBdSpringData.model.Aluno;
import guilherme.gustavo.TrabalhoBdSpringData.model.Telefone;

public interface ITelefoneRepository extends JpaRepository<Telefone, String>{
	@Procedure(name = "Telefone.sp_iudTelefone")
	String sp_iudTelefone(@Param("op") String op, @Param("cpf") String cpf, 
			@Param("telefoneAntigo") String telefoneAntigo, @Param("telefoneNovo") String telefoneNovo);
	
	@Query(value = "SELECT nome, cpf, numero FROM fn_listarTelefones()", nativeQuery = true)
	List<Aluno> fn_listarTelefones();
}