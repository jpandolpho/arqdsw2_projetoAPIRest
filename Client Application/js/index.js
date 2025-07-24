const BASE_URL = "http://localhost:8080/MyFinanceAPI/finance"

let selects = ["#filtroCategoria","#formCategoria"]
let paginaAtual = 1
let totalPaginas

function initialLoad(categories) {
    loadCategories(categories[0])
    loadCategories(categories[1])
    filterTable()
}

function checkPages(){
    let btnPrev = document.querySelector("#btnPrevious")
    if(paginaAtual==1){
        btnPrev.disabled = true
    }else{
        btnPrev.disabled = false
    }
    let btnNext = document.querySelector("#btnNext")
    if(paginaAtual==totalPaginas){
        btnNext.disabled = true
    }else{
        btnNext.disabled = false
    }
}

function clearForm(target){
    let form = document.querySelector(target)
    let inputs = form.getElementsByTagName("input")
    for(i in inputs){
        inputs[i].value=""
    }
    let textarea = form.getElementsByTagName("textarea")
    if(textarea!=null){
        textarea.value = ""
    }
}

function filterTable(){
    let categoria = document.querySelector("#filtroCategoria").value
    let tipo = document.querySelector("#filtroTipo").value
    let mesAno = document.querySelector("#filtroMonth").value.split("-")
    limite = document.querySelector("#filtroQtd").value
    let ano = mesAno[0]
    let mes = mesAno[1]
    let url = "?"
    let flag = false
    if(categoria!=''){
        url+=`categoria=${categoria}`
        flag = true
    }
    if(mesAno.length > 1){
        if(flag)
            url+="&"
        url+=`month=${mes}&year=${ano}`
        flag = true
    }
    if(tipo!=""){
        if(flag)
            url+="&"
        url+=`type=${tipo}`
    }
    countPages(`${BASE_URL}/contagem${url}`)
    if(flag)
        url+='&'
    url+=`page=${paginaAtual}&limit=${limite}`
    loadData(`${BASE_URL}/transacao${url}`)
}

async function setupView(id) {
    fetch(`${BASE_URL}/transacao/${id}`)
        .then(response =>{
            if(response.ok){
                return response.json()
            }else{
                throw new Error(response.statusText)
            }
        })
        .then(data =>{
            document.querySelector("#infoValor").textContent = `Valor: ${new Intl.NumberFormat("br-BR",{style:"currency", currency:"BRL"}).format(parseFloat(data.valor))}`
            document.querySelector("#infoDescrição").textContent = `Descrição: ${data.descricao}`
            document.querySelector("#infoTipo").textContent = `Tipo: ${data.tipo}`
            let idCat = data.idCategoria
            fetch(`${BASE_URL}/categoria/${idCat}`)
                .then(response =>{
                    if(response.ok)
                        return response.json()
                })
                .then(data =>{
                    document.querySelector("#infoCategoria").textContent = `Categoria: ${data.nome}`
                })
            document.querySelector("#infoData").textContent = `Data: ${new Intl.DateTimeFormat("br-BR").format(Date.parse(data.data))}`
        })
}

async function setupEdit(id) {
    newTransaction.style.display = "none"
    editTransaction.style.display = "block"
    fetch(`${BASE_URL}/transacao/${id}`)
        .then(response =>{
            if(response.ok){
                return response.json()
            }else{
                throw new Error(response.statusText)
            }
        })
        .then(data =>{
            document.querySelector("#formId").value = data.id
            document.querySelector("#formValue").value = data.valor
            document.querySelector("#formDescription").value = data.descricao
            document.querySelector("#formTipo").value = data.tipo
            document.querySelector("#formCategoria").value = data.idCategoria
            document.querySelector("#formData").value = (new Date(data.data)).toISOString().substring(0,16)
        })
}

