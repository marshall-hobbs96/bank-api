let url = 'ec2-3-137-191-168.us-east-2.compute.amazonaws.com';
//let url = 'localhost'
/* When the window object is loaded (load event), go ahead and fetch all of the data about students
and populate the student table */
window.addEventListener('load', getAndPopulateAccounts);

async function getAndPopulateAccounts() {
    let res = await fetch(`http://${url}:8080/accounts`);

    let accountsArray = await res.json();

    populateAccountsTable(accountsArray);

}

function populateAccountsTable(array) {
    let tbody = document.querySelector('.main-section .table-container tbody');
    tbody.innerHTML = '';
    /*
        we could iterate over the array using
        1. traditional for loop ( for (let i = 0; i < ...; i++) {} )
        2. for of: iterates over the actual elements (or values)
        3. for in: iterates over the indices of whatever you are iterating over (or keys)
    */

    for (let personObject of array) {
        let tr = document.createElement('tr');

        let td1 = document.createElement('td');
        td1.innerText = personObject.account_id;
        let td2 = document.createElement('td');
        td2.innerText = personObject.client_id;
        let td3 = document.createElement('td');
        td3.innerText = personObject.account_type;
        let td5 = document.createElement('td');
        td5.innerText = personObject.funds;

        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td5);

        tbody.appendChild(tr);
    }

}


let accountSubmitButton = document.querySelector('#account-submit');
accountSubmitButton.addEventListener('click', async () => {

    let clientIdInputElement = document.querySelector('#client-id');
    let accountTypeInputElement = document.querySelector('#account-type');
    let fundsInputElemenet = document.querySelector("#initial-funds");
    
    let personObjectToAdd = {
        'client_id': clientIdInputElement.value,
        'account_type': accountTypeInputElement.value,
        'funds' : fundsInputElemenet.value
    };

    console.log(personObjectToAdd);

    let res = await fetch(`http://${url}:8080/clients/${clientIdInputElement.value}/accounts`, {
        method: 'POST',
        body: JSON.stringify(personObjectToAdd)
    });
    /* JSON.stringify(obj) takes an object and converts it into a JSON String */
    /* JSON.parse(string) takes a JSON string and turns it into a JavaScript object */

    getAndPopulateAccounts();
});