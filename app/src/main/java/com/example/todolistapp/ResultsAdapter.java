package com.example.todolistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.data.ProjectData;
import com.example.todolistapp.data.Results;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultsItemViewHolder> {
    private Results results;
    private OnProjectItemClickListener onProjectItemClickListener;

    public interface OnProjectItemClickListener {
        void onProjectItemClick(ProjectData projectData);
    }

    public ResultsAdapter(OnProjectItemClickListener onProjectItemClickListener) {
        this.onProjectItemClickListener = onProjectItemClickListener;
    }

    public void updateProjectsData(Results results){
        this.results = results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.results_list_item, parent, false);
        return new ResultsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsItemViewHolder holder, int position) {
        ProjectData projectData = this.results.getProjectsDataList().get(position);
        holder.bind(projectData);
    }

    @Override
    public int getItemCount(){
        return this.results.getProjectsDataList().size();
    }

    class ResultsItemViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV;

        public ResultsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTV = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onProjectItemClickListener.onProjectItemClick(
                            results.getProjectsDataList().get(getAdapterPosition())
                    );
                }
            });
        }

        void bind(ProjectData projectData) {
            this.nameTV.setText(projectData.getName());
        }
    }
}
