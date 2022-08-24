function getAdminToken() {
    return localStorage.getItem("admin-token");
}

function deleteTeam(teamId) {
    const checkDelete = prompt(`정말 삭제하시겠습니까? 삭제하려면 팀 ID(${teamId}) 입력`);

    if (checkDelete === teamId.toString()) {
        requestDeleteTeam(teamId);
    } else {
        alert("팀 ID를 다시 확인해주세요.");
    }
}

function requestDeleteTeam(teamId) {
    fetch(`/admin/teams/${teamId}?token=${getAdminToken()}`, {
        method: "DELETE",
    }).then(
        res => res.json()
    ).then(value => {
        if (value['message'] !== undefined) {
            alert(value['message']);
            return;
        }
        alert(`${teamId} 팀을 삭제했습니다.`);
    }).finally(() => {
        location.reload();
    });
}

function closeTeam(teamId) {
    const checkClose = prompt(`정말 종료하시겠습니까? 종료하려면 팀 ID(${teamId}) 입력`);

    if (checkClose === teamId.toString()) {
        requestCloseTeam(teamId);
    } else {
        alert("팀 ID를 다시 확인해주세요.");
    }
}

function requestCloseTeam(teamId) {
    fetch(`/admin/teams/${teamId}/close?token=${getAdminToken()}`, {
        method: "POST",
    }).then(
        res => res.json()
    ).then(value => {
        if (value['message'] !== undefined) {
            alert(value['message']);
            return;
        }
        alert(`${teamId} 팀을 종료했습니다.`);
    }).finally(() => {
        location.reload();
    });
}

function toStatus() {
    const statuses = document.getElementsByClassName("status");
    for (const status of statuses) {
        switch (status.innerText) {
            case "READY":
                status.innerText = "준비 중";
                status.style.color = "blue";
                break
            case "IN_PROGRESS":
                status.innerText = "진행 중";
                status.style.color = "green";
                break
            case "CLOSED":
                status.innerText = "종료";
                status.style.color = "red";
                break
            default:
                break
        }
    }
}

function toStartAt() {
    const times = document.getElementsByClassName("start_at");
    for (const time of times) {
        if (time.innerText === "시작 시간") {
            continue
        }
        time.innerText = time.innerText.slice(2, 10) + " ∙ " + time.innerText.slice(11);
    }
}

toStatus();
toStartAt()
