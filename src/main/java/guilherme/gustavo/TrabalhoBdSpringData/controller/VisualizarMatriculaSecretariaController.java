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
public class VisualizarMatriculaSecretariaController {

	@Autowired
	private IMatriculaRepository mRep;

	@RequestMapping(name = "visualizarMatriculaSecretaria", value = "/visualizarMatriculaSecretaria", method = RequestMethod.GET)
	public ModelAndView vizualizarMatriculaSecretariaGet(ModelMap model) {
		return new ModelAndView("visualizarMatriculaSecretaria");
	}

	@RequestMapping(name = "visualizarMatriculaSecretaria", value = "/visualizarMatriculaSecretaria", method = RequestMethod.POST)
	public ModelAndView vizualizarMatriculaSecretariaPost(@RequestParam Map<String, String> param, ModelMap model) {
		String cmd = param.get("botao");
		String pesquisaRa = param.get("pesquisaRa");

		String saida = "";
		String erro = "";

		List<Matricula> matriculas = new ArrayList<>();

		Aluno a = new Aluno();
		Matricula m = new Matricula();
		Disciplina d = new Disciplina();

		a.setRa(pesquisaRa);

		String aux = "";

		if (!cmd.contains("Pesquisa Ra")) {

			if (cmd.contains(".")) {
				aux = cmd.substring(0, cmd.length() - 1);
				d.setCodDisciplina(Integer.parseInt(aux));
			} else {
				d.setCodDisciplina(Integer.parseInt(cmd));
			}

			m.setAluno(a);
			m.setDisciplina(d);
		}

		try {
			if (cmd.contains("Pesquisa Ra")) {
				if (a.getRa().length() == 9) {
					if (verificaRa(a) == 1) {
						matriculas = listarMatriculas(a);
						if (matriculas.isEmpty()) {
							erro = "O aluno do Ra " + pesquisaRa + " nao possui matriculas";
						}
					} else {
						erro = "RA invalido";
					}
				} else {
					erro = "Tamanho de RA incorreto";
				}

			}

		} catch (Exception e) {
			erro = e.getMessage();

		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("matriculas", matriculas);
		}

		return new ModelAndView("visualizarMatriculaSecretaria");

	}

	private int verificaRa(Aluno a) throws Exception {
		return mRep.sp_validaRa(a.getRa());
	}

	private List<Matricula> listarMatriculas(Aluno a) throws Exception {
		List<Matricula> matriculas = new ArrayList<>();
		List<Object[]> objetos = mRep.listarMatriculas(a.getRa());
		
		for(Object[] row: objetos) {
			Disciplina d = new Disciplina();
			Matricula m = new Matricula();
			
			d.setCodDisciplina((Integer) row[0]);
			d.setDisciplina((String) row[1]);
			d.setHorasSemanais((String) row[2].toString());
			d.setHoraInicio((String) row[3].toString());
			d.setHoraFinal((String) row[4].toString());
			m.setStatus((String) row[5]);
			
			m.setDisciplina(d);
			matriculas.add(m);
		}
		return matriculas;
	}

}
