package guilherme.gustavo.TrabalhoBdSpringData.controller;

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
import guilherme.gustavo.TrabalhoBdSpringData.repository.IMatriculaRepository;
import guilherme.gustavo.TrabalhoBdSpringData.repository.INotasRepository;

@Controller
public class NotasController {

	@Autowired
	private INotasRepository nRep;

	@Autowired
	private IListaChamadaRepository lRep;
	
	@Autowired
	private IMatriculaRepository mRep;

	@RequestMapping(name = "notas", value = "/notas", method = RequestMethod.GET)
	public ModelAndView notasGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("notas");
	}

	@RequestMapping(name = "notas", value = "/notas", method = RequestMethod.POST)
	public ModelAndView notasPost(@RequestParam Map<String, String> param, ModelMap model) {

		String cmd = param.get("botao");
		String codigoProfessor = param.get("codigoProfessor");
		String codDisciplina = param.get("codDisciplina");
		String cpf = param.get("cpf");
		String nota1 = param.get("nota1");
		String nota2 = param.get("nota2");
		String nota3 = param.get("nota3");
		String statusSelect = param.get("statusSelect");
		String cpfSelect = param.get("cpfSelect");

		List<Disciplina> disciplinas = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();
		List<PesoAvaliacao> tiposAvaliacao = new ArrayList<>();
		List<Object> alunosAvaliacaoObject = new ArrayList<>();
		Professor professor = new Professor();
		Disciplina disciplina = new Disciplina();
		Avaliacao avaliacao = new Avaliacao();
		Matricula matricula = new Matricula();
		PesoAvaliacao pesoAvaliacao = new PesoAvaliacao();
		Aluno aluno = new Aluno();
		int qtdNotas = 0;
		String erro = "";
		String saida = "";

		if (cmd == null) {
			cmd = "";
		}

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

		if (cmd.equalsIgnoreCase("Buscar Disciplinas")) {
			professor.setCodProfessor(Integer.parseInt(codigoProfessor));
		}
		if (cmd.equalsIgnoreCase("Finalizar Semestre")) {
			disciplina.setCodDisciplina(Integer.parseInt(codDisciplina));
			professor.setCodProfessor(Integer.parseInt(codigoProfessor));
		}

		if ((cmd.equalsIgnoreCase("Confirmar") || cmd.equalsIgnoreCase("Buscar Aluno"))
				|| cmd.equalsIgnoreCase("Lancar Notas")) {
			disciplina.setCodDisciplina(Integer.parseInt(codDisciplina));
			professor.setCodProfessor(Integer.parseInt(codigoProfessor));
			if (cpfSelect == null) {
				aluno.setCpf(cpf);
			} else {
				aluno.setCpf(cpfSelect);
				cpf = cpfSelect;
			}

		}

		if (statusSelect != null && statusSelect.equalsIgnoreCase("true")) {
			professor.setCodProfessor(Integer.parseInt(codigoProfessor));
			disciplina.setCodDisciplina(Integer.parseInt(codDisciplina));
		}

		if (cmd.equalsIgnoreCase("Confirmar")) {

			List<Avaliacao> CodigosAvaliacoes = new ArrayList<>();
			CodigosAvaliacoes = buscaCodigosAvaliacoes(disciplina, aluno);

			if (nota1 != null) {
				avaliacao.setNota(Float.parseFloat(nota1));
				avaliacao.setCodigo(CodigosAvaliacoes.get(0).getCodigo());
				avaliacoes.add(avaliacao);
			}
			if (nota2 != null) {
				avaliacao = new Avaliacao();
				avaliacao.setNota(Float.parseFloat(nota2));
				avaliacao.setCodigo(CodigosAvaliacoes.get(1).getCodigo());
				avaliacoes.add(avaliacao);
			}
			if (nota3 != null) {
				avaliacao = new Avaliacao();
				avaliacao.setNota(Float.parseFloat(nota3));
				avaliacao.setCodigo(CodigosAvaliacoes.get(2).getCodigo());
				avaliacoes.add(avaliacao);
			}

		}

		try {
			if (cmd.equalsIgnoreCase("Buscar Disciplinas") || codigoProfessor != null) {
				if (validaProfessor(professor) == 1) {
					disciplinas = buscaDisciplina(professor);
				}
			}

			if (cmd.equalsIgnoreCase("Lancar Notas")) {
				if (aluno.getCpf().length() == 11) {
					avaliacoes = buscaNotasComParam(aluno, disciplina);
				}
			}

			if (cmd.equalsIgnoreCase("Confirmar")) {
				saida = cadastrarAvaliacoes(avaliacoes);
				avaliacoes = new ArrayList<>();
			}
			
			if (cmd.equalsIgnoreCase("Finalizar Semestre")) {
				saida = finalizarSemestre(disciplina);
			}

			if ((statusSelect != null && statusSelect.equalsIgnoreCase("true")) || cmd.equalsIgnoreCase("Confirmar")
				 || cmd.equalsIgnoreCase("Lancar Notas") || cmd.equalsIgnoreCase("Buscar Aluno")) {
				List<Avaliacao> tabelaAvaliacoes = new ArrayList<>();
				tabelaAvaliacoes = buscaNotas(disciplina);
				alunosAvaliacaoObject = organizaTabelaNotas(tabelaAvaliacoes);
				qtdNotas = nRep.buscaQtdPesos(disciplina.getCodDisciplina());
			}

		} catch (Exception e1) {
			erro = trataErro(e1.getMessage());
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("codigoProfessor", codigoProfessor);
			model.addAttribute("cpf", cpf);
			model.addAttribute("cpfSelect", cpfSelect);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("avaliacoes", avaliacoes);
			model.addAttribute("disciplina", disciplina);
			model.addAttribute("qtdNotas", qtdNotas);
			model.addAttribute("alunosAvaliacaoObject", alunosAvaliacaoObject);
		}

		return new ModelAndView("notas");
	}

	private String finalizarSemestre(Disciplina disciplina) {
		return mRep.sp_finalizaSemestre(disciplina.getCodDisciplina(), Integer.parseInt(anoSemestre()));
	}

	private List<Object> organizaTabelaNotas(List<Avaliacao> avaliacoes) {
		List<Object> objetos = new ArrayList<>();
		List<String> cpfsProcessados = new ArrayList<>();

		for (Avaliacao a : avaliacoes) {
			Aluno aluno = a.getMatricula().getAluno();
			String cpf = aluno.getCpf();

			if (!cpfsProcessados.contains(cpf)) {
				objetos.add(aluno);
				cpfsProcessados.add(cpf);

				List<Avaliacao> avaliacoesAluno = new ArrayList<>();
				for (Avaliacao av : avaliacoes) {
					if (av.getMatricula().getAluno().getCpf().equals(cpf)) {
						Avaliacao avaliacaoSimplificada = new Avaliacao();
						avaliacaoSimplificada.setNota(av.getNota());
						avaliacoesAluno.add(avaliacaoSimplificada);
					}
				}
				objetos.add(avaliacoesAluno);
			}
		}

		return objetos;
	}

	private List<Avaliacao> buscaNotas(Disciplina disciplina) {
		List<Object[]> objetos = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();
		objetos = nRep.buscaNotas(disciplina.getCodDisciplina(), Integer.parseInt(anoSemestre()));

		for (Object[] row : objetos) {
			Avaliacao avaliacao = new Avaliacao();
			PesoAvaliacao pesoAvaliacao = new PesoAvaliacao();
			Matricula matricula = new Matricula();
			Aluno aluno = new Aluno();

			aluno.setNome((String) row[0].toString());
			aluno.setCpf((String) row[1].toString());
			matricula.setAluno(aluno);

			pesoAvaliacao.setTipo((String) row[2].toString());
			avaliacao.setNota(((Number) row[3]).floatValue());

			avaliacao.setMatricula(matricula);
			avaliacao.setPesoAvaliacao(pesoAvaliacao);

			avaliacoes.add(avaliacao);
		}
		return avaliacoes;
	}

	private List<Avaliacao> buscaCodigosAvaliacoes(Disciplina disciplina, Aluno aluno) {
		List<Object[]> objetos = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();
		objetos = nRep.buscaCodigosAvaliacoes(disciplina.getCodDisciplina(), aluno.getCpf(), Integer.parseInt(anoSemestre()));
		for (Object[] row : objetos) {
			Avaliacao avaliacao = new Avaliacao();
			avaliacao.setCodigo((Integer) row[0]);
			avaliacoes.add(avaliacao);
		}

		return avaliacoes;
	}

	private String cadastrarAvaliacoes(List<Avaliacao> avaliacoes) throws Exception {
		for (Avaliacao a : avaliacoes) {
			nRep.cadastraNotas(a.getNota(), a.getCodigo());
		}
		return "Notas atualizadas com sucesso";
	}

	private List<Avaliacao> buscaNotasComParam(Aluno aluno, Disciplina disciplina) {
		List<Object[]> objetos = new ArrayList<>();
		List<Avaliacao> avaliacoes = new ArrayList<>();
		objetos = nRep.buscaNotasComParam(aluno.getCpf(), disciplina.getCodDisciplina(),
				Integer.parseInt(anoSemestre()));

		for (Object[] row : objetos) {
			Avaliacao avaliacao = new Avaliacao();
			PesoAvaliacao pesoAvaliacao = new PesoAvaliacao();
			Matricula matricula = new Matricula();
			aluno = new Aluno();

			aluno.setNome((String) row[0].toString());
			aluno.setCpf((String) row[1].toString());
			matricula.setAluno(aluno);

			pesoAvaliacao.setTipo((String) row[2].toString());
			avaliacao.setNota(((Number) row[3]).floatValue());

			avaliacao.setMatricula(matricula);
			avaliacao.setPesoAvaliacao(pesoAvaliacao);

			avaliacoes.add(avaliacao);
		}

		return avaliacoes;
	}


	private String anoSemestre() {

		LocalDate hoje = LocalDate.now();
		int anoAtual = hoje.getYear();
		int mesAtual = hoje.getMonthValue();
		int semestreAtual = mesAtual <= 6 ? 1 : 2;

		return anoAtual + "" + semestreAtual;

	}

	private List<Disciplina> buscaDisciplina(Professor p) throws Exception {

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

	private int validaProfessor(Professor p) throws Exception {
		return lRep.validaProfessor(p.getCodProfessor());
	}
	
	private String trataErro(String erro) {
		if (erro.contains("Professor nao leciona nenhuma disciplina")){
			return "Professor nao leciona nenhuma disciplina";
		}
		if (erro.contains("O codigo de professor nao existe")){
			return "O codigo de professor nao existe";
		}
		return erro;
	}

}
