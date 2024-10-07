package com.melon.group_scheduler;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.melon.app.service.CredentialsProvider;
import com.melon.app.service.DefaultCredentialsProvider;
import com.melon.app.service.GoogleSheetsService;

@ExtendWith(MockitoExtension.class)
public class GoogleSheetsServiceTest {

    @InjectMocks
    private GoogleSheetsService googleSheetsService;

    @Mock
    private HttpTransport httpTransport;

    private static final String SPREADSHEET_ID = "1iXAKutl9do2Nwf0X4RQMR4QDx7unk4h83kSWp1eR8Oo";

    private static final String MOCK_CREDENTIALS = "{\n" +
            "  \"installed\": {\n" +
            "    \"client_id\": \"mock-client-id.apps.googleusercontent.com\",\n" +
            "    \"project_id\": \"mock-project-id\",\n" +
            "    \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "    \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "    \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "    \"client_secret\": \"mock-client-secret\",\n" +
            "    \"redirect_uris\": [\"urn:ietf:wg:oauth:2.0:oob\", \"http://localhost\"]\n" +
            "  }\n" +
            "}";

    @BeforeEach
    void setUp() throws IOException, GeneralSecurityException {
        // MockitoAnnotations.openMocks(this);
        
        // Use reflection to set private static final fields if necessary
        // ReflectionTestUtils.setField(GoogleSheetsService.class, "CREDENTIALS_FILE_PATH", "/credentials.json");

        // Reset to default provider before each test
        GoogleSheetsService.setCredentialsProvider(new DefaultCredentialsProvider());
    }

    @Test
    void testGetCredentials() throws IOException, GeneralSecurityException {
        boolean useMockData = true;
        if (useMockData) {
            // Create a mock credentials provider
            CredentialsProvider mockProvider = new CredentialsProvider() {
                @Override
                public InputStream getCredentialsStream() {
                    return new ByteArrayInputStream(MOCK_CREDENTIALS.getBytes());
                }
            };

            // Set the mock provider
            GoogleSheetsService.setCredentialsProvider(mockProvider);
        }
        

        // Use reflection to invoke the private getCredentials method
        Credential credential = (Credential) ReflectionTestUtils.invokeMethod(
            googleSheetsService, // googleSheetsService
            "getCredentials",
            GoogleNetHttpTransport.newTrustedTransport()
        );

        System.out.println("access token = " + credential.getAccessToken());
        
        // Assert that the credential is not null
        assertNotNull(credential);
    }

    @Test
    void testGetCredentialsFileNotFound() {
        // Create a mock provider that returns null
        CredentialsProvider mockProvider = new CredentialsProvider() {
            @Override
            public InputStream getCredentialsStream() {
                return null;
            }
        };
        
        // Set the mock provider
        GoogleSheetsService.setCredentialsProvider(mockProvider);
        
        // Test that service creation throws FileNotFoundException
        assertThrows(FileNotFoundException.class, () -> {
            new GoogleSheetsService();
        });
    }

    @Test
    void testGetDataFromSheet() throws IOException {
        // // Mock the Sheets service
        // Sheets mockSheetsService = mock(Sheets.class);
        // Sheets.Spreadsheets mockSpreadsheets = mock(Sheets.Spreadsheets.class);
        // Sheets.Spreadsheets.Values mockValues = mock(Sheets.Spreadsheets.Values.class);
        // Sheets.Spreadsheets.Values.Get mockGet = mock(Sheets.Spreadsheets.Values.Get.class);
        
        // when(mockSheetsService.spreadsheets()).thenReturn(mockSpreadsheets);
        // when(mockSpreadsheets.values()).thenReturn(mockValues);
        // when(mockValues.get(anyString(), anyString())).thenReturn(mockGet);
        
        // ValueRange mockValueRange = new ValueRange();
        // List<List<Object>> mockData = Arrays.asList(
        //     Arrays.asList("Name", "Email"),
        //     Arrays.asList("John Doe", "john@example.com")
        // );
        // mockValueRange.setValues(mockData);
        
        // when(mockGet.execute()).thenReturn(mockValueRange);
        
        // // Use reflection to set the mock Sheets service
        // ReflectionTestUtils.setField(googleSheetsService, "sheetsService", mockSheetsService);

        // Similar to before, but now we need to ensure we can create the service first
        CredentialsProvider mockProvider = new CredentialsProvider() {
            @Override
            public InputStream getCredentialsStream() {
                return new ByteArrayInputStream(MOCK_CREDENTIALS.getBytes());
            }
        };
        GoogleSheetsService.setCredentialsProvider(mockProvider);

        //================

        // Test the method
        List<List<Object>> result = googleSheetsService.getDataFromSheet(SPREADSHEET_ID, "A1:B2");
        
        // Verify the results
        assertNotNull(result);
        // assertEquals(2, result.size());
        // assertEquals("John Doe", result.get(1).get(0));
        // assertEquals("john@example.com", result.get(1).get(1));
    }
}