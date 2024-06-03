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
import guilherme.gustavo.TrabalhoBdSpringData.model.Telefone;
import guilherme.gustavo.TrabalhoBdSpringData.repository.ITelefoneRepository;

@Controller
public class TelefoneController {
	
	@Autowired
	private ITelefoneRepository tRep;
	
	@RequestMapping(name = "telefone", value = "/telefone", method = RequestMethod.GET)
	public ModelAndView telefoneGet(@RequestParam Map<String, String> param, ModelMap model) {
		return new ModelAndView("telefone");
	}
	
	@RequestMapping(name = "telefone", value = "/telefone", method = RequestMethod.POST)
	public ModelAndView telefonePost(@RequestParam Map<String, String> param, ModelMap model) {
		String cmd = param.get("botao");
		String cpf = param.get("cpf");
		String telefoneNovo = param.get("telefoneNovo");
		String telefoneAntigo = param.get("telefoneAntigo");

		String saida = "";
		String erro = "";

		Aluno a = new Aluno();
		Telefone tel = new Telefone();
		Telefone telAntigo = new Telefone();
		List<Telefone> telefones = new ArrayList<>();

		if (cmd.contains("Confirmar")) {
			if (cpf.trim().isEmpty() || telefoneNovo.trim().isEmpty()) {
				erro = "CPF ou telefone vazios";
			} else if (telefoneAntigo.trim().isEmpty()) {
				erro = "Por favor, digite o telefone a ser atualizado";
			}
		} else if (cmd.contains("Cadastrar") || cmd.contains("Excluir")) {
			if (cpf.trim().isEmpty() || telefoneNovo.trim().isEmpty()) {
				erro = "CPF ou telefone vazios";
			}
		}

		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("telefone");
		}
		
		a.setCpf(cpf);
		tel.setNumero(telefoneNovo);
		tel.setAluno(a);

		if (cmd.contains("Confirmar")) {
			telAntigo.setNumero(telefoneAntigo);
		}

		try {
			if (cmd.contains("Cadastrar")) {
				saida = cadastrarTelefone(tel);
				tel = null;
				a = null;
			}
			if (cmd.contains("Confirmar")) {
				saida = atualizarTelefone(tel, telAntigo);
				tel = null;
				a = null;
			}
			if (cmd.contains("Excluir")) {
				saida = excluirTelefone(tel);
				tel = null;
				a = null;
			}
			if (cmd.contains("Listar") && cpf.trim().isEmpty()) {
				telefones = listarTelefones();
				if (telefones.isEmpty()) {
					erro = "Nao existe nenhum telefone cadastrado";
				}
			}
			if (cmd.contains("Listar") && !cpf.trim().isEmpty()) {
				telefones = listarTelefonesComParam(a);
				if (telefones.isEmpty()) {
					erro = "Nao existe nenhum telefone cadastrado para este Aluno";
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno", a);
			model.addAttribute("telefones", telefones);
		}

		return new ModelAndView("telefone");
	}
	
	private List<Telefone> listarTelefonesComParam(Aluno aluno) {
		List<Object[]> objetos = tRep.listarTelefonesComParam(aluno.getCpf());
		List<Telefone> telefones = new ArrayList<>();
		for(Object[] row: objetos) {
			Aluno a = new Aluno();
			Telefone t = new Telefone();
			
			a.setCpf((String) row[0]);
			a.setNome((String) row[1]);
			t.setNumero((String) row[2]);
			t.setAluno(a);
			
			telefones.add(t);
		} 
		return telefones;
	}

	private String cadastrarTelefone(Telefone telefone) throws SQLException, ClassNotFoundException {
		String saida = tRep.sp_iudTelefone("I", telefone.getAluno().getCpf(), null, telefone.getNumero());
		return saida;
	}

	private String atualizarTelefone(Telefone telefone, Telefone telefoneAntigo) throws SQLException, ClassNotFoundException {
		String saida = tRep.sp_iudTelefone("U", telefone.getAluno().getCpf(), telefoneAntigo.getNumero(), telefone.getNumero());
		return saida;
	}

	private String excluirTelefone(Telefone telefone) throws SQLException, ClassNotFoundException {
		String saida = tRep.sp_iudTelefone("D", telefone.getAluno().getCpf(), null, telefone.getNumero());
		return saida;
	}

	private List<Telefone> listarTelefones() throws SQLException, ClassNotFoundException {
		List<Object[]> objetos = tRep.fn_listarTelefones();
		List<Telefone> telefones = new ArrayList<>();
		for(Object[] row: objetos) {
			Aluno a = new Aluno();
			Telefone t = new Telefone();
			
			a.setCpf((String) row[0]);
			a.setNome((String) row[1]);
			t.setNumero((String) row[2]);
			t.setAluno(a);
			
			telefones.add(t);
		} 
		return telefones;
	}

}
