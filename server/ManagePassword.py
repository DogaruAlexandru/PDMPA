from argon2 import PasswordHasher
import os


def hash_password(password):
    # Generate a random salt
    salt = os.urandom(16)  # 16 bytes for a reasonable size

    # Convert salt to string for concatenation
    salt = salt.hex()

    # Convert password and salt to bytes
    password_bytes = password.encode('utf-8')
    salt_bytes = salt.encode('utf-8')

    # Hash the password using Argon2
    hasher = PasswordHasher()
    hashed_password = hasher.hash(password_bytes + salt_bytes)

    return hashed_password, salt


def verify_password(input_password, hashed_password, salt):
    # Verify the password using Argon2
    hasher = PasswordHasher()

    try:
        # Verify the password by hashing the input password with the stored salt
        hasher.verify(hashed_password, input_password + salt)
        return True  # Passwords match
    except Exception:
        return False  # Passwords do not match