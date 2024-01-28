from flask import Flask, jsonify, request
import pymysql
import ManagePassword

app = Flask(__name__)

# Configure the database connection
connection = pymysql.connect(
    host='127.0.0.1',
    user='root',
    password='mysql1234',
    database='android_app'
)


# Endpoint to create a new user
#@app.route('/app_users', methods=['POST', 'GET'])
#def login_credentials_usage():
#    try:
#        request.get_json()
#    except Exception:
#        return jsonify({'error': "Wrong input type"})
#
#    data = request.get_json()
#    username = data.get('username')
#    password = data.get('password')
#    if request.method == 'POST':
#        with connection.cursor() as cursor:
#            sql = "INSERT INTO app_users (username, password_hash) VALUES (%s, %s)"
#            cursor.execute(sql, (username, password))
#            connection.commit()
#        return jsonify({'message': 'User created successfully'})
#
#    if request.method == 'GET':
#        with connection.cursor() as cursor:
#            sql = "SELECT * FROM app_users WHERE username = %s and password_hash = %s"
#            cursor.execute(sql, (username, password))
#            users = cursor.fetchall()
#            return jsonify(users)


# Endpoint to create a new user
@app.route('/register', methods=['POST'])
def register():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type"})

    data = request.get_json()
    username = data.get('username')
    password = data.get('password')
    if request.method == 'POST':
        with connection.cursor() as cursor:
            sql = "SELECT * FROM app_users WHERE username = %s"
            cursor.execute(sql, username)
            users = cursor.fetchall()
            if users.count() != 0:
                return jsonify({"error": "Already existing user"})

            password_hash, salt = ManagePassword.hash_password(password)

            sql = "INSERT INTO app_users (username, password_hash, salt) VALUES (%s, %s, %s)"
            cursor.execute(sql, (username, password_hash, salt))
            connection.commit()

            sql_get_userinfo = "SELECT * FROM app_users WHERE username = %s and password_hash = %s and salt = %s"
            cursor.execute(sql_get_userinfo, (username, password_hash, salt))
            users = cursor.fetchall()

            if len(users) > 1:
                return jsonify({'error': 'Multiple users with same info'})

            for user in users:
                print(jsonify(user))
                return jsonify({'user_id': user["user_id"], 'message': 'User created successfully'})

        return jsonify({'message': 'User created successfully'})


# Endpoint to log in with user
@app.route('/login', methods=['POST'])
def login():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type"})

    data = request.get_json()
    username = data.get('username')
    password = data.get('password')
    if request.method == 'POST':
        with connection.cursor() as cursor:
            sql = "SELECT * FROM app_users WHERE username = %s"
            cursor.execute(sql, (username))
            users = cursor.fetchall()

            if len(users) == 0:
                return jsonify({"error": "User not found"})

            for user in users:
                if ManagePassword.verify_password(password, user["password_hash"], user["salt"]):
                    return jsonify({'user_id': user["user_id"], 'message': 'User logged successfully'})
                
            return jsonify({"error": "Invalid password"})


# Endpoint for storage space management
@app.route('/storage_space', methods=['POST', 'GET'])
def storage_space_management():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type. If you don't want to add a request body, sent {} as body"})

    data = request.get_json()
    storage_name = data.get('storage_name')
    if request.method == 'POST':
        with connection.cursor() as cursor:
            sql = "INSERT INTO storage_space (storage_name) VALUES (%s)"
            cursor.execute(sql, storage_name)
            connection.commit()
        return jsonify({'message': 'Storage space created successfully'})

    if request.method == 'GET':
        with connection.cursor() as cursor:
            sql = "SELECT * FROM storage_space"
            cursor.execute(sql)
            return jsonify(cursor.fetchall())


