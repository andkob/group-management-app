package com.melon.app.service.providers;

import java.io.InputStream;

import com.melon.app.service.GoogleSheetsService;

/**
 * The {@code DefaultCredentialsProvider} class is responsible for providing the credentials required 
 * to authenticate with the Google Sheets API. It retrieves the credentials from a file located at a 
 * predefined path.
 * 
 * At the moment, its only purpose is to get around being forced to use the Host of the application's 
 * credentials for testing purposes. For example:
 * 
 * <pre>
 * CredentialsProvider mockProvider = new CredentialsProvider() {
 *      <p>
        @Override
        <p>
        public InputStream getCredentialsStream() {
            return new ByteArrayInputStream(MOCK_CREDENTIALS.getBytes());
        } </p>
    };
    </p>
 * </pre>
 *
 * <p>The credentials are expected to be in a JSON file located at {@code /credentials.json} in the 
 * classpath.</p>
 * 
 
 */
public class DefaultCredentialsProvider implements CredentialsProvider {
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    @Override
    public InputStream getCredentialsStream() {
        return GoogleSheetsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    }
}