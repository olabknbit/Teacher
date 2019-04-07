package com.hack.teach.teacher;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListActivityFragment extends ListFragment implements AdapterView.OnItemClickListener {
    ArrayList<String> filenames;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    private ArrayList<String> fetchFiles() {
        filenames = new ArrayList<>();
        String path = FileManager.getMP3sDirPath(this.getActivity());

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (File file : files) {
            String file_name = file.getName();
            filenames.add(file_name);
        }
        return filenames;
    }

    private void uploadFiles(){
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
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, fetchFiles());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

//        uploadFiles();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = FileManager.getMP3sDirPath(this.getActivity()) + filenames.get((int) id);
        if (path.contains(".mp3")) {
            MP3Player.playMP3(path);
        }
    }
}
