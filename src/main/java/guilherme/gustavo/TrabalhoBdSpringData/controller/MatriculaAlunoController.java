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
import guilherme.gustavo.TrabalhoBdSpringData.model.Disciplina;
import guilherme.gustavo.TrabalhoBdSpringData.model.Matricula;
import guilherme.gustavo.TrabalhoBdSpringData.repository.IMatriculaRepository;

@Controller
public class MatriculaAlunoController {

	@Autowired
	private IMatriculaRepository mRep;

	@RequestMapping(name = "matriculaAluno", value = "/matriculaAluno", method = RequestMethod.GET)
	public ModelAndView matriculaAlunoGet(@RequestParam Map<String, String> param, ModelMap model) {

		String pesquisaRa = param.get("pesquisaRa");
		String saida = "";
		String erro = "";

		Aluno a = new Aluno();
		List<Matricula> disciplinas = new ArrayList<>();

		if (pesquisaRa != null) {
			a.setRa(pesquisaRa);
			try {
				if (a.getRa().length() == 9) {
					if (verificaRa(a) == 1) {
						disciplinas = listarDisciplinas(a);
						if (disciplinas.isEmpty()) {
							erro = "Voce concluiu todas as disciplinas de seu curso";
						}
					} else {
						erro = "RA invalido";
					}
				} else {
					erro = "Tamanho de RA incorreto";
				}

			} catch (Exception e) {
				erro = e.getMessage();
			} finally {
				model.addAttribute("saida", saida);
				model.addAttribute("erro", erro);
				model.addAttribute("disciplinas", disciplinas);
				model.addAttribute("pesquisaRa", pesquisaRa);

			}
		}

		return new ModelAndView("matriculaAluno");
	}

	@RequestMapping(name = "matriculaAluno", value = "/matriculaAluno", method = RequestMethod.POST)
	public ModelAndView matriculaAlunoPost(@RequestParam Map<String, String> param, ModelMap model) {
		String cmd = param.get("botao");
		String pesquisaRa = param.get("pesquisaRa");

		String saida = "";
		String erro = "";

		List<Matricula> disciplinas = new ArrayList<>();

		Aluno a = new Aluno();
		Matricula m = new Matricula();
		Disciplina d = new Disciplina();

		a.setRa(pesquisaRa);

		if (!cmd.contains("pesquisa Ra")) {

			d.setCodDisciplina(Integer.parseInt(cmd));

			m.setAluno(a);
			m.setDisciplina(d);
		}

		try {
			saida = cadastrarMatricula(m);
			disciplinas = listarDisciplinas(a);
		} catch (Exception e) {
			erro = e.getMessage();

			try {
				disciplinas = listarDisciplinas(a);
			} catch (Exception e1) {
				erro = e1.getMessage();
			}

		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("disciplinas", disciplinas);
		}

		return new ModelAndView("matriculaAluno");
	}

	private int verificaRa(Aluno a) throws Exception {
		return mRep.sp_validaRa(a.getRa());
	}

	private String cadastrarMatricula(Matricula m) throws Exception {
		return mRep.cadastrarMatricula(m.getAluno().getRa(), m.getDisciplina().getCodDisciplina());
	}

	private List<Matricula> listarDisciplinas(Aluno a) throws Exception {
		List<Matricula> disciplinas = new ArrayList<>();
		List<Object[]> objetos = mRep.listarDisciplinas(a.getRa());

		for (Object[] row : objetos) {
			Disciplina d = new Disciplina();
			Matricula m = new Matricula();

			d.setDiaSemana((String) row[0]);
			d.setCodDisciplina((Integer) row[1]);
			d.setDisciplina((String) row[2]);
			d.setHorasSemanais((String) row[3].toString());
			d.setHoraInicio((String) row[4].toString());
			d.setHoraFinal((String) row[5].toString());
			m.setStatus((String) row[6]);

			m.setDisciplina(d);
			disciplinas.add(m);
		}

		return disciplinas;
	}
}
