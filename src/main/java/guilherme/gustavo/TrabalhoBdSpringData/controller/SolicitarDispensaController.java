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
import guilherme.gustavo.TrabalhoBdSpringData.model.Dispensa;
import guilherme.gustavo.TrabalhoBdSpringData.repository.IDispensaRepository;

@Controller
public class SolicitarDispensaController {

	@Autowired
	private IDispensaRepository dRep;
	
	@RequestMapping(name = "solicitarDispensa", value = "/solicitarDispensa", method = RequestMethod.GET)
	public ModelAndView solicitarDispensaGet(@RequestParam Map<String, String> param, ModelMap model) {

		String cmd = param.get("botao");
		String cpf = param.get("cpf");
		
		List<Dispensa> dispensas = new ArrayList<>();

		String saida = "";
		String erro = "";

		if (cpf != null) {

			List<Disciplina> disciplinas = new ArrayList<>();
			Aluno aluno = new Aluno();

			aluno.setCpf(cpf);

			try {
				if(buscaAluno(aluno) == 0) {
					
					if (cmd.contains("Buscar")) {
						disciplinas = popularDisciplinas(aluno);
					}
					
					if (cmd.contains("Listar Dispensas")) {
						
						dispensas = listarDispensas(cpf);
						disciplinas = popularDisciplinas(aluno);
						
						if (dispensas.isEmpty()) {
							erro = "Voce ainda nao solicitou nenhuma dispensa";
						}
					}	
				}else {
					erro = "CPF nao cadastrado";
				}

			} catch (Exception e) {
				erro = e.getMessage();
			} finally {
				model.addAttribute("saida", saida);
				model.addAttribute("erro", erro);
				model.addAttribute("disciplinas", disciplinas);
				model.addAttribute("dispensas", dispensas);
				model.addAttribute("cpf", cpf);
			}
		}

		return new ModelAndView("solicitarDispensa");
	}

	@RequestMapping(name = "solicitarDispensa", value = "/solicitarDispensa", method = RequestMethod.POST)
	public ModelAndView solicitarDispensaPost(@RequestParam Map<String, String> param, ModelMap model) {

		String cmd = param.get("botao");
		String cpf = param.get("cpf");
		String disciplinaInput = param.get("disciplina");
		String instituicao = param.get("instituicao");

		String saida = "";
		String erro = "";

		if (cmd.contains("Buscar")) {
			if (cpf.trim().isEmpty()) {
				erro = "Por favor, informe o CPF.";
			}
		} else if (cmd.contains("Solicitar")) {
			if (cpf.trim().isEmpty() || instituicao.trim().isEmpty()
					|| disciplinaInput.contains("Selecione a Disciplina")) {
				erro = "Por favor, preencha todos os campos obrigatorios.";
			}
		}

		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("solicitarDispensa");
		}

		List<Disciplina> disciplinas = new ArrayList<>();
		List<Dispensa> dispensas = new ArrayList<>();
		Aluno aluno = new Aluno();
		Disciplina disciplina = new Disciplina();
		Dispensa dispensa = new Dispensa();

		aluno.setCpf(cpf);

		if (cmd.contains("Solicitar")) {
			disciplina.setCodDisciplina(Integer.parseInt(disciplinaInput));

			dispensa.setDisciplina(disciplina);
			dispensa.setAluno(aluno);
			dispensa.setInstituicao(instituicao);
		}

		try {
			if (aluno.getCpf().length() == 11) {
				if (cmd.contains("Solicitar")) {
					saida = cadastrarDispensa(dispensa);
					disciplinas = popularDisciplinas(aluno);
				}
			} else {
				erro = "Tamanho de CPF invalido";
			}
		} catch (Exception e) {
			erro = trataErro(e.getMessage());
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("disciplinas", disciplinas);
			model.addAttribute("dispensas", dispensas);
		}

		return new ModelAndView("solicitarDispensa");
	}

	private List<Dispensa> listarDispensas(String cpf) throws Exception {
		List<Dispensa> dispensas = new ArrayList<>();
		List<Object[]> objetos = new ArrayList<>();
		objetos = dRep.listarDispensasComParam(cpf);
		for(Object[] row: objetos) {
			Disciplina disciplina = new Disciplina();
			Dispensa dispensa = new Dispensa();
			
			disciplina.setCodDisciplina((Integer) row[0]);
			dispensa.setDataSolicitacao((String) row[1].toString());
			dispensa.setInstituicao((String) row[2].toString());
			dispensa.setStatusDispensa((String) row[3].toString());
			dispensa.setDisciplina(disciplina);
			
			dispensas.add(dispensa);
		}
		
		return dispensas;
	}

	private String cadastrarDispensa(Dispensa dispensa) throws Exception {
		return dRep.sp_iDispensa(dispensa.getAluno().getCpf(), dispensa.getDisciplina().getCodDisciplina(), 
				dispensa.getInstituicao());
	}

	private int buscaAluno(Aluno aluno) throws Exception {
		int saida = dRep.sp_validaCpfDuplicado(aluno.getCpf());
		return saida;
	}

	private List<Disciplina> popularDisciplinas(Aluno aluno) throws Exception {
		List<Disciplina> disciplinas = new ArrayList<>();
		List<Object[]> objetos = new ArrayList<>();
		objetos = dRep.popularDisciplinas(aluno.getCpf());
		
		for(Object[] row: objetos) {
			Disciplina disciplina = new Disciplina();
			
			disciplina.setDisciplina((String) row[0].toString());
			disciplina.setCodDisciplina((Integer) row[1]);
			
			disciplinas.add(disciplina);
		}
		
		return disciplinas;
	}
	
	private String trataErro(String erro) {
		if (erro.contains("Cpf nao esta cadastrado")){
			return "Cpf nao esta cadastrado";
		}
		if (erro.contains("CPF inexistente")){
			return "CPF inexistente";
		}
		if (erro.contains("CPF invalido, todos os digitos sao iguais")){
			return "CPF invalido, todos os digitos sao iguais";
		}
		if (erro.contains("CPF invalido, numero de caracteres incorreto")){
			return "CPF invalido, numero de caracteres incorreto";
		}
		return erro;
	}

}
