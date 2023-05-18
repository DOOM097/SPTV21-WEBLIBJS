
class AdminModule{
    printFormChangeRole(){
        document.getElementById('content').innerHTML =
       `<h3 class="w-100 d-flex justify-content-center mt-5">Администрирование</h3>
        <div class="w-100 p-3 d-flex justify-content-center">
            <form action="changeRole" method="POST">
                <div class="card border-0 m-2" style="width: 51rem;">
                    <div class="mb-3 row">
                        <label for="user_id" class="col-sm-4 col-form-label text-end">Список пользователей и их ролей</label>
                        <div class="col-sm-8">
                            <select name="userId" id="userSelect" class="form-select">
                                <option value="" selected disabled>Выберите пользователя</option>
                                
                            </select>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="listRoles" class="col-sm-4 col-form-label text-end">Список ролей</label>
                        <div class="col-sm-8">
                            <select name="selectedRole" id="roleSelect" class="form-select">
                                <option value="" selected disabled>Выберите роль</option>
                                
                            </select>
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <div class="col-sm-12 d-flex justify-content-end">
                            <button id="addRole" class="btn btn-primary m-2" type="submit">Добавить роль</button>
                            <button id="removeRole" class="btn btn-primary m-2" type="submit">Удалить роль</button>
                        </div>
                    </div>`;
        fetch('changeRolesData',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
        })
        .then(response=>response.json())
        .then(response=>{
            let userSelect = document.getElementById('userSelect');
            for(let i = 0;i< response.users.length;i++){
                let option = document.createElement('option');
                option.text = response.users[i].firstname+' '+response.users[i].lastname+'. '+response.users[i].login+': {';
                for(let j=0;j<response.users[i].roles.length;j++){
                    if(j === 0){
                        option.text += response.users[i].roles[j];
                    }else{
                        option.text += ' - '+response.users[i].roles[j];
                    }
                    
                };
                option.text += '}';
                option.value = response.users[i].id;
                userSelect.appendChild(option);
            };
            let roleSelect = document.getElementById('roleSelect');
            for(let i = 0;i< response.roles.length;i++){
                let option = document.createElement('option');
                option.text = response.roles[i];
                option.value = response.roles[i];
                roleSelect.appendChild(option);
            };
        })
        .catch(error=>document.getElementById('info').innerHTML = 'error: '+error);
        let addRole = document.getElementById('addRole');
        addRole.addEventListener('click',e=>{
            e.preventDefault();
            adminModule.addRole();
        });
        let removeRole = document.getElementById('removeRole');
        removeRole.addEventListener('click',e=>{
            e.preventDefault();
            adminModule.removeRole();
        });
    }
    addRole(){
        let data = {
            userId: document.getElementById('userSelect').value,
            role: document.getElementById('roleSelect').value
        }
        fetch('addRole',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
            body: JSON.stringify(data)
        })
        .then(response=>response.json())
        .then(response=>{
            document.getElementById('info').innerHTML = response.info;
            adminModule.printFormChangeRole();
        })
        .catch(error => document.getElementById('info').innerHTML = 'Ошибка формирования JSON: '+error)
    }
    removeRole(){
        let data = {
            userId: document.getElementById('userSelect').value,
            role: document.getElementById('roleSelect').value
        }
        fetch('removeRole',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
            body: JSON.stringify(data)
        })
        .then(response=>response.json())
        .then(response=>{
            document.getElementById('info').innerHTML = response.info;
            adminModule.printFormChangeRole();
        })
        .catch(error => document.getElementById('info').innerHTML = 'Ошибка формирования JSON: '+error)
    }
}
const adminModule = new AdminModule();
export {adminModule};


