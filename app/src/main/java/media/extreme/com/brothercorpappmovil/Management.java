package media.extreme.com.brothercorpappmovil;

import android.os.Bundle;
//import androidx.core.app.Fragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Management.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Management#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Management extends Fragment {
    public Management() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_management, container, false);

        return rootView;
    }
}
