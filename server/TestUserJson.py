import requests


BASE_URL_REGISTER = 'http://127.0.0.1:5000/register'


def test_register(username, password, email):
    try:
        data = {'username': username, 'password': password, 'email': email}

        response = requests.post(BASE_URL_REGISTER, json=data)
        print("POST Request - Create User:")

        response.raise_for_status()
        print('Connection successful')
    except requests.exceptions.ConnectionError as e:
        print(f'Connection error: {e}')
    except requests.exceptions.RequestException as e:
        print(f'Request error: {e}')


if __name__ == '__main__':
    test_register("newuser", "arcadia", "newuser@example.com")
