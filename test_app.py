import pytest
from flask import Flask
from database import db, Destination, get_all_levels, get_beginners_level, get_intermediates_level, get_advanced_level

@pytest.fixture
def app():
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///:memory:'
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    db.init_app(app)
    
    with app.app_context():
        db.create_all()

    yield app

    with app.app_context():
        db.session.remove()
        db.drop_all()

@pytest.fixture
def client(app):
    return app.test_client()

def test_get_destinations_list(app, client):
    with app.app_context():
        des1 = Destination(location='MALDIVES', surf_spot="COKES", level="ADVANCED" , date="28 OCT - 07 NOV", imglink="https://lushpalm.com/wp-content/uploads/2020/10/maldives-surf-1.webp")
        des2 = Destination(location='SRI LANKA', surf_spot="WELIGAMA", level="BEGINNER", date="10 NOV - 20 NOV", imglink="https://lushpalm.com/wp-content/uploads/2017/08/surfing-sri-lanka-mirissa.webp")
        des3 = Destination(location='HAWAII', surf_spot="SUNSET BEACH", level="ADVANCED", date="09 JAN - 23 JAN", imglink="http://cdn.lightgalleries.net/4bd5ebf97479c/images/LS6556_29-2.jpg")
        des4 = Destination(location='PORTUGAL', surf_spot="SAGRES", level="INTERMEDIATE", date="03 APR - 15 APR", imglink="https://thesurfatlas.com/wp-content/uploads/2021/01/Sagres-surf.jpg")
        des5 = Destination(location='PHILIPPINES', surf_spot="CLOUD 9", level="ADVANCED", date="15 DEC - 25 DEC", imglink="https://gttp.imgix.net/198646/x/0/cloud-9-2.jpg?auto=compress%2Cformat&ch=Width%2CDPR&dpr=1&ixlib=php-3.3.0&w=883")
        des6 = Destination(location='Indonesia', surf_spot="KUTA", level="INTERMEDIATE", date="02 OCT - 14 OCT", imglink="https://www.thecolonyhotelbali.com/wp-content/uploads/2018/01/Surfer-4.jpg")
        des7 = Destination(location='MALDIVES', surf_spot="NINJA", level="BEGINNER", date="05 APR - 17 APR", imglink="https://www.xtremespots.com/wp-content/uploads/2013/12/Surfing-at-Ninjas.jpg")
        des8 = Destination(location='PORTUGAL', surf_spot="ERICEIRA ", level="BEGINNER", date="14 MAY - 28 MAY", imglink="https://images.ctfassets.net/xhzuh2up4xai/4aV7Lb6hpazK1XtAALe3o8/a09d7ad6c1878ab349a8afa2a309a193/Surf_Ericeira39-min.jpg?fm=jpg&fl=progressive&w=1920&h=1080&q=75")
        des9 = Destination(location='MOROCCO', surf_spot="PANORAMAS", level="INTERMEDIATE", date="27 NOV - 09 DEC ", imglink="https://surfberbere.com/wp-content/uploads/2016/05/panoramas-beach-taghazout-surf-spot.jpg")

        db.session.add(des1)


        db.session.add(des1)
        db.session.add(des2)
        db.session.add(des3)
        db.session.add(des4)
        db.session.add(des5)
        db.session.add(des6)
        db.session.add(des7)
        db.session.add(des8)
        db.session.add(des9)

        db.session.commit()


        all_levels = get_all_levels()
        assert len(all_levels) == 9
        beginners_level = get_beginners_level()
        assert len(beginners_level) == 3
        intermediates_level = get_intermediates_level()
        assert len(intermediates_level) == 3
        advanced_level = get_advanced_level()
        assert len(advanced_level) == 3
