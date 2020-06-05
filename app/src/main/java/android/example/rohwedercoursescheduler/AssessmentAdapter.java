package android.example.rohwedercoursescheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AssessmentAdapter extends RecyclerView.Adapter {
    private List<Assessment> assessments = new ArrayList<>();
    private OnItemClickListener listener;
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_item, parent, false);
        return new AssessmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AssessmentAdapter.AssessmentHolder assessmentHolder = (AssessmentAdapter.AssessmentHolder) holder;
        Assessment currentAssessment = assessments.get(position);
        assessmentHolder.textViewTitle.setText(currentAssessment.getTitle());
        assessmentHolder.textViewDueDate.setText(currentAssessment.getDueDate());
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
        notifyDataSetChanged();
    }

    class AssessmentHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDueDate;

        public AssessmentHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.assessment_card_title);
            textViewDueDate = itemView.findViewById(R.id.assessment_card_dueDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(assessments.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Assessment assessment);
    }

    public void setOnItemClickListener(AssessmentAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
