package com.example.membersmdb;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {

    /** Return the equivalent file name from a person's name. This can
     *  be done by putting it in lower case and removing all spaces.*/
    public static String getFileNameVersion(String name) {
        return name.toLowerCase().replace(" ", "");
    }

    /** Get the Drawable from the image's name.*/
    public static Drawable getImage(Context c, String imageName) {
        int resourceID = c.getResources().getIdentifier(imageName, "raw", c.getPackageName());
        return c.getDrawable(resourceID);
    }

    /** Count all the votes in the votes.txt file. Return a hashmap with
     *  each candidates name mapping to their total vote count.*/
    public static ArrayList<String> readMemberNames(Context c) throws IOException {
        ArrayList<String> allMemberNames = new ArrayList<>();
        AssetManager assetManager = c.getAssets();
        InputStream is = assetManager.open("names.txt");
        Scanner voteScanner = new Scanner(is);
        while (voteScanner.hasNextLine()) {
            String candidateName = voteScanner.nextLine();
            allMemberNames.add(candidateName);
        }
        return allMemberNames;
    }

}
