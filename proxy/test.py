# create 5 flask app listening on 5 different ports: 8080, 8081, 8082, 8083, 8084 that return the port number

from multiprocessing import Process
import threading
from flask import Flask
import os

def generate_functions():
    apps = []
    for i in range(5):
        app = Flask(__name__ + str(i))
        @app.route('/')
        def print_value(i=i):  # capture i as a local variable
            return f"Value {i}"
        
        @app.route('/card')
        def card():
            return f"Card {i}"
        # functions.append(print_value)
        apps.append(app)
    return apps


if __name__ == '__main__':
    threads = []
    apps = generate_functions()
    i = 0
    for app in apps:
        thread = threading.Thread(target=app.run, kwargs={'host': 'localhost', 'port': 8080+i})
        thread.start()
        threads.append(thread)
        i += 1
    

    input("Press Enter to continue...")
    for thread in threads:
        thread.join()

