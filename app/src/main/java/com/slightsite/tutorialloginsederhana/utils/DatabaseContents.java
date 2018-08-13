package com.slightsite.tutorialloginsederhana.utils;

public enum DatabaseContents {

    DATABASE("loginsederhana.db"),
    TABLE_USERS("tbl_users");

    private String name;

    /**
     * Constructs DatabaseContents.
     * @param name name of this content for using in database.
     */
    private DatabaseContents(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
