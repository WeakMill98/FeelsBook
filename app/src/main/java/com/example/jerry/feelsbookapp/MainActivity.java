package com.example.jerry.feelsbookapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Emotion> emotionsArrayList = new ArrayList<>();
    ListView oldEmotionsList;
    EmotionAdapter emotionAdapter;
    EditText editQuickComment;
    Button emotionSummaryButton;
    Button loveButton;
    Button joyButton;
    Button surpriseButton;
    Button angerButton;
    Button sadnessButton;
    Button fearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view, load old data
        setContentView(R.layout.activity_main);

        // Get the reference to the summary button as well as the old emotions list
        // Create an object for the edit text
        emotionSummaryButton = findViewById(R.id.emotionSummaryButton);
        oldEmotionsList = findViewById(R.id.listView);
        editQuickComment = findViewById(R.id.quickcommentEditText);

        // Set up a hint for the quick edit comment
        editQuickComment.setHint("Enter a Quick Comment Here!");

        // Get the references to the buttons in the activity
        // Set onclick Listeners for all the Buttons
        loveButton = findViewById(R.id.loveButton);
        joyButton = findViewById(R.id.joyButton);
        angerButton = findViewById(R.id.angerButton);
        sadnessButton = findViewById(R.id.sadnessButton);
        fearButton = findViewById(R.id.fearButton);
        surpriseButton = findViewById(R.id.surpriseButton);

        // When you click on an item in the list view
        // Sends user to another view, supplies the data of the Object clicked
        // emotion id is an index for the id of the Object clicked
        oldEmotionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EmotionActivity.class);
                intent.putExtra("emotionId", position);
                startActivity(intent);
            }
        });

        emotionSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmotionSummaryActivity.class);
                startActivity(intent);
            }
        });

        // Call the method to set an onclick listener for all buttons
        setAllEmotionOnClick();
    }

    // On start, load data, set the adapter to the custom emotion adapter
    // Also be sure to sort the list by date
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadData();
        sortArrayByDate();
        emotionAdapter = new EmotionAdapter(getApplicationContext(), emotionsArrayList);
        oldEmotionsList.setAdapter(emotionAdapter);
    }

    // Called to load the data from system preferences
    // Source of code is listed in the readme
    // Special thanks to CodingFlow for code
    // Instantiate an Array list of type Fear since Emotion is abstract and Gson can instantiate it
    private void loadData(){
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("task list", null);
            Type type = new TypeToken<ArrayList<Fear>>() {}.getType();
            emotionsArrayList = gson.fromJson(json, type);

            if (emotionsArrayList == null) {
                emotionsArrayList = new ArrayList<>();
            }
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // Method to sort the array by date
    private void sortArrayByDate(){
        Collections.sort(emotionsArrayList, new Comparator<Emotion>() {
            @Override
            public int compare(Emotion o1, Emotion o2) {
                // TODO Auto-generated method stub
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }

    // Method to set the on click listeners for all emotion buttons
    // Long method, but logic is repeated to its fine
    private void setAllEmotionOnClick(){
        fearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fear fear = new Fear(new Date());
                emotionsArrayList.add(fear);
                emotionAdapter.notifyDataSetChanged();
                fear.setComment(editQuickComment.getText().toString());
                stepsForEmotionClicked();
            }
        });
        joyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Joy joy = new Joy(new Date());
                emotionsArrayList.add(joy);
                emotionAdapter.notifyDataSetChanged();
                joy.setComment(editQuickComment.getText().toString());
                stepsForEmotionClicked();
            }
        });
        loveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Love love = new Love(new Date());
                emotionsArrayList.add(love);
                emotionAdapter.notifyDataSetChanged();
                love.setComment(editQuickComment.getText().toString());
                stepsForEmotionClicked();
            }
        });
        surpriseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Surprise surprise = new Surprise(new Date());
                emotionsArrayList.add(surprise);
                emotionAdapter.notifyDataSetChanged();
                surprise.setComment(editQuickComment.getText().toString());
                stepsForEmotionClicked();
            }
        });
        sadnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sadness sadness = new Sadness(new Date());
                emotionsArrayList.add(sadness);
                emotionAdapter.notifyDataSetChanged();
                sadness.setComment(editQuickComment.getText().toString());
                stepsForEmotionClicked();
            }
        });
        angerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Anger anger = new Anger(new Date());
                emotionsArrayList.add(anger);
                emotionAdapter.notifyDataSetChanged();
                anger.setComment(editQuickComment.getText().toString());
                stepsForEmotionClicked();
            }
        });
    }

    // Method to move to the specific view of each emotion
    // Allows the user to modify new emotions
    // IMPORTANT: This method should only called for the most-recently added item
    private void moveToDetails(){
        int position = emotionsArrayList.size()-1;
        Intent intent = new Intent(getApplicationContext(), EmotionActivity.class);
        intent.putExtra("emotionId", position);
        startActivity(intent);
    }

    // Steps taken for when any emotion button is clicked
    // use a function to reduce redundancy
    // saves the data for the new emotion, sets the comment equal to the quick comment
    // Moves to another view and displays a successful toast message
    // If the quick comment field is not empty, then we do not need to go to another activity
    private void stepsForEmotionClicked(){
        saveData();
        if (editQuickComment.getText().toString().trim().length() == 0) {
            editQuickComment.getText().clear();
            displaySuccessful();
            moveToDetails();
        }
        else {
            sortArrayByDate();
            emotionAdapter.notifyDataSetChanged();
            editQuickComment.getText().clear();
            displaySuccessful();
        }
    }

    // Called to save changes
    // Code source listed in readme
    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.emotionsArrayList);
        editor.putString("task list", json);
        editor.apply();
    }

    // If saving an emotion is successful
    public void displaySuccessful(){
        Toast.makeText(MainActivity.this, "Emotion Successfully Added!",
                Toast.LENGTH_LONG).show();
    }

}
