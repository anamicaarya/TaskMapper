package com.comp3617.assignment2.anamicakartik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class ActionActivity extends AppCompatActivity implements View.OnClickListener{
    private String desc;
    private String category;
    private String status;
    private String priority;
    private Task taskObj;
    private Task receiveTaskObj;
    private EditText title;
    private EditText description;
    private EditText address;
    private Spinner spinnerStatus;
    private Spinner spinnerCategory;
    private Spinner spinnerPriority;
    private Button  btnAddTask;
    private ImageView imageCategory;
    private boolean edit = false;
    private int positionIndex = 0;
    private int imageResourceID = 0;

    //59:80:54:88:B6:62:31:85:0B:8A:EB:4D:7E:6A:7D:7B:F7:B1:5A:55
    //AIzaSyB41MB84qAxJApfbeF8k3JzhELFy5v-v10

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        imageCategory = (ImageView) findViewById(R.id.imgCategory);
        title = (EditText) findViewById(R.id.editTitle);
        description = (EditText) findViewById(R.id.editDesc);
        address = (EditText) findViewById(R.id.editAddress);
        spinnerStatus = (Spinner) findViewById(R.id.editStatus);
        spinnerCategory = (Spinner) findViewById(R.id.editCategory);
        spinnerPriority = (Spinner) findViewById(R.id.editPriority);
        btnAddTask = (Button) findViewById(R.id.btnAddTask);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            receiveTaskObj = bundle.getParcelable("selectedTaskItem");

        if (receiveTaskObj != null) {

            btnAddTask.setText("Edit Task");
            editActivity();
            edit = true;
            positionIndex = receiveTaskObj.getMyPositionInList();
        }

        else {

            addNewActivity();
        }


        btnAddTask.setOnClickListener(this);
    }


    private void editActivity() {
        taskObj = new Task();

        title.setText(receiveTaskObj.getTitle());
        description.setText(receiveTaskObj.getDescription());
        address.setText(receiveTaskObj.getAddress());

        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setSelection(adapterStatus.getPosition(receiveTaskObj.getStatus()));

        if (spinnerStatus != null) {
            spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    taskObj.setStatus(parentView.getSelectedItem().toString());
                    System.out.println(taskObj.getStatus());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }

        if (spinnerCategory != null) {
            ArrayAdapter<CharSequence> adapterCat = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
            adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapterCat);
            spinnerCategory.setSelection(adapterCat.getPosition(receiveTaskObj.getCategory()));

            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    taskObj.setCategory(parentView.getSelectedItem().toString());
                    System.out.println(taskObj.getCategory());

                    String[] categories = getResources().getStringArray(R.array.category_array);
                    if(taskObj.getCategory().equals(categories[0]))
                        imageResourceID = R.drawable.home;
                    if(taskObj.getCategory().equals(categories[1]))
                        imageResourceID = R.drawable.work;
                    if(taskObj.getCategory().equals(categories[2]))
                        imageResourceID = R.drawable.personal;
                    imageCategory.setImageResource(imageResourceID);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }


        ArrayAdapter<CharSequence> adapterPriority = ArrayAdapter.createFromResource(this, R.array.priority_array, android.R.layout.simple_spinner_item);
        adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapterPriority);
        spinnerPriority.setSelection(adapterPriority.getPosition(receiveTaskObj.getPriority()));

        if (spinnerPriority != null) {
            spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    taskObj.setStatus(parentView.getSelectedItem().toString());
                    System.out.println(taskObj.getStatus());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }
        imageCategory.setImageResource(receiveTaskObj.getImageResource());
    }

    private void addNewActivity(){

        title.getText().clear();
        description.getText().clear();
        address.getText().clear();
        taskObj = new Task();

        ArrayAdapter<CharSequence> adapterStatus = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);

        if (spinnerStatus != null) {
            spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    taskObj.setStatus(parentView.getSelectedItem().toString());
                    System.out.println(taskObj.getStatus());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }

        if (spinnerCategory != null) {
            ArrayAdapter<CharSequence> adapterCat = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
            adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(adapterCat);

            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    taskObj.setCategory(parentView.getSelectedItem().toString());
                    System.out.println(taskObj.getCategory());

                    System.out.println(taskObj.getCategory());

                    String[] categories = getResources().getStringArray(R.array.category_array);
                    if(taskObj.getCategory().equals(categories[0]))
                        imageResourceID = R.drawable.home;
                    if(taskObj.getCategory().equals(categories[1]))
                        imageResourceID = R.drawable.work;
                    if(taskObj.getCategory().equals(categories[2]))
                        imageResourceID = R.drawable.personal;

                    imageCategory.setImageResource(imageResourceID);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }

        ArrayAdapter<CharSequence> adapterPriority = ArrayAdapter.createFromResource(this, R.array.priority_array, android.R.layout.simple_spinner_item);
        adapterPriority.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapterPriority);

        if (spinnerPriority != null) {
            spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    taskObj.setStatus(parentView.getSelectedItem().toString());
                    System.out.println(taskObj.getStatus());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        }

    }

    @Override
    public void onClick(View view){
        System.out.println("OnClick is called ");
        if(taskObj != null) {

            if(TextUtils.isEmpty(title.getText().toString())) {
                title.setError(getResources().getString(R.string.error_title));
                return;
            }
            if(TextUtils.isEmpty(description.getText().toString())) {
                description.setError(getResources().getString(R.string.error_description));
                return;
            }

            if(TextUtils.isEmpty(address.getText().toString())) {
                address.setError(getResources().getString(R.string.error_address));
                return;
            }
            taskObj.setTitle(title.getText().toString());
            taskObj.setDescription(description.getText().toString());
            taskObj.setStatus(spinnerStatus.getSelectedItem().toString());
            taskObj.setCategory(spinnerCategory.getSelectedItem().toString());
            taskObj.setPriority(spinnerPriority.getSelectedItem().toString());
            taskObj.setImageResource(imageResourceID);
            taskObj.setAddress(address.getText().toString());

            double [] latLng = null;
            latLng = LocationFinder.getLocationFromAddress(this,taskObj.getAddress());
            if(latLng == null) {
                address.setError("Please check the address!");
            }
            else {
               taskObj.setLattitude(latLng[0]);
               taskObj.setLattitude(latLng[1]);
            }

            //Log.i("lattitude " + latLng[0] ,"longitude" + latLng[1]) ;
            System.out.println("lattitude " + latLng[0] + "longitude" + latLng[1]);

            if(edit) {
                TaskList.getInstance().editTask(taskObj, positionIndex);
            }
            else{
                TaskList.getInstance().addTask(taskObj);
            }
            Intent mainIntent = new Intent(this, DisplayTasksActivity.class);
            startActivity(mainIntent);
        }
    }

}




