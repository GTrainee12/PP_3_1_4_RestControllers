const apiUrl = '/api/user'

document.addEventListener('DOMContentLoaded', function() {
    fetchUserProfile()
    fetch(`${apiUrl}/profile`)
        .then(response => response.json())
        .then(data => populateTable(data))
        .catch(error => console.error('Error fetching data:', error));
});

function populateTable(user) {
    const tableBody = document.getElementById('userTableBody');
    const row = `
        <tr>
            <td>${user.id}</td>
            <td>${user.email}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.age}</td>
            <td>${user.role.map(r => r.role).join(', ')}</td>
        </tr>
    `;
    tableBody.insertAdjacentHTML('beforeend', row);
}

function fetchUserProfile() {
    fetch(`${apiUrl}/profile`)
        .then(response => response.json())
        .then(data => {
            document.getElementById('user-email').textContent = data.email;
            document.getElementById('user-roles').textContent = data.role.map(r => r.role).join(', ');
        })
        .catch(error => console.error('Error fetching user profile:', error));
}
