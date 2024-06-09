package guilherme.gustavo.TrabalhoBdSpringData.controller;

import java.sql.SQLException;
import java.time.LocalDate;
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
import guilherme.gustavo.TrabalhoBdSpringData.model.Avaliacao;
import guilherme.gustavo.TrabalhoBdSpringData.repository.IAlunoRespository;
import guilherme.gustavo.TrabalhoBdSpringData.repository.INotasRepository;

@Controller
public class NotasParciaisAlunoController {

	@Autowired
	private INotasRepository nRep;

	@Autowired
	private IAlunoRespository aRep;

	@RequestMapping(name = "notasAluno", value = "/notasAluno", method = RequestMethod.GET)
	public ModelAndView notasParciaisGet(ModelMap model) {
		return new ModelAndView("notasAluno");
	}

	@RequestMapping(name = "notasAluno", value = "/notasAluno", method = RequestMethod.POST)
	public ModelAndView notasParciaisPost(@RequestParam Map<String, String> param, ModelMap model) {

		String cmd = param.get("botao");
		String pesquisaCpf = param.get("pesquisaCpf");

		List<Avaliacao> avaliacoes = new ArrayList<>();
		List<Object> objetosAvaliacoes = new ArrayList<>();
		Aluno aluno = new Aluno();
		String saida = "";
		String erro = "";

		if (pesquisaCpf.trim().isEmpty()) {
			erro = "Por favor, digite um cpf";
		}

		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("notasAluno");
		}

		if (cmd.equalsIgnoreCase("Pesquisar CPF")) {
			aluno.setCpf(pesquisaCpf);
		}

		try {
			if (cmd.equalsIgnoreCase("Pesquisar CPF")) {
				if (validaCPF(aluno) == 1) {
					objetosAvaliacoes = buscaAvaliacoes(aluno);
				}
			}
		} catch (Exception e) {
			erro = trataErro(e.getMessage());
		} finally {
			model.addAttribute("erro", erro);
			model.addAttribute("saida", saida);
			model.addAttribute("objetosAvaliacoes", objetosAvaliacoes);
		}

		return new ModelAndView("notasAluno");
	}

	private int validaCPF(Aluno aluno) {
		return aRep.sp_consultaCpf(aluno.getCpf());
	}

	private List<Object> buscaAvaliacoes(Aluno aluno) throws SQLException, ClassNotFoundException {
		return nRep.buscaAvaliacoes(aluno.getCpf(), Integer.parseInt(anoSemestre()));
	}

	private String trataErro(String erro) {
		if (erro.contains("CPF inexistente")) {
			return "CPF inexistente";
		}
		if (erro.contains("CPF invalido, todos os digitos sao iguais")) {
			return "CPF invalido, todos os digitos sao iguais";
		}
		if (erro.contains("CPF invalido, numero de caracteres incorreto")) {
			return "CPF invalido, numero de caracteres incorreto";
		}
		return erro;
	}

	private String anoSemestre() {

		LocalDate hoje = LocalDate.now();
		int anoAtual = hoje.getYear();
		int mesAtual = hoje.getMonthValue();
		int semestreAtual = mesAtual <= 6 ? 1 : 2;

		return anoAtual + "" + semestreAtual;

	}
}