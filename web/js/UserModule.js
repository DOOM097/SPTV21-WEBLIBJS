import {checkAuthorization} from './main.js';
class UserModule{
    printNewUserForm(){
        let html = 
        `<h3 class="w-100 d-flex justify-content-center mt-5">Регистрация читателя</h3>
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
                        <label for="inputPhone" class="col-sm-3 col-form-label">Телефон</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputPhone" name="phone">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputLogin" class="col-sm-3 col-form-label">Логин</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputLogin" name="login">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputPassword" class="col-sm-3 col-form-label">Пароль</label>
                        <div class="col-sm-9">
                          <input type="password" class="form-control" id="inputPassword" name="password">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <div class="col-sm-12 d-flex justify-content-end">
                            <button id="btnNewUser" class="btn btn-primary col-sm-9" type="submit">Зарегистрировать</button>
                        </div>
                    </div>
                </div>
            </form>
          </div>
        </div>`;
        document.getElementById('content').innerHTML = html;
        let btnNewUser = document.getElementById('btnNewUser');
        btnNewUser.addEventListener('click',e=>{
            e.preventDefault();
            userModule.sentNewUserData();
        });
    }
    sentNewUserData(){
        const newUserData = {
            "firstname": document.getElementById("inputFirstname").value,
            "lastname": document.getElementById('inputLastname').value,
            "phone": document.getElementById('inputPhone').value,
            "login": document.getElementById('inputLogin').value,
            "password": document.getElementById('inputPassword').value
        }
        fetch('createUser',{
            method: "POST",
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
            body: JSON.stringify(newUserData)
        })
                .then(response => response.json())
                .then(response =>{
                    if(response.status){
                        document.getElementById('info').innerHTML=response.info;
                        document.getElementById('content').innerHTML = '';
                    }else{
                        document.getElementById('info').innerHTML=response.info;
                    }
                })
                .catch(error => document.getElementById('info').innerHTML='Пользователь не сохранен ('+e+')');


    }
    printProfileForm(){
        const user = JSON.parse(sessionStorage.getItem('authUser'));
        if(user === null){
            document.getElementById('info').innerHTML='Войдите!';
            return;
        }
        const html= 
        `<h3 class="w-100 d-flex justify-content-center mt-5">Профиль пользователя</h3>
          <div class="w-100 p-3 d-flex justify-content-center">
            <form action="createReader" method="POST">
                <div class="card border-0 m-2" style="width: 30rem;">
                    <div class="mb-3 row">
                        <label for="inputName" class="col-sm-3 col-form-label">Имя</label>
                        <div class="col-sm-9">
                          <input type="hidden" class="form-control" id="inputUserId" name="userId" value="${user.id}">
                          <input type="text" class="form-control" id="inputFirstname" name="firstname" value="${user.firstname}">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputLastname" class="col-sm-3 col-form-label">Фамилия</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputLastname" name="lastname"  value="${user.lastname}">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputPhone" class="col-sm-3 col-form-label">Телефон</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputPhone" name="phone"   value="${user.phone}">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputLogin" class="col-sm-3 col-form-label">Логин</label>
                        <div class="col-sm-9">
                          <input type="text" class="form-control" id="inputLogin" name="login" readonly value="${user.login}">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <label for="inputPassword" class="col-sm-3 col-form-label">Пароль</label>
                        <div class="col-sm-9">
                          <input type="password" class="form-control" id="inputPassword" name="password">
                        </div>
                    </div>
                    <div class="mb-3 row">
                        <div class="col-sm-12 d-flex justify-content-end">
                            <button id="btnChangeProfile" class="btn btn-primary col-sm-9" type="button">Изменить профиль</button>
                        </div>
                    </div>
                </div>
            </form>
          </div>
        </div>`;
        document.getElementById('content').innerHTML = html;
        let btnChangeProfile = document.getElementById('btnChangeProfile');
        btnChangeProfile.addEventListener('click',e=>{
            userModule.sendNewProfile();
        })
    }
    sendNewProfile(){
        const userProfile = {
            "userId": document.getElementById("inputUserId").value,
            "firstname": document.getElementById("inputFirstname").value,
            "lastname": document.getElementById('inputLastname').value,
            "phone": document.getElementById('inputPhone').value,
            "login": document.getElementById('inputLogin').value,
            "password": document.getElementById('inputPassword').value
        }
        fetch('changeUserProfile',{
            method: "POST",
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
            body: JSON.stringify(userProfile)
        })
                .then(response => response.json())
                .then(response =>{
                    if(response.status){
                        document.getElementById('info').innerHTML=response.info;
                        document.getElementById('content').innerHTML = '';
                        sessionStorage.setItem('authUser',JSON.stringify(response.user));
                        checkAuthorization();
                        userModule.printProfileForm();
                    }else{
                        document.getElementById('info').innerHTML=response.info;
                    }
                })
                .catch(error => document.getElementById('info').innerHTML='Профиль пользователя изменить не удалось ('+error+')');

    }
    printListUsers(){
        fetch('getListUsers',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include"
        })
        .then(response=>response.json())
        .then(response=>{
            if(!response.status){
                document.getElementById('info').innerHTML=response.info;
                return;
            }
            document.getElementById('content').innerHTML=
                `<h3 class="w-100 d-flex justify-content-center mt-5">Список пользователей</h3>
                   <div class="w-100 p-3 d-flex justify-content-center">
                        <div class="card m-2 border-0" style="width: 45rem;">
                            <div class="card-body">
                                <div class="container text-center">
                                    <table class="table" id="table">
                                        <thead>
                                        <tr><th scope="col">№</th><th scope="col" class="text-start">Пользователь</th></tr>
                                        </thead>

                                    </table>
                                </div>
                            </div>
                         </div>
                   </div>`;
            let table = document.getElementById('table');
            let tbody = document.createElement("tbody");
            for (let i = 0; i < response.users.length; i++) {
                let user = response.users[i];
                let row = document.createElement("tr");
                let cell1 = document.createElement("td");
                cell1.textContent = i+1;
                let cell2 = document.createElement("td");
                cell2.setAttribute("class","text-start");
                cell2.textContent = `${user.firstname} ${user.lastname} (${user.login})`;
                row.appendChild(cell1);
                row.appendChild(cell2);
                tbody.appendChild(row);
                
            }
            table.appendChild(tbody);
        })
        .catch(error=>{
            console.log(error)
        });
    }
};
const userModule = new UserModule();
export {userModule};

