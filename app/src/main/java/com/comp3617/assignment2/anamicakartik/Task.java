package com.comp3617.assignment2.anamicakartik;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Task implements Parcelable {
    private String title;
    private String description;
    private String status;
    private Date dueDate;
    private String category;
    private String priority;
    private int imageResource;
    private int myPostitionInList;
    private String address;
    private double lattitude;
    private double longitude;

    public Task() {
        title = "";
        description = "";
        status = "";
        dueDate = null;
        category = "";
        priority = "";
        imageResource = 0;
        myPostitionInList = 0;
        address = null;
        lattitude = 0;
        longitude = 0;
    }

    public Task(Parcel in) {
        title = in.readString();
        description = in.readString();
        category = in.readString();
        status = in.readString();
        priority = in.readString();
        imageResource = in.readInt();
        myPostitionInList = in.readInt();
        address = in.readString();
        lattitude = in.readDouble();
        longitude = in.readDouble();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getMyPositionInList() {
        return myPostitionInList;
    }

    public void setMyPositionInList(int pos) {
        this.myPostitionInList = pos;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String pAddress) {
        this.address = pAddress;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double pLattitude) {
        this.lattitude = pLattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double pLongitude) {
        this.lattitude = pLongitude;
    }

    @Override
    public void writeToParcel(Parcel taskObj, int flags) {
        taskObj.writeString(this.title);
        taskObj.writeString(this.description);
        taskObj.writeString(this.category);
        taskObj.writeString(this.status);
        taskObj.writeString(this.priority);
        taskObj.writeInt(this.imageResource);
        taskObj.writeInt(this.myPostitionInList);
        taskObj.writeString(this.address);
        taskObj.writeDouble(this.lattitude);
        taskObj.writeDouble(this.longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>()
    {
        public Task createFromParcel(Parcel in)
        {
            return new Task(in);
        }
        public Task[] newArray(int size)
        {
            return new Task[size];
        }
    };
}
