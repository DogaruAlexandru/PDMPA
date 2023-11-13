import requests


BASE_URL = 'http://127.0.0.1:5000/product_info'

# Test GET request to retrieve a specific product by product_id


def test_get_product_by_id(product_id):
    response = requests.get(f'{BASE_URL}?product_id={product_id}')
    print(f"GET Request - Product by ID {product_id}:")
    print(response.json())

# Test POST request to add a new product


def test_add_product():
    data = {
        'product_name': 'New Product',
        'energy_value': 100,
        'fat_value': 5,
        'carbohydrate_value': 15,
        'sodium': 10,
        'calcium': 20,
        'protein': 8,
        'vitamin': 2,
        'vitamin_type': 'Vitamin A',
        'allergens': 'None'
    }
    response = requests.post(BASE_URL, json=data)
    print("POST Request - Add Product:")
    print(response.json())

# Test PUT request to update an existing product


def test_update_product(product_id):
    data = {
        'product_id': product_id,
        'product_name': 'Updated Product',
        'energy_value': 120,
        'fat_value': 6,
        'carbohydrate_value': 18,
        'sodium': 12,
        'calcium': 25,
        'protein': 10,
        'vitamin': 3,
        'vitamin_type': 'Vitamin B',
        'allergens': 'None'
    }
    response = requests.put(BASE_URL, json=data)
    print("PUT Request - Update Product:")
    print(response.json())

# Test DELETE request to delete a product


def test_delete_product(product_id):
    data = {'product_id': product_id}
    response = requests.delete(BASE_URL, json=data)
    print("DELETE Request - Delete Product:")
    print(response.json())


if __name__ == '__main__':
    # Uncomment the functions you want to test

    # test_get_product_by_id(1)
    # test_add_product()
    # test_update_product(1)
    test_delete_product(1)
