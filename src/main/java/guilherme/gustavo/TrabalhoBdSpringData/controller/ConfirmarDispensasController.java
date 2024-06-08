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
public class ConfirmarDispensasController {

	@Autowired
	private IDispensaRepository dRep;

	@RequestMapping(name = "confirmarDispensa", value = "/confirmarDispensa", method = RequestMethod.GET)
	public ModelAndView confirmarDispensaGet(@RequestParam Map<String, String> param, ModelMap model) {

		String cpf = param.get("cpf");

		String saida = "";
		String erro = "";

		List<Dispensa> dispensas = new ArrayList<>();

		if (cpf == null) {
			try {
				dispensas = listarDispensas();
				if (dispensas.isEmpty()) {
					erro = "Nao existe solicitacoes de dispensa no momento";
				}
			} catch (Exception e) {
				erro = trataErro(e.getMessage());
			} finally {
				model.addAttribute("saida", saida);
				model.addAttribute("erro", erro);
				model.addAttribute("dispensas", dispensas);
				model.addAttribute("cpf", cpf);
			}
		}

		return new ModelAndView("confirmarDispensa");
	}

	@RequestMapping(name = "confirmarDispensa", value = "/confirmarDispensa", method = RequestMethod.POST)
	public ModelAndView confirmarDispensaPost(@RequestParam Map<String, String> param, ModelMap model) {
		
		String cpf = param.get("cpf");
		String codDisciplina = param.get("codDisciplina");
		String ra = param.get("ra");
		String cmd = param.get("botao");
		
		String saida = "";
		String erro = "";	
		List<Dispensa> dispensas = new ArrayList<>();
		
	
		if(cmd.contains("Pesquisa Cpf")) {
			if(cpf.trim().isEmpty()) {
				erro = "Por favor, informe o Cpf";
			}
		}
		
		if (!erro.isEmpty()) {
			try {
				dispensas = listarDispensas();
				if(dispensas.isEmpty()) {
					erro = "Nao existe solicitacoes de dispensa no momento";
				}
			} catch (Exception e) {
				erro = trataErro(e.getMessage());
			} finally {
				model.addAttribute("erro", erro);
				model.addAttribute("dispensas", dispensas);

			}
			
			return new ModelAndView("confirmarDispensa");
		}
			
		
		Dispensa disp = new Dispensa();
		Disciplina d = new Disciplina();
		Aluno a = new Aluno();
		

		if(cmd.contains("Aceitar") || cmd.contains("Negar")) {
			
			a.setRa(ra);
			disp.setAluno(a);
			
			d.setCodDisciplina(Integer.parseInt(codDisciplina));
			disp.setDisciplina(d);
			
			if(cmd.contains("Aceitar")) {
				disp.setStatusDispensa("Deferido");
			}else {
				disp.setStatusDispensa("Indeferido");
			}
			
		}
		
		if(cmd.contains("Pesquisa Cpf")) {
			a.setCpf(cpf);
		}
		
		try {
			
			if (a.getCpf() != null) {
				if (a.getCpf().length() == 11) {
					if(verificaCpf(a) == 1) {
						if(verificaCpfSeExiste(a) == 0) {
							dispensas = listarDispensasComCpf(a);
							if(dispensas.isEmpty()) {
								erro = "Nao existe solicitacoes de dispensa no momento";
							}
						}else {
							erro = "Cpf nao cadastrado";
						}
					}
				}else {
					erro = "Tamanho de Cpf invalido";
				}
			}
			
			if(cmd.contains("Aceitar") || cmd.contains("Negar")) {
				saida = cofirmarDispensa(disp);
				dispensas = listarDispensas();
				if(dispensas.isEmpty()) {
					erro = "Nao existe solicitacoes de dispensa no momento";
				}
			}

		} catch (Exception e) {
			erro = trataErro(e.getMessage());
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("dispensas", dispensas);
		}
		
		
		return new ModelAndView("confirmarDispensa");
	}

	private int verificaCpfSeExiste(Aluno a) throws Exception {
		return dRep.sp_validaCpfDuplicado(a.getCpf());
	}
	
	private int verificaCpf(Aluno a) throws Exception {
		return dRep.sp_consultaCpf(a.getCpf());
	}

	private List<Dispensa> listarDispensasComCpf(Aluno a) throws Exception {
		List<Dispensa> dispensas = new ArrayList<>();
		List<Object[]> objetos = dRep.listarDispensasComCpf(a.getCpf());
		
		for(Object[] row: objetos) {
			Disciplina d = new Disciplina();
			Dispensa disp = new Dispensa();
			a = new Aluno();
			
			a.setRa((String) row[0]);
			a.setNome((String) row[1]);
			disp.setDataSolicitacao((String) row[2]);
			d.setDisciplina((String) row[3]);
			disp.setInstituicao((String) row[4]);
			d.setCodDisciplina((Integer) row[5]);
			
			disp.setAluno(a);
			disp.setDisciplina(d);
			
			dispensas.add(disp);
		}
		
		return dispensas;
	}

	private String cofirmarDispensa(Dispensa disp) throws Exception {
		String saida = dRep.sp_atualizaDispensa(disp.getAluno().getRa(), disp.getDisciplina().getCodDisciplina(), disp.getStatusDispensa());
		return saida;
	}

	private List<Dispensa> listarDispensas() throws Exception {
		List<Dispensa> dispensas = new ArrayList<>();
		List<Object[]> objetos = dRep.listarDispensas();
		
		for(Object[] row: objetos) {
			Disciplina d = new Disciplina();
			Dispensa disp = new Dispensa();
			Aluno a = new Aluno();
			
			a.setRa((String) row[0]);
			a.setNome((String) row[1]);
			disp.setDataSolicitacao((String) row[2]);
			d.setDisciplina((String) row[3]);
			disp.setInstituicao((String) row[4]);
			d.setCodDisciplina((Integer) row[5]);
			
			disp.setAluno(a);
			disp.setDisciplina(d);
			
			dispensas.add(disp);
		}
		
		return dispensas;
	}
	
	private String trataErro(String erro) {
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