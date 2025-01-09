🖥 Course: CSCI 2020U - Software Systems Development and Integration

This is a template for your Assignment 01 project. The goal is to build a spam detector to filter out unwanted emails! 🧹✉️


## Overview
Tired of all the spam in your inbox? In this project, you'll create a spam detector that filters out unwanted messages. The detector uses a dataset of emails (spam or ham) to train your model and classify new emails based on word frequencies using a unigram approach 📜

### ⚙️ SpamDetectorServer - Endpoints
📂 Listing All Test Files
This endpoint returns a JSON response with test files, showing each file's spam probability and classification.

Endpoint: GET http://localhost:8080/spamDetector-1.0/api/spam
Content-Type: application/json
Here's an example illustrating what a sample response might resemble:
```
[{"spamProbRounded":"0.00000","file":"00006.654c4ec7c059531accf388a807064363","spamProbability":5.901245803391957E-62,"actualClass":"Ham"},{"spamProbRounded":"0.00000","file":"00007.2e086b13730b68a21ee715db145522b9","spamProbability":2.800348071907053E-12,"actualClass":"Ham"},{"spamProbRounded":"0.00000","file":"00008.6b73027e1e56131377941ff1db17ff12","spamProbability":8.66861037294167E-14,"actualClass":"Ham"},{"spamProbRounded":"0.00000","file":"00009.13c349859b09264fa131872ed4fb6e4e","spamProbability":6.947265471550557E-12,"actualClass":"Ham"},{"spamProbRounded":"0.00000","file":"00010.d1b4dbbad797c5c0537c5a0670c373fd","spamProbability":1.8814467288977145E-7,"actualClass":"Ham"},{"spamProbRounded":"0.00039","file":"00011.bc1aa4dca14300a8eec8b7658e568f29","spamProbability":3.892844289937937E-4,"actualClass":"Ham"}]
```

## Calculate and Get Accuracy
This endpoint calculates and returns the accuracy of your spam detector.

Endpoint: GET http://localhost:8080/spamDetector-1.0/api/spam/accuracy
Content Type: application/json
```
{"val": 0.87564}
```

## Calculate and Get Precision
This endpoint calculates and returns the precision of your spam detector.

Endpoint: GET http://localhost:8080/spamDetector-1.0/api/spam/precision
Content Type: application/json
Here's an example illustrating what a sample response might resemble:
```
{"val": 0.56484}
```
### SpamDetectorServer - SpamDetector class

Most of your programming will be in the SpamDetector class. This class is responsible for:

Reading the training and testing data files
Training the model based on historical email data
Testing the model to determine if new emails are spam or ham

💡 Tips:

Feel free to create additional helper classes if needed.
You are not expected to get the exact same values as the sample outputs due to dataset variations.

### References 
[1] https://en.wikipedia.org/wiki/Bag-of-words_model 

[2] https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering 
