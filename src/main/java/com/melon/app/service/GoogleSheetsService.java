package com.melon.app.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * A service for interacting with Google Sheets API to retrieve data from Google Sheets.
 */
public class GoogleSheetsService {
    private static final String APPLICATION_NAME = "Schedule Coordination App";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static CredentialsProvider credentialsProvider = new DefaultCredentialsProvider();

    private final Sheets sheetsService;
    
    /**
     * Constructs a new {@code GoogleSheetsService} object, initializing the Google Sheets API client.
     *
     * @throws IOException              if there is an issue with credentials file access or API requests.
     * @throws GeneralSecurityException if a security exception occurs while setting up Google Sheets API.
     */
    public GoogleSheetsService() throws IOException, GeneralSecurityException {
        Credential credentials = getCredentials(GoogleNetHttpTransport.newTrustedTransport());
        sheetsService = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credentials)
                                .setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Retrieves the Google API credentials from the credentials file.
     *
     * @param HTTP_TRANSPORT the transport to use for HTTP requests.
     * @return the authorized Google API credential.
     * @throws IOException if the credentials file cannot be found or loaded.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream inputStream = credentialsProvider.getCredentialsStream();
        if (inputStream == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
                
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Retrieves data from a Google Sheet based on the provided spreadsheet ID and range.
     *
     * @param spreadsheetID the ID of the Google Spreadsheet.
     * @param range         the A1 notation of the range to retrieve data from.
     * @return a list of lists containing the sheet data, or {@code null} if no data is found.
     * @throws IOException if an error occurs during the request to Google Sheets API.
     * 
     */
    public List<List<Object>> getDataFromSheet(String spreadsheetID, String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values().get(spreadsheetID, range).execute();
        return response.getValues();
    }

    // For testing purposes
    public static void setCredentialsProvider(CredentialsProvider provider) {
        credentialsProvider = provider;
    }

    /**
     * Client Secrets: These are stored in the credentials.json file and loaded in the getCredentials() method.
     * 
     * Authorization Flow: The GoogleAuthorizationCodeFlow class handles the flow of user authorization,
     * where the user logs in and grants access.
     * 
     * LocalServerReceiver: This component starts a local server to handle the callback from Google’s OAuth2
     * servers after the user authorizes your app. It listens on port 8888.
     * 
     * Token Storage: The app stores tokens in the tokens directory, so the user doesn’t have to authorize the app every time.
     * The token file allows the app to request an updated access token without requiring user re-authorization.
     * 
     * 
     */
}