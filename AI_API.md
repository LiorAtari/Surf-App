# Integration of Generative AI Tool through an API
In this guide, you will integrate a generative AI tool, specifically OpenAI's GPT-3.5-turbo, into your Flask application.  
The goal is to add a button to your application that, when clicked, sends a request to the API and displays information about surfing in a specific location retrieved from a database.

## Prerequisites
1. An OpenAI API key
2. Flask app 

### Step 1: Set Up Your Environment
add the "openai" package to your requirements.txt file

### Step 2: Modify Your Flask Application
2.1. Import the necessary module at the top of your Flask application file  
  
    
     from flask import Flask, render_template, request, jsonify
     import os
     import openai 
    
      
2.2. Configure your OpenAI API key as an Environment variable:  
    
    openai.api_key = os.environ.get('API_KEY', "api_key")
    
go to surf-booking-chart/templates/deployment.yaml and insert -> 
    
    env:
        - name: API_KEY
          value: {{ .Values.openai.api_key }}
    
go to surf-booking-chart/values.yaml ->
    
    openai:
        api_key: "AddYourKey"

2.3. Create a new route to handle the AI generation and response. For example:

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


### Step 3: Create the button
3.1. In your HTML template (templates/index.html), add a button that triggers the API request:

        <form method="POST" action="/get_answer">
            <input type="hidden" name="location" value="{{ level.location }}">
            <button class="get-answer-button" type="submit">MORE INFO</button>
        </form>

3.2. You can add another HTML for the next page (/get_answer), otherwise, the answer will be shown in a JSON format.



## Credits 
1. https://levelup.gitconnected.com/build-your-own-question-answering-system-with-openai-and-flask-2200507ac601
2. https://python.plainenglish.io/introduction-to-the-chatgpt-api-creating-a-python-flask-app-with-chatgpt-api-ad3d971404df
3. https://platform.openai.com/docs/api-reference/introduction
4. chatGPT
