package android.example.rohwedercoursescheduler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MentorAdapter extends RecyclerView.Adapter {
    private List<Mentor> mentors = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mentor_item, parent, false);
        return new MentorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MentorAdapter.MentorHolder mentorHolder = (MentorAdapter.MentorHolder) holder;
        Mentor currentMentor = mentors.get(position);
        mentorHolder.textViewTitle.setText(currentMentor.getName());
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public void setMentors(List<Mentor> mentors) {
        this.mentors = mentors;
        notifyDataSetChanged();
    }

    class MentorHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        public MentorHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.mentor_card_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(mentors.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Mentor mentor);
    }

    public void setOnItemClickListener(MentorAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