async function loadCategories(target) {
    fetch(`${BASE_URL}/categoria`)
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

async function excluirTransacao(id) {
    if(confirm("Deseja excluir esta transação?")){
        fetch(`${BASE_URL}/transacao/${id}`,{
            method: "DELETE"
        })
            .then(response =>{
                if(response.status=204){
                    alert("Transação excluída!")
                    document.querySelector("#tbody").innerHTML=""
                    filterTable()
                }else{
                    throw new Error(response.statusText)
                }
            })
    }

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
                fetch(`${BASE_URL}/categoria/${idCat}`)
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
                infoBtn.classList.add("btn", "btn-primary")
                infoBtn.textContent= "Info"
                infoBtn.setAttribute("data-bs-toggle", "modal")
                infoBtn.setAttribute("data-bs-target", "#informationModal")
                let id = data[d].id
                infoBtn.addEventListener('click', ()=>{
                    setupView(id)
                })
                verMais.appendChild(infoBtn)
                
                let editBtn = document.createElement("button")
                editBtn.classList.add("btn", "btn-primary")
                editBtn.textContent= "Edit"
                editBtn.setAttribute("data-bs-toggle", "modal")
                editBtn.setAttribute("data-bs-target", "#transactionModal")
                editBtn.setAttribute("data-bs-id", id)
                editar.appendChild(editBtn)
                
                let delBtn = document.createElement("button")
                delBtn.classList.add("btn", "btn-danger")
                delBtn.textContent= "Del"
                delBtn.addEventListener('click',()=>{
                    excluirTransacao(id)
                })
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

async function loadResumo(){
    let receitas = document.querySelector("#resumoReceita")
    let despesas = document.querySelector("#resumoDespesa")
    let saldo = document.querySelector("#resumoConta")
    receitas.textContent = "Receitas: "
    despesas.textContent = "Despesas: "
    saldo.textContent = "Saldo: "
    let table = document.querySelector("#tbodyResumo")
    table.innerHTML=""
    fetch(`${BASE_URL}/resumo`)
        .then(response =>{
            if(response.ok)
                return response.json()
            else
                return {"erro":"Não existem transações."}
        })
        .then(data =>{
            if(data.erro != null){
                let div = document.querySelector("#bodyModalResumo")
                div.innerHTML = ""
                let h3 = document.createElement("h3")
                h3.textContent = data.erro
                div.appendChild(h3)
            }else{
                receitas.textContent += new Intl.NumberFormat("br-BR",{style:"currency", currency:"BRL"}).format(parseFloat(data.receitas))
                despesas.textContent += new Intl.NumberFormat("br-BR",{style:"currency", currency:"BRL"}).format(parseFloat(data.despesas))
                saldo.textContent += new Intl.NumberFormat("br-BR",{style:"currency", currency:"BRL"}).format(parseFloat(data.saldo))

                let categorias = data.despesasPorCategoria
                for([key, value] of Object.entries(categorias)){
                    let tr = document.createElement("tr")
                    let categoria = document.createElement("td")
                    let valor = document.createElement("td")
                    fetch(`${BASE_URL}/categoria/${key}`)
                        .then(response =>{
                            if(response.ok)
                                return response.json()
                        })
                        .then(data =>{
                            categoria.innerHTML = data.nome
                        })
                    valor.innerHTML = new Intl.NumberFormat("br-BR",{style:"currency", currency:"BRL"}).format(parseFloat(value))
                    tr.appendChild(categoria)
                    tr.appendChild(valor)
                    table.appendChild(tr)
                }
            }
        })
}

async function countPages(url) {
    await fetch(url)
        .then(response =>{
            if(response.ok){
                return response.json()
            }else{
                throw new Error("Erro na requisição")
            }
        })
        .then(data =>{
            totalPaginas = Math.ceil(data.transacoes/limite)
        })
    checkPages()
}

document.addEventListener("DOMContentLoaded", ()=>{
    initialLoad(selects)
})

let newCategoria = document.querySelector("#buttonCategory")
newCategoria.addEventListener("click", ()=>{
    let nome = document.querySelector("#formName").value
    let json = {
        "nome": nome
    }
    fetch(`${BASE_URL}/categoria`, {
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
    fetch(`${BASE_URL}/transacao`, {
        method:"POST",
        body: JSON.stringify(json),
        headers:{ "Content-type": "application/json; charset=UTF-8"}
    })
        .then(response =>{
            if(response.status=201){
                document.querySelector("#tbody").innerHTML=""
                filterTable()
            }
        })
})

let modalTransaction = document.querySelector("#transactionModal")
modalTransaction.addEventListener('show.bs.modal', event=>{
    let title = document.querySelector("#transactionTitle")
    newTransaction.style.display = "block"
    editTransaction.style.display = "none"
    title.textContent = "Nova Transação"
    clearForm("#transactionModal")
    let button = event.relatedTarget;
    let id = button.getAttribute('data-bs-id')
    if(id!=null){
        title.textContent = "Editar Transação"
        setupEdit(id)
    }
})

let modalCategory = document.querySelector("#categoryModal")
modalCategory.addEventListener('show.bs.modal',()=>{
    clearForm("#categoryModal")
})

let summary = document.querySelector("#resumoModal")
summary.addEventListener('show.bs.modal', ()=>{
    loadResumo()
})

let buttonFilter = document.querySelector("#buttonFilter")
buttonFilter.addEventListener('click', ()=>{
    paginaAtual = 1
    document.querySelector("#tbody").innerHTML=""
    filterTable()
})

let buttonPrevious = document.querySelector("#btnPrevious")
buttonPrevious.addEventListener('click', ()=>{
    paginaAtual--
    document.querySelector("#tbody").innerHTML=""
    filterTable()
    checkPages()
})

let buttonNext = document.querySelector("#btnNext")
buttonNext.addEventListener('click', ()=>{
    paginaAtual++
    document.querySelector("#tbody").innerHTML=""
    filterTable()
    checkPages()
})

let editTransaction = document.querySelector("#buttonEdit")
editTransaction.addEventListener('click', ()=>{
    let dataTransacao = new Date(document.querySelector("#formData").value).toJSON()
    let json = {
        "valor": document.querySelector("#formValue").value,
        "descricao": document.querySelector("#formDescription").value,
        "tipo": document.querySelector("#formTipo").value,
        "idCategoria":document.querySelector("#formCategoria").value,
        "data": dataTransacao,
        "id": document.querySelector("#formId").value
    }
    fetch(`${BASE_URL}/transacao`, {
        method:"PUT",
        body: JSON.stringify(json),
        headers:{ "Content-type": "application/json; charset=UTF-8"}
    })
        .then(response =>{
            if(response.status=201){
                document.querySelector("#tbody").innerHTML=""
                filterTable()
            }
        })
})