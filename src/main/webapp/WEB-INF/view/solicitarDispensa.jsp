
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sistema AGIS - Aluno</title>
<link rel="stylesheet" type="text/css"
	href='<c:url value = "./resources/css/styleSecretariaCurso.css"/>'>
</head>

<body>

	<script src="./resources/js/headerAluno.js"></script>

	<main>
		<div class="titulo">
			<h1>Solicitar Dispensa</h1>
		</div>

		<br />

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

		<form action="solicitarDispensa" method="get" class="formDispensa1">
			<table>
				<tr>
					<td colspan="3"><input type="text" id="cpf" name="cpf"
						placeholder="CPF" pattern="[0-9]*" value="${param.cpf}"></td>
					<td colspan="1"><input type="submit" id="botao" name="botao"
						value="Buscar"></td>
				</tr>
				<tr>
					<td colspan="4" class="center"><input type="submit" id="botao"
						name="botao" value="Listar Dispensas"></td>
				</tr>
			</table>
		</form>

		<form action="solicitarDispensa" method="post" class="formDispensa">

			<input type="hidden" name="cpf" id="cpf" value="${param.cpf}">

			<table class="tabelaDispensa">
				<tr>
					<td colspan="4"><select name="disciplina" id="disciplina"
						class="selectCustom">
							<option value="" disabled selected>Selecione a
								Disciplina</option>
							<c:forEach var="d" items="${disciplinas}">
								<option value="${d.codDisciplina}">
									<c:out value="${d.disciplina}" />
								</option>
							</c:forEach>
					</select></td>
				</tr>
				<tr>
					<td colspan="4"><input type="text" id="instituicao"
						name="instituicao"
						placeholder="Instituição onde realizou a disciplina"
						style="width: 100%;"></td>
				</tr>
				<tr>
					<td colspan="4" class="center"><input type="submit" id="botao"
						name="botao" value="Solicitar"></td>
				</tr>
			</table>

		</form>

		<c:if test="${not empty dispensas}">
			<table class="tabelaAluno">
				<thead>
					<tr>
						<th class="lista">Cod. Disciplina</th>
						<th class="lista">Data Dispensa</th>
						<th class="lista">Instituição</th>
						<th class="lista">Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="d" items="${dispensas}">
						<tr>
							<td><c:out
									value="${d.getDisciplina().getCodDisciplina()}"></c:out>
							<td><c:out value="${d.getDataSolicitacao()}"></c:out>
							<td><c:out value="${d.getInstituicao()}"></c:out>
							<td><c:out value="${d.getStatusDispensa()}"></c:out>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:if>

	</main>

	<script src="./resources/js/navegacao.js"></script>

</body>
</html>
