package me.kisoft.pdf.merger;

import java.util.ArrayList;
import java.util.List;

public class Base64MergeRequest {

    private List<String> files = new ArrayList<>();

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
    
    
}
