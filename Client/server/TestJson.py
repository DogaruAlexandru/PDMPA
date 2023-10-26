import requests

# The endpoint URL
url = 'http://127.0.0.1:5000/app_users'

# The data to be sent in the POST request
data = {
    'username': 'hsda',
    'password': 'aasdasda'
}

# Send the POST request
response = requests.post(url, json=data)

# Check the status code of the response
if response.status_code == 200:
    print("User created successfully")
else:
    print(f"Failed to create user. Status code: {response.status_code}")
    print(response.text)
