package com.m2dl.mobebmp.mobe_catapulte;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Franck on 16/03/2018.
 */

public class ScoreAdatper extends ArrayAdapter<Score> {

    private final Context _context;
    private LinkedList<Score> _scores;
    public ScoreAdatper(@NonNull Context context, int resource, LinkedList<Score> scores) {
        super(context, resource, scores);
        _context = context;
        _scores = scores;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_score, parent, false);
        } else {
            convertView = (LinearLayout) convertView;
        }
        TextView viewName = (TextView) convertView.findViewById(R.id.name);
        TextView viewScore = (TextView) convertView.findViewById(R.id.score);
        viewName.setText(_scores.get(position).getName());
        viewScore.setText(String.valueOf(_scores.get(position).getScore()));
        return convertView;
    }
}
