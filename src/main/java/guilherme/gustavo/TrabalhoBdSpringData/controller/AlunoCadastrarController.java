package guilherme.gustavo.TrabalhoBdSpringData.controller;

import java.sql.SQLException;
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
import guilherme.gustavo.TrabalhoBdSpringData.model.Curso;
import guilherme.gustavo.TrabalhoBdSpringData.repository.IAlunoRespository;

@Controller
public class AlunoCadastrarController {

	@Autowired
	private IAlunoRespository aRep;
	
	@RequestMapping(name = "alunoCadastrar", value = "/alunoCadastrar", method = RequestMethod.GET)
	public ModelAndView alunoCadastrarGet(ModelMap model) {
		return new ModelAndView("alunoCadastrar");
	}
	
	@RequestMapping(name = "alunoCadastrar", value = "/alunoCadastrar", method = RequestMethod.POST)
	public ModelAndView alunoCadastrarPost(@RequestParam Map<String, String> param, ModelMap model ) {
		String cmd = param.get("botao");
		String codCurso = param.get("codCurso");
		String cpf = param.get("cpf");
		String nome = param.get("nome");
		String dataNascimento = param.get("dataNascimento");
		String nomeSocial = param.get("nomeSocial");
		String email = param.get("email");
		String dataConclusao2Grau = param.get("dataConclusao2Grau");
		String instituicaoConclusao2Grau = param.get("instituicaoConclusao2Grau");
		String pontuacaoVestibular = param.get("pontuacaoVestibular");
		String posicaoVestibular = param.get("posicaoVestibular");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		String saida = "";
		String erro = "";

		Aluno a = new Aluno();
		List<Aluno> alunos = new ArrayList<>();
		
		if (cmd.contains("Buscar")) {
			if (cpf.trim().isEmpty()) {
				erro = "Por favor, informe o CPF.";
			}
		} else if (cmd.contains("Cadastrar") || cmd.contains("Alterar")) {
			if (codCurso.trim().isEmpty() || cpf.trim().isEmpty() || nome.trim().isEmpty()
					|| dataNascimento.trim().isEmpty() || email.trim().isEmpty() || dataConclusao2Grau.trim().isEmpty()
					|| instituicaoConclusao2Grau.trim().isEmpty() || pontuacaoVestibular.trim().isEmpty()
					|| posicaoVestibular.trim().isEmpty()) {

				erro = "Por favor, preencha todos os campos obrigatorios.";
			}
		}
		
		if (!erro.isEmpty()) {
			model.addAttribute("erro", erro);
			return new ModelAndView("alunoCadastrar");
		}
		
		if ((cmd.contains("Cadastrar") || cmd.contains("Alterar"))) {

			Curso c = new Curso();

			c.setCodigo(Integer.parseInt(codCurso));
			a.setCurso(c);

			a.setCpf(cpf);
			a.setNome(nome);

			a.setAnoIngresso(LocalDate.now().getYear());

			int mesAtual = LocalDate.now().getMonthValue();

			if (mesAtual >= 1 && mesAtual <= 6) {
				a.setSemestreIngresso(1);
				a.setSemestreLimite(1);
			} else {
				a.setSemestreIngresso(2);
				a.setSemestreLimite(2);
			}

			LocalDate dataNascLocalDate = LocalDate.parse(dataNascimento, formatter);
			a.setDataNascimento(dataNascLocalDate);

			a.setNomeSocial(nomeSocial);
			a.setEmail(email);

			LocalDate dataConclusao2GrauLocalDate = LocalDate.parse(dataConclusao2Grau, formatter);
			a.setDataConclusao2Grau(dataConclusao2GrauLocalDate);

			a.setInstituicao2Grau(instituicaoConclusao2Grau);

			a.setPontuacaoVestibular(Integer.parseInt(pontuacaoVestibular));
			a.setPosicaoVestibular(Integer.parseInt(posicaoVestibular));

		} else if (cmd.contains("Buscar")) {
			a.setCpf(cpf);
		}

		try {
			if (a.getCpf() != null) {
				if (a.getCpf().length() == 11) {
					if (cmd.contains("Cadastrar")) {
						saida = cadastrarAluno(a);
						a = null;
					}
					if (cmd.contains("Alterar")) {
						saida = atualizarAluno(a);
						a = null;
					}
					if (cmd.contains("Buscar")) {
						if (verificaCpf(a) == 1) {
							a = buscarAluno(a);
							if(a == null) {
								erro = "CPF Inexistente";
							}
						}
					}
				} else {
					erro = "Tamanho de CPF invalido";
				}
			}

		} catch (Exception e) {
			erro = trataErro(e.getMessage());
			if (erro.contains("verificaDataConclusao")) {
				erro = "A data de conclusao deve ser maior que a data de nascimento";
			}
		} finally {
			model.addAttribute("saida", saida);
			model.addAttribute("erro", erro);
			model.addAttribute("aluno", a);
			model.addAttribute("alunos", alunos);

		}

		return new ModelAndView("alunoCadastrar");
	}

	private String cadastrarAluno(Aluno aluno) throws Exception {
		String saida = aRep.sp_iuAluno(
	            "I",
	            aluno.getCpf(),
	            aluno.getCurso().getCodigo(),
	            aluno.getNome(),
	            aluno.getNomeSocial(),
	            aluno.getDataNascimento().toString(),
	            aluno.getEmail(),
	            aluno.getDataConclusao2Grau().toString(),
	            aluno.getInstituicao2Grau(),
	            aluno.getPontuacaoVestibular(),
	            aluno.getPosicaoVestibular(),
	            aluno.getAnoIngresso(),
	            aluno.getSemestreIngresso(),
	            aluno.getSemestreLimite()
	        );
		
		return saida;
	}

	private String atualizarAluno(Aluno aluno) throws Exception {
		String saida = aRep.sp_iuAluno(
	            "U",
	            aluno.getCpf(),
	            aluno.getCurso().getCodigo(),
	            aluno.getNome(),
	            aluno.getNomeSocial(),
	            aluno.getDataNascimento().toString(),
	            aluno.getEmail(),
	            aluno.getDataConclusao2Grau().toString(),
	            aluno.getInstituicao2Grau(),
	            aluno.getPontuacaoVestibular(),
	            aluno.getPosicaoVestibular(),
	            aluno.getAnoIngresso(),
	            aluno.getSemestreIngresso(),
	            aluno.getSemestreLimite()
	        );
		return saida;
	}

	private Aluno buscarAluno(Aluno a) throws Exception {
		a = aRep.findById(a.getCpf()).orElse(null);
		return a;
	}

	private int verificaCpf(Aluno a) throws Exception{
		return aRep.sp_consultaCpf(a.getCpf());
	}

	private String trataErro(String erro) {
		if (erro.contains("CPF ja cadastrado")){
			return "CPF ja cadastrado";
		}
		if (erro.contains("O codigo do curso e invalido")){
			return "O codigo do curso e invalido";
		}
		if (erro.contains("A idade e menor que 16 anos")){
			return "A idade e menor que 16 anos";
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

