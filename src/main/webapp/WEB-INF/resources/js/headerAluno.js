// template.js
const template = document.createElement('template');

template.innerHTML = `
<header>
    <div class="logo">
        <a href="./index"><img src="./resources/images/LogoAGIS.png" alt="Logo"></a>
    </div>
    <nav>
        <ul>
            <li><a href="matriculaAluno">Matrícula</a></li>
            <li><a href="visualizarMatriculaAluno">Disciplinas</a></li>
            <li><a href="solicitarDispensa" class="funcional">Solicitar Dispensas</a></li>
            <li><a href="notasAluno" class="funcional">Notas</a></li>
        </ul>
    </nav>
</header>
`

document.body.appendChild(template.content);