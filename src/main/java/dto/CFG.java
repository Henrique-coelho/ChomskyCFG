package dto;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CFG implements AutoCloseable{

    private String file;
    private Object json;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Object getJson() {
        return json;
    }

    public void setJson(Object json) {
        this.json = json;
    }

    public CFG(String fileName) {
        this.file =  getFileString(fileName);
        Gson gson = new Gson();
        this.json =  gson.fromJson(file, Object.class);

    }

    public String getFileString(String name){
        String token = "";
        File fileName = new File(name).getAbsoluteFile();
        Scanner inFile = null;
        try {
            inFile = new Scanner(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while( inFile.hasNext() )
        {
            String temp = inFile.next( );
            token = token + temp;
        }
        inFile.close();
        return token;
    }

    @Override
    public void close() throws Exception {
    }
}
