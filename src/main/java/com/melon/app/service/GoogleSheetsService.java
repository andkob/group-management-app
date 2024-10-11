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
import com.melon.app.entity.Schedule;
import com.melon.app.entity.ScheduleEntry;
import com.melon.app.service.objects.FormUpdateResponse;
import com.melon.app.service.providers.CredentialsProvider;
import com.melon.app.service.providers.DefaultCredentialsProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * A service for interacting with Google Sheets API to retrieve data from Google Sheets.
 */
@Service
public class GoogleSheetsService {
    private static final String APPLICATION_NAME = "Schedule Coordination App";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static CredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
    private static final int PORT = 8888;

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
                
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(PORT).build();
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

    public Schedule convertToSchedule(List<List<Object>> scheduleData, String scheduleName) {
        Schedule schedule = new Schedule();
        schedule.setName(scheduleName);
        List<ScheduleEntry> entries = new ArrayList<>();

        for (List<Object> row : scheduleData) {
            // Assuming the first column is day and the second is time
            ScheduleEntry entry = new ScheduleEntry();
            entry.setDay(row.get(0).toString());
            entry.setTime(row.get(1).toString());
            entry.setSchedule(schedule); // Set reference to the parent schedule

            entries.add(entry);
        }

        schedule.setEntries(entries);
        return schedule;
    }

    /**
     * Polls the Google Sheet for any new updates since the last known row count.
     *
     * This method retrieves the current data from the Google Sheet and compares
     * the current row count (excluding the header) to the last known count. If there 
     * are new responses, it returns the new rows of data; otherwise, it returns an 
     * empty list.
     *
     * @param spreadsheetID  the ID of the Google Sheet to poll for updates
     * @param lastKnownCount the last known number of rows (excluding the header) 
     *                       processed in the Google Sheet
     * @return a {@link ResponseEntity} containing a {@link FormUpdateResponse} object 
     *         that includes the current row count, a flag indicating whether there are 
     *         new responses, and a list of new rows (if any)
     * @throws IOException if an I/O error occurs when accessing the Google Sheet
     */
    public ResponseEntity<FormUpdateResponse> pollForUpdates(String spreadsheetID, int lastKnownCount) throws IOException {
        List<List<Object>> currentData = getDataFromSheet(spreadsheetID, "A1:Z");
        int currentCount = currentData.size() - 1; // minus the header

        boolean hasNewResponses = currentCount > lastKnownCount;
        List<List<Object>> newResponses = hasNewResponses ? currentData.subList(lastKnownCount + 1, currentData.size())
                                                            : Collections.emptyList();

        return ResponseEntity.ok(new FormUpdateResponse(currentCount, hasNewResponses, newResponses));
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
     */
}