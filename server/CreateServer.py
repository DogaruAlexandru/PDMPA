from datetime import datetime
from flask import Flask, jsonify, request
import pymysql
import ManagePassword
from collections import OrderedDict
import json 

app = Flask(__name__)

# Configure the database connection
connection = pymysql.connect(
    host='127.0.0.1',
    password='1q2w3e',
    user='root',
    database='android_app'
)

class Container:
    def __init__(self, id, name):
        self.id = id
        self.name = name
        
class Product:
    def __init__(self, id, name, expiration_date, quantity, container, added_date):
        self.id = id
        self.name = name
        self.expirationDate = expiration_date
        self.quantity = quantity
        self.container = container
        self.addedDate = added_date

class ProductFull:
    def __init__(self, productId, productName, productContainer, expirationDate, quantity, addedDate,
                 energyValue, fatValue, carbohydrateValue, sodium, calcium, protein, vitamin, vitaminType, allergens):
        self.productId = productId
        self.productName = productName
        self.productContainer = productContainer
        self.expirationDate = expirationDate
        self.quantity = quantity
        self.addedDate = addedDate
        self.energyValue = energyValue
        self.fatValue = fatValue
        self.carbohydrateValue = carbohydrateValue
        self.sodium = sodium
        self.calcium = calcium
        self.protein = protein
        self.vitamin = vitamin
        self.vitaminType = vitaminType
        self.allergens = allergens


# Endpoint to create a new user
# @app.route('/app_users', methods=['POST', 'GET'])
# def login_credentials_usage():
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
        return jsonify({'error': "Wrong input type"}), 400

    data = request.get_json()

    username = data.get('username')
    password = data.get('password')
    if request.method == 'POST':
        with connection.cursor() as cursor:
            sql = "SELECT * FROM app_users WHERE username = %s"
            cursor.execute(sql, username)
            users = cursor.fetchall()
            if len(users) != 0:
                return jsonify({"error": "Already existing user"}), 400

            password_hash, salt = ManagePassword.hash_password(password)

            sql = "INSERT INTO app_users (username, password_hash, salt) VALUES (%s, %s, %s)"
            cursor.execute(sql, (username, password_hash, salt))
            connection.commit()

            user_id = cursor.lastrowid  # Get the ID of the last inserted row

            return jsonify({'userId': user_id, 'email': username}), 201  # Use 201 status code for successful creation




# Endpoint to log in with user
@app.route('/login', methods=['POST'])
def login():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type"}), 400

    data = request.get_json()
    username = data.get('username')
    password = data.get('password')
    if request.method == 'POST':
        with connection.cursor() as cursor:
            sql = "SELECT user_id, password_hash, salt FROM app_users WHERE username = %s"
            cursor.execute(sql, (username,))
            users = cursor.fetchall()
            print(users)
            if len(users) == 0:
                return jsonify({"error": "User not found"}), 400

            for user in users:
                # user[1] - password_hash, user[2] - salt
                if ManagePassword.verify_password(password, user[1], user[2]):
                    # user[0] - user_id
                    return jsonify({'userId': user[0], 'email': username}), 201

            return jsonify({"error": "Invalid password"}), 400

# Endpoint for storage space management


@app.route('/create_container', methods=['POST'])
def create_storage_space():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type. If you don't want to add a request body, sent {} as body"})

    try:
        data = request.get_json()
        userId=data.get('userId')
        container = data.get('container')
        name = container.get('name')
        
        
        
        with connection.cursor() as cursor:
            sql = "INSERT INTO storage_space (storage_name,user_id) VALUES (%s,%s)"
            cursor.execute(sql, (name,userId))
            connection.commit()
        return jsonify({'message': 'Storage space created successfully'})
    except Exception as e:
        return jsonify({'error': str(e)})


