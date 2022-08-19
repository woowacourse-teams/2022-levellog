function getAdminToken() {
    return localStorage.getItem("admin-token");
}

function adminLogin() {
    const password = document.getElementById('admin-password').value;

    fetch('/admin/login', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({value: password}),
    }).then(res => {
        if (res.ok) {
            return res.json();
        }
        throw new Error(value["errorMessage"]);
    }).then(value => {
        localStorage.setItem("admin-token", value['accessToken']);
        const token = localStorage.getItem("admin-token");
        location.replace("teams?token=" + token);
    }).catch(() => alert("비밀번호를 틀렸습니다."));
}

function deleteTeams(teamId) {
    let checkDelete = prompt("정말 삭제하시겠습니까?(\"예\" 입력 시 삭제)");

    if (checkDelete === "예") {
        deleteTeam(teamId);
    } else {
        alert("삭제하지 않았습니다");
        location.reload();
    }
}

function deleteTeam(teamId) {
    fetch('/admin/teams/' + teamId + "?token=" + getAdminToken(), {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
    }).then(() => {
        alert(teamId + " 팀이 삭제되었습니다.");
        location.reload();
    }).catch(() => {
        alert(teamId + " 팀 삭제에 실패했습니다.");
        location.reload();
    });
}
