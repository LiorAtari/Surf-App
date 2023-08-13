document.addEventListener('DOMContentLoaded', () => {
    const buttons = document.querySelectorAll('.get-answer-button');
    const answerElement = document.getElementById('answer');

    buttons.forEach(button => {
        button.addEventListener('click', async () => {
            try {
                const location = button.getAttribute('data-location');

                const response = await fetch('/get_answer', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        location: location
                    })
                });

                const data = await response.json();
                const answer = data.generated_reply;

                // Display the answer on the webpage
                answerElement.textContent = answer;
            } catch (error) {
                console.error('Error fetching answer:', error);
            }
        });
    });
});