# Enpoint for creating a new product
@app.route('/product', methods=['POST'])
def create_product():
    """try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type. If you don't want to add a request body, sent {} as body"})
"""
    data = request.get_json()
    user_id = data.get('user_id')
    container_id = data.get('container_id')
    product_name = data.get('product_name')
    expiration_date = data.get('expiration_date')
    quantity_grams = data.get('quantity_grams')
    date_added = data.get('date_added')
    product_info_id = data.get('product_info_id')
    # print(type(user_id),type(container_id), type(product_name),type(expiration_date),type(quantity_grams),type(date_added),type(product_info_id))
    with connection.cursor() as cursor:
        sql = "INSERT INTO product (user_id,container_id,product_name,expiration_date,quantity_grams,date_added, product_info_id) VALUES (%s,%s,%s,%s,%s,%s,%s)"
        cursor.execute(sql, (user_id, container_id, product_name,
                       expiration_date, quantity_grams, date_added, product_info_id))
        connection.commit()
    return jsonify({'message': 'Product created successfully'})


# Enpoint to retreive products from a specific user
@app.route('/product/all_from_user', methods=['GET'])
def get_products_list():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type. Needed request body format: { \"user_id\":<value>}"})

    data = request.get_json()
    user_id = data.get('user_id')

    if user_id is None:
        return jsonify({'error': "Wrong input type. Need for user_id"})

    with connection.cursor() as cursor:
        sql = "SELECT * FROM product WHERE user_id = %s"
        cursor.execute(sql, user_id)
        return jsonify(cursor.fetchall())

# Enpoint to retreive products AND their container from a specific user
@app.route('/productAndContainer/all_from_user', methods=['GET'])
def get_products_with_containers_list():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type. Needed request body format: { \"user_id\":<value>}"})

    data = request.get_json()
    user_id = data.get('user_id')

    if user_id is None:
        return jsonify({'error': "Wrong input type. Need for user_id"})

    with connection.cursor() as cursor:
        sql = "SELECT * FROM product join storage_space on product.container_id = storage_space.storage_id WHERE user_id = %s"
        cursor.execute(sql, user_id)
        return jsonify(cursor.fetchall())


# Endpoint to handle CRUD operations for recipes
@app.route('/recipe', methods=['GET', 'POST', 'PUT', 'DELETE'])
def handle_recipes():
    if request.method == 'GET':
        recipe_id = request.args.get('recipe_id')

        if not recipe_id:
            return jsonify({'error': 'Missing recipe_id parameter'})

        with connection.cursor() as cursor:
            sql = "SELECT recipe_name, ingredients, recipe_description FROM recipe WHERE recipe_id = %s"
            cursor.execute(sql, (recipe_id,))
            recipe_data = cursor.fetchone()

            if not recipe_data:
                return jsonify({'message': 'Recipe not found'})

            return jsonify(recipe_data)

    elif request.method == 'POST':
        try:
            request.get_json()
        except Exception:
            return jsonify({'error': "Wrong input type"})

        data = request.get_json()
        recipe_name = data.get('recipe_name')
        ingredients = data.get('ingredients')
        recipe_description = data.get('recipe_description')

        with connection.cursor() as cursor:
            sql = "INSERT INTO recipe (recipe_name, ingredients, recipe_description) VALUES (%s, %s, %s)"
            cursor.execute(sql, (recipe_name, ingredients, recipe_description))
            connection.commit()
        return jsonify({'message': 'Recipe created successfully'})

    elif request.method == 'PUT':
        data = request.get_json()
        recipe_id = data.get('recipe_id')
        recipe_name = data.get('recipe_name')
        ingredients = data.get('ingredients')
        recipe_description = data.get('recipe_description')

        with connection.cursor() as cursor:
            sql = "UPDATE recipe SET recipe_name = %s, ingredients = %s, recipe_description = %s WHERE recipe_id = %s"
            cursor.execute(sql, (recipe_name, ingredients,
                           recipe_description, recipe_id))
            connection.commit()
        return jsonify({'message': 'Recipe updated successfully'})

    elif request.method == 'DELETE':
        data = request.get_json()
        recipe_id = data.get('recipe_id')

        with connection.cursor() as cursor:
            sql = "DELETE FROM recipe WHERE recipe_id = %s"
            cursor.execute(sql, (recipe_id,))
            connection.commit()
        return jsonify({'message': 'Recipe deleted successfully'})

    else:
        return jsonify({'message': 'Method not allowed'})


