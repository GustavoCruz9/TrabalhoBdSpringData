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
	<main>
		
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

		<form action="notas" method="post" class="formDispensa1">
				<input type="submit" id="botao"	name="botao" value="Confirmar"/>
		</form>

	</main>
</body>
</html>
