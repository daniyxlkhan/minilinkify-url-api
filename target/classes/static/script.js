const inputForm = document.querySelector('#url-form');
const result = document.querySelector('#result');

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
            result.innerHTML = `Shortened URL: <a href="${data.shortUrl}" target="_blank">${data.shortUrl}</a>`;
        })
        .catch(err => {
            console.log(err);
        });
});