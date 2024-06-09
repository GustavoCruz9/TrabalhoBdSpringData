<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
					<input type="submit" value="Pesquisar CPF" class="btnCPF" id="botao"
						name="botao">
				</div>
			</div>
			
			
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
			
			<c:forEach var="obj" items="${objetosAvaliacoes}">
				<c:set var="tipos" value="${obj[1]}"/>
				<c:set var="notas" value="${obj[2]}"/>
				<c:set var="media" value="${obj[3]}"/>
				<c:set var="status" value="${obj[4]}"/>
				<table class="vizualizarNotas" align="center">
					<thead>
						<tr>
							<th colspan="5" class="tableTitle"><c:out value="${obj[0]}"/></th>
						</tr>
						<tr>
							<c:forEach var="tipo" items="${fn:split(tipos, ';')}">
		                        <th><c:out value="${tipo}"/></th>
		                    </c:forEach>
							<th>Media</th>
							<th>Status</th>
						</tr>
					</thead>	
					<tbody>
						<tr>
							<c:forEach var="nota" items="${fn:split(notas, ';')}">
		                        <td><c:out value="${nota}"/></td>
		                    </c:forEach>
		                    <c:choose>
                            <c:when test="${media != -1}">
                                <td><c:out value="${media}"/></td>
                            </c:when>
                            <c:otherwise>
                                <td>Nenhuma nota foi lançada</td>
                            </c:otherwise>
                        	</c:choose>
                        	<c:forEach items="${status}">
		                        <td><c:out value="${status}"/></td>
		                    </c:forEach>
						</tr>
					</tbody>
				</table>
			</c:forEach>
		</form>
	</main>
</body>
</html>