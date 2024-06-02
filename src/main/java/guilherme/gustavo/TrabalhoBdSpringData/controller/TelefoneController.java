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
		List<Aluno> alunos = new ArrayList<>();
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
		
		tel.setNumero(telefoneNovo);
		telefones.add(tel);
		a.setTelefones(telefones);
		a.setCpf(cpf);

		if (cmd.contains("Confirmar")) {
			tel = new Telefone();
			tel.setNumero(telefoneAntigo);
		}

		try {
			if (cmd.contains("Cadastrar")) {
				saida = cadastrarTelefone(a, tel);
				tel = null;
				a = null;
			}
			if (cmd.contains("Confirmar")) {
				saida = atualizarTelefone(a, tel);
				tel = null;
				a = null;
			}
			if (cmd.contains("Excluir")) {
				saida = excluirTelefone(a, tel);
				tel = null;
				a = null;
			}
			if (cmd.contains("Listar")) {
				alunos = listarTelefones();
				if (alunos.isEmpty()) {
					erro = "Nao existe nenhum telefone cadastrado";
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno", a);
			model.addAttribute("alunos", alunos);
		}

		return new ModelAndView("telefone");
	}
	
	private String cadastrarTelefone(Aluno a, Telefone telefoneAntigo) throws SQLException, ClassNotFoundException {
		String saida = tRep.sp_iudTelefone("I", a.getCpf(), telefoneAntigo.getNumero(), a.getTelefones().get(0).getNumero());
		return saida;
	}

	private String atualizarTelefone(Aluno a, Telefone telefoneAntigo) throws SQLException, ClassNotFoundException {
		String saida = tRep.sp_iudTelefone("U", a.getCpf(), telefoneAntigo.getNumero(), a.getTelefones().get(0).getNumero());
		return saida;
	}

	private String excluirTelefone(Aluno a, Telefone telefoneAntigo) throws SQLException, ClassNotFoundException {
		String saida = tRep.sp_iudTelefone("D", a.getCpf(), telefoneAntigo.getNumero(), a.getTelefones().get(0).getNumero());
		return saida;
	}

	private List<Aluno> listarTelefones() throws SQLException, ClassNotFoundException {
		List<Aluno> alunos = new ArrayList<>();
		alunos = tRep.fn_listarTelefones();
		return alunos;
	}

}
