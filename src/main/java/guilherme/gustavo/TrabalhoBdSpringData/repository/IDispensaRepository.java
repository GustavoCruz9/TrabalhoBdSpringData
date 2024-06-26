package guilherme.gustavo.TrabalhoBdSpringData.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import guilherme.gustavo.TrabalhoBdSpringData.model.Dispensa;
import guilherme.gustavo.TrabalhoBdSpringData.model.DispensaId;

public interface IDispensaRepository extends JpaRepository<Dispensa, DispensaId>{
	
	@Procedure(name = "Dispensa.sp_validaCpfDuplicado")
	int sp_validaCpfDuplicado(@Param("cpf") String cpf);
	
	@Procedure(name = "Dispensa.sp_consultaCpf")
	int sp_consultaCpf(@Param("cpf") String cpf);
	
	@Query(value = """
			 	select a.ra, a.nome, disp.dataDispensa, d.nome as nomeDisciplina, disp.instituicao,  d.codDisciplina
				from Dispensa disp, Aluno a, Disciplina d
				where disp.codDisciplina = d.codDisciplina
				and disp.cpf = a.cpf
				and disp.statusDispensa like 'em analise'
				and disp.cpf = :cpf
		""", nativeQuery = true)
	List<Object[]> listarDispensasComCpf(@Param("cpf") String cpf);
	
	@Procedure(name = "Dispensa.sp_atualizaDispensa")
	String sp_atualizaDispensa(@Param("ra") String ra, @Param("codDisciplina") int codDisciplina, @Param("statusDispensa") String statusDispensa);

	
	@Query(value = """
			 	select a.ra, a.nome, disp.dataDispensa, d.nome as nomeDisciplina, disp.instituicao, d.codDisciplina
				from Dispensa disp, Aluno a, Disciplina d
				where disp.codDisciplina = d.codDisciplina
				and disp.cpf = a.cpf
				and disp.statusDispensa like 'em analise'
		""", nativeQuery = true)
	List<Object[]> listarDispensas();
	
	@Query(value = """
			select d.nome, d.codDisciplina 
			from Disciplina d left outer join Dispensa disp on d.codDisciplina = disp.codDisciplina
			where d.codCurso = (select codCurso from Aluno where cpf = :cpf) and disp.cpf is null
			""", nativeQuery = true)
	List<Object[]> popularDisciplinas(@Param("cpf") String cpf);
	
	@Procedure(name = "Dispensa.sp_iDispensa")
	String sp_iDispensa(@Param("cpf") String cpf, @Param("codDisciplina") int codDisciplina,
			@Param("instituicao") String instituicao);
	
	@Query(value = """
			select codDisciplina, dataDispensa, instituicao, statusDispensa
			from Dispensa
			where cpf = :cpf
			""", nativeQuery = true)
	List<Object[]> listarDispensasComParam(@Param("cpf") String cpf);
}