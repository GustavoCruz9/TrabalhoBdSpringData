package guilherme.gustavo.TrabalhoBdSpringData.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import guilherme.gustavo.TrabalhoBdSpringData.model.Aluno;
import guilherme.gustavo.TrabalhoBdSpringData.model.Avaliacao;
import guilherme.gustavo.TrabalhoBdSpringData.model.Disciplina;
import guilherme.gustavo.TrabalhoBdSpringData.model.Matricula;
import guilherme.gustavo.TrabalhoBdSpringData.model.PesoAvaliacao;
import guilherme.gustavo.TrabalhoBdSpringData.model.Professor;
import guilherme.gustavo.TrabalhoBdSpringData.repository.IListaChamadaRepository;
import guilherme.gustavo.TrabalhoBdSpringData.repository.INotasRepository;

@Controller
public class NotasController {

	@Autowired
	private INotasRepository nRep;

	@Autowired
	private IListaChamadaRepository lRep;

	@RequestMapping(name = "notas", value = "/notas", method = RequestMethod.GET)
	public ModelAndView notasGet(ModelMap model) {

		// fazer listar todos os alunos com suas notas e ra quando ele selecionar a
		// disicplina
		return new ModelAndView("notas");
	}

	@RequestMapping(name = "notas", value = "/notas", method = RequestMethod.POST)
	public ModelAndView notasPost(@RequestParam Map<String, String> param, ModelMap model) {

		String cmd = param.get("botao");
		String codigoProfessor = "1"; //param.get("codigoProfessor");
		String codDisciplina = "1"; //param.get("codDisciplina");
		String cpf = "41707740860"; // param.get("cpf");
		String nota1 = "8"; // param.get("nota1");
		String nota2 =  "5"; //param.get("nota2");
		String nota3 = "7"; // param.get("nota3");

		List<Disciplina> disciplinas = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();
		List<PesoAvaliacao> tiposAvaliacao = new ArrayList<>();
		Professor professor = new Professor();
		Disciplina disciplina = new Disciplina();
		Avaliacao avaliacao = new Avaliacao();
		Matricula matricula = new Matricula();
		PesoAvaliacao pesoAvaliacao = new PesoAvaliacao();
		Aluno aluno = new Aluno();
		String erro = "";
		String saida = "";

		if (cmd.equalsIgnoreCase("Buscar Disciplinas")) {
			if (codigoProfessor.trim().isEmpty()) {
				erro = "Por favor, informe o codigo do Professor";
			}
		}

		if (cmd.equalsIgnoreCase("Buscar Aluno")) {
			if (cpf.trim().isEmpty() || codDisciplina == null) {
				erro = "Por favor, informe o cpf do aluno e selcione uma materia";
			}
		}

		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("notas");
		}
		
		if(cmd.equalsIgnoreCase("Buscar Disciplinas")) {
			professor.setCodProfessor(Integer.parseInt(codigoProfessor));
		}
		
		if(cmd.equalsIgnoreCase("Buscar Aluno") || (cmd.equalsIgnoreCase("Confirmar"))){
			disciplina.setCodDisciplina(Integer.parseInt(codDisciplina));
			aluno.setCpf(cpf);
		}
		
		if(cmd.equalsIgnoreCase("Confirmar")){
			
			matricula.setAluno(aluno);
			matricula.setAnoSemestre(Integer.parseInt(anoSemestre()));
			matricula.setDisciplina(disciplina);
			
			tiposAvaliacao = buscaPesoAvaliacao(disciplina);
			
			if(!nota1.isEmpty()) {
				avaliacao.setMatricula(matricula);
				avaliacao.setNota(Double.parseDouble(nota1));
				pesoAvaliacao.setCodigo(tiposAvaliacao.get(0).getCodigo());
				avaliacoes.add(avaliacao);
			}
			if(!nota2.isEmpty()) {
				avaliacao.setMatricula(matricula);
				avaliacao.setNota(Double.parseDouble(nota2));
				pesoAvaliacao.setCodigo(tiposAvaliacao.get(1).getCodigo());
				avaliacoes.add(avaliacao);
			}
			if(!nota3.isEmpty()) {
				avaliacao.setMatricula(matricula);
				avaliacao.setNota(Double.parseDouble(nota3));
				pesoAvaliacao.setCodigo(tiposAvaliacao.get(2).getCodigo());
				avaliacoes.add(avaliacao);
			}

		}


