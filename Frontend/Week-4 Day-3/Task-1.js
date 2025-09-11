fetch('https://dummy.restapiexample.com/api/v1/employees')
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  })
  .then(data => {
    console.log(data); // Displays the entire JSON response on the console
  })
  .catch(error => {
    console.error('There was a problem with the fetch operation:', error);
});