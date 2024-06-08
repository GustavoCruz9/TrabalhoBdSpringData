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
import guilherme.gustavo.TrabalhoBdSpringData.repository.IAlunoRespository;

@Controller
public class AlunoListarController {

	@Autowired
	private IAlunoRespository aRep;

	@RequestMapping(name = "alunoListar", value = "/alunoListar", method = RequestMethod.GET)
	public ModelAndView alunoListarGet(ModelMap model) {
		return new ModelAndView("alunoListar");
	}

	@RequestMapping(name = "alunoListar", value = "/alunoListar", method = RequestMethod.POST)
	public ModelAndView alunoListarPost(@RequestParam Map<String, String> param, ModelMap model) {
		String cmd = param.get("botao");

		String saida = "";
		String erro = "";

		List<Aluno> alunos = new ArrayList<>();

		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("alunoListar");
		}

		try {
			if (cmd.contains("Listar")) {
				alunos = listarAlunos();
				if (alunos.isEmpty()) {
					erro = "Nao existem alunos cadastrados";
				}
			}

		} catch (Exception e) {
			erro = e.getMessage();
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("alunos", alunos);
		}
		
		return new ModelAndView("alunoListar");
	}

	private List<Aluno> listarAlunos() throws Exception {
		List<Aluno> alunos = new ArrayList<>();
		alunos = aRep.findAll();
		return alunos;
	}
}
