document.addEventListener('DOMContentLoaded',()=>{
    fetch('http://localhost:8080/data').then(response=>{
        if(!response.ok){
            throw new Error('Network response we not ok');
        }
        return response.json();
    })
    .then(users=>{
        const userList = document.getElementById('userList');
        userList.innerHTML= '';
        if(users.length === 0){

            userList.innerHTML = '<tr><td colspan="3" class = "message">No user found.</td></tr>';
            return;
        }
        users.forEach(user => {
            const row = document.createElement('tr');
            row.innerHTML = `
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.email}</td>
                        `;
                        userList.appendChild(row);
        });

    })
    .catch(error =>{
        console.error('Error fetching data : ',error);
        document.getElementById('userList').innerHTML=`<tr><td colspan="3" class="message" style="color: #ef4444;">Failed to load user data.</td></tr>
        `;
    });
});
