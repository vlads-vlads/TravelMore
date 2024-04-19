document.addEventListener('DOMContentLoaded', function() {
    fetchCountries();
    window.addEventListener('load', function() {
        const tripCards = document.querySelectorAll('.card');
        tripCards.forEach(function(card) {
            const tripId = card.querySelector('img').id.split('-')[1];
            const destination = card.querySelector('.card-text').textContent.trim();
            fetchRandomImage(destination, tripId, card);
        });
    });
});

function fetchCountries() {
    const url = 'https://restcountries.com/v3.1/all';

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const countries = data.map(country => country.name.common);
            const selectDropdown = document.getElementById('tripName');
            countries.forEach(country => {
                const option = document.createElement('option');
                option.text = country;
                option.value = country;
                selectDropdown.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching countries:', error));
}

function fetchRandomImage(destination, tripId, card) {
    const imageUrl = `https://api.unsplash.com/photos/random?query=${destination}&orientation=landscape&client_id=u2wkrKKJxinMI8ocyh-Xm7euYttlDtgp3Fdg7XPFtd4`;

    fetch(imageUrl)
        .then(response => response.json())
        .then(data => {
            const imageSrc = data.urls.regular;
            const tripImage = card.querySelector('img');
            tripImage.src = imageSrc;
        })
        .catch(error => console.error('Error fetching random image:', error));
}