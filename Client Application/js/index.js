let categories = new Map()
let selects = ["#filtroCategoria","#formCategoria"]

function initialLoad(categories, url) {
    loadCategories(categories[0])
    loadCategories(categories[1])
    loadData(url)
}

async function loadCategories(target) {
    fetch("http://localhost:8080/MyFinanceAPI/finance/categoria")
        .then(response =>{
            if(response.ok){
                return response.json()
            }else{
                throw new Error("Erro na requisição")
            }
        })
        .then(data =>{
            let select = document.querySelector(target)
            categories = new Map()
            for(d in data){
                let option = document.createElement("option")
                option.value= data[d].id
                option.innerHTML = data[d].nome
                select.appendChild(option)
                categories.set(data[d].id,data[d].name)
            }
        })
        .catch(error =>{
            console.log("erro: ", error)
        })
}

async function loadData(url) {
    fetch(url)
        .then(response =>{
            if(response.ok){
                return response.json()
            }else{
                throw new Error("Erro na requisição")
            }
        })
        .then(data =>{
            let table = document.querySelector("#tbody")
            for(d in data){
                let tr = document.createElement("tr")
                
                let descricao = document.createElement("td")
                let tipo = document.createElement("td")
                let categoria = document.createElement("td")
                let date = document.createElement("td")
                let valor = document.createElement("td")
                let verMais = document.createElement("td")
                let editar = document.createElement("td")
                let excluir = document.createElement("td")
                
                descricao.innerHTML= data[d].descricao
                let tipoTransacao = data[d].tipo
                tipo.innerHTML = tipoTransacao
                if(tipoTransacao == "RECEITA"){
                    tr.className = "table-success"
                }else{
                    tr.className = "table-danger"
                }
                let idCat = data[d].idCategoria
                fetch(`http://localhost:8080/MyFinanceAPI/finance/categoria/${idCat}`)
                    .then(response =>{
                        if(response.ok)
                            return response.json()
                    })
                    .then(data =>{
                        categoria.innerHTML = data.nome
                    })
                date.innerHTML = new Intl.DateTimeFormat("br-BR").format(Date.parse(data[d].data))
                valor.innerHTML = new Intl.NumberFormat("br-BR",{style:"currency", currency:"BRL"}).format(parseFloat(data[d].valor))
                verMais.appendChild(document.createElement("button"))
                editar.appendChild(document.createElement("button"))
                excluir.appendChild(document.createElement("button"))
                
                tr.appendChild(descricao)
                tr.appendChild(tipo)
                tr.appendChild(categoria)
                tr.appendChild(date)
                tr.appendChild(valor)
                tr.appendChild(verMais)
                tr.appendChild(editar)
                tr.appendChild(excluir)
                
                table.appendChild(tr)
            }
        })
}

document.addEventListener("DOMContentLoaded", ()=>{
    initialLoad(selects,"http://localhost:8080/MyFinanceAPI/finance/transacao")
})

let newCategoria = document.querySelector("#buttonCategory")
newCategoria.addEventListener("click", ()=>{
    let nome = document.querySelector("#formName").value
    let json = {
        "nome": nome
    }
    fetch("http://localhost:8080/MyFinanceAPI/finance/categoria", {
        method:"POST",
        body: JSON.stringify(json),
        headers:{ "Content-type": "application/json; charset=UTF-8"}
    })
        .then(response => {
            if(response.status=201){
                for(s in selects){
                    loadCategories(selects[s])
                }
            }
        })
})