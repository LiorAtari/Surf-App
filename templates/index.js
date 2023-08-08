function showResults() {
    var form = document.getElementById("surfingForm");
    var results = document.getElementById("surfingResults");
    var level = document.getElementById("surfing_level").value;

    // Make a POST request to the Flask app to get the results
    fetch("/", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "surfing_level=" + level
    })
    .then(response => response.text())
    .then(data => {
        results.innerHTML = data;
        form.style.display = "none";
        results.style.display = "block";
    })
    .catch(error => {
        console.error("Error fetching data: ", error);
    });
}
