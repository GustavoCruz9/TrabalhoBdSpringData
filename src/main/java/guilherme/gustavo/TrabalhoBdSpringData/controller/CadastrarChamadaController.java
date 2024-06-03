package guilherme.gustavo.TrabalhoBdSpringData.controller;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import guilherme.gustavo.TrabalhoBdSpringData.repository.IListaChamadaRepository;

@Controller
public class CadastrarChamadaController {

	@Autowired
	IListaChamadaRepository lRep;

	@RequestMapping(name = "cadastrarChamada", value = "/cadastrarChamada", method = RequestMethod.GET)
	public ModelAndView cadastrarChamadaGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("cadastrarChamada");
	}

	@RequestMapping(name = "cadastrarChamada", value = "/cadastrarChamada", method = RequestMethod.POST)
	public ModelAndView cadastrarChamadaPost(@RequestParam Map<String, String> param,
			ModelMap model) {

		String codDisciplina = param.get("codDisciplina");
		String cmd = param.get("botao");
		String dataChamada = param.get("dataChamada");
		

		String saida = "";
		String erro = "";
		String horasSemanais = "";

		List<Matricula> matriculas = new ArrayList<>();
		List<ListaChamada> listaChamada = new ArrayList<>();
		Disciplina d = new Disciplina();

		d.setCodDisciplina(Integer.parseInt(codDisciplina));

		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("cadastrarChamada");
		}

		try {
			if (cmd.contains("Listar Alunos")) {
				if (!dataChamada.trim().isEmpty()) {
					matriculas = buscarAlunos(d);
					if (matriculas.isEmpty()) {
						erro = "Nao existem alunos matriculados nessa materia";
					}

					horasSemanais = matriculas.get(0).getDisciplina().getHorasSemanais();

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

					LocalDate dataChamadaLocalDate = LocalDate.parse(dataChamada, formatter);
					DayOfWeek diaDaSemana = dataChamadaLocalDate.getDayOfWeek();

					if (!traduzDiaSemana(diaDaSemana).equals(matriculas.get(0).getDisciplina().getDiaSemana())) {
						erro = "A data escolhida deve ser no dia da semana equivalente ao da disciplina";
						matriculas = new ArrayList<>();
					}
				} else {
					erro = "Insira a data da Chamada";
				}
			}
			
			if (cmd.contains("Cadastrar Chamada")) {
				List<Matricula> matriculasAux = new ArrayList<>();
				matriculasAux = buscarAlunos(d);
				while(!matriculasAux.isEmpty()) {
					int presencas = 0;
					int ausencias = 0;
					String raAluno = matriculasAux.get(0).getAluno().getRa();
					String checkboxAula1 = param.get("checkboxAula1_"+raAluno);
					String checkboxAula2 = param.get("checkboxAula2_"+raAluno);
					String checkboxAula3 = param.get("checkboxAula3_"+raAluno);
					String checkboxAula4 = param.get("checkboxAula4_"+raAluno);
					
					if(checkboxAula1 == null) {
						checkboxAula1 = "0";
						ausencias++;
					} else {
						presencas++;
					}
					if(checkboxAula2 == null) {
						checkboxAula2 = "0";
						ausencias++;
					} else {
						presencas++;
					}
					if(checkboxAula3 == null) {
						checkboxAula3 = "0";
						ausencias++;
					} else {
						presencas++;
					}
					if(checkboxAula4 == null) {
						checkboxAula4 = "0";
						ausencias++;
					} else {
						presencas++;
					}
					
					Aluno aluno = new Aluno();
					Matricula matricula = new Matricula();
					ListaChamada lc = new ListaChamada();
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

					LocalDate dataChamadaLocalDate = LocalDate.parse(dataChamada, formatter);
					
					aluno.setCpf(matriculasAux.get(0).getAluno().getCpf());
					
					matricula.setAluno(aluno);
					matricula.setDisciplina(d);
					matricula.setAnoSemestre(matriculasAux.get(0).getAnoSemestre());
					
					lc.setMatricula(matricula);
					lc.setAula1(checkboxAula1);
					lc.setAula2(checkboxAula2);
					lc.setAula3(checkboxAula3);
					lc.setAula4(checkboxAula4);
					lc.setAusencia(ausencias);
					lc.setPresenca(presencas);
					lc.setDataChamada(dataChamadaLocalDate.toString());
					
					listaChamada.add(lc);
					
					matriculasAux.remove(0);
					cadastraChamada(lc);
				}
				
				saida = "Chamada cadastrada com sucesso";
			}
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("matriculas", matriculas);
			model.addAttribute("horasSemanais", horasSemanais);
			model.addAttribute("dataChamada", dataChamada);
		}

		return new ModelAndView("cadastrarChamada");
	}

	private void cadastraChamada(ListaChamada listaChamada) throws SQLException, ClassNotFoundException {
		lRep.cadastrarChamada(listaChamada.getDataChamada(), listaChamada.getMatricula().getAnoSemestre(),
				listaChamada.getMatricula().getAluno().getCpf(), listaChamada.getMatricula().getDisciplina().getCodDisciplina(),
				listaChamada.getPresenca(), listaChamada.getAusencia(), listaChamada.getAula1(), listaChamada.getAula2(),
				listaChamada.getAula3(), listaChamada.getAula4()); 
	}

	private List<Matricula> buscarAlunos(Disciplina d) throws SQLException, ClassNotFoundException {
		
		List<Object[]> objetos = new ArrayList<>();
		List<Matricula> matriculas = new ArrayList<>();
		
		objetos = lRep.buscarAlunos(d.getCodDisciplina());
		
		if(objetos.size() != 0) {
			for(Object[] row : objetos) {
				Aluno aluno = new Aluno();
				Disciplina disciplina = new Disciplina();
				Matricula matricula = new Matricula();
				
				aluno.setNome((String) row[0].toString());
				aluno.setRa((String) row[1].toString());
				aluno.setCpf((String) row[2].toString());
				
				disciplina.setDisciplina((String) row[3].toString());
				disciplina.setCodDisciplina((Integer) row[4]);
				disciplina.setHorasSemanais((String) row[5].toString());
				disciplina.setDiaSemana((String) row[6].toString());
				
				matricula.setAnoSemestre((Integer) row[7]);
				
				matricula.setDisciplina(disciplina);
				matricula.setAluno(aluno);
				
				matriculas.add(matricula);
				
			}	
		}
		
		return matriculas;
	}

	private String traduzDiaSemana(DayOfWeek diaDaSemana) {
		switch (diaDaSemana) {
		case MONDAY:
			return "Segunda-feira";
		case TUESDAY:
			return "Terça-feira";
		case WEDNESDAY:
			return "Quarta-feira";
		case THURSDAY:
			return "Quinta-feira";
		case FRIDAY:
			return "Sexta-feira";
		case SATURDAY:
			return "Sábado";
		case SUNDAY:
			return "Domingo";
		default:
			return "Dia inválido";
		}
	}

}
