package guilherme.gustavo.TrabalhoBdSpringData.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import guilherme.gustavo.TrabalhoBdSpringData.model.Aluno;
import guilherme.gustavo.TrabalhoBdSpringData.model.Avaliacao;
import guilherme.gustavo.TrabalhoBdSpringData.model.Disciplina;
import guilherme.gustavo.TrabalhoBdSpringData.model.PesoAvaliacao;
import guilherme.gustavo.TrabalhoBdSpringData.repository.IAlunoRespository;

public class NotasParciaisAlunoController {
	
	@Autowired
	private IAlunoRespository aRep;

	@RequestMapping(name = "notasAluno", value = "/notasAluno", method = RequestMethod.GET)
	public ModelAndView notasGet(ModelMap model) {
		return new ModelAndView("notasAluno");
	}
	
	@RequestMapping(name = "notasAluno", value = "/notasAluno", method = RequestMethod.POST)
	public ModelAndView notasPost(@RequestParam Map<String, String> param, ModelMap model) {
//		String cmd = param.get("botao");
//		String pesquisaCpf = param.get("pesquisaCpf");
//		
//		String saida = "";
//		String erro = "";
//		Aluno aluno = new Aluno();
//		
//		aluno.setCpf(pesquisaCpf);
//		
//		List<Avaliacao> avaliacoes = new ArrayList<>();
//		
//		try {
//			if(cmd.equalsIgnoreCase("Pesquisa CPF")) {
//				if(validaCPF(aluno) == 1) {
//					avaliacoes = buscaAvaliacoes(aluno);
//				}
//			}
//		}catch (Exception e) {
//			erro = e.getMessage();
//		} finally {
//			model.addAttribute("erro", erro);
//			model.addAttribute("saida", saida);
//			model.addAttribute("avaliacoes", avaliacoes);
//		}
		
		return new ModelAndView("notasAluno");
	}

	private int validaCPF(Aluno aluno) {
		return aRep.sp_consultaCpf(aluno.getCpf());
	}

	private List<Avaliacao> buscaAvaliacoes(Aluno aluno) throws SQLException, ClassNotFoundException {
		List<Object[]> objetos = aRep.buscaAvaliacoes(aluno.getCpf());
		List<Avaliacao> avaliacoes = new ArrayList<>();
		
		for(Object[] row : objetos) {
			PesoAvaliacao pesoAvaliacao = new PesoAvaliacao();
			Avaliacao avaliacao = new Avaliacao();
			Disciplina disciplina = new Disciplina();
			
			disciplina.setDisciplina((String) row[0].toString());
			pesoAvaliacao.setTipo((String) row[1].toString());
			avaliacao.setNota((Float) row[2]);
			
			pesoAvaliacao.setDisciplina(disciplina);
			avaliacao.setPesoAvaliacao(pesoAvaliacao);
			
			avaliacoes.add(avaliacao);
		}
		
		return avaliacoes;
	}
}
