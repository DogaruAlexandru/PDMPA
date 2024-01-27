import requests


BASE_URL = 'http://127.0.0.1:5000/recipe'

# Test GET request


def test_get_recipe(recipe_id):
    response = requests.get(f'{BASE_URL}?recipe_id={recipe_id}')
    print(f"GET Request - Recipe by ID {recipe_id}:")
    print(response.json())
# Test POST request


def test_create_recipe():
    data = {
        'recipe_name': 'Test Recipe 2',
        'ingredients': 'Ingredient 1, Ingredient 2',
        'recipe_description': 'This is a test recipe 2.'
    }
    response = requests.post(BASE_URL, json=data)
    print("POST Request:")
    print(response.json())

# Test PUT request


def test_update_recipe():
    data = {
        'recipe_id': 1,
        'recipe_name': 'Updated Test Recipe',
        'ingredients': 'Updated Ingredient 1, Updated Ingredient 2',
        'recipe_description': 'This is an updated test recipe.'
    }
    response = requests.put(BASE_URL, json=data)
    print("PUT Request:")
    print(response.json())

# Test DELETE request


def test_delete_recipe():
    data = {'recipe_id': 1}
    response = requests.delete(BASE_URL, json=data)
    print("DELETE Request:")
    print(response.json())


if __name__ == '__main__':
    # Uncomment the functions you want to test
    # test_get_recipe(1)
    test_create_recipe()
    # test_update_recipe()
    # test_delete_recipe()
