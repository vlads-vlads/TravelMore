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

window.onload = fetchCountries;

document.addEventListener('DOMContentLoaded', function() {
    function toggleCards() {
        var tripCardsContainer = document.getElementById('tripCardsContainer');
        var postCardsContainer = document.getElementById('postCardsContainer');
        if (tripCardsContainer.style.display === 'none') {
            tripCardsContainer.style.display = 'block';
            postCardsContainer.style.display = 'none';
        } else {
            tripCardsContainer.style.display = 'none';
            postCardsContainer.style.display = 'block';
        }
    }

    if (document.getElementById('toggleButton')) {
        document.getElementById('toggleButton').addEventListener('click', toggleCards);
    }

    function toggleCards2() {
        var tripCardsContainer2 = document.getElementById('tripCardsContainer2');
        var postCardsContainer2 = document.getElementById('postCardsContainer2');
        if (tripCardsContainer2.style.display === 'none') {
            tripCardsContainer2.style.display = 'block';
            postCardsContainer2.style.display = 'none';
        } else {
            tripCardsContainer2.style.display = 'none';
            postCardsContainer2.style.display = 'block';
        }
    }

    if (document.getElementById('toggleButton2')) {
        document.getElementById('toggleButton2').addEventListener('click', toggleCards2);
    }


    function toggleCards3() {
            var tripCardsContainer3 = document.getElementById('tripCardsContainer3');
            var postCardsContainer3 = document.getElementById('postCardsContainer3');
            if (tripCardsContainer3.style.display === 'none') {
                tripCardsContainer3.style.display = 'block';
                postCardsContainer3.style.display = 'none';
            } else {
                tripCardsContainer3.style.display = 'none';
                postCardsContainer3.style.display = 'block';
            }
        }

        if (document.getElementById('toggleButton3')) {
            document.getElementById('toggleButton3').addEventListener('click', toggleCards3);
        }
});


