package media.extreme.com.brothercorpappmovil;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
//import androidx.core.app.Fragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
/*import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;*/


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Sync.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Sync#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sync extends Fragment {

    private Button btnLeerFichero;
    private final static String LOGTAG = "android-drive";
    protected static final int REQ_CREATE_FILE = 1001;
    protected static final int REQ_OPEN_FILE = 1002;
    private GoogleApiClient apiClient;




    public Sync() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sync, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        /*apiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), (GoogleApiClient.OnConnectionFailedListener) getActivity())
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                //.addScope(Drive.SCOPE_APPFOLDER)
                .build();

        btnLeerFichero= (Button)getView().findViewById(R.id.btnSyncIn);*/




    }
}
