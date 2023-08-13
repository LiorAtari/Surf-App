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

    @app.route('/get_answer', methods=['POST'])
    def get_answer():
        try:
            location = request.form['location']

            # You can customize the prompt based on the selected location
            prompt = f"Tell me about surfing in {location}."

            # Generate a response from GPT-3.5-turbo using the standard completions approach
            response = openai.Completion.create(
                engine="text-davinci-003",  # Use the text-davinci-003 engine
                prompt=prompt,
                max_tokens=500
            )

            generated_reply = response.choices[0].text.strip()

            return render_template('answer.html', generated_reply=generated_reply)
        except Exception as e:
            return jsonify({"error": str(e)}), 500


    return app

app = create_app()


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=80)
