<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>Sistema AGIS - Secretaria Acadêmica</title>
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styleSecretariaCurso.css"/>'>
</head>

<body>

	<script src="./resources/js/header.js"></script>

	<main>

		<h1>Historico</h1>

		<div align="center">
			<c:if test="${not empty erro }">
				<h2>
					<b> <c:out value="${erro }" />
					</b>
				</h2>
			</c:if>
		</div>

		<div align="center">
			<c:if test="${not empty saida }">
				<h3>
					<b> <c:out value="${saida }" />
					</b>
				</h3>
			</c:if>
		</div>

		 <div class="conteudoHistorico">
			<form action="historico" method="post">

				<div class="cpfHistorico">
					<div class="pesquisaCpf">
						<input type="text" name="pesquisaRa" id="pesquisaRa"
							name="pesquisaRa" placeholder="RA" class="inputCPF"> <input
							type="submit" value="Pesquisa Ra" class="btnCPF" id="botao"
							name="botao">
					</div>
				</div>

			</form>
			<c:if test="${matricula.aluno.ra ne null}">
			<h3>Informacoes sobre o(a) Aluno(a)</h3>
			<table class="tabela-centrada">
				<thead>
					<tr>
						<th>Ra</th>
						<th>Nome</th>
						<th>Curso</th>
						<th>Data 1° Matricula</th>
						<th>Pont. Vestibular</th>
						<th>Pos. Vestibular</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><c:out value="${matricula.aluno.ra}" /></td>
						<td><c:out value="${matricula.aluno.nome}" /></td>
						<td><c:out value="${matricula.aluno.curso.nome}" /></td>
						<td><c:out value="${matricula.dataMatricula}" /></td>
						<td><c:out value="${matricula.aluno.pontuacaoVestibular}" /></td>
						<td><c:out value="${matricula.aluno.posicaoVestibular}" /></td>
					</tr>
				</tbody>
			</table>
			</c:if>

			<c:if test="${not empty matriculas}">
				<h3>Histórico</h3>
				<table class="tabela-centrada">
					<thead>
						<tr>
							<th>Codigo</th>
							<th>Disciplina</th>
							<th>Professor</th>
							<th>Nota Final</th>
							<th>Qtd. Faltas</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="m" items="${matriculas}"> 
							<tr>
								<td><c:out value="${m.disciplina.codDisciplina}" /></td>
								<td><c:out value="${m.disciplina.disciplina}" /></td>
								<td><c:out value="${m.disciplina.professor.nome}" /></td>
								<td><c:out value="${m.nota}" /></td>
								<td><c:out value="${m.qtdFaltas}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
		</div>

	</main>

	<script src="./resources/js/navegacao.js"></script>

</body>

</html>