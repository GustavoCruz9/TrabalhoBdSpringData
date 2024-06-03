<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sistema AGIS - Aluno</title>
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styleMenuAluno.css"/>'>
</head>
<body>

	<script src="./resources/js/headerAluno.js"></script>
	
	<main>
		<h1>Cadastro de Matrícula</h1>

		<form action="matriculaAluno" method="get" class="formMatricula">
			<div class="cpfMatricula">
				<div class="pesquisaCpf">
					<input type="text" name="pesquisaRa" id="pesquisaRa"
						name="pesquisaRa" placeholder="RA" class="inputCPF"> <input
						type="submit" value="pesquisa Ra" class="btnCPF" id="botao" name="botao">
				</div>
			</div>
		</form>


		<form action="matriculaAluno" method="post" class="formMatricula">

			<input type="hidden" name="pesquisaRa" id="pesquisaRa"
				value="${param.pesquisaRa}"> <br>

			<div align="center">
				<c:if test="${not empty erro }">
					<h2>
						<b><c:out value="${erro }" /></b>
					</h2>
				</c:if>
			</div>

			<br />
			<div align="center">
				<c:if test="${not empty saida }">
					<h3>
						<b><c:out value="${saida }" /></b>
					</h3>
				</c:if>
			</div>

			<br> <br>

			<div class="diasSemana">
				<table class="segunda">
					<thead>
						<tr>
							<th colspan="6" class="tableTitle">Segunda-feira</th>
						</tr>
						<tr>
							<th>Código</th>
							<th>Disciplina</th>
							<th>Horas Semanais</th>
							<th>Hora de Início</th>
							<th>Status</th>
							<th>Selecionar</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="d" items="${disciplinas }">
							<c:if test="${d.disciplina.diaSemana eq 'Segunda-feira' }">
									<tr>
									<td><c:out value="${d.disciplina.codDisciplina}"></c:out>
									<td><c:out value="${d.disciplina.disciplina}"></c:out>
									<td><c:out value="${d.disciplina.horasSemanais}"></c:out>
									<td><c:out value="${d.disciplina.horaInicio}"></c:out>
									<td><c:out value="${d.status}"></c:out>
									<td class="status"><input type="submit" id="botao"
										name="botao" value="${d.disciplina.codDisciplina}">
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
				<table class="terca">
					<thead>
						<tr>
							<th colspan="6" class="tableTitle">Terca-feira</th>
						</tr>
						<tr>
							<th>Código</th>
							<th>Disciplina</th>
							<th>Horas Semanais</th>
							<th>Hora de Início</th>
							<th>Status</th>
							<th>Selecionar</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="d" items="${disciplinas }">
							<c:if test="${d.disciplina.diaSemana eq 'Terça-feira' }">
								<tr>
									<td><c:out value="${d.disciplina.codDisciplina}"></c:out>
									<td><c:out value="${d.disciplina.disciplina}"></c:out>
									<td><c:out value="${d.disciplina.horasSemanais}"></c:out>
									<td><c:out value="${d.disciplina.horaInicio}"></c:out>
									<td><c:out value="${d.status}"></c:out>
									<td class="status"><input type="submit" id="botao"
										name="botao" value="${d.disciplina.codDisciplina}">
									</td>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
				<table class="quarta">
					<thead>
						<tr>
							<th colspan="6" class="tableTitle">Quarta-feira</th>
						</tr>
						<tr>
							<th>Código</th>
							<th>Disciplina</th>
							<th>Horas Semanais</th>
							<th>Hora de Início</th>
							<th>Status</th>
							<th>Selecionar</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="d" items="${disciplinas }">
							<c:if test="${d.disciplina.diaSemana eq 'Quarta-feira' }">
								<tr>
									<td><c:out value="${d.disciplina.codDisciplina}"></c:out>
									<td><c:out value="${d.disciplina.disciplina}"></c:out>
									<td><c:out value="${d.disciplina.horasSemanais}"></c:out>
									<td><c:out value="${d.disciplina.horaInicio}"></c:out>
									<td><c:out value="${d.status}"></c:out>
									<td class="status"><input type="submit" id="botao"
										name="botao" value="${d.disciplina.codDisciplina}">
									</td>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
				<table class="quinta">
					<thead>
						<tr>
							<th colspan="6" class="tableTitle">Quinta-feira</th>
						</tr>
						<tr>
							<th>Código</th>
							<th>Disciplina</th>
							<th>Horas Semanais</th>
							<th>Hora de Início</th>
							<th>Status</th>
							<th>Selecionar</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="d" items="${disciplinas }">
							<c:if test="${d.disciplina.diaSemana eq 'Quinta-feira' }">
								<tr>
									<td><c:out value="${d.disciplina.codDisciplina}"></c:out>
									<td><c:out value="${d.disciplina.disciplina}"></c:out>
									<td><c:out value="${d.disciplina.horasSemanais}"></c:out>
									<td><c:out value="${d.disciplina.horaInicio}"></c:out>
									<td><c:out value="${d.status}"></c:out>
									<td class="status"><input type="submit" id="botao"
										name="botao" value="${d.disciplina.codDisciplina}">
									</td>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
				<table class="sexta">
					<thead>
						<tr>
							<th colspan="6" class="tableTitle">Sexta-feira</th>
						</tr>
						<tr>
							<th>Código</th>
							<th>Disciplina</th>
							<th>Horas Semanais</th>
							<th>Hora de Início</th>
							<th>Status</th>
							<th>Selecionar</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="d" items="${disciplinas }">
							<c:if test="${d.disciplina.diaSemana eq 'Sexta-feira' }">
								<tr>
									<td><c:out value="${d.disciplina.codDisciplina}"></c:out>
									<td><c:out value="${d.disciplina.disciplina}"></c:out>
									<td><c:out value="${d.disciplina.horasSemanais}"></c:out>
									<td><c:out value="${d.disciplina.horaInicio}"></c:out>
									<td><c:out value="${d.status}"></c:out>
									<td class="status"><input type="submit" id="botao"
										name="botao" value="${d.disciplina.codDisciplina}">
									</td>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</form>

	</main>
</body>
</html>