import{bookModule} from './BookModule.js';
class CoverModule{
    printFormNewCover(){
        document.getElementById('content').innerHTML = 
                ` <div class="w-100 d-flex justify-content-center p-5">
           <div class="card border-0" style="width: 38rem;">
            <div class="card-body">
                <form id='formAddCover' action="" method="POST" enctype="multipart/form-data">
                    <div class="mb-3 row">
                        <label for="fileName" class="col-sm-4 col-form-label">Обложка книги:</label>
                        <div class="col-sm-8">
                          <input type="file" class="form-control" id="fileName" name="file" value="">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="descript" class="col-sm-4 col-form-label">Описание файла</label>
                        <div class="col-sm-8">
                          <input type="text" class="form-control" id="descript" name="description" value="">
                        </div>
                    </div>
                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                        <button id="btnAddCover" type="submit" class="btn btn-primary me-md-2">Загрузить</button>
                    </div>
                </form>
            </div>
           </div>
        </div>`;
        let btnAddCover = document.getElementById('btnAddCover');
        btnAddCover.addEventListener('click',e=>{
            e.preventDefault();
            coverModule.sendCover();
        })
    }
    sendCover(){
        fetch('uploadCover',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
            body: JSON.stringify(new FormData(document.getElementById('formAddCover')))
        })
                .then(response => response.json())
                .then(response =>{
                    document.getElementById('info').innerHTML=response.info;
                    bookModule.printFormAddBook();
                })
                .catch(error => console.log("error: "+error));
    }
};
const coverModule = new CoverModule();
export {coverModule};