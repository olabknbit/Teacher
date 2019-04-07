package com.hack.teach.teacher;

import android.app.ListFragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListActivityFragment extends ListFragment implements AdapterView.OnItemClickListener {
    ArrayList<String> filenames = new ArrayList<>();
    SimpleAdapter adapter;
    List<HashMap<String,String>> aList;
    MediaPlayer player;
    Long playingId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        return view;
    }

    private ArrayList<String> fetchFiles() {
        String path = FileManager.getMP3sDirPath(this.getActivity());

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (File file : files) {
            String file_name = file.getName();
            filenames.add(file_name);
        }
        return filenames;
    }

    private void uploadFiles() {
        String path = FileManager.getPicturesDirPath(this.getActivity());

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (File file : files) {
            new FileUploader(this.getActivity()).postContentToUrl(file);
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        filenames = fetchFiles();
        final List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

        for(int i=0;i<filenames.size();i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", filenames.get(i).split("_")[0]);
            aList.add(hm);
        }
// Keys used in Hashmap
        String[] from = {"txt"};

        // Ids of views in listview_layout
        int[] to = {R.id.txt};
        //setListAdapter(adapter);
        adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.listview_layout, from, to);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(this);

        Button refreshButton = getActivity().findViewById(R.id.button_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("adapter", "notifying dataset changed");
               // adapter.clear();
                filenames.clear();
                fetchFiles();
                aList.clear();

                for(int i=0;i<filenames.size();i++){
                    HashMap<String, String> hm = new HashMap<String,String>();
                    hm.put("txt", filenames.get(i).split("_")[0]);
                    aList.add(hm);
                }
                adapter.notifyDataSetChanged();
            }
        });

//        uploadFiles();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = FileManager.getMP3sDirPath(this.getActivity()) + filenames.get((int) id);
        if (path.contains(".mp3")) {
            if(player != null && player.isPlaying()) {
                player.pause();
            }
            if(playingId == null || id != playingId) {
                playingId = id;

                player = MP3Player.playMP3(path);
            }

        }
    }
}
