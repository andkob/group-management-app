package com.melon.app.service;

import java.io.InputStream;

public class DefaultCredentialsProvider implements CredentialsProvider {
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    @Override
    public InputStream getCredentialsStream() {
        return GoogleSheetsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    }
}