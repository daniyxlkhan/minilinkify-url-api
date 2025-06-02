const inputForm = document.querySelector('#url-form');
const resultInput = document.querySelector('#result');
const copyIcon = document.getElementById("copy_icon");

inputForm.addEventListener('submit', (event) => {
    event.preventDefault();
    const inputUrl = document.querySelector('#url-input').value;

    fetch("/shorten", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ url: inputUrl })
    })
        .then(async (response) => {
            if (!response.ok) {
                const text = await response.text();
                throw new Error(text);  // This will hit catch below
            }
            return response.json();
        })
        .then(data => {
            resultInput.value = data.shortUrl;
            copyIcon.style.display = "inline";
        })
        .catch(err => {
            console.error("Something went wrong:", err.message);
        });

});

copyIcon.addEventListener("click", () => {
    if (resultInput.value) {
        navigator.clipboard.writeText(resultInput.value).then(() => { // write text doesnt work for http websites
            console.log("Copy successful!");
        }).catch(err => {
            console.error("Copy failed:", err);
        });

        resultInput.select();
        try {
            const success = document.execCommand("copy");
            if (success) {
                console.log("Copied with fallback method!");
            } else {
                console.error("Fallback copy failed");
            }
        } catch (fallbackErr) {
            console.error("Fallback exception:", fallbackErr);
        }
    }
});
