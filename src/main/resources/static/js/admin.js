const apiUrl = '/api/admin';

document.addEventListener('DOMContentLoaded', () => {
    fetchUsers();
    fetchUserProfileAdmin();
});

document.getElementById('showCurrentUser').addEventListener('click', () => {
    document.getElementById('users-table').classList.add('d-none');
    fetchCurrentUser();
});

document.querySelector('.btn-primary').addEventListener('click', () => {
    fetchUsers();
    document.querySelector('#users-table tbody').innerHTML = '';
    document.getElementById('users-table').classList.remove('d-none');
});
function fetchCurrentUser() {
    fetch(`${apiUrl}/profile`)
        .then(response => response.json())
        .then(user => {
            const tableBody = document.querySelector('#users-table tbody');
            tableBody.innerHTML = '';
            const userRow = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.email}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.role.map(r => r.role).join(', ')}</td>
                    <td><button class="btn btn-info" onclick="editUser(${user.id})">Edit</button></td>
                    <td><button class="btn btn-danger" onclick="deleteUser(${user.id})">Delete</button></td>         
                </tr>
            `;
            document.getElementById('users-table').classList.remove('d-none');
            tableBody.innerHTML = userRow;
        })
        .catch(error => console.error('Error fetching current user:', error));
}

function fetchUserProfileAdmin() {
    fetch(`${apiUrl}/profile`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('user-email').textContent = data.email;
            document.getElementById('user-roles').textContent = data.role.map(r => r.role).join(', ');
        })
        .catch(error => console.error('Error fetching user profile:', error));
}

function fetchUsers() {
    fetch(`${apiUrl}/users`)
        .then(response => response.json())
        .then(data => renderUsers(data));
}

function renderUsers(users) {
    const tableBody = document.querySelector('#users-table tbody');
    tableBody.innerHTML = '';
    users.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.role.map(r => r.role).join(', ')}</td>
                <td><button class="btn btn-info" onclick="editUser(${user.id})">Edit</button></td>
                <td><button class="btn btn-danger" onclick="deleteUser(${user.id})">Delete</button></td>
            `;
        tableBody.appendChild(row);
    });
}
function fetchRoles() {
    return fetch(`${apiUrl}/roles`)
        .then(response => response.json())
        .then(roles => {
            const rolesContainer = document.getElementById('roles-container');
            rolesContainer.innerHTML = '';
            roles.forEach(role => {
                const checkboxContainer = document.createElement('div');
                checkboxContainer.className = 'form-check';

                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.className = 'form-check-input';
                checkbox.id = `role-${role.id}`;
                checkbox.name = 'roles';
                checkbox.value = role.id;

                const label = document.createElement('label');
                label.className = 'form-check-label';
                label.htmlFor = `role-${role.id}`;
                label.textContent = role.role;

                checkboxContainer.appendChild(checkbox);
                checkboxContainer.appendChild(label);
                rolesContainer.appendChild(checkboxContainer);
            });
        })
        .catch(error => console.error('Error fetching roles:', error));
}

function showAddUserForm() {
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('userModalTitle').textContent = 'Add User';
    fetchRoles().then(() => {
        $('#userModal').modal('show');
    });
}

async function editUser(id) {
    try {
        const response = await fetch(`${apiUrl}/users/${id}`);
        if (!response.ok) {
            throw new Error(`Failed to fetch user: ${response.statusText}`);
        }
        const user = await response.json();

        document.getElementById('userId').value = user.id || '';
        document.getElementById('email').value = user.email || '';
        document.getElementById('firstName').value = user.firstName || '';
        document.getElementById('lastName').value = user.lastName || '';
        document.getElementById('age').value = user.age || '';
        document.getElementById('password').value;

        await fetchRoles();
        const userRoles = user.role || [];
        userRoles.forEach(userRole => {
            const checkbox = document.getElementById(`role-${userRole.id}`);
            if (checkbox) {
                checkbox.checked = true;
            }
        });
        document.getElementById('userModalTitle').textContent = 'Edit User';
        $('#userModal').modal('show');
    } catch (error) {
        console.error('Error editing user:', error);
        alert('Failed to load user data. Please try again.');
    }
}

function deleteUser(id) {
    fetch(`${apiUrl}/users/${id}`, { method: 'DELETE' })
        .then(() => fetchUsers());
}

document.getElementById('userForm').addEventListener('submit', async event => {
    event.preventDefault();
    const userId = document.getElementById('userId').value;
    const passwordValue = document.getElementById('password').value;
    const userData = {
        email: document.getElementById('email').value,
        password: passwordValue ? passwordValue : undefined,
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        age: document.getElementById('age').value,
        role: Array.from(document.querySelectorAll('#roles-container input[type="checkbox"]:checked'))
            .map(checkbox => ({ id: parseInt(checkbox.value) }))
    };
    const method = userId ? 'PUT' : 'POST';
    const endpoint = userId ? `${apiUrl}/users/${userId}` : `${apiUrl}/users`;

    try {
        const response = await fetch(endpoint, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Error updating user: ${response.status} ${errorText}`);
        }

        $('#userModal').modal('hide');
        fetchUsers();
    } catch (error) {
        console.error('Error updating/adding user:', error);
        alert('Failed to save user data. Please try again.');
    }
});