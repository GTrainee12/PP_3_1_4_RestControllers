const apiUrl = '/api/register';
document.addEventListener('DOMContentLoaded', function () {
    function addRoles() {
        fetch(`${apiUrl}/roles`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error: ${response.statusText}`);
                }
                return response.json();
            })
            .then(roles => {
                console.log('Roles fetched:', roles);
                const rolesContainerReg = document.getElementById('roles-container-reg');
                rolesContainerReg.innerHTML = '';

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
                    rolesContainerReg.appendChild(checkboxContainer);
                });
            })
            .catch(error => {
                console.error('Error fetching roles:', error);
                alert('Error fetching roles: ' + error.message);
            });
    }
    addRoles();

    function handleRegister() {
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;
        const age = document.getElementById('age').value;
        let roles = Array.from(document.querySelectorAll('#roles-container-reg input[type="checkbox"]:checked'))
            .map(checkbox => {
                return {
                    id: parseInt(checkbox.value),
                    role: checkbox.dataset.role
                };
            });
        if (!email || !password || !firstName || !lastName || !age || roles.length === 0) {
            displayMessage('Please fill out all the fields and choose at least one role.', 'danger');
            return;
        }

        checkEmailExists(email)
            .then(emailExists => {
                if (emailExists) {
                    displayMessage('Email is already registered.', 'danger');
                } else {
                    const requestBody = {
                        email,
                        password,
                        firstName,
                        lastName,
                        age: parseInt(age),
                        role: roles
                    };

                    console.log('Request body:', JSON.stringify(requestBody));

                    fetch(apiUrl, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(requestBody)
                    })
                        .then(response => {
                            if (response.ok) {
                                displayMessage('Registration successful.', 'success');
                            } else {
                                return response.json().then(data => {
                                    throw new Error(data.message || 'Registration failed.');
                                });
                            }
                        })
                        .catch(error => {
                            displayMessage(error.message, 'danger');
                        });
                }
            })
            .catch(error => {
                displayMessage(error.message, 'danger');
            });
    }

    function displayMessage(message, type) {
        const messageDiv = document.getElementById('registerMessage');
        messageDiv.style.display = 'block';
        messageDiv.textContent = message;
        messageDiv.className = type === 'success' ? 'text-success' : 'text-danger';
    }

    document.getElementById('registerModal').addEventListener('show.bs.modal', addRoles);
    document.getElementById('registerSubmit').addEventListener('click', handleRegister);


    let roles = Array.from(document.querySelectorAll('#roles-container-reg input[type="checkbox"]:checked'))
        .map(checkbox => {
            console.log('Checked checkbox:', checkbox);
            return { id: parseInt(checkbox.value) };
        });
    console.log('Selected roles:', roles);

    function checkEmailExists(email) {
        const checkEmailUrl = `${apiUrl}/check-email?email=${encodeURIComponent(email)}`;
        return fetch(checkEmailUrl, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to check email.');
                }
                return response.json();
            })
            .then(data => data.exists);
    }
});

