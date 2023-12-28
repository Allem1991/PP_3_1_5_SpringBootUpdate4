const usersUrl = '/admin/users/';
const rolesUrl = '/admin/roles/';

const allUsers = fetch(usersUrl).then(response => response.json())
const allRoles = fetch(rolesUrl).then(response => response.json())

const deleteUserModal = new bootstrap.Modal(document.getElementById('deleteUserModal'))
const editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'))

const on = (element, event, selector, handler) => {
    element.addEventListener(event, e => {
        if (e.target.closest(selector)) {
            handler(e.target)
        }
    })
}

allUsers.then(users => {
        let result = ''
        for (const i in users) {
            let roles = ''
            users[i].roles.forEach(role => {
                roles += ' '
                roles += role.name.replace("ROLE_", "")
            })
            result += `<tr>
                    <td>${users[i].id}</td>
                    <td>${users[i].name}</td>
                    <td>${users[i].lastname}</td>
                    <td>${users[i].age}</td>
                    <td>${users[i].email}</td>
                    <td>${roles}</td>
                    <td>
                        <button type="button" class="btn btn-warning" id="editUserBtn">Edit</button>
                    </td>
                    <td>
                        <button type="button" class="btn btn-danger" id="deleteUserBtn">Delete</button>
                    </td>
                    </tr>`
        }
        document.getElementById("users-info-table").innerHTML = result
    }
)

fillRoles = function (elementId) {
    allRoles.then(roles => {
        let result = ''
        for (const i in roles) {
            result += `<option value = ${roles[i].id}>${roles[i].name.replace("ROLE_", "")}</option>`
        }
        document.getElementById(elementId).innerHTML = result
    })
}
fillRoles("role_select")

document.getElementById('newUserForm').addEventListener('submit', (e) => {
    e.preventDefault()
    let role = document.getElementById('role_select')
    let rolesAddUser = []
    let rolesAddUserValue = ''
    for (let i = 0; i < role.options.length; i++) {
        if (role.options[i].selected) {
            rolesAddUser.push({id: role.options[i].value, name: 'ROLE_' + role.options[i].innerHTML})
            rolesAddUserValue += role.options[i].innerHTML + ' '
        }
    }
    fetch(usersUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            name: document.getElementById('name').value,
            lastname: document.getElementById('lastname').value,
            age: document.getElementById('age').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            roles: rolesAddUser
        })
    })
        .then(response => response.json())
        .then(user => {
            let newRow = document.createElement('tr')
            newRow.innerHTML = `<tr>
                           <td>${user.id}</td>
                           <td>${user.name}</td>
                           <td>${user.lastname}</td>
                           <td>${user.age}</td>
                           <td>${user.email}</td>
                           <td>${rolesAddUserValue}</td>
                           <td><button type="button" class="btn btn-warning" id="editUserBtn"
                                 id="editUserBtn">Edit</button></td>
                           <td><button type="button" class="btn btn-danger" id="deleteUserBtn">Delete</button></td>
                           </tr>`
            document.getElementById("users-info-table").appendChild(newRow)
            document.getElementById('newUserForm').reset()

        })
    document.getElementById("all-users-tab").click()
})

const idEdit = document.getElementById('id_edit')
const nameEdit = document.getElementById('name_edit')
const lastnameEdit = document.getElementById('lastname_edit')
const ageEdit = document.getElementById('age_edit')
const emailEdit = document.getElementById('email_edit')
const passwordEdit = document.getElementById('password_edit')
const rolesEdit = document.getElementById('role_edit')

let rowEdit = null

on(document, 'click', '#editUserBtn', e => {
    rowEdit = e.parentNode.parentNode

    idEdit.value = rowEdit.children[0].innerHTML
    nameEdit.value = rowEdit.children[1].innerHTML
    lastnameEdit.value = rowEdit.children[2].innerHTML
    ageEdit.value = rowEdit.children[3].innerHTML
    emailEdit.value = rowEdit.children[4].innerHTML
    passwordEdit.value = ''
    let option = ''
    allRoles.then(roles => {
        roles.forEach(role => {
            let selected = rowEdit.children[5].innerHTML.includes(role.name.replace('ROLE_', '')) ? 'selected' : ''
            option += `<option value="${role.id}" ${selected}>${role.name.replace('ROLE_', '')}</option>`
        })
        rolesEdit.innerHTML = option
    })
    editUserModal.show()
})

document.getElementById('edit_user_form').addEventListener('submit', (e) => {
    e.preventDefault()
    let role = document.getElementById('role_edit')
    let rolesUserEdit = []
    let rolesUserEditValue = ''
    for (let i = 0; i < role.options.length; i++) {
        if (role.options[i].selected) {
            rolesUserEdit.push({id: role.options[i].value, name: 'ROLE_' + role.options[i].innerHTML})
            rolesUserEditValue += role.options[i].innerHTML + ' '
        }
    }
    fetch(usersUrl, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: idEdit.value,
            name: nameEdit.value,
            lastname: lastnameEdit.value,
            age: ageEdit.value,
            email: emailEdit.value,
            password: passwordEdit.value,
            roles: rolesUserEdit
        })
    })
        .then(response => response.json())
        .catch(error => console.log(error))
    rowEdit.children[0].innerHTML = idEdit.value
    rowEdit.children[1].innerHTML = nameEdit.value
    rowEdit.children[2].innerHTML = lastnameEdit.value
    rowEdit.children[3].innerHTML = ageEdit.value
    rowEdit.children[4].innerHTML = emailEdit.value
    rowEdit.children[5].innerHTML = rolesUserEditValue
    editUserModal.hide()
})

let rowDelete = null
on(document, 'click', '#deleteUserBtn', e => {
    rowDelete = e.parentNode.parentNode
    document.getElementById('id_delete').value = rowDelete.children[0].innerHTML
    document.getElementById('name_delete').value = rowDelete.children[1].innerHTML
    document.getElementById('lastname_delete').value = rowDelete.children[2].innerHTML
    document.getElementById('age_delete').value = rowDelete.children[3].innerHTML
    document.getElementById('email_delete').value = rowDelete.children[4].innerHTML

    let option = ''
    allRoles.then(roles => {
        roles.forEach(role => {
            if (rowDelete.children[5].innerHTML.includes(role.name.replace('ROLE_', ''))) {
                option += `<option value="${role.id}">${role.name.replace('ROLE_', '')}</option>`
            }
        })
        document.getElementById('role_delete').innerHTML = option
    })
    deleteUserModal.show()
})

document.getElementById('delete_user_form').addEventListener('submit', (e) => {
    e.preventDefault()
    fetch(usersUrl + rowDelete.children[0].innerHTML, {
        method: 'DELETE'
    }).then(() => {
        deleteUserModal.hide()
        rowDelete.parentNode.removeChild(rowDelete)
    })
})