package com.example.karisong.frags;

import android.graphics.Bitmap;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.StorageReference;

public class CourseModel {
    private Bitmap bitmap;
    private int imgid;
    private StorageReference storageReference;




    public CourseModel(Bitmap bitmap, StorageReference storageReference) { this.bitmap = bitmap; this.storageReference = storageReference; }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void setStorageReference(StorageReference storageReference) { this.storageReference = storageReference; }

    public StorageReference getStorageReference() { return storageReference; }
    public int getImgid() { return imgid; }
    public Bitmap getBitmap() {
        return bitmap;
    }

}