		try {
			if (cmd.equalsIgnoreCase("Buscar Disciplinas")) {
				if (validaProfessor(professor) == 1) {
					disciplinas = buscaDisciplina(professor); // funcionando
				}
			}
			
			if(cmd.equalsIgnoreCase("Buscar Aluno")) {
				if(cpf.length() == 11) {
					avaliacoes = buscaNotas(aluno, disciplina);
				}
			}
			
			if(cmd.equalsIgnoreCase("Confirmar")){
				saida = cadastrarAvaliacoes(avaliacoes);
			}
				
		} catch (SQLException | ClassNotFoundException e1) {
			erro = e1.getMessage();
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("codigoProfessor", codigoProfessor);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("avaliacoes", avaliacoes);
		}

		return new ModelAndView("notas");
	}


	private String cadastrarAvaliacoes(List<Avaliacao> avaliacoes) throws SQLException, ClassNotFoundException {
		nRep.saveAll(avaliacoes);
		return "notas atualizadas com sucesso";
	}

	private List<Avaliacao> buscaNotas(Aluno aluno, Disciplina disciplina) {
		List<Object[]> objetos = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();
		objetos = nRep.buscaNotas(aluno.getCpf(), disciplina.getCodDisciplina());
		if(!objetos.isEmpty()) {
			for (Object[] row : objetos) {
				Avaliacao avaliacao = new Avaliacao();
				PesoAvaliacao pesoAvaliacao = new PesoAvaliacao();
				Matricula matricula = new Matricula();
				aluno = new Aluno();
				
				aluno.setNome((String) row[0].toString());
				aluno.setCpf((String) row[1].toString());
				matricula.setAluno(aluno);
				
				pesoAvaliacao.setTipo((String) row[2].toString());
				avaliacao.setNota((double) row[3]);
				
				avaliacao.setMatricula(matricula);
				avaliacao.setPesoAvaliacao(pesoAvaliacao);
				
				avaliacoes.add(avaliacao);
			}
		}else {
			List<PesoAvaliacao> tiposAvaliacao = new ArrayList<>();
			Avaliacao avaliacao = new Avaliacao();
			Matricula matricula = new Matricula();
			Aluno aluno2 = new Aluno();
			
			tiposAvaliacao = buscaPesoAvaliacao(disciplina);
			
			int tamanho = tiposAvaliacao.size();
			
			for(int i = 0; i < tamanho; i++) {
				
				avaliacao.setNota(0);
				aluno2.setCpf(aluno.getCpf());
				matricula.setAluno(aluno2);
				matricula.setAnoSemestre(Integer.parseInt(anoSemestre()));
				matricula.setDisciplina(disciplina);
				avaliacao.setMatricula(matricula);
				avaliacao.setPesoAvaliacao(tiposAvaliacao.get(0));
				
				avaliacoes.add(avaliacao);
			}
			
			nRep.saveAll(avaliacoes);
		}
		
		return avaliacoes;
	}
	
	private List<PesoAvaliacao> buscaPesoAvaliacao(Disciplina disciplina) {
		return nRep.buscaPesoAvaliacao(disciplina.getCodDisciplina());
	}
	
	private String anoSemestre() {
		
		LocalDate hoje = LocalDate.now();
		int anoAtual = hoje.getYear();
        int mesAtual = hoje.getMonthValue();
        int semestreAtual = mesAtual <= 6 ? 1 : 2;
        
		return anoAtual + "" + semestreAtual;

	}

	private List<Disciplina> buscaDisciplina(Professor p) throws ClassNotFoundException, SQLException {

		List<Disciplina> disciplinas = new ArrayList<>();
		List<Object[]> objetos = new ArrayList<>();
		objetos = lRep.buscaDisciplina(p.getCodProfessor());
		for (Object[] row : objetos) {
			Disciplina disciplina = new Disciplina();

			disciplina.setCodDisciplina((Integer) row[0]);
			disciplina.setDisciplina((String) row[1].toString());

			disciplinas.add(disciplina);
		}
		return disciplinas;
	}

	private int validaProfessor(Professor p) throws SQLException, ClassNotFoundException {
		return lRep.validaProfessor(p.getCodProfessor());
	}

}
