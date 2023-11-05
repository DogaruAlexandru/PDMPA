from flask import Flask, jsonify, request
import pymysql

app = Flask(__name__)

# Configure the database connection
connection = pymysql.connect(
    host='127.0.0.1',
    user='root',
    password='@Nk22bdpizznthw50',
    database='android_app'
)


# Endpoint to create a new user
@app.route('/app_users', methods=['POST', 'GET'])
def login_credentials_usage():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type"})

    data = request.get_json()
    username = data.get('username')
    password = data.get('password')
    if request.method == 'POST':
        with connection.cursor() as cursor:
            sql = "INSERT INTO app_users (username, password_hash) VALUES (%s, %s)"
            cursor.execute(sql, (username, password))
            connection.commit()
        return jsonify({'message': 'User created successfully'})

    if request.method == 'GET':
        with connection.cursor() as cursor:
            sql = "SELECT * FROM app_users WHERE username = %s and password_hash = %s"
            cursor.execute(sql, (username, password))
            users = cursor.fetchall()
            return jsonify(users)


#Endpoint for storage space management
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


#Enpoint for creating a new product
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
    #print(type(user_id),type(container_id), type(product_name),type(expiration_date),type(quantity_grams),type(date_added),type(product_info_id))
    with connection.cursor() as cursor:
        sql = "INSERT INTO product (user_id,container_id,product_name,expiration_date,quantity_grams,date_added, product_info_id) VALUES (%s,%s,%s,%s,%s,%s,%s)"
        cursor.execute(sql, (user_id,container_id,product_name,expiration_date,quantity_grams,date_added, product_info_id))
        connection.commit()
    return jsonify({'message': 'Product created successfully'})


#Enpoint to retreive products from a specific user
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


if __name__ == '__main__':
    app.run(debug=True)
