package com.example.treemapp;

/**
 * Created by 5dv173 on 5/24/17.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SetChbAdapter extends ArrayAdapter<CheckboxModel>{
    CheckboxModel[] modelItems = null;
    Context context;

    public SetChbAdapter(Context context, CheckboxModel[] resource) {
        super(context,R.layout.checkbox_settings_row,resource);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.modelItems = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.checkbox_settings_row, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.textView1);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
            name.setText(modelItems[position].getName());
            if (modelItems[position].getValue() == 1)
                cb.setChecked(true);
            else
                cb.setChecked(false);
        }
        return convertView;
    }
}