import {userModule} from './UserModule.js';
import {loginModule} from './LoginModule.js';
import {adminModule} from './AdminModule.js';
import {authorModule} from './AuthorModule.js';
import {bookModule} from './BookModule.js';

export {checkAuthorization};

let newUser = document.getElementById('newUser');
newUser.addEventListener('click',e=>{
    e.preventDefault();
    userModule.printNewUserForm();
});
let userProfile = document.getElementById('userProfile');
userProfile.addEventListener('click',e=>{
    e.preventDefault();
    userModule.printProfileForm();
});
let logIn = document.getElementById('logIn');
logIn.addEventListener('click',e=>{
    e.preventDefault();
    loginModule.printFormLogIn();
});
let logout = document.getElementById('logout');
logout.addEventListener('click',e=>{
    e.preventDefault();
    loginModule.sendLogout();
});
let listUsers = document.getElementById('listUsers');
listUsers.addEventListener('click',e=>{
    e.preventDefault();
    userModule.printListUsers();
});
let addBook = document.getElementById('addBook');
addBook.addEventListener('click',e=>{
    e.preventDefault();
    bookModule.printFormAddBook();
});
let changeRole = document.getElementById('changeRole');
changeRole.addEventListener('click',e=>{
    e.preventDefault();
    adminModule.printFormChangeRole();
});
let addAuthor = document.getElementById('addAuthor');
addAuthor.addEventListener('click',e=>{
    e.preventDefault();
    authorModule.printFormAddAuthor();
});


function checkAuthorization(){
    
    let authUser = sessionStorage.getItem("authUser");
    if(authUser === null){
        console.log("Авторизация отсутствует");
        //show
        logIn.hidden = false;
        newUser.hidden=false;
        document.getElementById('listAuthors').hidden=false;
        
        //hidden
        document.getElementById('userLogin').hidden = true;
        logout.hidden = true;
        userProfile.hidden = true;
        listUsers.hidden=true;
        addBook.hidden=true;
        document.getElementById('takeOnBook').hidden=true;
        document.getElementById('returnBook').hidden=true;
        document.getElementById('addAuthor').hidden=true;
        document.getElementById('liAdministrator').hidden=true;
        return;
    }
    authUser = JSON.parse(authUser);
    console.log(authUser);
    let USER = false;
    let MANAGER = false;
    let ADMINISTRATOR = false;
    for(let index in authUser.roles){
        if("USER" === authUser.roles[index]) USER = true;
        if("MANAGER" === authUser.roles[index]) MANAGER = true;
        if("ADMINISTRATOR" === authUser.roles[index]) ADMINISTRATOR = true;
    }
    
    if(USER){
        //show
        logout.hidden = false;
        document.getElementById('userLogin').hidden = false;
        document.getElementById('userLogin').innerHTML = 'Вы вошли как '+authUser.login;
        userProfile.hidden=false;
        document.getElementById('takeOnBook').hidden=false;
        document.getElementById('returnBook').hidden=false;
        document.getElementById('listAuthors').hidden=false;
        //hidden
        logIn.hidden = true;
        
        
    }
    if(MANAGER){
        //show
        newUser.hidden=false;
        userProfile.hidden=false;
        addBook.hidden=false;
        document.getElementById('addAuthor').hidden=false;
        //hidden
        
        
    }
    if(ADMINISTRATOR){
        //all show
        newUser.hidden=false;
        userProfile.hidden=false;
        listUsers.hidden=false;
        document.getElementById('liAdministrator').hidden=false;
    }
    
}
checkAuthorization();
