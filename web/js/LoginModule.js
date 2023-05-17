
import {checkAuthorization} from './main.js';

class LoginModule{
    printFormLogIn(){
        let html = `<h3 class="w-100 d-flex justify-content-center mt-5">Авторизация</h3>
            <div class="w-100 p-3 d-flex justify-content-center">
                <form action="login" method="POST">
                    <div class="card border-0 m-2" style="width: 30rem;">
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
                                <button id="btnLogIn" class="btn btn-primary w-25" type="submit">Войти</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>`;
        document.getElementById('content').innerHTML = html;
        document.getElementById('btnLogIn').addEventListener('click',e=>{
            e.preventDefault();
            loginModule.sendCredentinal();
        });
    };
    sendCredentinal(){
        const credential = {
            "login": document.getElementById('inputLogin').value,
            "password": document.getElementById('inputPassword').value
        }
        let promise = fetch('login',{
            method: "POST",
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include",
            body: JSON.stringify(credential)
        });
        promise.then(response => response.json())
                .then(response =>{
                    document.getElementById('info').innerHTML=response.info;
                    sessionStorage.setItem("authUser",JSON.stringify(response.user));
                    document.getElementById('content').innerHTML = '';
                    checkAuthorization();
                })
                .catch(error => {
                    document.getElementById('info').innerHTML='Авторизоваться не удалось ('+error+')';
                    checkAuthorization();
                });


    };
    sendLogout(){
        fetch('logout',{
            method: "GET",
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            credential: "include"           
        })
                .then(response => response.json())
                .then(response =>{
                    document.getElementById('info').innerHTML=response.info;
                    if(response.status){
                        if(sessionStorage.getItem("authUser")!== null){
                            sessionStorage.removeItem("authUser");
                        }
                        checkAuthorization();
                        document.getElementById('content').innerHTML = '';
                    }
                })
                .catch(error => {
                    document.getElementById('info').innerHTML='Выйти не удалось ('+e+')';
                    checkAuthorization();
                });
    };
    
    
};
const loginModule = new LoginModule();
export {loginModule};