@app.route('/user_containers', methods=['GET'])
def get_user_storage_space():
    
    userId = request.args.get('userId')

    try:
        with connection.cursor() as cursor:
            sql = "SELECT storage_id, storage_name FROM storage_space WHERE user_id = %s"
            cursor.execute(sql, (userId,))
            storage_spaces = cursor.fetchall()

        # Construct the list of Container objects
        containers = []
        for space in storage_spaces:
            container = Container(id=space[0], name=space[1])
            containers.append(container)

        # Return the list of Container objects as JSON
        return jsonify([container.__dict__ for container in containers])

    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/update_container', methods=['PUT'])
def storage_space_update():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type. If you don't want to add a request body, sent {} as body"})

    try:
        data = request.get_json()
        
        id = data.get('id')
        name = data.get('name')
        
        with connection.cursor() as cursor:
            sql = "UPDATE storage_space SET storage_name=%s WHERE storage_id = %s"
            cursor.execute(sql, (name, id))
            connection.commit()
            
        return jsonify({'message': 'Storage space updated successfully'})
    except Exception as e:
        return jsonify({'error': str(e)})

@app.route('/get_container', methods=['GET'])
def get_storage_space():

    containerId = request.args.get('containerId')

    if containerId is None:
        return jsonify({'error': "Missing storage_id parameter."}), 400
    
    try:
        with connection.cursor() as cursor:
            sql = "SELECT storage_id, storage_name FROM storage_space WHERE storage_id = %s"
            cursor.execute(sql, (containerId,))
            storage_space = cursor.fetchone()  # Fetch only one row

        # If storage space is found, construct the Container object
        if storage_space:
            container = Container(id=storage_space[0], name=storage_space[1])
            return jsonify(container.__dict__)

        # If storage space is not found, return appropriate error message
        return jsonify({'error': f"Storage space with ID {containerId} not found."}), 404

    except Exception as e:
        return jsonify({'error': str(e)})

        
  

@app.route('/delete_container', methods=['DELETE'])
def delete_storage_space():
   

    containerId = request.args.get('containerId') 
    
    if containerId is None:
        return jsonify({'error': "Missing storage_id parameter."}), 400
    try:
        
        
        with connection.cursor() as cursor:
            sql_product = "DELETE FROM product WHERE container_id = %s"
            cursor.execute(sql_product, (containerId,))
            connection.commit()

            sql_storage_space = "DELETE FROM storage_space WHERE storage_id = %s"
            cursor.execute(sql_storage_space, (containerId,))
            connection.commit()
            
        return jsonify({'message': 'Storage deleted successfully'})
    except Exception as e:
        return jsonify({'error': str(e)})

