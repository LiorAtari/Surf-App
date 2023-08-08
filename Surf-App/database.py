from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import create_engine

db = SQLAlchemy()

class Destination(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    location = db.Column(db.String(50))
    surf_spot = db.Column(db.String(50))
    level = db.Column(db.String(50))
    date = db.Column(db.String(50))
    imglink = db.Column(db.String(400))

def get_all_levels():
    return Destination.query.all()

def get_beginners_level():
    return Destination.query.filter_by(level="BEGINNER").all()

def get_intermediates_level():
    return Destination.query.filter_by(level="INTERMEDIATE").all()

def get_advanced_level():
    return Destination.query.filter_by(level="ADVANCED").all()
