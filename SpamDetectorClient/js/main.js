function toggleNav() {
  console.log('Function called');
  var sidebar = document.getElementById("mySidebar");

  if (sidebar.style.width === "250px") {
    sidebar.style.width = "0";
  } else {
    sidebar.style.width = "250px";
  }
}

function closeNav() {
  document.getElementById("mySidebar").style.width = "0";
}

function openNav() {
  document.getElementById("mySidebar").style.width = "250px";
}
// Fetch data and populate table
fetch('http://localhost:8080/spamDetector-1.0/api/spam')
  .then(response => response.json())
  .then(data => {
    data.forEach(entry => {
      PopulateT(entry);
    });
  });
// Function to fetch accuracy from server and update HTML element
function fetchAccuracy() {
  fetch('http://localhost:8080/spamDetector-1.0/api/spam/accuracy')
    .then(response => response.json())
    .then(accuracy => {
      document.getElementById('accuracyValue').textContent = accuracy.toFixed(15); // Assuming accuracy is a decimal value
    })
    .catch(error => {
      console.error('Error fetching accuracy:', error);
    });
}

// Function to fetch precision from server and update HTML element
function fetchPrecision() {
  fetch('http://localhost:8080/spamDetector-1.0/api/spam/precision')
    .then(response => response.json())
    .then(precision => {
      document.getElementById('precisionValue').textContent = precision.toFixed(15); // Assuming precision is a decimal value
    })
    .catch(error => {
      console.error('Error fetching precision:', error);
    });
}
// Function to populate table
function PopulateT(entry) {
  let table = document.getElementById('main').getElementsByTagName('tbody')[0];
  let newRow = table.insertRow();

  let filenameCell = newRow.insertCell(0);
  let spamProbabilityCell = newRow.insertCell(1);
  let classCell = newRow.insertCell(2);

  filenameCell.textContent = entry.filename;
  spamProbabilityCell.textContent = entry.spamProbability;
  classCell.textContent = entry.actualClass;
}
fetchAccuracy();
fetchPrecision();
