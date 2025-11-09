const fitFileInput = document.getElementById('fitFileInput');
const uploadButton = document.getElementById('uploadButton');
const url = 'http://localhost:8080/api/strava/upload';
const bearerToken = "Bearer 354c18813bbac64763dcd134560fbf6c2c743db2";

function uploadFile() {
    const file = fitFileInput.files[0];
    if(!file){
        console.error('No file selected');
        alert('Please select a file to upload.');
        return;
    }
    const formData = new FormData();
    formData.append('file', file);
    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': bearerToken
        },
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        console.log('Success:', data);
        alert('File uploaded successfully.');
    })
    .catch((error) => {
        console.log('Error:', error);
        alert('File upload failed.');
    });
}