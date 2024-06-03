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
        <div class="titulo">
            <h1>Confirmar Dispensas</h1>
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

        <br />


        <form action="confirmarDispensa" method="post">
        
        	<div class="cpfDispensa">
                <div class="pesquisaCpf">
                    <input type="text" pattern="[0-9]*" name="cpf" id="cpf" placeholder="Cpf" class="inputCPF"> 
                    <input type="submit" value="Pesquisa Cpf" class="btnCPF" id="botao" name="botao">
                </div>
            </div>
        

            <table class="tabelaSolicitaDispensa">
                <thead>
                    <tr>
                        <th>RA</th>
                        <th>Nome</th>
                        <th>Data Solicitacao</th>
                        <th>Disciplina</th>
                        <th>Instituicao</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                     <c:forEach var="d" items="${dispensas}">  
                    <tr>
                        <td><c:out value="${d.aluno.ra}"/></td>
                        <td><c:out value="${d.aluno.nome}"/></td>
                        <td><c:out value="${d.dataSolicitacao}"/></td>
                        <td><c:out value="${d.disciplina.disciplina}"/></td>
                        <td><c:out value="${d.instituicao}"/></td>
                        <td>
                            <input class="confirmar" type="submit" value="Aceitar" id="botao" name="botao">
                        </td>
                        
                        <td>
                               <input class="negar" type="submit" value="Negar" id="botao" name="botao">
                        </td>
                        <td>
                         	<form action="confirmarDispensa" method="post">
                         		<input type="hidden" name="ra" value="${d.aluno.ra}">
                         		<input type="hidden" name="codDisciplina" value="${d.disciplina.codDisciplina}">
                         	</form>
                        </td>
                    </tr>
                    </c:forEach> 
                </tbody>
            </table>
            
        </form>

        <br />

    </main>

    <script src="./resources/js/navegacao.js"></script>

</body>

</html>