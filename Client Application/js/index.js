let selects = ["#filtroCategoria","#formCategoria"]

function initialLoad(categories, url) {
    loadCategories(categories[0])
    loadCategories(categories[1])
    loadData(url)
}

function clearForm(){
    document.querySelector("#transactionForm").reset()

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
            select.innerHTML = ""
            select.appendChild(document.createElement("option"))
            for(d in data){
                let option = document.createElement("option")
                option.value= data[d].id
                option.innerHTML = data[d].nome
                select.appendChild(option)
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
                
                let infoBtn = document.createElement("button")
                infoBtn.classList = ["btn btn-primary"]
                infoBtn.textContent= "Info"
                verMais.appendChild(infoBtn)
                let editBtn = document.createElement("button")
                editBtn.classList = ["btn btn-primary"]
                editBtn.textContent= "Edit"
                editar.appendChild(editBtn)
                let delBtn = document.createElement("button")
                delBtn.classList = ["btn btn-danger"]
                delBtn.textContent= "Del"
                excluir.appendChild(delBtn)
                
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

let newTransaction = document.querySelector("#buttonSave")
newTransaction.addEventListener("click", ()=>{
    let dataTransacao = new Date(document.querySelector("#formData").value).toJSON()
    let json = {
        "valor": document.querySelector("#formValue").value,
        "descricao": document.querySelector("#formDescription").value,
        "tipo": document.querySelector("#formTipo").value,
        "idCategoria":document.querySelector("#formCategoria").value,
        "data": dataTransacao
    }
    fetch("http://localhost:8080/MyFinanceAPI/finance/transacao", {
         method:"POST",
        body: JSON.stringify(json),
        headers:{ "Content-type": "application/json; charset=UTF-8"}
    })
        .then(response =>{
            if(response.status=201){
                document.querySelector("#tbody").innerHTML=""
                clearForm()
                loadData("http://localhost:8080/MyFinanceAPI/finance/transacao")
            }
        })
})