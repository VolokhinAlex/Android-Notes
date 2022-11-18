package com.example.java.android1.java_android_notes.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataNoteFromFirebase extends DataNote {

    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DATE = "date";

    public DataNoteFromFirebase(String mNoteName, String mNoteDescription, String mNoteDate) {
        super(mNoteName, mNoteDescription, mNoteDate);
    }

    public DataNoteFromFirebase(String id, String title, String description, String date) {
        this(title, description, date);
        setId(id);
    }

    public DataNoteFromFirebase(String id, Map<String, Object> fields) {
        this(id, (String) fields.get(FIELD_TITLE),
                (String) fields.get(FIELD_DESCRIPTION),
                (String) fields.get(FIELD_DATE));
    }

    public DataNoteFromFirebase(DataNote dataNote) {
        this(dataNote.getId(), dataNote.getNoteName(),
                dataNote.getNoteDescription(), dataNote.getNoteDate());
    }

    public final Map<String, Object> getFields() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(FIELD_TITLE, getNoteName());
        hashMap.put(FIELD_DESCRIPTION, getNoteDescription());
        hashMap.put(FIELD_DATE, getNoteDate());
        return Collections.unmodifiableMap(hashMap);
    }

}
