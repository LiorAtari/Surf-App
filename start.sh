#!/bin/sh
python -c "from surfApp import create_all; create_all()"
exec gunicorn -b 0.0.0.0:80 surfApp:app
