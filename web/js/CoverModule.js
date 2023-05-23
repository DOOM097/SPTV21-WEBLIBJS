import{bookModule} from './BookModule.js';
class CoverModule{
    printFormNewCover(){
        document.getElementById('content').innerHTML=
        `
        <div class="w-100 d-flex justify-content-center p-5">
               <div class="card" style="width: 38rem;">
                <div class="card-body">
                    <form id='formAddCover' action="" method="POST" enctype="multipart/form-data">
                        <div class="mb-3 row">
                            <label for="fileName" class="col-sm-5 col-form-label">Обложка книги:</label>
                            <div class="col-sm-7">
                              <input type="file" class="form-control" id="fileName" name="file" value="">
                            </div>
                        </div>
                        <div class="mb-3 row">
                            <label for="descript" class="col-sm-5 col-form-label">Описание файла</label>
                            <div class="col-sm-7">
                              <input type="text" class="form-control" id="descript" name="description" value="">
                            </div>
                        </div>
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <button id='btnAddCover' type="button" class="btn btn-primary me-md-2">Загрузить</button>
                        </div>
                    </form>
                </div>
               </div>
            </div>
                `;
        const btnAddCover = document.getElementById('btnAddCover');
        btnAddCover.addEventListener('click',e=>{
            coverModule.createNewCover();
        });
    };
    async createNewCover(){
        await fetch('createCover',{
            method:'POST',
            credentials: 'include',
            body: new FormData(document.getElementById('formAddCover'))
        })
        .then(response=>response.json())
        .then(response=>{
            document.getElementById('info').innerHTML=response.info;
            bookModule.printCreateBook();
        })
        .cathch(error => {
            document.getElementById('info').innerHTML="Ошибка createNewCover: "+error;
        });
    }
};
const coverModule = new CoverModule();
export {coverModule};