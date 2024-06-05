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
import guilherme.gustavo.TrabalhoBdSpringData.model.ListaChamada;
import guilherme.gustavo.TrabalhoBdSpringData.model.Matricula;
import guilherme.gustavo.TrabalhoBdSpringData.model.Professor;
import guilherme.gustavo.TrabalhoBdSpringData.repository.IListaChamadaRepository;

@Controller
public class ChamadaController {

	@Autowired
	private IListaChamadaRepository lRep;

	@RequestMapping(name = "chamada", value = "/chamada", method = RequestMethod.GET)
	public ModelAndView chamadaGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("chamada");
	}

	@SuppressWarnings("finally")
	@RequestMapping(name = "chamada", value = "/chamada", method = RequestMethod.POST)
	public ModelAndView chamadaPost(@RequestParam Map<String, String> param, ModelMap model) {

		String cmd = param.get("botao");
		String codigoProfessor = param.get("codigoProfessor");
		String codDisciplina = param.get("codDisciplina");
		String dataChamada = param.get("dataChamda");
		

		String saida = "";
		String erro = "";
		List<Disciplina> disciplinas = new ArrayList<>();
		List<ListaChamada> ListaChamada = new ArrayList<>();
		Professor p = new Professor();
		Disciplina d = new Disciplina();
		
		if (cmd.contains("Buscar Disciplinas")) {
			if (codigoProfessor.trim().isEmpty()) {
				erro = "Por favor, informe o codigo do Professor";
			}
		}
		
		if (cmd.contains("Nova Chamada")){
			if(codigoProfessor.trim().isEmpty() || codDisciplina == null) {
				erro = "Preencha os campos de codigo professor e a disciplina desejada";
			}
		}
		
		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("chamada");
		}
		
		if (cmd.contains("Buscar Disciplinas") || cmd.contains("Nova Chamada") 
				|| cmd.contains("Buscar Chamadas")  ) {
			p.setCodProfessor(Integer.parseInt(codigoProfessor));
		}


		if (cmd.contains("Buscar Chamadas")) {
            try {
                Integer.parseInt(codDisciplina);
            }catch (Exception e) {
            	try {
					if (validaProfessor(p) == 1) {
						disciplinas = buscaDisciplina(p);
					}
				} catch (SQLException | ClassNotFoundException e1) {
					erro = e1.getMessage();	
				} finally {
					model.addAttribute("erro", erro);
	                model.addAttribute("codigoProfessor", codigoProfessor);
	    			model.addAttribute("disciplinas", disciplinas);
	                return new ModelAndView("chamada");
				}     
            }
        }

		if (cmd.contains("Buscar Chamadas")) {
				d.setCodDisciplina(Integer.parseInt(codDisciplina));
		}

		if (cmd.contains("Nova Chamada")) {
			try {
				if (validaProfessor(p) == 1) {
					if (!codDisciplina.contains("Selecione a Disciplina")) {
						model.addAttribute("codigoProfessor", codigoProfessor);
						model.addAttribute("codDisciplina", codDisciplina);
						return new ModelAndView("cadastrarChamada");
					}
				}
			} catch (SQLException | ClassNotFoundException e) {
				erro = e.getMessage();
			}

		}
		
		if (cmd.contains("Alterar")) {
			model.addAttribute("dataChamada", dataChamada);
			model.addAttribute("codDisciplina", codDisciplina);
			return new ModelAndView("editarChamada");
		}

		try {
			if (cmd.contains("Buscar Disciplinas")) {
				if (validaProfessor(p) == 1) {
					disciplinas = buscaDisciplina(p);
				}
			}

			if (cmd.contains("Buscar Chamadas")) {
				ListaChamada = buscaChamada(d);
				if(ListaChamada.isEmpty()) {
					erro = "Nao existe chamadas para esta disciplina";
				}
				if (validaProfessor(p) == 1) {
					disciplinas = buscaDisciplina(p);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("professor", p);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("ListaChamada", ListaChamada);
		}

		return new ModelAndView("chamada");
	}

	private List<ListaChamada> buscaChamada(Disciplina d) throws ClassNotFoundException, SQLException {

		List<ListaChamada> chamadas = new ArrayList<>();
		List<Object[]> objetos = new ArrayList<>();
		objetos = lRep.buscaChamada(d.getCodDisciplina());
		for(Object[] row : objetos) {
			ListaChamada listaChamada = new ListaChamada();
			Aluno aluno = new Aluno();
			Matricula matricula = new Matricula();
			Disciplina disciplina = new Disciplina();
			
			listaChamada.setDataChamada((String) row[0].toString());
			disciplina.setDisciplina((String) row[1].toString());
			matricula.setAnoSemestre((Integer) row[2]);
			aluno.setCpf((String) row[3].toString());
			disciplina.setCodDisciplina((Integer) row[4]);
			
			matricula.setAluno(aluno);
			matricula.setDisciplina(disciplina);
			
			listaChamada.setMatricula(matricula);
			
			chamadas.add(listaChamada);
		}
		return chamadas;
	}

	private List<Disciplina> buscaDisciplina(Professor p) throws ClassNotFoundException, SQLException {
		
		List<Disciplina> disciplinas = new ArrayList<>();
		List<Object[]> objetos = new ArrayList<>();
		objetos = lRep.buscaDisciplina(p.getCodProfessor());
		for(Object[] row : objetos) {
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
