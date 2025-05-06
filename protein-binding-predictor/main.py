import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score


#simulating protein features 
data = {
    "size": [5.1, 4.9, 6.2, 5.9, 5.0, 6.3, 5.8, 6.0, 6.1, 4.8],
    "polarity": [1.4, 1.5, 1.3, 1.2, 1.7, 1.0, 1.2, 1.4, 1.5, 1.6],
    "charge": [0, 1, 1, 0, 1, 0, 1, 0, 1, 0],
    "binds": [1, 0, 1, 1, 0, 1, 1, 0, 1, 0]  #target: 1 = binds, 0 = doesn't bind
}
df = pd.DataFrame(data)

#splitting the data
X = df.drop("binds", axis=1)
y = df["binds"]
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

#model training
model = RandomForestClassifier()
model.fit(X_train, y_train)

#evaluation
predictions = model.predict(X_test)
accuracy = accuracy_score(y_test, predictions)
print("Model accuracy:", round(accuracy * 100, 2), "%")

#user interactive
print("\nTry it yourself!")
try:
    size = float(input("Enter protein size (e.g. 5.5): "))
    polarity = float(input("Enter protein polarity (e.g. 1.3): "))
    charge = int(input("Enter charge (0 or 1): "))
    result = model.predict([[size, polarity, charge]])
    print("🧬 Binding Prediction:", "Binds" if result[0] == 1 else "Does NOT Bind")
except:
    print("Invalid input. Please try again.")
