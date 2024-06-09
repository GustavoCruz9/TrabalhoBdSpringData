package guilherme.gustavo.TrabalhoBdSpringData.controller;

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

import guilherme.gustavo.TrabalhoBdSpringData.model.Avaliacao;
import guilherme.gustavo.TrabalhoBdSpringData.model.Disciplina;
import guilherme.gustavo.TrabalhoBdSpringData.model.Professor;
import guilherme.gustavo.TrabalhoBdSpringData.repository.IListaChamadaRepository;

@Controller
public class FaltasController {
	
	
	@Autowired
	private IListaChamadaRepository lRep;

	@RequestMapping(name = "faltas", value = "/faltas", method = RequestMethod.GET)
	public ModelAndView faltasGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("faltas");
	}
	@RequestMapping(name = "faltas", value = "/faltas", method = RequestMethod.POST)
	public ModelAndView faltasPost(@RequestParam Map<String, String> param, ModelMap model) {
		

		String cmd = param.get("botao");
		String codigoProfessor = param.get("codigoProfessor");
		String codDisciplina = param.get("codDisciplina");
		String statusSelect = param.get("statusSelect");
		
		List<Disciplina> disciplinas = new ArrayList<>();
		List<Object> alunosFaltasObject = new ArrayList<>();
		Professor professor = new Professor();
		Disciplina disciplina = new Disciplina();
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
		
		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("notas");
		}
		
		if (cmd.equalsIgnoreCase("Buscar Disciplinas")) {
			professor.setCodProfessor(Integer.parseInt(codigoProfessor));
		}
		
		if (statusSelect != null && statusSelect.equalsIgnoreCase("true")) {
			professor.setCodProfessor(Integer.parseInt(codigoProfessor));
			disciplina.setCodDisciplina(Integer.parseInt(codDisciplina));
		}
		
		try {
			if (cmd.equalsIgnoreCase("Buscar Disciplinas") || codigoProfessor != null) {
				if (validaProfessor(professor) == 1) {
					disciplinas = buscaDisciplina(professor);
				}
			}
			
			if ((statusSelect != null && statusSelect.equalsIgnoreCase("true"))){
				alunosFaltasObject = buscaFaltaPorSemanas(disciplina);
			}
		} catch (Exception e) {
			erro = trataErro(e.getMessage());
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("codigoProfessor", codigoProfessor);
			model.addAttribute("disciplina", disciplina);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("alunosFaltasObject", alunosFaltasObject);
		}

		
		
		
		
		return new ModelAndView("faltas");
	}
	
	private List<Object> buscaFaltaPorSemanas(Disciplina disciplina) {
		return lRep.buscaFaltaPorSemanas(disciplina.getCodDisciplina());
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
	
	private String trataErro(String message) {
		// TODO Auto-generated method stub
		return null;
	}
}