# Enpoint for creating a new product
@app.route('/create_product', methods=['POST'])
def create_product():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type. If you don't want to add a request body, sent {} as body"})

    try:
        data = request.get_json()
        userId = data.get('userId')
        productFull = data.get('productFull')

        productName = productFull.get('productName')
        productContainer = productFull.get('productContainer')
        
        quantity = productFull.get('quantity')
        
        energyValue = productFull.get('energyValue')
        fatValue = productFull.get('fatValue')
        carbohydrateValue = productFull.get('carbohydrateValue')
        sodium = productFull.get('sodium')
        calcium = productFull.get('calcium')
        protein = productFull.get('protein')
        vitamin = productFull.get('vitamin')
        vitaminType = productFull.get('vitaminType')
        allergens = productFull.get('allergens')
        
        '''expiration_date_parts = expirationDate.split(' ')
        month_str = expiration_date_parts[0]
        day_str = expiration_date_parts[1][:-1]  # eliminarea virgulei
        year_str = expiration_date_parts[2]

        # Mapare lună
        months = {
            "Jan": "01", "Feb": "02", "Mar": "03", "Apr": "04",
            "May": "05", "Jun": "06", "Jul": "07", "Aug": "08",
            "Sep": "09", "Oct": "10", "Nov": "11", "Dec": "12"
        }
        month = months[month_str]

        # Rearanjarea în formatul "YYYY-MM-DD" pentru data de expirare
        expiration_date_formatted = f"{year_str}-{month}-{day_str}"

        # Extragerea componentelor datei de adăugare
        added_date_parts = addedDate.split(' ')
        added_month_str = added_date_parts[0]
        added_day_str = added_date_parts[1][:-1]  # eliminarea virgulei
        added_year_str = added_date_parts[2]

        added_month = months[added_month_str]

        added_date_formatted = f"{added_year_str}-{added_month}-{added_day_str}"
      '''
        expirationDate = datetime.strptime(productFull.get('expirationDate'), "%b %d, %Y %H:%M:%S").strftime('%Y-%m-%d')
        addedDate = datetime.strptime(productFull.get('addedDate'), "%b %d, %Y %H:%M:%S").strftime('%Y-%m-%d')


        with connection.cursor() as cursor:
            # Insert into product_info table
            sql = "INSERT INTO product_info (energy_value,fat_value,carbohydrate_value,sodium,calcium,protein,vitamin,vitamin_type,allergens) VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s)"
            cursor.execute(sql, ( energyValue, fatValue, carbohydrateValue, sodium, calcium, protein, vitamin, vitaminType, allergens))
            connection.commit()
            # Get the generated product_info_id
            product_info_id = cursor.lastrowid

            # Get container_id based on container_name
            sql_container = "SELECT storage_id FROM storage_space WHERE storage_name = %s AND user_id=%s"
            cursor.execute(sql_container, (productContainer,userId))
            container_data = cursor.fetchone()
            container_id = container_data[0] if container_data else None

            # Insert into product table
            sql = "INSERT INTO product (user_id,container_id,product_name,expiration_date,quantity_grams,date_added, product_info_id) VALUES (%s,%s,%s,%s,%s,%s,%s)"
            cursor.execute(sql, (userId, container_id, productName, expirationDate, quantity, addedDate, product_info_id))
            connection.commit()

        return jsonify({'message': 'Product created successfully'})

    except Exception as e:
        return jsonify({'error': str(e)}),400



@app.route('/user_products', methods=['GET'])
def get_products_list():
    userId = request.args.get('userId')

    if userId is None:
        return jsonify({'error': "Missing user_id parameter."}), 400

    products = []

    with connection.cursor() as cursor:
        sql = "SELECT p.product_id, p.product_name, p.expiration_date, p.quantity_grams, p.date_added, s.storage_name FROM product p JOIN storage_space s ON p.container_id = s.storage_id WHERE p.user_id = %s"
        cursor.execute(sql, (userId,))
        product_data = cursor.fetchall()

        for row in product_data:
            product_id, product_name, expiration_date, quantity_grams, date_added, container_name = row

            # Converting dates to ISO format
            expiration_date = expiration_date.isoformat() if expiration_date else None
            date_added = date_added.isoformat() if date_added else None

            # Constructing Product object
            product = Product(product_id, product_name, expiration_date, quantity_grams, container_name, date_added)
            products.append(product.__dict__)

    return jsonify(products)

# Enpoint to retreive products AND their container from a specific user


