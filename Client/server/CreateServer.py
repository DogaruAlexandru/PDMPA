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
@app.route('/app_users', methods=['POST'])
def create_user():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')
    with connection.cursor() as cursor:
        sql = "INSERT INTO app_users (username, password_hash) VALUES (%s, %s)"
        cursor.execute(sql, (username, password))
        connection.commit()
    return jsonify({'message': 'User created successfully'})


if __name__ == '__main__':
    app.run(debug=True)
