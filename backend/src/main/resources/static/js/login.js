function adminLogin() {
    const password = document.getElementById('admin-password').value;

    fetch(`/admin/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({value: password}),
    }).then(
        res => res.json()
    ).then(value => {
        if (value['message'] !== undefined) {
            alert(value['message']);
            document.getElementById('admin-password').value = "";
            return;
        }

        localStorage.setItem("admin-token", value['accessToken']);
        const token = localStorage.getItem("admin-token");
        location.replace(`teams?token=${token}`);
    });

    return false;
}
