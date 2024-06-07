<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sistema AGIS - Aluno</title>
<link rel="stylesheet" type="text/css" href='<c:url value = "./resources/css/styleAlunoNotas.css"/>'>
</head>
<body>

	<script src="./resources/js/headerAluno.js"></script>
	
	<main>
		<h1>Notas Parciais</h1>
		
		<form action="notasAluno" method="post">
			<div class="cpfMatricula">
				<div class="pesquisaCpf">
					<input type="text" name="pesquisaCpf"
						id="pesquisaCpf" placeholder="CPF" class="inputCPF">
					<input type="submit" value="Pesquisa CPF" class="btnCPF" id="botao"
						name="botao">
				</div>
			</div>
			
			<!--<c:forEach var="a" items="${avaliacoes }">-->
				<table class="vizualizarNotas">
					<thead>
						<tr>
							<th colspan="4" class="tableTitle">Materia</th>
						</tr>
						<tr>
							<th>Nota 1</th>
							<th>Nota 2</th>
							<th>Nota 3</th>
							<th>Media</th>
						</tr>
					</thead>	
					<tbody>
						<tr>
							<td>10.0</td>
							<td>10.0</td>
							<td>10.0</td>
							<td>10.0</td>
						</tr>
					</tbody>
				</table>
			<!--</c:forEach>-->
		</form>
	</main>

</body>
</html>