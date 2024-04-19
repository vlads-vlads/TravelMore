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

window.onload = fetchCountries;