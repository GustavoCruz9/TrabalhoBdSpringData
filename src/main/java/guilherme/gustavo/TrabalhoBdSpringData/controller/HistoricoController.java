package guilherme.gustavo.TrabalhoBdSpringData.controller;

import java.sql.SQLException;
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
import guilherme.gustavo.TrabalhoBdSpringData.model.Curso;
import guilherme.gustavo.TrabalhoBdSpringData.model.Disciplina;
import guilherme.gustavo.TrabalhoBdSpringData.model.Matricula;
import guilherme.gustavo.TrabalhoBdSpringData.model.Professor;
import guilherme.gustavo.TrabalhoBdSpringData.repository.IMatriculaRepository;

@Controller
public class HistoricoController {

	@Autowired
	private IMatriculaRepository mRep;

	@RequestMapping(name = "historico", value = "/historico", method = RequestMethod.GET)
	public ModelAndView historicoGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("historico");
	}

	@RequestMapping(name = "historico", value = "/historico", method = RequestMethod.POST)
	public ModelAndView historicoPost(@RequestParam Map<String, String> param, ModelMap model) {
		String cmd = param.get("botao");
		String pesquisaRa = param.get("pesquisaRa");

		String saida = "";
		String erro = "";

		if (cmd.contains("Pesquisa Ra")) {
			if (pesquisaRa.trim().isEmpty()) {
				erro = "Por favor, informe o RA.";
			}
		}

		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("historico");
		}

		Aluno aluno = new Aluno();
		Matricula matricula = new Matricula();
		List<Matricula> matriculas = new ArrayList<>();

		aluno.setRa(pesquisaRa);
		matricula.setAluno(aluno);

		try {
			if (aluno.getRa().length() == 9) {
				if (verificaRa(aluno) == 1) {
					if (cmd.contains("Pesquisa Ra")) {
						matricula = populaInfosAluno(matricula);
						matriculas = populaHistorico(aluno);
					}
				} else {
					erro = "RA inexistente";
				}
			} else {
				erro = "Tamanho de Ra invalido";
				matricula = new Matricula();
			}
		} catch (Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("matricula", matricula);
			model.addAttribute("matriculas", matriculas);
		}

		return new ModelAndView("historico");
	}

	private int verificaRa(Aluno aluno) throws Exception {
		int saida = mRep.sp_validaRa(aluno.getRa());
		return saida;
	}

	private Matricula populaInfosAluno(Matricula matricula) throws Exception {
		Matricula m = new Matricula();
		List<Object[]> objetos = mRep.populaInfosMatricula(matricula.getAluno().getRa());
		Aluno a = new Aluno();
		Curso c = new Curso();

		for (Object[] row : objetos) {
			a.setRa((String) row[0].toString());
			a.setNome((String) row[1].toString());
			c.setNome((String) row[2].toString());
			m.setDataMatricula((String) row[3].toString());
			a.setPontuacaoVestibular((Integer) row[4]);
			a.setPosicaoVestibular((Integer) row[5]);

			a.setCurso(c);
			m.setAluno(a);
		}

		return m;
	}

	private List<Matricula> populaHistorico(Aluno aluno) throws Exception {
		List<Matricula> matriculas = new ArrayList<>();
		List<Object[]> objetos = mRep.populaHistorico(aluno.getRa());

		for (Object[] row : objetos) {
			Professor p = new Professor();
			Disciplina d = new Disciplina();
			Matricula m = new Matricula();

			d.setCodDisciplina((Integer) row[0]);
			d.setDisciplina((String) row[1].toString());
			p.setNome((String) row[2].toString());
			if (row[3] != null) {
				m.setNota((double) row[3]);
			} else {
				m.setNota(0.0);
			}
			if (row[4] != null) {
				m.setQtdFaltas((Integer) row[4]);
			} else {
				m.setQtdFaltas(0);
			}
			d.setProfessor(p);
			m.setDisciplina(d);

			matriculas.add(m);
		}

		return matriculas;
	}
}
