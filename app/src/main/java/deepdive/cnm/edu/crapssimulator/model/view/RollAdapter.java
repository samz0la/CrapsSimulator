package deepdive.cnm.edu.crapssimulator.model.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import deepdive.cnm.edu.crapssimulator.R;
import deepdive.cnm.edu.crapssimulator.model.Game.Roll;

public class RollAdapter extends ArrayAdapter<Roll> {

  public RollAdapter(@NonNull Context context,
      int resource) {
    super(context, resource);
    //TODO Load dice images into a drawable array.
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
  Roll roll = getItem(position);
  View view = LayoutInflater.from(getContext()).inflate(R.layout.roll_item, parent, false);

  // TODO Populate, and return view
    return view;
  }
}
