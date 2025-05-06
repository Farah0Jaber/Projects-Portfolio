# AI-Powered Protein Binding Predictor
This is a simple machine learning project that simulates protein-ligand binding predictions using basic biochemical features (size, polarity, and charge). It demonstrates the end-to-end ML workflow including data handling, model training, evaluation, and user interaction.

## Tools Used
- **Python** – glue language for everything
- **Pandas** – data handling and organization
- **Scikit-learn** – machine learning model (Random Forest)

## How It Works
1. A small synthetic dataset is created with protein features.
2. The data is split into training and testing sets.
3. A Random Forest Classifier is trained to predict binding likelihood.
4. The user can input new values to test real-time predictions.

## Run the Project
```bash
pip install pandas scikit-learn
python main.py