@app.route('/update_product', methods=['PUT'])
def update_product():
    try:
        request.get_json()
    except Exception:
        return jsonify({'error': "Wrong input type. Needed request body format: { \"user_id\":<value>}"})

    data = request.get_json()
    
    productId=data.get('productId')
    productContainer = data.get('productContainer')
    productName = data.get('productName')
    
    quantity = data.get('quantity')
   

    energyValue = data.get('energyValue')
    fatValue = data.get('fatValue')
    carbohydrateValue = data.get('carbohydrateValue')
    sodium = data.get('sodium')
    calcium = data.get('calcium')
    protein = data.get('protein')
    vitamin=data.get('vitamin')
    vitaminType = data.get('vitaminType')
    allergens = data.get('allergens')
    

    expirationDate = datetime.strptime(data.get('expirationDate'), "%b %d, %Y %H:%M:%S").strftime('%Y-%m-%d')
    addedDate = datetime.strptime(data.get('addedDate'), "%b %d, %Y %H:%M:%S").strftime('%Y-%m-%d')
   

    with connection.cursor() as cursor:
        sql = "SELECT storage_id FROM storage_space WHERE storage_name= %s"
        cursor.execute(sql, (productContainer))
        container_data = cursor.fetchone()
        if container_data:
            container_id = container_data[0]
        else:
            # Tratarea cazului în care nu există nicio înregistrare găsită
            container_id = None
        
        sql_product = "UPDATE product SET container_id =%s, product_name = %s, expiration_date = %s, quantity_grams = %s ,date_added =%s  WHERE product_id = %s"
        cursor.execute(sql_product, (container_id, productName,expirationDate,quantity,addedDate,productId))
        connection.commit()
        
        sql_product_info_id = "SELECT product_info_id FROM product WHERE product_id = %s"
        cursor.execute(sql_product_info_id, (productId))
        product_info_id=cursor.fetchall()
        
        
        
        sql_product_info = "UPDATE product_info SET  energy_value = %s, fat_value = %s, carbohydrate_value = %s ,sodium =%s ,calcium=%s , protein =%s, vitamin=%s , vitamin_type = %s , allergens =%s WHERE product_id = %s"
        cursor.execute(sql_product_info, (energyValue,fatValue,carbohydrateValue,sodium,calcium,protein,vitamin,vitaminType,allergens,product_info_id))
        connection.commit()
        
        return jsonify({'message': 'Product updated successfully'})


@app.route('/get_product', methods=['GET'])
def get_product_info():
    productId = request.args.get('productId')

    if productId is None:
        return jsonify({'error': "Missing productId parameter."}), 400

    try:
        with connection.cursor() as cursor:
            sql = "SELECT p.container_id, p.product_name, p.expiration_date, p.quantity_grams, p.date_added, p.product_info_id FROM product p WHERE p.product_id = %s"
            cursor.execute(sql, (productId,))
            product_data = cursor.fetchone()  # Fetch only one row
            print(product_data)
            if product_data:
                container_id, product_name, expiration_date, quantity_grams, date_added, product_info_id = product_data
                
                sql = "SELECT storage_name FROM storage_space WHERE storage_id = %s "
                cursor.execute(sql, (container_id,))
                container_data = cursor.fetchone()
                
                if container_data:
                    container_name = container_data[0]
                else:
                    container_name = None  # sau alt comportament adecvat în funcție de cerințele aplicației

                sql = "SELECT energy_value, fat_value, carbohydrate_value, sodium, calcium, protein, vitamin, vitamin_type, allergens FROM product_info WHERE product_id = %s"
                cursor.execute(sql, (product_info_id,))
                product_info = cursor.fetchone()

                if product_info:
                    
                   
                    expiration_date_formatted = expiration_date.strftime('%b %d, %Y %H:%M:%S')
                    date_added_formatted = date_added.strftime('%b %d, %Y %H:%M:%S')

                    # Construirea obiectului ProductFull
                    product_full = ProductFull(
                        productId,
                        product_name,
                        container_name,
                        expiration_date_formatted,
                        quantity_grams,
                        date_added_formatted,
                        product_info[0],
                        product_info[1],
                        product_info[2],
                        product_info[3],
                        product_info[4],
                        product_info[5],
                        product_info[6],
                        product_info[7],
                        product_info[8]
                    )

                return jsonify(product_full.__dict__)

            return jsonify({'error': f"Product with ID {productId} not found."}), 404

    except Exception as e:
        return jsonify({'error': str(e)})

    
@app.route('/delete_product', methods=['DELETE'])
def get_products_with_containers_list():
   
    productId = request.args.get('productId')
   
    if productId is None:
        return jsonify({'error': "Missing productId parameter."}), 400

    try:
        with connection.cursor() as cursor:
            sql = "DELETE FROM product WHERE product_id = %s"
            cursor.execute(sql, (productId))
            connection.commit()   
            
            sql = "DELETE FROM product_info WHERE product_id = %s"
            cursor.execute(sql, (productId))
            connection.commit()
            
        
            
            return jsonify({'message': 'deleted  successfully'})
    except Exception as e:
        return jsonify({'error': str(e)})    


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

'''
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
'''

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
