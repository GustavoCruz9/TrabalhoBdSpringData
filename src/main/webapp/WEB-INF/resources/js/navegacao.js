var urlsAluno = {
    "cadastrar": "alunoCadastrar",
    "alterar": "alunoCadastrar",
    "vizualizar": "alunoListar",
    "telefone" : "telefone"
};

var urlsMatricula = {
    "cadastrar": "matriculaSecretaria",
    "vizualizar": "visualizarMatriculaSecretaria"
}

// Função para redirecionar com base no valor selecionado
function redirecionar(selectedValue, urls) {
    var url = urls[selectedValue];
    if (url) {
        window.location.href = url;
    } else {
        console.error("URL não encontrada para a opção selecionada: " + selectedValue);
    }
}

// Manipulador de eventos para selectAluno
document.getElementById("selectAluno").addEventListener("change", function () {
    var selectedValue = this.value;
    redirecionar(selectedValue, urlsAluno);
});

document.getElementById("selectMatricula").addEventListener("change", function () {
    var selectedValue = this.value;
    redirecionar(selectedValue, urlsMatricula);
});