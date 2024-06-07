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
    href='<c:url value = "./resources/css/styleProfessorNotas.css"/>'>
</head>

<body>

    <script src="./resources/js/headerProfessor.js"></script>
    <main>
        <h1>Notas</h1>
        
        
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
        
        
        <form id="DisciplinaForm" action="notas" method="post">
            <div class="formulario">
                <div class="cima">
                    <div class="esquerda">
                        <div class="conteudo">
                            <input type="text" pattern="[0-9]*" name="codigoProfessor"
                                id="codigoProfessor" placeholder="Codigo Professor"
                                value="${param.codigoProfessor}"/> <input type="submit"
                                value="Buscar Disciplinas" id="botao" name="botao"/>
                        </div>
                        <div class="conteudo">
                            <select name="codDisciplina" id="codDisciplina" onchange="submitForm()">
						        <option disabled ${disciplina.disciplina == null ? 'selected' : '' }>Selecione a Disciplina</option>
						        <c:forEach var="d" items="${disciplinas}">
						            <option value="${d.codDisciplina}"
						            ${disciplina.codDisciplina != null && disciplina.codDisciplina == d.codDisciplina ? 'selected' : ''}>
						                <c:out value="${d.disciplina}" />
						            </option>
						        </c:forEach>
						    </select>
                            <input type="hidden" name="codDisciplina" id="codDisciplina" value="${disciplina.codDisciplina}">	
									<input type="hidden" name="codigoProfessor" id="codigoProfessor"
									value="${param.codigoProfessor}">
									<input type="hidden" name="cpf" id="cpf"
									value="<c:out value="${cpf}"></c:out>">	
                        </div>
                        <div class="conteudo">
                            <input type="text" pattern="[0-9]*" name="cpf" id="cpf"
                                placeholder="CPF do Aluno" value="${param.cpf}"/> <input type="submit"
                                value="Buscar Aluno" id="botao" name="botao">
                        </div>
                    </div>

                    <div class="direita">
                        <c:if test="${not empty avaliacoes}">
                            <div class="conteudo">
                                <label>Nome:</label>
                                <h3><c:out value="${avaliacoes.get(0).matricula.aluno.nome}"></c:out></h3>
                            </div>
                            <c:forEach var="avaliacao" items="${avaliacoes}" varStatus="status">
                                <div class="conteudo">
                                    <label><c:out value="${avaliacao.pesoAvaliacao.tipo}"></c:out>:</label>
                                    <input type="text" placeholder="Nota"
                                        name="nota${status.index + 1}" id="nota${status.index + 1}" oninput="validateInput(this)"
                                        pattern="^[0-9]+([.,][0-9]{1,2})?$"
                                        value='<c:out value="${avaliacao.nota}"></c:out>' />
                                </div>
                            </c:forEach>
                        </c:if>
                        
                        <script>
					        function validateInput(input) {
					            input.value = input.value.replace(',', '.');
					            let value = parseFloat(input.value);
					            if (isNaN(value)) {
					                input.setCustomValidity("Por favor, insira um número válido.");
					            } else if (value < 0 || value > 10) {
					                input.setCustomValidity("Por favor, insira um número entre 0 e 10.");
					            } else {
					                input.setCustomValidity("");
					            }
					        }
		
					        document.querySelectorAll('input[type="number"]').forEach(input => {
					            input.addEventListener('input', () => {
					                input.value = input.value.replace(',', '.');
					                let value = input.value;
					                if (!/^[0-9]*[.,]?[0-9]*$/.test(value)) {
					                    input.value = value.slice(0, -1);
					                    alert("Valor inválido. Insira um número entre 0 e 10.");
					                }
					            });
					        });
	    				</script>
                    </div>
                </div>
                <div class="baixo">
                    <input type="submit" value="Confirmar" id="botao"
                        name="botao">
                </div>
            </div>
            
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

            <div class="tabelaAluno">
                <table>
                    <thead>
                        <tr class="tableTitle">
                            <th>CPF</th>
                            <th>Nome</th>
                            <th>Nota 1</th>
                            <th>Nota 2</th>
                            <c:if test="${fn:length(avaliacoes) > 2}">
                                <th>Nota 3</th>
                            </c:if>
                            <th>Ação</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="obj" items="${alunosAvaliacaoObject}" varStatus="status">
                <c:choose>
                    <c:when test="${status.index % 2 == 0}">
                        <c:set var="aluno" value="${obj}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="avaliacoesAluno" value="${obj}" />
                        <tr>
                            <td><c:out value="${aluno.cpf}" /></td>
                            <td><c:out value="${aluno.nome}" /></td>
                            <c:forEach var="av" items="${avaliacoesAluno}" varStatus="avStatus">
                                <td><c:out value="${av.nota}" /></td>
                            </c:forEach>
                            <c:if test="${fn:length(avaliacoesAluno) < 3}">
                                <td></td>
                            </c:if>
                            <c:if test="${fn:length(avaliacoesAluno) < 2}">
                                <td></td>
                            </c:if>
                            <td>
                            	<form action="notas" method="post">
                            		<input type="submit" value="Lancar Notas" id="botao" name="botao" onclick="setCpfAndSubmit('<c:out value='${aluno.cpf}' />')">
                            		<input type="hidden" name="cpfSelect" id="cpfSelect"
									value="<c:out value="${aluno.cpf}" />">
									<input type="hidden" name="codigoProfessor" id="codigoProfessor"
									value="${param.codigoProfessor}">
									<input type="hidden" name="cpf" id="cpf"
									value="${param.cpf}">	
									<input type="hidden" name="codDisciplina" id="codDisciplina" value="${disciplina.codDisciplina}">		
								</form>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
                    </tbody>
                </table>
            </div>            
            				
    </main>
</body>
</html>
