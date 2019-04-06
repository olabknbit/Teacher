package com.hack.teach.teacher;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Environment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    private ArrayList<String> fetchFiles() {
        filenames = new ArrayList<>();
        String path = getDirPath();

        File directory = new File(path);
        File[] files = directory.listFiles();

        for (File file : files) {
            String file_name = file.getName();
            filenames.add(file_name);
        }
        return filenames;
    }

    private String getDirPath() {
        return this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, fetchFiles());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = getDirPath() + "/" + filenames.get((int) id);
        MP3Player.playMP3(path);
    }
}
