let url = 'ec2-3-137-191-168.us-east-2.compute.amazonaws.com';
//let url = 'localhost';
/* When the window object is loaded (load event), go ahead and fetch all of the data about students
and populate the student table */
window.addEventListener('load', getAndPopulateClients);

async function getAndPopulateClients() {
    let res = await fetch(`http://${url}:8080/clients`);

    let clientsArray = await res.json();

    populateClientsTable(clientsArray);

}

function populateClientsTable(array) {
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
        td1.innerText = personObject.client_id;
        let td2 = document.createElement('td');
        td2.innerText = personObject.first_name;
        let td3 = document.createElement('td');
        td3.innerText = personObject.last_name;
        let td5 = document.createElement('td');
        td5.innerText = personObject.num_accounts;

        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td5);

        tbody.appendChild(tr);
    }

}


let clientSubmitButton = document.querySelector('#client-submit');
clientSubmitButton.addEventListener('click', async () => {

    let submissionButton = document.getElementById('client-submit');

    if(submissionButton.textContent == 'Add Client') {

        let firstNameInputElement = document.getElementById('first-name-form');
        let lastNameInputElement = document.getElementById('last-name-form');
        
        let personObjectToAdd = {
            'first_name': firstNameInputElement.value,
            'last_name': lastNameInputElement.value,
        };
    
        console.log(personObjectToAdd);
    
        let res = await fetch(`http://${url}:8080/clients`, {
            method: 'POST',
            body: JSON.stringify(personObjectToAdd)
        });        

    } else if(submissionButton.textContent == 'Update Client') {

        let firstNameInputElement = document.getElementById('first-name-form');
        let lastNameInputElement = document.getElementById('last-name-form');
        let clientIdInputElement = document.getElementById('client-id-form');

        let personObjectToUpdate = {

            'first_name' : firstNameInputElement.value,
            'last_name' : lastNameInputElement.value,
            'client_id' : clientIdInputElement.value

        }

        let res = await fetch(`http://${url}:8080/clients/${clientIdInputElement.value}`, {

            method : 'PUT',
            body: JSON.stringify(personObjectToUpdate)

        });

    } else if(submissionButton.textContent == 'Delete Client') {

        let clientIdInputElement = document.getElementById('client-id-form');

        let res = await fetch(`http://${url}:8080/clients/${clientIdInputElement.value}`, {

            method: 'DELETE',

        })

    }
                

    getAndPopulateClients();
});

let actionSelector = document.querySelector('#action_selector');
actionSelector.addEventListener('change', actionSelectorChange);

function actionSelectorChange() {

    action = actionSelector.value; 

    switch(action) {

        case 'Add Client':

            addClientSelected();
            break;

        case 'Delete Client':

            deleteClientSelected();
            break;

        case 'Update Client':   

            updateClientSelected();
            break;



    }

}

function addClientSelected() {


    /*<input id="first-name" type="text" />*/
    let formBody = document.querySelector('.form-container');
    removeAllFields();

    let clientSubmitButton = document.querySelector('#client-submit');
    clientSubmitButton.textContent = 'Add Client';

    let firstNameLabel = document.createElement('label');
    let lastNameLabel = document.createElement('label');
    let firstNameForm = document.createElement('input');
    let lastNameForm = document.createElement('input');



    firstNameLabel.textContent = 'First Name:';
    firstNameLabel.id = 'first-name-label';
    lastNameLabel.textContent = 'Last Name:';
    lastNameLabel.id = 'last-name-label';

    firstNameForm.id = 'first-name-form';
    firstNameForm.type = 'text';

    lastNameForm.id = 'last-name-form';
    lastNameForm.type = 'text';
 

    formBody.appendChild(firstNameLabel);
    formBody.appendChild(firstNameForm);

    formBody.appendChild(lastNameLabel);
    formBody.appendChild(lastNameForm);


}

function updateClientSelected() {

    let formBody = document.querySelector('.form-container');
   removeAllFields();

    let clientSubmitButton = document.querySelector('#client-submit');
    clientSubmitButton.textContent = 'Update Client';

    let clientIdLabel = document.createElement('label');
    let firstNameLabel = document.createElement('label');
    let lastNameLabel = document.createElement('label');

    let clientIdForm = document.createElement('input');
    let firstNameForm = document.createElement('input');
    let lastNameForm = document.createElement('input');

    clientIdLabel.textContent = "Client ID";
    clientIdLabel.id = 'client-id-label';
    firstNameLabel.textContent = 'First Name:';
    firstNameLabel.id = 'first-name-label';
    lastNameLabel.textContent = 'Last Name:';
    lastNameLabel.id = 'last-name-label';


    clientIdForm.id = 'client-id-form';
    clientIdForm.type = 'text';

    firstNameForm.id = 'first-name-form';
    firstNameForm.type = 'text';

    lastNameForm.id = 'last-name-form';
    lastNameForm.type = 'text';

    formBody.appendChild(clientIdLabel);
    formBody.appendChild(clientIdForm);

    formBody.appendChild(firstNameLabel);
    formBody.appendChild(firstNameForm);

    formBody.appendChild(lastNameLabel);
    formBody.appendChild(lastNameForm);


}

function deleteClientSelected() {

    let formBody = document.querySelector('.form-container');
   removeAllFields();

    let clientSubmitButton = document.querySelector('#client-submit');
    clientSubmitButton.textContent = 'Delete Client';

    let clientIdLabel = document.createElement('label');
    let clientIdForm = document.createElement('input');

    clientIdLabel.textContent = "Client ID";
    clientIdLabel.id = 'client-id-label';

    clientIdForm.id = 'client-id-form';
    clientIdForm.type = 'text';

    formBody.appendChild(clientIdLabel);
    formBody.appendChild(clientIdForm);

}

function removeAllFields() {

    let firstNameForm = document.getElementById('first-name-form');
    let firstNameLabel = document.getElementById('first-name-label');
    
    let lastNameForm = document.getElementById('last-name-form');
    let lastNameLabel = document.getElementById('last-name-label');

    let clientIdForm = document.getElementById('client-id-form');
    let clientIdLabel = document.getElementById('client-id-label');

    let elementArray = [];

    elementArray.push(firstNameLabel);
    elementArray.push(firstNameForm);
    elementArray.push(lastNameForm);
    elementArray.push(lastNameLabel);
    elementArray.push(clientIdForm);
    elementArray.push(clientIdLabel);

    for(let i = 0; i < elementArray.length; i++) {
        if(elementArray[i] == null) {



        }

        else{

            elementArray[i].parentNode.removeChild(elementArray[i]);


        }
    



    }

}

function removeAllChildNodes(parent) {

    while(parent.firstChild) {

        parent.removeChild(parent.firstChild);

    }

}