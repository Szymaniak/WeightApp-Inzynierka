package com.example.workoutlog;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<AdaperRekord> {
    private int resourceLayout;
    private Context mContext;

    public ListViewAdapter(@NonNull Context context, int resource, @NonNull List<AdaperRekord> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(resourceLayout, null);
        }

        AdaperRekord adaperRekord = getItem(position);

        if (adaperRekord != null) {
            TextView tvAdapterWeight = (TextView) view.findViewById(R.id.tvAdapterWeight);
            TextView tvAdapterWeightDifference = (TextView) view.findViewById(R.id.tvAdapterDifference);
            TextView tvAdapterDate = (TextView) view.findViewById(R.id.tvAdapterDate);

            if (tvAdapterWeight != null) {
                tvAdapterWeight.setText(adaperRekord.getWaga());
            }
            if (tvAdapterWeightDifference != null) {
                if(adaperRekord.getWagaRoznica().contains("+"))
                    tvAdapterWeightDifference.setTextColor(tvAdapterWeightDifference.getContext().getResources().getColor(R.color.plus));
                else if(adaperRekord.getWagaRoznica().contains("-"))
                    tvAdapterWeightDifference.setTextColor(tvAdapterWeightDifference.getContext().getResources().getColor(R.color.minus));
                else
                    tvAdapterWeightDifference.setTextColor(tvAdapterWeightDifference.getContext().getResources().getColor(R.color.neutralny));
                tvAdapterWeightDifference.setText(adaperRekord.getWagaRoznica());
            }
            if (tvAdapterDate != null) {
                tvAdapterDate.setText(adaperRekord.getData());
            }
        }
        return view;
    }
}