# Endpoint for CRUD operations on the user_recipe table
@app.route('/user_recipe', methods=['GET', 'POST', 'PUT', 'DELETE'])
def user_recipes():
    if request.method == 'GET':
        user_id = request.args.get('user_id')

        if not user_id:
            return jsonify({'error': 'Missing user_id parameter'})

        with connection.cursor() as cursor:
            sql = "SELECT recipe_id FROM user_recipe WHERE user_id = %s"
            cursor.execute(sql, (user_id,))
            user_recipes_data = cursor.fetchall()

            if not user_recipes_data:
                return jsonify({'message': 'User recipes not found'})

            return jsonify(user_recipes_data)

    elif request.method == 'POST':
        data = request.get_json()
        user_id = data['user_id']
        recipe_id = data['recipe_id']
        with connection.cursor() as cursor:
            sql = "INSERT INTO user_recipe (user_id, recipe_id) VALUES (%s, %s)"
            cursor.execute(sql, (user_id, recipe_id))
            connection.commit()
        return jsonify({'message': 'User recipe created successfully'})

    elif request.method == 'PUT':
        data = request.get_json()
        user_recipe_id = data['user_recipe_id']
        user_id = data['user_id']
        recipe_id = data['recipe_id']
        with connection.cursor() as cursor:
            sql = "UPDATE user_recipe SET user_id = %s, recipe_id = %s WHERE user_recipe_id = %s"
            cursor.execute(sql, (user_id, recipe_id, user_recipe_id))
            connection.commit()
        return jsonify({'message': 'User recipe updated successfully'})

    elif request.method == 'DELETE':
        data = request.get_json()
        user_recipe_id = data['user_recipe_id']
        with connection.cursor() as cursor:
            sql = "DELETE FROM user_recipe WHERE user_recipe_id = %s"
            cursor.execute(sql, (user_recipe_id,))
            connection.commit()
        return jsonify({'message': 'User recipe deleted successfully'})

    else:
        return jsonify({'message': 'Method not allowed'})


# Endpoint for CRUD operations on the product_info table
@app.route('/product_info', methods=['GET', 'POST', 'PUT', 'DELETE'])
def product_info():
    if request.method == 'GET':
        product_id = request.args.get('product_id')

        if not product_id:
            return jsonify({'error': 'Missing product_id parameter'})

        with connection.cursor() as cursor:
            sql = "SELECT product_name, energy_value, fat_value, carbohydrate_value, sodium, calcium, protein, vitamin, vitamin_type, allergens FROM product_info WHERE product_id = %s"
            cursor.execute(sql, (product_id,))
            product_data = cursor.fetchone()

            if not product_data:
                return jsonify({'message': 'Product not found'})

            return jsonify(product_data)

    elif request.method == 'POST':
        data = request.get_json()
        with connection.cursor() as cursor:
            sql = "INSERT INTO product_info (product_name, energy_value, fat_value, carbohydrate_value, sodium, calcium, protein, vitamin, vitamin_type, allergens) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
            cursor.execute(sql, (data['product_name'], data['energy_value'], data['fat_value'], data['carbohydrate_value'],
                           data['sodium'], data['calcium'], data['protein'], data['vitamin'], data['vitamin_type'], data['allergens']))
            connection.commit()
        return jsonify({'message': 'Product added successfully'})

    elif request.method == 'PUT':
        data = request.get_json()
        with connection.cursor() as cursor:
            sql = "UPDATE product_info SET product_name=%s, energy_value=%s, fat_value=%s, carbohydrate_value=%s, sodium=%s, calcium=%s, protein=%s, vitamin=%s, vitamin_type=%s, allergens=%s WHERE product_id=%s"
            cursor.execute(sql, (data['product_name'], data['energy_value'], data['fat_value'], data['carbohydrate_value'], data['sodium'],
                           data['calcium'], data['protein'], data['vitamin'], data['vitamin_type'], data['allergens'], data['product_id']))
            connection.commit()
        return jsonify({'message': 'Product updated successfully'})

    elif request.method == 'DELETE':
        data = request.get_json()
        with connection.cursor() as cursor:
            sql = "DELETE FROM product_info WHERE product_id=%s"
            cursor.execute(sql, (data['product_id'],))
            connection.commit()
        return jsonify({'message': 'Product deleted successfully'})
    else:
        return jsonify({'message': 'Method not allowed'})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
