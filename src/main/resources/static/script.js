const inputForm = document.querySelector('#url-form');
const resultInput = document.querySelector('#result');
const copyIcon = document.getElementById("copy_icon");

inputForm.addEventListener('submit', (event) => {
    event.preventDefault();
    const inputUrl = document.querySelector('#url-input').value;

    fetch("http://localhost:8080/shorten", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ url: inputUrl })
    })
        .then(response => response.json())
        .then(data => {
            resultInput.value = data.shortUrl;
            copyIcon.style.display = "inline";
        })
        .catch(err => {
            console.log(err);
        });
});

copyIcon.addEventListener("click", () => {
    if (resultInput.value) {
        navigator.clipboard.writeText(resultInput.value).then(() => {
            console.log("Copy successful!");
        }).catch(err => {
            console.error("Copy failed:", err);
        });
    }
});
