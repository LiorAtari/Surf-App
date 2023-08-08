# Use python runtime on alpine image
FROM python:3.6-alpine

# Create /app directory
RUN mkdir /app

# Set working directory as /app
WORKDIR /app

# Copy entire repo into /app
COPY . /app

# Install needed packages
RUN pip install --no-cache-dir -r requirements.txt

# Run pytest using test_app.py
RUN pytest test_app.py

COPY start.sh /usr/local/bin/

RUN chmod +x /usr/local/bin/start.sh

# Expose port 80 for the application
EXPOSE 80

# Run the surfApp.py application
ENTRYPOINT ["start.sh"]
