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
 * {@link fixtures.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fixtures#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fixtures extends Fragment {
    public fixtures() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fixtures, container, false);

        return rootView;
    }
}
