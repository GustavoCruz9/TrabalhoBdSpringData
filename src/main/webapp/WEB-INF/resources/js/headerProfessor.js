// template.js
const template = document.createElement('template');

template.innerHTML = `
    <header>
        <div class="logo">
            <a href="./index"><img src="./resources/images/LogoAGIS.png" alt="Logo"></a>
        </div>
        <nav>
	        <ul>
	            <li>
	               	<a href="chamada" class="funcional">Chamada</a>
	            </li>
	            <li>
	                <a href="notas" class="funcional">Notas</a>
	            </li>
	         </ul>
         </nav>
    </header>
`;

document.body.appendChild(template.content);