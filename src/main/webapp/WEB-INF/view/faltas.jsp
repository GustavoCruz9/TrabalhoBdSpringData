<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sistema AGIS - Professor</title>
<link rel="stylesheet" type="text/css"
	href='<c:url value = "./resources/css/styleProfessorFaltas.css"/>'>
</head>

<body>

	<script src="./resources/js/headerProfessor.js"></script>
	<main>

		<h1>Faltas</h1>

		<form id="DisciplinaForm" action="faltas" method="post">
			<div class="conteudo">
				<input type="text" pattern="[0-9]*" name="codigoProfessor"
					id="codigoProfessor" placeholder="Codigo Professor"
					value="${param.codigoProfessor}" /> <input type="submit"
					value="Buscar Disciplinas" id="botao" name="botao" />
			</div>
			<select name="codDisciplina" id="codDisciplina" onchange="submitForm()">
				<option disabled ${disciplina.disciplina==null?'selected' : '' }>Selecione
					a Disciplina</option>
				<c:forEach var="d" items="${disciplinas}">
					<option value="${d.codDisciplina}"
						${disciplina.codDisciplina != null && disciplina.codDisciplina == d.codDisciplina ? 'selected' : ''}>
						<c:out value="${d.disciplina}" />
					</option>
				</c:forEach>
			</select>
			<input type="hidden" name="codDisciplina" id="codDisciplina" value="${disciplina.codDisciplina}">	
			<input type="hidden" name="codigoProfessor" id="codigoProfessor" value="${param.codigoProfessor}">
			
			<script>
			function submitForm() {
				var statusSelect = true;

				var input = document.createElement("input");
				input.type = "hidden";
				input.name = "statusSelect";
				input.value = statusSelect;

				var form = document.getElementById("DisciplinaForm");
				form.appendChild(input);

				form.submit();

			}
		</script>
			
		</form>
		
		<br>
		
		<c:if test="${not empty alunosFaltasObject}">
		<form action="faltasRelatorio" method="post" target="_blank">
             	<input type="submit" value="Gerar Relatorio" id="botao" name="botao"/>   
             	<input type="hidden" name="codDisciplina" id="codDisciplina" value="${disciplina.codDisciplina}">
        </form>	
        </c:if>
		
        
          <div align="center">
                <c:if test="${not empty erro}">
                    <h2>
                        <b><c:out value="${erro}" /></b>
                    </h2>
                </c:if>
            </div>

            <div align="center">
                <c:if test="${not empty saida}">
                    <h3>
                        <b><c:out value="${saida}" /></b>
                    </h3>
                </c:if>
            </div>
            
            <br>

		

		<div class="tabelaAluno">
			<c:forEach var="obj" items="${alunosFaltasObject}">
				 <c:set var="cpf" value="${obj[0]}"/>
		        <c:set var="nome" value="${obj[1]}"/>
		        <c:set var="datas" value="${obj[2]}"/>
		        <c:set var="faltas" value="${obj[4]}"/>
		        <c:set var="totalfaltas" value="${obj[5]}"/>
		        <c:set var="status" value="${obj[6]}"/>
			<table>
	            <thead>
	                <tr class="tableTitle">
	                    <th>CPF</th>
	                    <th>Nome</th>
	                    <c:forEach var="data" items="${fn:split(datas, ';')}">
		                        <th><c:out value="${data}"/></th>
		                 </c:forEach>
	                    <th>Total de Faltas</th>
	                    <th>Status</th>
	                </tr>
	            </thead>
			    <tbody>
                <tr>
                    <td><c:out value="${cpf}"/></td>
                    <td><c:out value="${nome}"/></td>
                    <c:forEach var="falta" items="${fn:split(faltas, ';')}">
		                 <td><c:out value="${falta}"/></td>
		            </c:forEach>
                    <td><c:out value="${totalfaltas}"/></td>
                    <td><c:out value="${status}"/></td>
                </tr>
            </tbody>		    
			 </c:forEach>
		</div>
		

	</main>

</body>
</html>