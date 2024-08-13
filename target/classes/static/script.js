// script.js

document.getElementById('createProfileForm').addEventListener('submit', function(event) {
    event.preventDefault();
    
    const userName = document.getElementById('userName').value;
    const email = document.getElementById('createEmail').value;
    const mobileNumber = document.getElementById('createMobileNumber').value;
    const password = document.getElementById('createPassword').value;

    // Clear previous errors
    document.getElementById('createEmailError').textContent = '';
    document.getElementById('createMobileNumberError').textContent = '';
    document.getElementById('message').textContent = '';

    const requestData = {
        userName: userName,
        email: email,
        mobileNumber: mobileNumber,
        password: password
    };

    // Send request to server
    fetch('http://localhost:8080/users/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.message) {
            document.getElementById('message').textContent = data.message;
        } else {
            document.getElementById('message').textContent = 'Profile created successfully. Please check your email.';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('message').textContent = 'An unexpected error occurred.';
    });
});

document.getElementById('resetPasswordForm').addEventListener('submit', function(event) {
    event.preventDefault();
    
    const email = document.getElementById('resetEmail').value;
    const mobileNumber = document.getElementById('resetMobileNumber').value;
    const newPassword = document.getElementById('newPassword').value;
    const retypePassword = document.getElementById('retypePassword').value;

    // Clear previous errors
    document.getElementById('resetEmailError').textContent = '';
    document.getElementById('resetMobileNumberError').textContent = '';
    document.getElementById('newPasswordError').textContent = '';
    document.getElementById('retypePasswordError').textContent = '';
    document.getElementById('message').textContent = '';

    // Validate inputs
    if (!email && !mobileNumber) {
        document.getElementById('resetEmailError').textContent = 'Please provide either an email or mobile number.';
        document.getElementById('resetMobileNumberError').textContent = 'Please provide either an email or mobile number.';
        return;
    }
    if (email && mobileNumber) {
        document.getElementById('resetEmailError').textContent = 'Please provide either an email or mobile number, not both.';
        document.getElementById('resetMobileNumberError').textContent = 'Please provide either an email or mobile number, not both.';
        return;
    }
    if (newPassword !== retypePassword) {
        document.getElementById('newPasswordError').textContent = 'Passwords do not match.';
        document.getElementById('retypePasswordError').textContent = 'Passwords do not match.';
        return;
    }

    const requestData = {
        newPassword: newPassword,
        retypePassword: retypePassword
    };

    if (email) {
        requestData.email = email;
    } else {
        requestData.mobileNumber = mobileNumber;
    }

    // Send request to server
    fetch('http://localhost:8080/users/reset-password', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.message) {
            document.getElementById('message').textContent = data.message;
        } else {
            document.getElementById('message').textContent = 'Password reset successfully. Please check your email.';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('message').textContent = 'An unexpected error occurred.';
    });
});
