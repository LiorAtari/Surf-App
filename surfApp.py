import os
from flask import Flask, jsonify, render_template
from database import db, Destination, get_all_levels, get_beginners_level, get_intermediates_level, get_advanced_level
from data_insertion import insert_data
from sqlalchemy import create_engine

app = Flask(__name__, static_folder='static')

#PostgreSQL database configuration
db_username = os.environ.get('DB_USERNAME', 'postgres')
db_password = os.environ.get('DB_PASSWORD', 'password')
db_host = os.environ.get('DB_HOST', '127.0.0.1')
db_name = os.environ.get('DB_NAME', 'surfdb')

db_uri = f"postgresql://{db_username}:{db_password}@{db_host}/{db_name}"
app.config['SQLALCHEMY_DATABASE_URI'] = db_uri
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

engine = create_engine(db_uri)

def create_app():
    db.init_app(app)

    with app.app_context():
        db.drop_all()
        db.create_all()
        insert_data()

    @app.route('/', methods=['GET'])
    def surf_data():
        all_levels = get_all_levels()
        beginners_level = get_beginners_level()
        intermediates_level = get_intermediates_level()
        advanced_level = get_advanced_level()
        return render_template('index.html', beginners_level=beginners_level, intermediates_level=intermediates_level, advanced_level=advanced_level, all_levels=all_levels)

    return app

app = create_app()


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=80)
