<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sistema AGIS - Professor</title>
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styleProfessor.css"/>'>
</head>

<body>

	<script src="./resources/js/headerProfessor.js"></script>

	<main>

		<div align="center">
			<c:if test="${not empty erro }">
				<h2>
					<b><c:out value="${erro }" /></b>
				</h2>
			</c:if>
		</div>

		<div align="center">
			<c:if test="${not empty saida }">
				<h3>
					<b><c:out value="${saida }" /></b>
				</h3>
			</c:if>
		</div>
		
		<form method="post" action="editarChamada"
			class="formCadastrarChamada">

			<input type="hidden" name="codDisciplina"
				value="${param.codDisciplina}">

			<div class="dataContainer">
				<label>Data da Chamada:</label> 
				<input type="date" id="dataChamada"
					name="dataChamada" value="${param.dataChamada}" readonly> 
				<input type="submit" id="botao" name="botao" value="Listar Alunos">
			</div>
			
			<c:if test="${not empty listaChamada}">	
				<h1>
					<c:out value="${listaChamada[0].matricula.disciplina.disciplina}" />
				</h1>
	
				<table>
					<c:choose>
						<c:when test="${horasSemanais eq '01:40'}">
							<thead>
								<tr>
									<th>Ra</th>
									<th>Nome</th>
									<th>Aula 1</th>
									<th>Aula 2</th>
								</tr>
							</thead>
						</c:when>
						<c:otherwise>
							<thead>
								<tr>
									<th>Ra</th>
									<th>Nome</th>
									<th>Aula 1</th>
									<th>Aula 2</th>
									<th>Aula 3</th>
									<th>Aula 4</th>
								</tr>
							</thead>
						</c:otherwise>
					</c:choose>
					<tbody>
						<c:forEach var="lc" items="${listaChamada}">
							<tr>
								<td>${lc.matricula.aluno.ra}</td>
								<td>${lc.matricula.aluno.nome}</td>
								<c:choose>
									<c:when test="${horasSemanais eq '01:40'}">
										<td><input type="checkbox" id="checkboxAula1_${lc.matricula.aluno.ra}" 
										name="checkboxAula1_${lc.matricula.aluno.ra}" value="1" 
										<c:if test="${lc.aula1 == '1'}">checked</c:if>>
										</td>
										<td><input type="checkbox" id="checkboxAula2_${lc.matricula.aluno.ra}" 
										name="checkboxAula2_${lc.matricula.aluno.ra}" value="1"
										<c:if test="${lc.aula2 == '1'}">checked</c:if>>
										</td>
									</c:when>
									<c:otherwise>
										<td><input type="checkbox" id="checkboxAula1_${lc.matricula.aluno.ra}" 
										name="checkboxAula1_${lc.matricula.aluno.ra}" value="1"
										<c:if test="${lc.aula1 == '1'}">checked</c:if>>
										</td>
										<td><input type="checkbox" id="checkboxAula2_${lc.matricula.aluno.ra}" 
										name="checkboxAula2_${lc.matricula.aluno.ra}" value="1"
										<c:if test="${lc.aula2 == '1'}">checked</c:if>>
										</td>
										<td><input type="checkbox" id="checkboxAula3_${lc.matricula.aluno.ra}" 
										name="checkboxAula3_${lc.matricula.aluno.ra}" value="1"
										<c:if test="${lc.aula3 == '1'}">checked</c:if>>
										</td>
										<td><input type="checkbox" id="checkboxAula4_${lc.matricula.aluno.ra}" 
										name="checkboxAula4_${lc.matricula.aluno.ra}" value="1"
										<c:if test="${lc.aula4 == '1'}">checked</c:if>>
										</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</tbody>
				</table>
	
				<input type="submit" id="botao" name="botao"
					value="Editar Chamada" class="btnCadastrarChamada">
			</c:if>
		</form>

	</main>
	
</body>
</html>