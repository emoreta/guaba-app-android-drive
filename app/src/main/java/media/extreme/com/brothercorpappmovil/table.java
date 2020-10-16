package media.extreme.com.brothercorpappmovil;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
//import androidx.core.app.Fragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link table.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link table#newInstance} factory method to
 * create an instance of this fragment.
 */
public class table extends Fragment {
    public table() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_table, container, false);

        return rootView;
    }
}
