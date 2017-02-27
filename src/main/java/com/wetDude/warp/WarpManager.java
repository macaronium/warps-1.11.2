package com.wetDude.warp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/**
 * Contains wap storage and manages its loading/saving.
 */
public class WarpManager {
    WarpStorage m_storage;

	/**
	 * Create new manager.
	 * @param f file to read warp list from.
	 */
    public WarpManager(File f) throws Exception
    {
        System.out.println("Used warp list file: " + f.getPath());

		// BUG: for some reason it says we cannot write to file but saving happens successfuly.
        /*if(!f.canRead()) {
            throw new Exception("Warp file is not readable!");
        }
        if(!f.canWrite()) {
            throw new Exception("Warp file is not Writable!");
        }*/

		// try to load warp list if it exists
        if(f.exists()) {
            GsonBuilder builder = new GsonBuilder();
            builder = builder.excludeFieldsWithoutExposeAnnotation();

            Gson gson = builder.create();
            try {
                m_storage = gson.fromJson(readFile(f), WarpStorage.class);
            } catch(Exception e) {
                // create empty storage if cannot cast
                m_storage = new WarpStorage();
            }
        } else {
            m_storage = new WarpStorage();
        }
    }

	/**
	 * Read file contents as string.
	 */
    private String readFile(File f) throws Exception {
        StringBuilder contents = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(f));

        try {
            String line;

            while((line = reader.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } finally {
            reader.close();
        }

        return contents.toString();
    }
	
	/**
	 * Save string to file.
	 */
    private void writeFile(String contents, File f) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        try {
            writer.write(contents);
        } finally {
            writer.close();
        }
    }

	/**
	 * Store warp list to file.
	 * @param f file to save to
	 */
    public void saveData(File f) throws Exception {
        if(!m_storage.isEmpty()) {
            GsonBuilder builder = new GsonBuilder();
            builder = builder.excludeFieldsWithoutExposeAnnotation();

            Gson gson = builder.create();
            writeFile(gson.toJson(m_storage), f);
        }
    }
}
