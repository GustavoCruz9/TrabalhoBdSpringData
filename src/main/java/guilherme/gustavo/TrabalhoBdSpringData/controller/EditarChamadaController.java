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
public class EditarChamadaController {
	
	@Autowired
	private IListaChamadaRepository lRep;

	@RequestMapping(name = "editarChamada", value = "/editarChamada", method = RequestMethod.GET)
	public ModelAndView editarChamadaGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("editarChamada");
	}
	
	@RequestMapping(name = "editarChamada", value = "/editarChamada", method = RequestMethod.POST)
	public ModelAndView editarChamadaPost(@RequestParam Map<String, String> param, ModelMap model) {
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
			return new ModelAndView("editarChamada");
		}
		
		try {
			if (cmd.contains("Listar Alunos")) {
				if (!dataChamada.trim().isEmpty()) {
					listaChamada = buscarAlunos(d, dataChamada);
					if (listaChamada.isEmpty()) {
						erro = "Nao existem alunos matriculados nessa materia";
					}

					horasSemanais = listaChamada.get(0).getMatricula().getDisciplina().getHorasSemanais().toString();

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

					LocalDate dataChamadaLocalDate = LocalDate.parse(dataChamada, formatter);
					DayOfWeek diaDaSemana = dataChamadaLocalDate.getDayOfWeek();
					
					if (!traduzDiaSemana(diaDaSemana).equals(listaChamada.get(0).getMatricula().getDisciplina().getDiaSemana())) {
						erro = "A data escolhida deve ser no dia da semana equivalente ao da disciplina";
						listaChamada = new ArrayList<>();
					}
				} else {
					erro = "Insira a data da Chamada";
				}
			}
			
			if (cmd.contains("Editar Chamada")) {
				List<ListaChamada> listaChamadaAux = new ArrayList<>();
				listaChamadaAux = buscarAlunos(d, dataChamada);
				while(!listaChamadaAux.isEmpty()) {
					int presencas = 0;
					int ausencias = 0;
					String raAluno = listaChamadaAux.get(0).getMatricula().getAluno().getRa();
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
					
					aluno.setCpf(listaChamadaAux.get(0).getMatricula().getAluno().getCpf());
					aluno.setRa(listaChamadaAux.get(0).getMatricula().getAluno().getRa());
					aluno.setNome(listaChamadaAux.get(0).getMatricula().getAluno().getNome());
					matricula.setAluno(aluno);
					horasSemanais = listaChamadaAux.get(0).getMatricula().getDisciplina().getHorasSemanais().toString();
					matricula.setDisciplina(d);
					matricula.setAnoSemestre(listaChamadaAux.get(0).getMatricula().getAnoSemestre());
					
					
					lc.setMatricula(matricula);
					lc.setAula1(checkboxAula1);
					lc.setAula2(checkboxAula2);
					lc.setAula3(checkboxAula3);
					lc.setAula4(checkboxAula4);
					lc.setAusencia(ausencias);
					lc.setPresenca(presencas);
					lc.setDataChamada(dataChamadaLocalDate.toString());
					
					listaChamada.add(lc);
					
					listaChamadaAux.remove(0);
					editarChamada(lc);
				}
				
				saida = "Chamada atualizada com sucesso";
			}
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("listaChamada", listaChamada);
			model.addAttribute("horasSemanais", horasSemanais);
			model.addAttribute("dataChamada", dataChamada);
		}
		
		return new ModelAndView("editarChamada");
	}

	private Object traduzDiaSemana(DayOfWeek diaDaSemana) {
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

	private List<ListaChamada> buscarAlunos(Disciplina d, String dataChamada) throws SQLException, ClassNotFoundException{
		List<ListaChamada> chamadas = new ArrayList<>();
		List<Object[]> objetos = new ArrayList<>();
		objetos = lRep.buscarAlunosEditarChamada(d.getCodDisciplina(), dataChamada);
		for(Object[] row : objetos) {
			ListaChamada listaChamada = new ListaChamada();
			Aluno aluno = new Aluno();
			Matricula matricula = new Matricula();
			Disciplina disciplina = new Disciplina();
			
			listaChamada.setDataChamada((String) row[0].toString());
			matricula.setAnoSemestre((Integer) row[1]);
			aluno.setCpf((String) row[2].toString());
			aluno.setNome((String) row[3].toString()) ;
			disciplina.setCodDisciplina((Integer) row[4]);
			disciplina.setHorasSemanais((String) row[5].toString());
			disciplina.setDisciplina((String) row[6].toString());
			disciplina.setDiaSemana((String) row[7].toString());
			listaChamada.setPresenca((Integer) row[8]);
			listaChamada.setAusencia((Integer) row[9]);
			listaChamada.setAula1((String) row[10].toString());
			listaChamada.setAula2((String) row[11].toString());
			listaChamada.setAula3((String) row[12].toString());
			listaChamada.setAula4((String) row[13].toString());
			aluno.setRa((String) row[14].toString());
			
			matricula.setAluno(aluno);
			matricula.setDisciplina(disciplina);
			
			listaChamada.setMatricula(matricula);
			
			chamadas.add(listaChamada);
		}
		return chamadas;
	}

	private void editarChamada(ListaChamada lc) throws SQLException, ClassNotFoundException{
		lRep.atualizaChamada(lc.getPresenca(), lc.getAusencia(), lc.getAula1(), lc.getAula2(), lc.getAula3(), 
				lc.getAula4(), lc.getMatricula().getDisciplina().getCodDisciplina(), lc.getMatricula().getAluno().getCpf(),
				lc.getDataChamada());
	}
	
}
