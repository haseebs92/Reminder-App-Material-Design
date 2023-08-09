package com.codify92.reminderappmaterialdesign.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codify92.reminderappmaterialdesign.R;

public class WidgetAdapter extends BaseAdapter {

    Context context;
    String[] mTitle;
    String[] mSubtext;

    LayoutInflater inflater;


    public WidgetAdapter(Context context, String[] mTitle, String[] mSubtext) {
        this.context = context;
        this.mTitle = mTitle;
        this.mSubtext = mSubtext;
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.custom_view,null);
        }

        TextView mTitle1 = convertView.findViewById(R.id.reminderTitle);
        TextView mSubtext1 = convertView.findViewById(R.id.subtext);

        mTitle1.setText(mTitle[position]);
        mSubtext1.setText(mSubtext[position]);

        return convertView;

    }
}
