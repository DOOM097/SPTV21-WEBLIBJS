import{coverModule} from './CoverModule.js';

class BookModule{
    printFormAddBook(){
        document.getElementById('content').innerHTML =`<h3 class="w-100 d-flex justify-content-center mt-5">Новая книга</h3>
        <a id='linkAddCover' class="w-100 d-flex justify-content-center mt-5" href=''>Добавить обложку</a>
          <div class="w-100 p-3 d-flex justify-content-center">
            <form action="createBook" method="POST">
                <div class="card border-0 m-2" style="width: 30rem;">
                    <div class="mb-3 row">
                        <label for="inputName" class="col-sm-3 col-form-label">Название книги</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputBookName" name="bookName">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputLastname" class="col-sm-3 col-form-label">Год издания</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputPublishedYear" name="publishedYear">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputBirthYear" class="col-sm-3 col-form-label">Количество экземпрляров</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputQuantity" name="quantity">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="selectedAuthors" class="col-sm-3 col-form-label">Выберите авторов книги</label>
                        <select id="selectedAuthors" multiple="true" class="form-select" aria-label="">
                            
                        </select>
                    </div>
                    <div class="mb-3 row">
                        <label for="selectedCovers" class="col-sm-3 col-form-label">Выберите обложку для книги</label>
                        <select id="selectedCover" class="form-select" aria-label="">
                            
                        </select>
                    </div>
                    
                    <div class="mb-3 row">
                        <div class="col-sm-12 d-flex justify-content-end">
                            <button id="btnNewBook" class="btn btn-primary col-sm-9" type="submit">Добавить</button>
                        </div>
                    </div>
                </div>
            </form>
          </div>
        </div>`;
        //Добываем список всех авторов и заполняем селект
        fetch('getListAuthors',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
        })
        .then(response=>response.json())
        .then(response=>{
            let selectedAuthors = document.getElementById('selectedAuthors');
            for(let i = 0;i< response.authors.length;i++){
                let option = document.createElement('option');
                option.text = response.authors[i].firstname+' '+response.authors[i].lastname;
                option.value = response.authors[i].id;
                selectedAuthors.appendChild(option);
            };
        })
        .catch(error=>document.getElementById('info').innerHTML = 'error: '+error);
       //Добываем список всех обложек и заполняем селект
        fetch('getListCovers',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
        })
        .then(response=>response.json())
        .then(response=>{
            let selectedCover = document.getElementById('selectedCover');
            for(let i = 0;i< response.covers.length;i++){
                let option = document.createElement('option');
                option.text = response.covers[i].description;
                option.value = response.covers[i].id;
                selectedCover.appendChild(option);
            };
        })
        .catch(error=>document.getElementById('info').innerHTML = 'error: '+error);

        let btnNewAuthor = document.getElementById('btnNewBook');
        btnNewAuthor.addEventListener('click',e=>{
            e.preventDefault();
            bookModule.creadeBook();
        });
        let linkAddCover = document.getElementById('linkAddCover');
        linkAddCover.addEventListener('click',e=>{
            e.preventDefault();
            coverModule.printFormNewCover();
        });
        
    }
    creadeBook(){
        let selectedAuthors = document.getElementById('selectedAuthors');
        let authors= Array.from(selectedAuthors.selectedOptions).map(option => option.value);
        let book = {
            bookName: document.getElementById('inputBookName').value,
            publishedYear: document.getElementById('inputPublishedYear').value,
            quantity: document.getElementById('inputQuantity').value,
            cover: document.getElementById('selectedCover').value,
            authors: authors,
        }
        fetch('createBook',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
            body: JSON.stringify(book)
        })
        .then(response=>response.json())
        .then(response=>{
            document.getElementById('info').innerHTML = response.info;
            bookModule.printFormAddBook();
        })
        .catch(error => document.getElementById('info').innerHTML = 'Ошибка формирования JSON: '+error)
    }
    printFormListBooks(){
        document.getElementById('content').innerHTML =
        ` <h3 class="w-100 d-flex justify-content-center mt-5">Список книг</h3>
            <div id="containerBooks" class="w-100 p-3 d-flex justify-content-center">
                
            </div>`;
        fetch('getListBooks',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include"
        }).then(response => response.json())
          .then(response=>{
                let containerBooks = document.getElementById('containerBooks');
                for(let i=0;i< response.books.length;i++){
                    let card = document.createElement('div');
                    card.setAttribute('class','card m-2');
                    card.setAttribute('style', 'width: 13rem');
                    card.innerHTML =`
                            <a id="book${response.books[i].id}" class="text-decoration-none" href=""> Book </a>
                            <a id="editBook${response.books[i].id}" class="editBookLink" href="#">Редактировать книгу</a>`;
                             
                    containerBooks.insertAdjacentElement('beforeend',card);
                    let abook = document.getElementById("book"+response.books[i].id);
                    abook.addEventListener('click',e=>{
                        e.preventDefault();
                        bookModule.printBookForm(response.books[i].id);
                    });
                    let editBook = document.getElementById("editBook"+response.books[i].id);
                    editBook.addEventListener('click',e=>{
                        e.preventDefault();
                        bookModule.editBookForm(response.books[i].id);
                    });
                }
            })
          .catch(error => document.getElementById('info').innerHTML="error: "+error);
        
        
    }
    printBookForm(bookId){
        //console.log("printBookForm("+bookId+");");
        document.getElementById('content').innerHTML =
        ` <h3 class="w-100 d-flex justify-content-center mt-5">Книга</h3>
            <div id="containerBook" class="w-100 p-3 d-flex justify-content-center">
                
            </div>`;
        const params = {
            "bookId": bookId
        };
        const queryString = new URLSearchParams(params).toString();

        fetch('getBook?'+queryString,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include"
        }).then(response => response.json())
          .then(response=>{
                let containerBook = document.getElementById('containerBook');
                let card = document.createElement('div');
                card.setAttribute('class','card m-2');
                card.setAttribute('style', 'width: 13rem');
                card.innerHTML =`
                            <img src="insertFile/${response.book.cover.url}" width='100%' heigth='200px'/>
                            <a id="loadBook${response.book.id}" class="text-decoration-none" href="">Читать книгу</a>
                            <a id="editTextBook${response.book.id}" class="text-decoration-none" href="#">Редактировать текст книги</a>`;
                             
                containerBook.insertAdjacentElement('beforeend',card);
                let loadBook = document.getElementById("loadBook"+response.book.id);
                loadBook.addEventListener('click',e=>{
                    e.preventDefault();
                    bookModule.printTextBook(response.book.id);
                });
                let editTextBook = document.getElementById("editTextBook"+response.books.id);
                editTextBook.addEventListener('click',e=>{
                    e.preventDefault();
                    bookModule.editTextBookForm(response.book.id);
                });
           })
           .catch(error => document.getElementById('info').innerHTML="error: "+error);
    }
    
    
    editBookForm(bookId){
        console.log("editBookForm("+bookId+");");
    }
    editTextBookForm(bookId){
        console.log("editTextBookForm("+bookId+");");
    }
}
const bookModule = new BookModule();
export {bookModule};


