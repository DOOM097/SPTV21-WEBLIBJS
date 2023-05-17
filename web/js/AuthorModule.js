
class AuthorModule{
    printFormAddAuthor(){
        document.getElementById('content').innerHTML =`<h3 class="w-100 d-flex justify-content-center mt-5">Новый автор</h3>
          <div class="w-100 p-3 d-flex justify-content-center">
            <form action="createReader" method="POST">
                <div class="card border-0 m-2" style="width: 30rem;">
                    <div class="mb-3 row">
                        <label for="inputName" class="col-sm-3 col-form-label">Имя</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputFirstname" name="firstname">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputLastname" class="col-sm-3 col-form-label">Фамилия</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputLastname" name="lastname">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputBirthYear" class="col-sm-3 col-form-label">Год рождения</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputBirthYear" name="birthYear">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="selectedBooks" class="col-sm-3 col-form-label">Выбор книг, написанных автором</label>
                        <select id="selectedBooks" multiple="true" class="form-select" aria-label="">
                        </select>
                    </div>
                    
                    <div class="mb-3 row">
                        <div class="col-sm-12 d-flex justify-content-end">
                            <button id="btnNewAuthor" class="btn btn-primary col-sm-9" type="submit">Создать</button>
                        </div>
                    </div>
                </div>
            </form>
          </div>
        </div>`;
        fetch('getListBooks',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
        })
        .then(response=>response.json())
        .then(response=>{
            let selectedBooks = document.getElementById('selectedBooks');
            for(let i = 0;i< response.books.length;i++){
                let option = document.createElement('option');
                option.text = response.books[i].bookName+'. '+response.books[i].publishedYear;
                option.value = response.books[i].id;
                selectedBooks.appendChild(option);
            };
        })
        .catch(error=>document.getElementById('info').innerHTML = 'error: '+error);
        const btnNewAuthor = document.getElementById('btnNewAuthor');
        btnNewAuthor.addEventListener('click',e=>{
            e.preventDefault();
            authorModule.creadeAuthor();
        });
    }
    creadeAuthor(){
        let author = {
            firstname: document.getElementById('inputFirstname').value,
            lastname: document.getElementById('inputLastname').value,
            birthYear: document.getElementById('inputBirthYear').value,
            selectedBooks: document.getElementById('selectedBooks').value
        }
        fetch('createAuthor',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
            body: JSON.stringify(author)
        })
        .then(response=>response.json())
        .then(response=>{
            document.getElementById('info').innerHTML = response.info;
            authorModule.printFormAddAuthor();
        })
        .catch(error => document.getElementById('info').innerHTML = 'Ошибка формирования JSON: '+error)
    }
    
}
const authorModule = new AuthorModule();
export {authorModule};


