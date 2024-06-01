// template.js
const template = document.createElement('template');

template.innerHTML = `
    <header>
        <div class="logo">
            <a href="./index"><img src="./resources/images/LogoAGIS.png" alt="Logo"></a>
        </div>
        

    </header>
`;

document.body.appendChild(template.content);