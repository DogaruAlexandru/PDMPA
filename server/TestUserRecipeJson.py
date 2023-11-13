import requests


BASE_URL = 'http://127.0.0.1:5000/user_recipe'

# Test GET request to retrieve user recipes by user_id


def test_get_user_recipes(user_id):
    response = requests.get(f'{BASE_URL}?user_id={user_id}')
    print(f"GET Request - User Recipes by User ID {user_id}:")
    print(response.json())

# Test POST request to create a user recipe


def test_create_user_recipe(user_id, recipe_id):
    data = {'user_id': user_id, 'recipe_id': recipe_id}
    response = requests.post(BASE_URL, json=data)
    print("POST Request - Create User Recipe:")
    print(response.json())

# Test PUT request to update a user recipe


def test_update_user_recipe(user_recipe_id, user_id, recipe_id):
    data = {'user_recipe_id': user_recipe_id,
            'user_id': user_id, 'recipe_id': recipe_id}
    response = requests.put(BASE_URL, json=data)
    print("PUT Request - Update User Recipe:")
    print(response.json())

# Test DELETE request to delete a user recipe


def test_delete_user_recipe(user_recipe_id):
    data = {'user_recipe_id': user_recipe_id}
    response = requests.delete(BASE_URL, json=data)
    print("DELETE Request - Delete User Recipe:")
    print(response.json())


if __name__ == '__main__':
    # Uncomment and modify the functions as needed

    # test_get_user_recipes(1)
    # test_create_user_recipe(1, 2)
    # test_update_user_recipe(2, 1, 3)
    test_delete_user_recipe(2)
