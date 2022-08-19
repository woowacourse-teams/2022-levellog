function adminLogin() {
    let password = document.getElementById('admin-password').value;

    const request = {
        value: password
    };

    fetch('/admin/login', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    }).then(res => {
        if (res.ok) {
            return res.json();
        }
        throw new Error(value["errorMessage"]);
    }).then(value => {
        localStorage.setItem("admin-token", value['accessToken']);
        let item = localStorage.getItem("admin-token");
        alert(item);
        location.reload(); // 새로고침
    }).catch(reason => alert(reason));
}
