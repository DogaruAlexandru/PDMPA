import requests

BASE_URL_LOGIN = 'http://127.0.0.1:5000/login'


def test_login(email, password):
    try:
        data = {'email': email, 'password': password}

        response = requests.post(BASE_URL_LOGIN, json=data)
        print("POST Request - Login:")

        response.raise_for_status()

        if 'error' in response.json():
            print(f"Login failed: {response.json()['error']}")
        else:
            print(f"Login successful: {response.json()['message']}")

    except requests.exceptions.ConnectionError as e:
        print(f'Connection error: {e}')
    except requests.exceptions.RequestException as e:
        print(f'Request error: {e}')


if __name__ == '__main__':
    test_login("newuser@example.com", "arcadia")
