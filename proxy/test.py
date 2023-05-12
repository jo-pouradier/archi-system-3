# create 5 flask app listening on 5 different ports: 8080, 8081, 8082, 8083, 8084 that return the port number

from flask import Flask
import os

apps = []

for i in range(1, 6):
    app = Flask(__name__ + str(i))
    @app.route('/')
    def hello_world():
        return str(os.environ['PORT'])
    apps.append(app)



if __name__ == '__main__':
    for app in apps:
        app.run(host='127.0.0.1', port=8080+i)


# Path: proxy/Dockerfile
