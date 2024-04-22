document.addEventListener('DOMContentLoaded', function() {
    fetchCountries('tripName');
    fetchCountries('tripName2');

    const shareTripForm = document.getElementById('shareTripForm');
    shareTripForm.addEventListener('submit', function(event) {
        event.preventDefault();
        fetchRandomImageAndSubmitForm('tripName');
    });

    const writePostForm = document.getElementById('writePostForm');
    writePostForm.addEventListener('submit', function(event) {
        event.preventDefault();
        fetchRandomImageAndSubmitForm('tripName2');
    });
});

function fetchCountries(elementId) {
    const url = 'https://restcountries.com/v3.1/all';

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const countries = data.map(country => country.name.common);
            const selectDropdown = document.getElementById(elementId);
            selectDropdown.innerHTML = ''; // Clear existing options
            countries.forEach(country => {
                const option = document.createElement('option');
                option.text = country;
                option.value = country;
                selectDropdown.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching countries:', error));
}

function fetchRandomImageAndSubmitForm(tripNameElementId) {
    const tripName = document.getElementById(tripNameElementId).value;
    const imageUrl = `https://api.unsplash.com/photos/random?query=${tripName}&orientation=landscape&client_id=u2wkrKKJxinMI8ocyh-Xm7euYttlDtgp3Fdg7XPFtd4`;

    fetch(imageUrl)
        .then(response => response.json())
        .then(data => {
            const imageSrc = data.urls.regular;
            const randomImageUrlField = document.getElementById(tripNameElementId === 'tripName' ? 'randomImageUrl' : 'randomImageUrl2');
            randomImageUrlField.value = imageSrc;

            document.getElementById(tripNameElementId === 'tripName' ? 'shareTripForm' : 'writePostForm').submit();
        })
        .catch(error => console.error('Error fetching random image:', error));
}


window.onload = fetchCountries;

document.addEventListener('DOMContentLoaded', function() {
    function toggleCards() {
        var tripCardsContainer = document.getElementById('tripCardsContainer',);
        var postCardsContainer = document.getElementById('postCardsContainer');
        var viewCaption = document.getElementById('viewCaption');

        if (tripCardsContainer.style.display === 'none') {
            tripCardsContainer.style.display = 'block';
            postCardsContainer.style.display = 'none';
            viewCaption.textContent = 'My Trips';
        } else {
            tripCardsContainer.style.display = 'none';
            postCardsContainer.style.display = 'block';
            viewCaption.textContent = 'My Posts';
        }
    }

    if (document.getElementById('toggleButton')) {
        document.getElementById('toggleButton').addEventListener('click', toggleCards);
    }

    function toggleCards2() {
        var tripCardsContainer2 = document.getElementById('tripCardsContainer2');
        var postCardsContainer2 = document.getElementById('postCardsContainer2');
        var viewCaption = document.getElementById('viewCaption2');

        if (tripCardsContainer2.style.display === 'none') {
            tripCardsContainer2.style.display = 'block';
            postCardsContainer2.style.display = 'none';
            viewCaption.textContent = 'Trips';

        } else {
            tripCardsContainer2.style.display = 'none';
            postCardsContainer2.style.display = 'block';
            viewCaption.textContent = 'Posts';
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

   function toggleContainers() {
          var tripCardsContainer = document.getElementById('tripCardsContainer4');
          var postCardsContainer = document.getElementById('postCardsContainer4');

          if (tripCardsContainer.style.display === 'none') {
              tripCardsContainer.style.display = 'block';
              postCardsContainer.style.display = 'none';
          } else {
              tripCardsContainer.style.display = 'none';
              postCardsContainer.style.display = 'block';
          }
      }
});


function search() {
    var input, filter, cards, card, i, txtValue;
    input = document.getElementById('searchInput');
    filter = input.value.toUpperCase();

    var tripCards = document.getElementById('tripCardsContainer2').getElementsByClassName('card');
    var postCards = document.getElementById('postCardsContainer2').getElementsByClassName('card2');

    cards = Array.from(tripCards).concat(Array.from(postCards));

    for (i = 0; i < cards.length; i++) {
        card = cards[i];
        txtValue = card.textContent || card.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            card.style.display = "";
        } else {
            card.style.display = "none";
        }
    }
}



